package database;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


	@SuppressWarnings("serial")
	@Entity
	@Table(name = "tabelleCount")
	public class EntityCount implements Serializable {  // um nicht Klassennamen zu verwenden wird Tabellennamen definiert

		@Id // Primärschlüssel
		@GeneratedValue(strategy = GenerationType.IDENTITY) // Attribut automatisch befüllen 
		private int id;
		
	
		// Name des Users
		private String userName;
		
		// gezählte Confirms
		private long numberOfReceivedConfirms;
				
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

		public long getNumberOfReceivedConfirms() {
			return numberOfReceivedConfirms;
		}
		
		public void setNumberOfReceivedConfirms(long numberOfReceivedConfirms) {
			this.numberOfReceivedConfirms = numberOfReceivedConfirms;
		}
	}



