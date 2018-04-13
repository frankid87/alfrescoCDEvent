package com.objectway.alfresco.messagebroker.provider;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;

public class RabbitMQProvider {
	
	final static Logger logger = Logger.getLogger(RabbitMQProvider.class);

	private AmqpTemplate amqpTemplate;
	private String exchange;
	private String[] headers; 
	private String headersSeparatorLog;
	private String headersLog;
	
	public String checkAndPrint() {
		if(headers == null || headers.length == 0 || headers[0].trim().isEmpty()) {
			headersSeparatorLog = headersSeparatorLog == null ? "EMPTY!" : headersSeparatorLog;
			headersLog = headersLog == null ? "EMPTY!" : headersLog;
			logger.warn("Headers not found. amqp.headers: " + headersLog + " amqp.headers.separator: " + headersSeparatorLog);	
			headers = new String[]{"$operation=documents"};
		}
		
		StringBuilder str = new StringBuilder();
		for(String x : headers) {
			str.append(x).append(" ");
		}
		return str.toString();
	}
	
	public void sendMessage(com.objectway.alfresco.messagebroker.message.Message messageCreator){
		Map<String, String> parameters = messageCreator.getBody();
		logger.info("Sending message: " + parameters + " headers : " + this.checkAndPrint());
		this.amqpTemplate.convertAndSend(exchange, null, parameters, new AlfrescoMessagePostProcessor(messageCreator.getHeader()));
	}

	class AlfrescoMessagePostProcessor implements MessagePostProcessor{
		
		private Map<String, String> header;
		
		AlfrescoMessagePostProcessor(Map<String, String> header){
			this.header= header;
		}
		
		@Override
		public Message postProcessMessage(Message message) throws AmqpException {
			MessageProperties mp = message.getMessageProperties();
			for(String header : headers) {
				String key = header.split("=")[0];
				String value = header.split("=")[1];
				mp.setHeader(key, value);
			}
			mp.setContentType("json");
			for(Entry<String, String> entry : header.entrySet()) {
				mp.setHeader(entry.getKey(), entry.getValue());
			}
			return message;
		}
	}
	
	public AmqpTemplate getAmqpTemplate() {
		return amqpTemplate;
	}

	public void setAmqpTemplate(AmqpTemplate amqpTemplate) {
		this.amqpTemplate = amqpTemplate;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String[] getHeaders() {
		return headers;
	}

	public void setHeaders(String[] headers) {
		this.headers = headers;
	}

	public String getHeadersSeparatorLog() {
		return headersSeparatorLog;
	}

	public void setHeadersSeparatorLog(String headersSeparatorLog) {
		this.headersSeparatorLog = headersSeparatorLog;
	}

	public String getHeadersLog() {
		return headersLog;
	}

	public void setHeadersLog(String headersLog) {
		this.headersLog = headersLog;
	}
	
}