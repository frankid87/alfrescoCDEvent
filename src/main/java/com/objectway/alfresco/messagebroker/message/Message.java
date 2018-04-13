package com.objectway.alfresco.messagebroker.message;

import java.util.HashMap;
import java.util.Map;

public class Message {
	
	private Map<String, String> body;
	private Map<String, String> header;
	
	public enum Key{
		DOCUMENT_ID("DocumentId"),
		ACTION("Action"),
		FOLDER("Folder"),
		CREATION_DATE("CreationDate"),
		DOCUMENT_DATE("DocumentDate"),
		DOCUMENT_TITLE("DocumentTitle"),
		TENANT("$tenantid");
		
		Key(String value){
			this.value = value;
		}
		
		private String value = "";
		
		public String getValue(){
			return this.value;
		}
		
	}
	
	public Message(){
		this.body = new HashMap<String, String>(0);
		this.header = new HashMap<String, String>(0);
	}
			
	public Message(Map<String, String> header, Map<String, String> body){
		this.body = body;
		this.header = header;
	}
	
	public void addBody(Key key, String value){
		if(this.body == null)
			 this.body = new HashMap<String, String>(1);
		this.body.put(key.getValue(), value);
	}
	
	public void addHeader(Key key, String value){
		if(this.header == null)
			 this.header = new HashMap<String, String>(1);
		this.header.put(key.getValue(), value);
	}

	public Map<String, String> getBody() {
		return body;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	@Override
	public String toString() {
		return "Message [body=" + body + ", header=" + header + "]";
	}
	
}