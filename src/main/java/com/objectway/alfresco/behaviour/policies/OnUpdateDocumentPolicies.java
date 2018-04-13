package com.objectway.alfresco.behaviour.policies;

import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;

import com.objectway.alfresco.messagebroker.message.Message;

public class OnUpdateDocumentPolicies extends DeleteDocumentPolicies implements NodeServicePolicies.OnUpdateNodePolicy {

	final static Logger logger = Logger.getLogger(OnUpdateDocumentPolicies.class);

	@Override
	public void onUpdateNode(NodeRef nodeRef) {
		throw new NotImplementedException();
	}

	public Message createAlfrescoMessage(NodeRef node){ 
		throw new NotImplementedException();
	}

}