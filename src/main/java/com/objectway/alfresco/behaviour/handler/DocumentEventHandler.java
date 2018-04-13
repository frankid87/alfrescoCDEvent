package com.objectway.alfresco.behaviour.handler;

import java.util.HashSet;
import java.util.Set;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.node.NodeServicePolicies;
import org.alfresco.repo.policy.Behaviour;
import org.alfresco.repo.policy.JavaBehaviour;
import org.alfresco.repo.policy.PolicyComponent;
import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.log4j.Logger;

import com.objectway.alfresco.behaviour.policies.DeleteDocumentPolicies;
import com.objectway.alfresco.behaviour.policies.OnAddDocumentPolicies;
import com.objectway.alfresco.behaviour.policies.OnUpdateDocumentPolicies;

public class DocumentEventHandler {

	final static Logger logger = Logger.getLogger(DocumentEventHandler.class);

	private OnAddDocumentPolicies onAddDocumentPolicies;
	private DeleteDocumentPolicies deleteDocumentPolicies;
	private OnUpdateDocumentPolicies onUpdateDocumentPolicies;
	
	private PolicyComponent policyComponent;
	
	private static final ThreadLocal<Set<String>> EXCLUDE_LIST = new ThreadLocal<Set<String>>(){
		protected Set<String> initialValue() {
	        return new HashSet<String>();
	    }
	};
	
	public static final String RESOURCE_KEY = "DOCUMENT_EVENT_KEY";

	public void registerEventHandlers() {
		logger.info("Registering event handlers");

		if(onAddDocumentPolicies != null) {
			Behaviour onCreateNode = new JavaBehaviour(onAddDocumentPolicies, "onCreateNode");
			this.policyComponent.bindClassBehaviour(NodeServicePolicies.OnCreateNodePolicy.QNAME, ContentModel.TYPE_CONTENT, onCreateNode);
		}
		
		if(onUpdateDocumentPolicies != null) {
			Behaviour onUpdateNode = new JavaBehaviour(onUpdateDocumentPolicies, "onUpdateNode");	
			this.policyComponent.bindClassBehaviour(NodeServicePolicies.OnUpdateNodePolicy.QNAME, ContentModel.TYPE_CONTENT, onUpdateNode);
		}
		
		if(deleteDocumentPolicies != null) {
			Behaviour beforeDeleteNode = new JavaBehaviour(deleteDocumentPolicies, "beforeDeleteNode");
			this.policyComponent.bindClassBehaviour(NodeServicePolicies.BeforeDeleteNodePolicy.QNAME, ContentModel.TYPE_CONTENT, beforeDeleteNode);
			
			Behaviour onDeleteNode = new JavaBehaviour(deleteDocumentPolicies, "onDeleteNode");
			this.policyComponent.bindClassBehaviour(NodeServicePolicies.OnDeleteNodePolicy.QNAME, ContentModel.TYPE_CONTENT, onDeleteNode);
		}
		
	}		
		
	public void setOnAddDocumentPolicies(OnAddDocumentPolicies onAddDocumentPolicies) {
		this.onAddDocumentPolicies = onAddDocumentPolicies;
	}

	public void setDeleteDocumentPolicies(DeleteDocumentPolicies deleteDocumentPolicies) {
		this.deleteDocumentPolicies = deleteDocumentPolicies;
	}

	public void setOnUpdateDocumentPolicies(OnUpdateDocumentPolicies onUpdateDocumentPolicies) {
		this.onUpdateDocumentPolicies = onUpdateDocumentPolicies;
	}

	public void setPolicyComponent(PolicyComponent policyComponent) {
		this.policyComponent = policyComponent;
	}
	
	public OnAddDocumentPolicies getOnAddDocumentPolicies() {
		return onAddDocumentPolicies;
	}

	public DeleteDocumentPolicies getDeleteDocumentPolicies() {
		return deleteDocumentPolicies;
	}

	public OnUpdateDocumentPolicies getOnUpdateDocumentPolicies() {
		return onUpdateDocumentPolicies;
	}

	public PolicyComponent getPolicyComponent() {
		return policyComponent;
	}

	public static boolean checkAndInsertFromExcludeList(NodeRef node){
		if(DocumentEventHandler.EXCLUDE_LIST.get().contains(node.getId())){
			return true;
		}
		DocumentEventHandler.insertIntoExcludeList(node);
		return false;
	}
	
	public static boolean checkFromExcludeList(NodeRef node){
		return DocumentEventHandler.EXCLUDE_LIST.get().contains(node.getId());
	}
	
	public static void insertIntoExcludeList(NodeRef node){
		DocumentEventHandler.EXCLUDE_LIST.get().add(node.getId());
	}
	
}