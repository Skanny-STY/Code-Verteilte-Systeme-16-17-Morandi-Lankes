package edu.hm.dako.chat.jms;

import edu.hm.dako.chat.connection.*;

public class Main {

	public static void main(String[] args) throws Exception {
		
		Connection jmsConnection = new JMSConnection("jms/queue/testqueue", "127.0.0.1:8080");
		jmsConnection.send("egalJetzt");
	}

}
