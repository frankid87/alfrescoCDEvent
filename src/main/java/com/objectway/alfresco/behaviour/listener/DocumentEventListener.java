package com.objectway.alfresco.behaviour.listener;

import org.alfresco.repo.transaction.AlfrescoTransactionSupport;
import org.alfresco.repo.transaction.TransactionListener;
import org.alfresco.util.transaction.TransactionListenerAdapter;
import org.apache.log4j.Logger;

import com.objectway.alfresco.behaviour.handler.DocumentEventHandler;
import com.objectway.alfresco.messagebroker.message.Message;
import com.objectway.alfresco.messagebroker.provider.RabbitMQProvider;

public class DocumentEventListener extends TransactionListenerAdapter implements TransactionListener {

	private final static Logger logger = Logger.getLogger(DocumentEventListener.class);

	protected RabbitMQProvider rabbitMQProvider;
	
	@Override
	public void flush() {}

	@Override
	public void afterCommit() {
		Object object = AlfrescoTransactionSupport.getResource(DocumentEventHandler.RESOURCE_KEY);
		if(object != null && object instanceof Message) {
			Message message = (Message) object;
			this.sendMessage(message);
		}
	}	
	
	protected void sendMessage(Message message){
		try{
			this.rabbitMQProvider.sendMessage(message);
		}catch(Throwable e){
			logger.error("Send message error " + this.getId(message), e);
		}
	}
	
	private String getId(Message message) {
		return message.getBody().get(Message.Key.DOCUMENT_ID.getValue());
	}
	
	public void setRabbitMQProvider(RabbitMQProvider rabbitMQProvider) {
		this.rabbitMQProvider = rabbitMQProvider;
	}

	public RabbitMQProvider getRabbitMQProvider() {
		return rabbitMQProvider;
	}
	
}