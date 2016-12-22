package jmsComm;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import database.EntityCount;
import database.PersistEntityCount;
import edu.hm.dako.chat.common.ChatPDU;

@MessageDriven( activationConfig = 
				{@ActivationConfigProperty( 
				propertyName = "destination",
				propertyValue = "java:jboss/exported/jms/queue/testqueue"
				)
			})


public class MsgBean implements MessageListener {
	@Inject JMSContext context;
	
	@Resource(mappedName = "java:jboss/exported/jms/topic/chattopic")
    private Destination chatTopic;
	
	@Inject 
	private PersistEntityCount persistEntityCount;

	@Inject
	private EntityCount entityCount;
	
	private TopicConnection topicConnection;

//	@Inject
//	PersistEntityTrace persistEntityTrace;
	

//	@Inject
//	EntityTrace entityTrace;
	
	
	
	//private Connection connection = new WriteToTopicConnection(context, chatTopic);
	
	@Override
	public void onMessage(Message message) {
				
			try {
//				String text = message.getBody(String.class);
//				System.out.println(text);
				ObjectMessage objectMessage = (ObjectMessage) message;
				ChatPDU pduFromQueue = (ChatPDU) objectMessage.getObject();
				System.out.println("folgenden Messagetyp aus Queue gelesen: \n" +pduFromQueue.getPduType());
				
				/*
				//Datenbankzeug
				entityCount.setUserName(pduFromQueue.getUserName() );
				entityCount.setNumberOfReceivedConfirms(pduFromQueue.getNumberOfReceivedConfirms());
				persistEntityCount.insertValues(entityCount);
				
				entityTrace.setUserName(pduFromQueue.getUserName());
				entityTrace.setMessage(pduFromQueue.getMessage());
				entityTrace.setUserName(pduFromQueue.getServerThreadName());
				persistEntityTrace.insertValues(entityTrace);
				 */	
				

				//  Topiczeug und Messagehandling
				topicConnection = new TopicConnection(context, chatTopic);
				Messagehandler msgHandler = new Messagehandler(topicConnection);
				msgHandler.handleMessage(pduFromQueue);
		
				
	
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	
}
