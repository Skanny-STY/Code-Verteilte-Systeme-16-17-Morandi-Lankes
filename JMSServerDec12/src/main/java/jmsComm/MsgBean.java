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

import database.EntityCount;
import database.EntityTrace;
import database.PersistEntityCount;
import database.PersistEntityTrace;
import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.PduType;


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
				//Auslesen der Nachricht und um rückwandeln in eine PDU
				ObjectMessage objectMessage = (ObjectMessage) message;
				ChatPDU pduFromQueue = (ChatPDU) objectMessage.getObject();
				System.out.println("folgenden Messagetyp aus Queue gelesen: \n" +pduFromQueue.getPduType());
				
				
				
				
				//Datenbankprozesse
				
				// vermutlich wegen fehlender XA-Transaktion ist es  
		        // immer nur möglich während einem Durchlauf 
		        // in eine, nicht aber in beide Datenbanken  zu schreiben. 
		         
		        //Operation für CountDatenbank 
				entityCount.setUserName(pduFromQueue.getUserName() );
				entityCount.setNumberOfReceivedConfirms(pduFromQueue.getNumberOfReceivedConfirms());
				persistEntityCount.insertValues(entityCount);
				
				
				/*
				//Operation in TraceDatenbank
				//
				if(pduFromQueue.getPduType() == PduType.CHAT_MESSAGE_REQUEST){
				}
					entityTrace.setServerThreaName(pduFromQueue.getServerThreadName());
					entityTrace.setClientThreadName(pduFromQueue.getClientThreadName());
					entityTrace.setMessage(pduFromQueue.getMessage());
					persistEntityTrace.insertValues(entityTrace);
				 */	
				

				//  Erzeugen von Topicconnection und 
				//  Verarbeitung von eigehender Nachricht, samt Absenden von Antwortnachrichten
				topicConnection = new TopicConnection(context, chatTopic);
				Messagehandler msgHandler = new Messagehandler(topicConnection);
				msgHandler.handleMessage(pduFromQueue);
		
				
	
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	
	
}
