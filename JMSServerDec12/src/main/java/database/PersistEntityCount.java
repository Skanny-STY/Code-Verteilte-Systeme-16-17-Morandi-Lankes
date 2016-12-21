package database;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

@Stateless
public class PersistEntityCount {
	
	@PersistenceContext(unitName="tabellecount" ,type=PersistenceContextType.TRANSACTION)
	private EntityManager emc;
	

	public void insertValues(EntityCount entityCount) {
		emc.persist(entityCount);
		//emc.close();
            }
}