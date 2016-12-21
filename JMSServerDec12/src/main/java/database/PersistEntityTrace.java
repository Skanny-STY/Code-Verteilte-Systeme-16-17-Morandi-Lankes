//package database;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.persistence.PersistenceContextType;
//
//public class PersistEntityTrace {
//	@PersistenceContext(unitName="tabelleTrace",type=PersistenceContextType.TRANSACTION)
//	private EntityManager emt;
//	
//	// Daten in DB speichern
//		public void addTraceValues(EntityTrace entityTrace) {
//			emt.persist(entityTrace);
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
