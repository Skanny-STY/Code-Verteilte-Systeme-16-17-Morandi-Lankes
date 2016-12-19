package jmsComm;

import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.PduType;
import common.ClientConversationStatus;
import common.ClientListEntry;
import common.Connection;
import common.SharedChatClientList;
import edu.hm.dako.chat.common.ExceptionHandler;

public class Messagehandler {
	
	private Log log = new Log4JLogger();
	private final SharedChatClientList clients = SharedChatClientList.getInstance();
	private Connection connection;
	
	protected AtomicInteger logoutCounter;
	protected AtomicInteger eventCounter;
	protected AtomicInteger confirmCounter;
	
	public Messagehandler(Connection connection){
	
		this.connection = connection;
		
	}
	
	
	public void handleMessage(ChatPDU receivedPDU){
	
	receivedPDU.getPduType();
	System.out.println("next!");
	loginRequestAction(receivedPDU);
			
//		try {
//			switch (receivedPDU.getPduType()) {
//	
//			case LOGIN_REQUEST:
//				// Login-Request vom Client empfangen
//				loginRequestAction(receivedPDU);
//				break;
//	
//			case CHAT_MESSAGE_REQUEST:
//				// Chat-Nachricht angekommen, an alle verteilen
//		//		chatMessageRequestAction(receivedPDU);
//				break;
//	
//			case LOGOUT_REQUEST:
//				// Logout-Request vom Client empfangen
//		//		logoutRequestAction(receivedPDU);
//				break;
//	
//			default:
//				log.debug("Falsche PDU empfangen von Client: " + receivedPDU.getUserName()
//						+ ", PduType: " + receivedPDU.getPduType());
//				break;
//			}
//		} catch (Exception e) {
//			log.error("Exception bei der Nachrichtenverarbeitung");
//			ExceptionHandler.logExceptionAndTerminate(e);
//		}
	}
	
	
	
	
	protected void sendLoginListUpdateEvent(ChatPDU pdu) {
		
		System.out.println("in sendLoginUpdateEvent-Methode angekommen");
		// Liste der eingeloggten bzw. sich einloggenden User ermitteln
		Vector<String> clientList = clients.getRegisteredClientNameList();

		log.debug("Aktuelle Clientliste, die an die Clients uebertragen wird: " + clientList);

		pdu.setClients(clientList);

		try {
			connection.send(pdu);
			log.debug("Login- oder Logout-Event-PDU an topic gesendet");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			// passiert scho nix
			e1.printStackTrace();
		}
		Vector<String> clientList2 = clients.getClientNameList();
		for (String s : new Vector<String>(clientList2)) {

			ClientListEntry client = clients.getClient(s);
			System.out.println(client.getUserName());
			try {

				log.debug(
						"Login- oder Logout-Event-PDU an " + client.getUserName() + " gesendet");
				clients.incrNumberOfSentChatEvents(client.getUserName());
				if(eventCounter != null){
					eventCounter.getAndIncrement();
				}
				System.out.println("faack ju");
				log.debug(s + ": EventCounter bei Login/Logout erhoeht = "
						+ eventCounter.get() + ", ConfirmCounter = " + confirmCounter.get());
			
			} catch (Exception e) {
				log.debug(
						"Senden einer Login- oder Logout-Event-PDU an " + s + " nicht moeglich");
				ExceptionHandler.logException(e);
			}
		}
	}
	
