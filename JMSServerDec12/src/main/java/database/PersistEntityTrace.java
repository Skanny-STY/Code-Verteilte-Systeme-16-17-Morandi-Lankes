//package database;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.PersistenceContextType;
//
//public class PersistEntityTrace {
//	@PersistenceContext(unitName="tabelleTrace",type=PersistenceContextType.TRANSACTION)
//	private EntityManager emTrace;
//	
//	// Daten aus entitiyobject in Tabelle eintragen
//		public void insertValues(EntityTrace entityTrace) {
//			emTrace.persist(entityTrace);
//		}
//}
//
//	
//	/*public TraceProcess create(TraceProcess traceProcess){
//			emt.persist(traceProcess);
//      return traceProcess;
//    }*/
//	
//	
//
//
