package database;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity // markiert Klasse als Entitaet
@Table(name = "tabelleTrace")
public class EntityTrace {

	@Id // Primärschlüssel
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Attribut automatisch
														// bef¸llen
	private int id;

	// Name des ClientThreads
	private String ClientThreadName;

	// Name des ServerThreads
	private String ServerThreadName;
	
	// Chatnachricht
	private String message;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientThreadName() {
		return ClientThreadName;
	}

	public void setClientThreadName(String clientThreadName) {
		ClientThreadName = clientThreadName;
	}

	public String getServerThreaName() {
		return ServerThreadName;
	}

	public void setServerThreaName(String serverThreaName) {
		ServerThreadName = serverThreaName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}