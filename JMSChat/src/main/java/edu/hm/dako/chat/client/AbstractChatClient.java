package edu.hm.dako.chat.client;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.hm.dako.chat.common.ChatPDU;
import edu.hm.dako.chat.common.ClientConversationStatus;
import edu.hm.dako.chat.common.ExceptionHandler;
import edu.hm.dako.chat.common.PduType;
import edu.hm.dako.chat.connection.DecoratingConnectionFactory;
import edu.hm.dako.chat.tcp.TcpConnectionFactory;

/**
 * Gemeinsame Funktionalitaet fuer alle Client-Implementierungen.
 * 
 * @author Peter Mandl
 *
 */
public abstract class AbstractChatClient implements ClientCommunication {

	protected static Log log = LogFactory.getLog(AbstractChatClient.class);

	// Username (Login-Kennung) des Clients
	protected String userName;

	protected String threadName;

	protected int localPort;

	protected int serverPort;
	protected String remoteServerAddress;

	protected ClientUserInterface userInterface;

	

	// Gemeinsame Daten des Clientthreads und dem Message-Listener-Threads
	protected SharedClientData sharedClientData;

	// Thread, der die ankommenden Nachrichten fuer den Client verarbeitet
	protected Thread messageListenerThread;

	/**
	 * @param userInterface
	 *          GUI-Interface
	 * @param serverPort
	 *          Port des Servers
	 * @param remoteServerAddress
	 *          Adresse des Servers
	 */


	/**
	 * Ergaenzt ConnectionFactory um Logging-Funktionalitaet
	 * 
	 * @param connectionFactory
	 *          ConnectionFactory
	 * @return Dekorierte ConnectionFactory
	 */
//	public static ConnectionFactory getDecoratedFactory(
//			ConnectionFactory connectionFactory) {
//		return new DecoratingConnectionFactory(connectionFactory);
//	}

	
}