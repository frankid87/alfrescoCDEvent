package com.objectway.alfresco.behaviour.policies;

import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;

import com.objectway.alfresco.behaviour.handler.DocumentEventHandler;
import com.objectway.alfresco.messagebroker.message.Message;

public class OnAddDocumentPolicies extends DocumentPoliciesTemplate implements NodeServicePolicies.OnCreateNodePolicy {

	final static Logger logger = Logger.getLogger(OnAddDocumentPolicies.class);

	@Override
	public void onCreateNode(ChildAssociationRef childAssocRef) {
		
		NodeRef parentRef = childAssocRef.getParentRef();
		NodeRef docRef = childAssocRef.getChildRef();

		if(docRef != null) DocumentEventHandler.insertIntoExcludeList(docRef);

		// Check if node exists, might be moved, or created and deleted in same transaction.
		if (docRef == null || !alfrescoNodeService.existDocument(docRef)) {
			// Does not exist, nothing to do
			logger.debug("On add document " + docRef.getId() + ": document was added but removed in same transaction");
			return;
		}
		
		// Check if document is a preview,
		if(alfrescoNodeService.isDocument(parentRef)){
			logger.debug("On add document " + docRef.getId() + ": parent folder "+ parentRef.getId()+" is not a folder");
			return;
		}
		
		if(alfrescoNodeService.isDocument(docRef)){
			logger.info("On add document " + docRef.getId() + " in " + alfrescoNodeService.getFullPath(parentRef));
			this.execute(docRef);
		}
	}
	
	@Override
	public Message createAlfrescoMessage(NodeRef node){ 
        String fileName = alfrescoNodeService.getFileInfo(node).getName();
        Message messageCreator = new Message();
		
		messageCreator.addHeader(Message.Key.TENANT, alfrescoNodeService.getSiteName(node));
		
		messageCreator.addBody(Message.Key.DOCUMENT_ID, node.getId());
		messageCreator.addBody(Message.Key.ACTION, "CREATE");
		messageCreator.addBody(Message.Key.FOLDER, alfrescoNodeService.getFilePath(node));
		messageCreator.addBody(Message.Key.CREATION_DATE, String.valueOf(alfrescoNodeService.getFileInfo(node).getCreatedDate().getTime()));
		messageCreator.addBody(Message.Key.DOCUMENT_DATE, String.valueOf(alfrescoNodeService.getFileInfo(node).getModifiedDate().getTime()));
        messageCreator.addBody(Message.Key.DOCUMENT_TITLE, fileName);

		logger.debug(messageCreator);

		return messageCreator;
	}
	
}