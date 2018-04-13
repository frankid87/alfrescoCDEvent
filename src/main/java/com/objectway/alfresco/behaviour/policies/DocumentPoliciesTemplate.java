package com.objectway.alfresco.behaviour.policies;

import org.alfresco.repo.transaction.AlfrescoTransactionSupport;
import org.alfresco.service.cmr.repository.NodeRef;

import com.objectway.alfresco.behaviour.handler.DocumentEventHandler;
import com.objectway.alfresco.behaviour.listener.DocumentEventListener;
import com.objectway.alfresco.messagebroker.message.Message;
import com.objectway.alfresco.service.AlfrescoNodeService;

public abstract class DocumentPoliciesTemplate {
	
	protected AlfrescoNodeService alfrescoNodeService;
	protected DocumentEventListener documentEventListener;
	
	public abstract Message createAlfrescoMessage(NodeRef node); 
	
	public void execute(NodeRef node) {
		AlfrescoTransactionSupport.bindListener(documentEventListener);
		Message message = this.createAlfrescoMessage(node);
		AlfrescoTransactionSupport.bindResource(DocumentEventHandler.RESOURCE_KEY, message);
	}

	public void setAlfrescoNodeService(AlfrescoNodeService alfrescoNodeService) {
		this.alfrescoNodeService = alfrescoNodeService;
	}

	public void setDocumentEventListener(DocumentEventListener documentEventListener) {
		this.documentEventListener = documentEventListener;
	}

	public AlfrescoNodeService getAlfrescoNodeService() {
		return alfrescoNodeService;
	}

	public DocumentEventListener getDocumentEventListener() {
		return documentEventListener;
	}
	
}