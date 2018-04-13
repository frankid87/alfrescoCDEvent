package com.objectway.alfresco.behaviour.policies;

import java.util.HashMap;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;

import com.objectway.alfresco.behaviour.handler.DocumentEventHandler;
import com.objectway.alfresco.messagebroker.message.Message;

public class DeleteDocumentPolicies extends DocumentPoliciesTemplate implements  NodeServicePolicies.BeforeDeleteNodePolicy, NodeServicePolicies.OnDeleteNodePolicy {

	final static Logger logger = Logger.getLogger(DeleteDocumentPolicies.class);

	private ThreadLocal<Map<String, Message>> map = new ThreadLocal<Map<String, Message>>(); 
	
	@Override
	public void beforeDeleteNode(NodeRef nodeRef) {
		ChildAssociationRef childAssociationRef = alfrescoNodeService.getParent(nodeRef);
		boolean isDeleteDocumentOperation = true;
		if (childAssociationRef != null){
			NodeRef parentRef = childAssociationRef.getParentRef();
			if(alfrescoNodeService.getNodeType(parentRef).compareTo(ContentModel.TYPE_FOLDER) != 0){
				logger.debug("Before delete node " + nodeRef.getId() + ": parent folder "+ parentRef.getId()+" is not a folder");
				DocumentEventHandler.insertIntoExcludeList(nodeRef);
				isDeleteDocumentOperation = false;
			}
		}
		if(isDeleteDocumentOperation) {
			logger.debug("Creating delete message");
			this.createMessage(nodeRef);
		}
	}

	@Override
	public void onDeleteNode(ChildAssociationRef childAssocRef, boolean isNodeArchived) {
		NodeRef docRef = childAssocRef.getChildRef();
		if(DocumentEventHandler.checkFromExcludeList(docRef)){
			logger.debug("On delete document " + docRef.getId() + ": id present into exclude list");
			return;
		}
		logger.info("On delete document  " + docRef.getId());
		this.execute(docRef);
	}
	
	@Override
	public Message createAlfrescoMessage(NodeRef node){ 
		return this.map.get() != null ? this.map.get().get(node.getId()) : null;
	}
	
	private void createMessage(NodeRef node) {
		String fileName = alfrescoNodeService.getFileInfo(node).getName();
        
		Message messageCreator = new Message();
		messageCreator.addHeader(Message.Key.TENANT, this.alfrescoNodeService.getSiteName(node));
		messageCreator.addBody(Message.Key.DOCUMENT_ID, node.getId());
		messageCreator.addBody(Message.Key.ACTION, "DELETE");
		messageCreator.addBody(Message.Key.FOLDER, this.alfrescoNodeService.getFilePath(node));
		messageCreator.addBody(Message.Key.CREATION_DATE, String.valueOf(alfrescoNodeService.getFileInfo(node).getCreatedDate().getTime()));
		messageCreator.addBody(Message.Key.DOCUMENT_DATE, String.valueOf(alfrescoNodeService.getFileInfo(node).getModifiedDate().getTime()));
        messageCreator.addBody(Message.Key.DOCUMENT_TITLE, fileName);
		
        logger.debug(messageCreator);

        if(map.get() == null) {
			map.set(new HashMap<String, Message>());
		}
		map.get().put(node.getId(), messageCreator);
    }

}