package edu.hm.dako.chat.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import edu.hm.dako.chat.common.*;

public class TopicListener implements MessageListener {
	
	private String userName;
	
	public TopicListener(String userName){
		this.userName = userName;
	}
	@Override
	public void onMessage(Message message) {
		
		try {
			System.out.println("aus topic ausgelöst");
			ChatPDU receivedPDU = (ChatPDU) ((ObjectMessage) message).getObject();
			PduType pduType = receivedPDU.getPduType();
			String offset = "Nachricht wird mit typ " + pduType.toString() + " für " +
					receivedPDU.getUserName();
			//Entscheidung ob Nachricht für den Empfänger bestimmt ist 
			//oder ob Sie nur autoamtisch durch das Topic auslesen auch
			//bei ihm angekommen ist:
			//wenn PDU from Typ ***_Response ist und der UserName nicht der des
			//Empfängers ist wird die Nachricht ignoriert
			if((pduType == PduType.LOGIN_RESPONSE || 
				pduType == PduType.LOGOUT_RESPONSE || 
				pduType == PduType.CHAT_MESSAGE_RESPONSE) && 
				!receivedPDU.getUserName().equals(userName))
			{
				System.out.println(offset + " wird ignoriert");
			} else {
				System.out.println(offset + " wird zur Verarbeitung freigegeben");
				// TODO: PDU an Geschäftslogik übergeben
				// nicht implementiert
			}
			System.out.println(receivedPDU.getPduType());
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
