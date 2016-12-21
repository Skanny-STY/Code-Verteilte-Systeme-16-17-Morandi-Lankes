package database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SuppressWarnings("serial")
@Entity // markiert Klasse als Entit‰t

public class EntitiyTrace {

	@Id // Primärschlüssel
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Attribut automatisch
														// bef¸llen
	private int id;

	// Name des Absenders
	private String userName;

	// Chatnachricht
	private String message;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}