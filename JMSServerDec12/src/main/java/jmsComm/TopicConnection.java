package jmsComm;

import java.io.Serializable;

import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.ObjectMessage;
import javax.jms.Topic;

import common.Connection;
import edu.hm.dako.chat.common.ChatPDU;

public class TopicConnection implements Connection{
	private JMSContext context;
	private Topic topic; 
	
	public TopicConnection(JMSContext context, Topic topic) {
		this.context = context;
		this.topic = topic;
	}
	
	@Override
	public Serializable receive(int timeout) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable receive() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void send(Serializable message) throws Exception {
//		ObjectMessage objm = (ObjectMessage) message;
//		ChatPDU tmpPDUCheck = (ChatPDU) objm;
//		System.out.println("sending to topic " + tmpPDUCheck.toString());
		//TODO hier mal nicht in Objectmessage umwandeln und dann mal kucken ob Message in Topic ankommt.
		JMSProducer producer = context.createProducer();
		ObjectMessage objectMessage = context.createObjectMessage(message);
		producer.send(topic, objectMessage);
		
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		
	}

}