	protected void loginRequestAction(ChatPDU receivedPdu) {

		ChatPDU pdu;
		log.debug("Login-Request-PDU fuer " + receivedPdu.getUserName() + " empfangen");

		// Neuer Client moechte sich einloggen, Client in Client-Liste
		// eintragen
		if (!clients.existsClient(receivedPdu.getUserName())) {
			log.debug("User nicht in Clientliste: " + receivedPdu.getUserName());
			ClientListEntry client = new ClientListEntry(receivedPdu.getUserName(), connection);
			client.setLoginTime(System.nanoTime());
			clients.createClient(receivedPdu.getUserName(), client);
			clients.changeClientStatus(receivedPdu.getUserName(),
					ClientConversationStatus.REGISTERING);
			log.debug("User " + receivedPdu.getUserName() + " nun in Clientliste");

			String userName = receivedPdu.getUserName();
		//	clientThreadName = receivedPdu.getClientThreadName();
			Thread.currentThread().setName(receivedPdu.getUserName());
			log.debug("Laenge der Clientliste: " + clients.size());
		//	serverGuiInterface.incrNumberOfLoggedInClients();
			System.out.println(clients.getClient(receivedPdu.getUserName()));
			// Login-Event an alle Clients (auch an den gerade aktuell
			// anfragenden) senden
			pdu = ChatPDU.createLoginEventPdu(userName, receivedPdu);
			sendLoginListUpdateEvent(pdu);

			// Login Response senden
			ChatPDU responsePdu = ChatPDU.createLoginResponsePdu(userName, receivedPdu);

			try {
				connection.send(responsePdu);
			} catch (Exception e) {
				log.debug("Senden einer Login-Response-PDU an " + userName + " fehlgeschlagen");
				log.debug("Exception Message: " + e.getMessage());
			}

			log.debug("Login-Response-PDU an Client " + userName + " gesendet");

			// Zustand des Clients aendern
			clients.changeClientStatus(userName, ClientConversationStatus.REGISTERED);

		} else {
			// User bereits angemeldet, Fehlermeldung an Client senden,
			// Fehlercode an Client senden
			pdu = ChatPDU.createLoginErrorResponsePdu(receivedPdu, ChatPDU.LOGIN_ERROR);

			try {
				connection.send(pdu);
				log.debug("Login-Response-PDU an " + receivedPdu.getUserName()
						+ " mit Fehlercode " + ChatPDU.LOGIN_ERROR + " gesendet");
			} catch (Exception e) {
				log.debug("Senden einer Login-Response-PDU an " + receivedPdu.getUserName()
						+ " nicth moeglich");
				ExceptionHandler.logExceptionAndTerminate(e);
			}
		}
	}
//
//	@Override
//	protected void logoutRequestAction(ChatPDU receivedPdu) {
//
//		ChatPDU pdu;
//		logoutCounter.getAndIncrement();
//		log.debug("Logout-Request von " + receivedPdu.getUserName() + ", LogoutCount = "
//				+ logoutCounter.get());
//
//		log.debug("Logout-Request-PDU von " + receivedPdu.getUserName() + " empfangen");
//
//		if (!clients.existsClient(userName)) {
//			log.debug("User nicht in Clientliste: " + receivedPdu.getUserName());
//		} else {
//
//			// Event an Client versenden
//			pdu = ChatPDU.createLogoutEventPdu(userName, receivedPdu);
//
//			clients.changeClientStatus(receivedPdu.getUserName(),
//					ClientConversationStatus.UNREGISTERING);
//			sendLoginListUpdateEvent(pdu);
//			serverGuiInterface.decrNumberOfLoggedInClients();
//
//			clients.changeClientStatus(receivedPdu.getUserName(),
//					ClientConversationStatus.UNREGISTERED);
//			// Logout Response senden
//			sendLogoutResponse(receivedPdu.getUserName());
//			// Worker-Thread des Clients, der den Logout-Request gesendet
//			// hat, auch gleich zum Beenden markieren
//			clients.finish(receivedPdu.getUserName());
//			log.debug("Laenge der Clientliste beim Vormerken zum Loeschen von "
//					+ receivedPdu.getUserName() + ": " + clients.size());
//		}
//	}
//
//	@Override
//	protected void chatMessageRequestAction(ChatPDU receivedPdu) {
//
//		ClientListEntry client = null;
//		clients.setRequestStartTime(receivedPdu.getUserName(), startTime);
//		clients.incrNumberOfReceivedChatMessages(receivedPdu.getUserName());
//		serverGuiInterface.incrNumberOfRequests();
//		log.debug("Chat-Message-Request-PDU von " + receivedPdu.getUserName()
//				+ " mit Sequenznummer " + receivedPdu.getSequenceNumber() + " empfangen");
//
//		if (!clients.existsClient(receivedPdu.getUserName())) {
//			log.debug("User nicht in Clientliste: " + receivedPdu.getUserName());
//		} else {
//			// Liste der betroffenen Clients ermitteln
//			Vector<String> sendList = clients.getClientNameList();
//			ChatPDU pdu = ChatPDU.createChatMessageEventPdu(userName, receivedPdu);
//
//			// Event an Clients senden
//			for (String s : new Vector<String>(sendList)) {
//				client = clients.getClient(s);
//				try {
//					if ((client != null)
//							&& (client.getStatus() != ClientConversationStatus.UNREGISTERED)) {
//						pdu.setUserName(client.getUserName());
//						client.getConnection().send(pdu);
//						log.debug("Chat-Event-PDU an " + client.getUserName() + " gesendet");
//						clients.incrNumberOfSentChatEvents(client.getUserName());
//						eventCounter.getAndIncrement();
//						log.debug(userName + ": EventCounter erhoeht = " + eventCounter.get()
//								+ ", Aktueller ConfirmCounter = " + confirmCounter.get()
//								+ ", Anzahl gesendeter ChatMessages von dem Client = "
//								+ receivedPdu.getSequenceNumber());
//					}
//				} catch (Exception e) {
//					log.debug("Senden einer Chat-Event-PDU an " + client.getUserName()
//							+ " nicht moeglich");
//					ExceptionHandler.logException(e);
//				}
//			}
//
//			client = clients.getClient(receivedPdu.getUserName());
//			if (client != null) {
//				ChatPDU responsePdu = ChatPDU.createChatMessageResponsePdu(
//						receivedPdu.getUserName(), 0, 0, 0, 0,
//						client.getNumberOfReceivedChatMessages(), receivedPdu.getClientThreadName(),
//						(System.nanoTime() - client.getStartTime()));
//
//				if (responsePdu.getServerTime() / 1000000 > 100) {
//					log.debug(Thread.currentThread().getName()
//							+ ", Benoetigte Serverzeit vor dem Senden der Response-Nachricht > 100 ms: "
//							+ responsePdu.getServerTime() + " ns = "
//							+ responsePdu.getServerTime() / 1000000 + " ms");
//				}
//
//				try {
//					client.getConnection().send(responsePdu);
//					log.debug(
//							"Chat-Message-Response-PDU an " + receivedPdu.getUserName() + " gesendet");
//				} catch (Exception e) {
//					log.debug("Senden einer Chat-Message-Response-PDU an " + client.getUserName()
//							+ " nicht moeglich");
//					ExceptionHandler.logExceptionAndTerminate(e);
//				}
//			}
//			log.debug("Aktuelle Laenge der Clientliste: " + clients.size());
//		}
//}
}