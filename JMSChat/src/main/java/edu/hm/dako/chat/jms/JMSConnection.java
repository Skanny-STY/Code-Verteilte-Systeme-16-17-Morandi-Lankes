package edu.hm.dako.chat.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


import edu.hm.dako.chat.connection.Connection;
import edu.hm.dako.chat.connection.ConnectionTimeoutException;
import edu.hm.dako.chat.jms.TopicListener;


public class JMSConnection implements Connection{
	
	    private Context namingContext = null;
	    private JMSContext context = null;
	    private Properties env;
	    private Destination queueDestination;
	    private Destination topicDestination;
	    private String providerIPAndPort;
	    private JMSConsumer consumer;
	    private JMSProducer producer;
	    
	    public JMSConnection(String providerUrlAndPort){
	    	
	    	this.providerIPAndPort = providerUrlAndPort;
    		this.env = new Properties();
    		
	    	
	        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
	        env.put(Context.PROVIDER_URL, "http-remoting://" + this.providerIPAndPort);
	        env.put(Context.SECURITY_CREDENTIALS, "guest");   // password
	        env.put(Context.SECURITY_PRINCIPAL, "guest");     // username
	        
	        try {
	        	this.namingContext = new InitialContext(this.env);
	      
		        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("jms/RemoteConnectionFactory");      
		        this.queueDestination = (Destination) namingContext.lookup("jms/queue/testqueue");
		        this.topicDestination = (Destination) namingContext.lookup("jms/topic/chattopic");
		        
		        context = connectionFactory.createContext("guest", "guest");
		        this.producer = context.createProducer();
		        this.consumer = context.createConsumer(topicDestination);
	
	        } catch(NamingException e){
	        	e.printStackTrace();
	        }
	        
	    }
	        public void send(Serializable message) throws Exception{
	        	
	        	ObjectMessage objectMessage = context.createObjectMessage(message);
		        try {
			        // Create a producer and send a message
			     //  message = (ObjectMessage) objectMessage;
			        producer.send(queueDestination,objectMessage);  
			        System.out.println("Message sent : " + message.toString());
		        } catch(Exception e){
		        	e.printStackTrace();
		        }
	    
	        }
			@Override
			public Serializable receive(int timeout) throws Exception, ConnectionTimeoutException {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public Serializable receive() throws Exception {
				// TODO Auto-generated method stub
				return null;
			}
			@Override
			public void close() throws Exception {
				// TODO Auto-generated method stub
				
			}
			
			public void setMessageListener() {
				TopicListener topicListener = new TopicListener();
				System.out.println(topicListener != null);
				this.consumer.setMessageListener(topicListener);
				
			}
			
			
	    }

