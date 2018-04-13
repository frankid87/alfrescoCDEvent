package com.objectway.alfresco.jms.provider.test;

//import javax.jms.Message;
//
//import org.apache.activemq.ActiveMQConnectionFactory;
//import org.springframework.jms.core.JmsTemplate;
//
//import com.objectway.alfresco.jms.message.AlfrescoHashMapMessage;
//import com.objectway.alfresco.jms.provider.ActiveMQProvider;

public class ActiveMQProviderTest {
	
//	public static void main(String[] args){
//    	ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
//    	connectionFactory.setBrokerURL("tcp://10.0.9.65:61616");
//    	JmsTemplate jmsTemplate = new JmsTemplate();
//    	jmsTemplate.setConnectionFactory(connectionFactory);
//    	ActiveMQProvider activeMQProvider = new ActiveMQProvider();
//    	activeMQProvider.setJmsTemplate(jmsTemplate);
//    	activeMQProvider.setQueueName("nomecodazz");
//    	
//    	AlfrescoHashMapMessage msg = new AlfrescoHashMapMessage();
//    	msg.addProperty("key", "value");
//    	
//    	activeMQProvider.sendMessage(msg);
//    	
//    	Message message = jmsTemplate.receive("nomecodazz");
//    }
	
}
