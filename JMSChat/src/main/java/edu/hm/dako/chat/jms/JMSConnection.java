package edu.hm.dako.chat.jms;

import java.io.Serializable;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.connection.Connection;
import edu.hm.dako.chat.connection.ConnectionTimeoutException;


public class JMSConnection implements Connection{
	
	    private Context namingContext = null;
	    private JMSContext context = null;
	    private Properties env;
	    private Destination queueDestination;
	    private Destination topicDestination;
	    private String providerIPAndPort;
	    private JMSConsumer consumer;
	    private JMSProducer producer;
	    private String userName = null; 
	    
	    public JMSConnection(String providerUrlAndPort){
	   
	    	this.providerIPAndPort = providerUrlAndPort;
    		this.env = new Properties();
	    	
    		//vorbereiten der Umgebung
	        env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
	        env.put(Context.PROVIDER_URL, "http-remoting://" + this.providerIPAndPort);
	        env.put(Context.SECURITY_CREDENTIALS, "guest");   // password
	        env.put(Context.SECURITY_PRINCIPAL, "guest");     // username
	      
	        try {
	        	this.namingContext = new InitialContext(this.env);
	        	
	        	//lookups erfolgen alle mit der JNDI ohne präfis java:jboss/exported
	        	
		        ConnectionFactory connectionFactory = (ConnectionFactory) namingContext.lookup("jms/RemoteConnectionFactory");   
		        //Queue
		        this.queueDestination = (Destination) namingContext.lookup("jms/queue/testqueue");
		        //Topic
		        this.topicDestination = (Destination) namingContext.lookup("jms/topic/chattopic");
		       
		        context = connectionFactory.createContext("guest", "guest");
		        
		        //Producer und Consumer Objekt
		        this.producer = context.createProducer();
		        this.consumer = context.createConsumer(topicDestination);
	
	        } catch(NamingException e){
	        	e.printStackTrace();
	        }
	        
	    }
	        public void send(Serializable message) throws Exception{
	        	// Eigenen Usernamen auslesen und speichern,
	        	// sodass er nachher für die Filterung der Nachrichten 
	        	// aus dem Topic verfügbar ist. (siehe TopicListener)
	        	if(userName == null){
	        
	        		ChatPDU toSendPDU = (ChatPDU) message;
	        		userName = toSendPDU.getUserName();
	        	}
	        	
	        	//mit PDU analoge ObjectMessage schaffen
	        	ObjectMessage objectMessage = context.createObjectMessage(message);
		        try {
			        // Mit Producerobjekt an Queue senden
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
				if(namingContext != null){
					namingContext.close();
				}
				if(context != null){
					context.close();
				}
			}
			
			public void setMessageListener() {
				//Setzen des MessageListeners auf das Topic
				TopicListener topicListener = new TopicListener(this.userName);
				this.consumer.setMessageListener(topicListener);
				
			}
			
			
	    }

