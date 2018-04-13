package com.objectway.alfresco.service;

import javax.annotation.PostConstruct;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.site.SiteModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.Path;
import org.alfresco.service.namespace.DynamicNamespacePrefixResolver;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.namespace.QName;
import org.alfresco.util.ISO9075;
import org.apache.log4j.Logger;

public class AlfrescoNodeService {
	
	final static Logger logger = Logger.getLogger(AlfrescoNodeService.class);
	
	private Integer depthFolderPath;
	private Integer tenantFolderPathPosition;
	
	private NodeService nodeService;
	private ServiceRegistry serviceRegistry;
	private FileFolderService fileFolderService;
	
	@PostConstruct
	public void postConstruct(){
		nodeService = this.serviceRegistry.getNodeService();
		fileFolderService = this.serviceRegistry.getFileFolderService();
	}
	
	public String getSiteName(NodeRef noderef){
		Path path = nodeService.getPath(noderef);
		if(path.size() - 1 <= tenantFolderPathPosition)
			throw new IllegalArgumentException("folderpath.position.tenant property is not correct. Decrease the value");
		
		String str = path.get(tenantFolderPathPosition).getPrefixedString(this.getResolver()).replace("cm:", "");
		return ISO9075.decode(str);
    }
	
	public String getFilePath(NodeRef noderef){
		Path path = nodeService.getPath(noderef);
		if(path.size() - 1 <= depthFolderPath)
			throw new IllegalArgumentException("folderpath.position.start property is not correct. Decrease the value");
		
		Path subpath = path.subPath(depthFolderPath, path.size()-2);
		String str = subpath.toPrefixString(this.getResolver()).replace("cm:", "");
		return ISO9075.decode(str);
    }
	
	public String getFullPath(NodeRef noderef){
		Path path = nodeService.getPath(noderef);
		Path subpath = path.subPath(1, path.size()-1);
    	return subpath.toPrefixString(this.getResolver());
    }
	
	private DynamicNamespacePrefixResolver getResolver() {
		DynamicNamespacePrefixResolver resolver = new DynamicNamespacePrefixResolver ();
		resolver.registerNamespace(NamespaceService.CONTENT_MODEL_PREFIX,  NamespaceService.CONTENT_MODEL_1_0_URI);
		resolver.registerNamespace(SiteModel.SITE_MODEL_PREFIX,  SiteModel.SITE_MODEL_URL);
		resolver.registerNamespace(NamespaceService.APP_MODEL_PREFIX, NamespaceService.APP_MODEL_1_0_URI);
		return resolver; 
	}
	
	public boolean isFolder(NodeRef node){
		return nodeService.getType(node).compareTo(ContentModel.TYPE_FOLDER) == 0;
	}
	
	public boolean existDocument(NodeRef node){
		return nodeService.exists(node);
	}
	
	public boolean isDocument(NodeRef node){
		return !fileFolderService.getFileInfo(node).isFolder();
	}
	
	public ChildAssociationRef getParent(NodeRef node){
		return nodeService.getPrimaryParent(node);
	}
	
	public FileInfo getFileInfo(NodeRef node){
		return fileFolderService.getFileInfo(node);
	}
	
	public boolean existsNode(NodeRef node){
		return nodeService.exists(node);
	}
	
	public QName getNodeType(NodeRef node){
		return this.nodeService.getType(node);
	}

	public Integer getDepthFolderPath() {
		return depthFolderPath;
	}

	public void setDepthFolderPath(Integer depthFolderPath) {
		this.depthFolderPath = depthFolderPath;
	}

	public NodeService getNodeService() {
		return nodeService;
	}

	public void setNodeService(NodeService nodeService) {
		this.nodeService = nodeService;
	}

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	public FileFolderService getFileFolderService() {
		return fileFolderService;
	}

	public void setFileFolderService(FileFolderService fileFolderService) {
		this.fileFolderService = fileFolderService;
	}

	public Integer getTenantFolderPathPosition() {
		return tenantFolderPathPosition;
	}

	public void setTenantFolderPathPosition(Integer tenantFolderPathPosition) {
		this.tenantFolderPathPosition = tenantFolderPathPosition;
	}
}
