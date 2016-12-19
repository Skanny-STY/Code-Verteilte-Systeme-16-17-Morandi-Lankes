package edu.hm.dako.chat.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.jms.MessageListener;

import edu.hm.dako.chat.connection.Connection;
import edu.hm.dako.chat.connection.ConnectionTimeoutException;



public class JMSConnection implements Connection, MessageListener{
	
	    private Context namingContext = null;
	    private JMSContext context = null;
	    private Properties env;
	    private String queueDestination;
	    private String providerIPAndPort;
//	    private String chatTopic = ;
	    
	    public JMSConnection(String queueDestination, String providerUrlAndPort){
	    	
	    	this.queueDestination = queueDestination;
	    	this.providerIPAndPort = providerUrlAndPort;
    		this.env = new Properties();
  //  		this.topic = 
	    	
	        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
	        env.put(Context.PROVIDER_URL, "http-remoting://" + this.providerIPAndPort);
	        env.put(Context.SECURITY_CREDENTIALS, "guest");   // password
	        env.put(Context.SECURITY_PRINCIPAL, "guest");     // username
	        try {
	        	this.namingContext = new InitialContext(this.env);
	      
		        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("jms/RemoteConnectionFactory");      
		        
		        context = connectionFactory.createContext("guest", "guest"); 
	//	        chatTopic = 
		 //       Destination topic = (Destination) namingContext.lookup("jms/topic/chattopic");
		 //       JMSConsumer consumer = context.createConsumer(topic);
		 //       consumer.setMessageListener(new TopicListener());
	        } catch(NamingException e){}
	        
	    }
	        public void send(Serializable message) throws Exception{
	        	
	        	ObjectMessage objectMessage = context.createObjectMessage(message);
		        try {
			        // Create a producer and send a message
			        Destination destination = (Destination) namingContext.lookup(this.queueDestination);
			     //  message = (ObjectMessage) objectMessage;
			        context.createProducer().send(destination,objectMessage);  
			        System.out.println("Message sent : " + message.toString());
		        } finally {
		        	if (namingContext != null) {
		        		namingContext.close();
		        	}
		        	if (context != null) {
		        		context.close();
		        	}
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
			@Override
			public void onMessage(Message arg0) {
//				context.createConsumer(a)
				
			}
			
			
	    }

