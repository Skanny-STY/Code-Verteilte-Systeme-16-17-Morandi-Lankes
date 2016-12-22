package jmsComm;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.Destination;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import common.PduType;
import database.EntityCount;
import database.EntityTrace;
import database.PersistEntityCount;
import database.PersistEntityTrace;
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

	@Inject
	PersistEntityTrace persistEntityTrace;
	

	@Inject
	EntityTrace entityTrace;
	
	
	

	
	@Override
	public void onMessage(Message message) {
				
			try {
				//Auslesen der eingegangen Nachricht als PDU
				ObjectMessage objectMessage = (ObjectMessage) message;
				ChatPDU pduFromQueue = (ChatPDU) objectMessage.getObject();
				System.out.println("folgenden Messagetyp aus Queue gelesen: \n" +pduFromQueue.getPduType());
				
				
				
				
				//Datenbankprozesse
				
				// vermutlich wegen fehlender XA-Transaktion ist es immer nur möglich 
				// in eine, nicht aber in beide Datenbanken zu schreiben.
				entityCount.setUserName(pduFromQueue.getUserName() );
				entityCount.setNumberOfReceivedConfirms(pduFromQueue.getNumberOfReceivedConfirms());
				persistEntityCount.insertValues(entityCount);
				
				/*
				
				entityTrace.setUserName(pduFromQueue.getUserName());
				entityTrace.setMessage(pduFromQueue.getMessage());			
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
