//package database;
//
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.Table;
//
//@SuppressWarnings("serial")
//@Entity // markiert Klasse als Entity
//@Table(name = "tabelleTrace")
//public class EntityTrace {
//
//	@Id // Primärschlüssel
//	@GeneratedValue(strategy = GenerationType.IDENTITY) // Attribut automatisch
//														// bef¸llen
//	private int id;
//
//	// Name des Absenders
//	private String userName;
//
//	// Chatnachricht
//	private String message;
//
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}
//
//	public String getUserName() {
//		return userName;
//	}
//
//	public void setUserName(String userName) {
//		this.userName = userName;
//	}
//
//	public String getMessage() {
//		return message;
//	}
//
//	public void setMessage(String message) {
//		this.message = message;
//	}
//
//}