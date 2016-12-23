package edu.hm.dako.chat.client;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.ClientConversationStatus;
import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.common.PduType;
import edu.hm.dako.chat.common.SystemConstants;
import edu.hm.dako.chat.jms.JMSConnection;



/**
 * <p/>
 * Verwaltet eine Verbindung zum Server.
 *
 * @author Mandl
 */
public class ClientImpl extends AbstractChatClient {

	JMSConnection connection ;
	/**
	 * Konstruktor
	 * 
	 * @param userInterface
	 *          Schnittstelle zum User-Interface
	 * @param serverPort
	 *          Portnummer des Servers
	 * @param remoteServerAddress
	 *          IP-Adresse/Hostname des Servers
	 */

	public ClientImpl(ClientUserInterface userInterface, int serverPort,
			String remoteServerAddress, String serverType) {


		this.userInterface = userInterface;
		this.serverPort = serverPort;
		this.remoteServerAddress = remoteServerAddress;
		

		log.debug("Verbindung zum Server steht");
		/*
		 * Gemeinsame Datenstruktur aufbauen
		 */
		sharedClientData = new SharedClientData();
		sharedClientData.messageCounter = new AtomicInteger(0);
		sharedClientData.logoutCounter = new AtomicInteger(0);
		sharedClientData.eventCounter = new AtomicInteger(0);
		sharedClientData.confirmCounter = new AtomicInteger(0);
		sharedClientData.messageCounter = new AtomicInteger(0);

		
		String providerIPAndPort = remoteServerAddress + ":" + serverPort;
		Thread.currentThread().setName("Client");
		threadName = Thread.currentThread().getName();
		
		connection = new JMSConnection(providerIPAndPort);

		
		try {
			
			if (serverType.equals(SystemConstants.IMPL_JMS)) {
				messageListenerThread = new JMSMessageListenerThreadImpl(userInterface,
						connection, sharedClientData);
			}
//			} else if(serverType.equals(SystemConstants.IMPL_TCP_SIMPLE)){
//				// Simple TCP Server erzeugen
//				messageListenerThread = new SimpleMessageListenerThreadImpl(userInterface,
//						connection, sharedClientData);
//			}else {
//				//Advanced TCP Server einfügen
//			}
			
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionHandler.logException(e);
		}
	}
@Override
public void login(String name) throws IOException {

	userName = name;
	sharedClientData.userName = name;
	sharedClientData.status = ClientConversationStatus.REGISTERING;
	ChatPDU requestPdu = new ChatPDU();
	requestPdu.setPduType(PduType.LOGIN_REQUEST);
	requestPdu.setClientStatus(sharedClientData.status);
	Thread.currentThread().setName("Client-" + userName);
	requestPdu.setClientThreadName(Thread.currentThread().getName());
	requestPdu.setUserName(userName);
	try {
		//Senden 
		connection.send(requestPdu);
		log.debug("Login-Request-PDU fuer Client " + userName + " an Server gesendet");
		//Messagelistener scharf stellen
		connection.setMessageListener();

	} catch (Exception e) {
		System.out.println("das ist die fehlernachricht " + e.toString());// extra line for testing. bitte löschen
		throw new IOException();
	}
}

@Override
public void logout(String name) throws IOException {

	sharedClientData.status = ClientConversationStatus.UNREGISTERING;
	ChatPDU requestPdu = new ChatPDU();
	requestPdu.setPduType(PduType.LOGOUT_REQUEST);
	requestPdu.setClientStatus(sharedClientData.status);
	requestPdu.setClientThreadName(Thread.currentThread().getName());
	requestPdu.setUserName(userName);
	try {
		connection.send(requestPdu);
		sharedClientData.logoutCounter.getAndIncrement();
		log.debug("Logout-Request von " + requestPdu.getUserName()
				+ " gesendet, LogoutCount = " + sharedClientData.logoutCounter.get());

	} catch (Exception e) {
		log.debug("Senden der Logout-Nachricht nicht moeglich");
		throw new IOException();
	}
}

@Override
public void tell(String name, String text) throws IOException {

	ChatPDU requestPdu = new ChatPDU();
	requestPdu.setPduType(PduType.CHAT_MESSAGE_REQUEST);
	requestPdu.setClientStatus(sharedClientData.status);
	requestPdu.setClientThreadName(Thread.currentThread().getName());
	requestPdu.setUserName(userName);
	requestPdu.setMessage(text);
	sharedClientData.messageCounter.getAndIncrement();
	requestPdu.setSequenceNumber(sharedClientData.messageCounter.get());
	try {
		connection.send(requestPdu);
		log.debug("Chat-Message-Request-PDU fuer Client " + name
				+ " an Server gesendet, Inhalt: " + text);
		log.debug("MessageCounter: " + sharedClientData.messageCounter.get()
				+ ", SequenceNumber: " + requestPdu.getSequenceNumber());
	} catch (Exception e) {
		log.debug("Senden der Chat-Nachricht nicht moeglich");
		throw new IOException();
	}
}

@Override
public void cancelConnection() {
	try {
		connection.close();
	} catch (Exception e) {
		ExceptionHandler.logException(e);
	}
}
	
}