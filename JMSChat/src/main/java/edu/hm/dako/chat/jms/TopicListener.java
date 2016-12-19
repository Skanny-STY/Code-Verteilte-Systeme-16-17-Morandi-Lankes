package edu.hm.dako.chat.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import edu.hm.dako.chat.common.*;

public class TopicListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		
		try {
			System.out.println("aus topic ausgelöst");
			ChatPDU receivedPDU = (ChatPDU) ((ObjectMessage) message).getObject();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

}
