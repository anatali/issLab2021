package unibo.wenvUsage22.actors;

import java.lang.reflect.Method;
import java.util.Vector;
import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;
import unibo.wenvUsage22.annot.State;
import unibo.wenvUsage22.annot.Transition;
 

public abstract class QakActor22FsmAnnot  extends QakActor22Fsm{
protected QakActor22FsmAnnot myself;
protected String initialState = null;

	public QakActor22FsmAnnot(String name) {
		super(name);
		myself = this;
		//readAnnots(this);
 	}
//    protected void readAnnots(Object element ) {
//    	try {
//		      ColorsOut.outappl("readAnnots: "+ element  , ColorsOut.CYAN);
//    		  //Class<?> clazz            = element.getClass();
//    		  Annotation[] annotations  = element.getClass().getAnnotations();
//		      ColorsOut.outappl("readAnnots: "+ annotations.length  , ColorsOut.CYAN);
//		      Method[] m = element.getClass().getDeclaredMethods( );
//		      ColorsOut.outappl("method: "+ m.length  , ColorsOut.CYAN);
//		      
//    		  for( int i=0; i<m.length; i++ ) {
//    			  m[i].setAccessible(true);
//    			  if( m[i].getName().startsWith("s0"))
//    				  ColorsOut.outappl("  " + m[i]  , ColorsOut.CYAN);
//				  if( m[i].isAnnotationPresent(StateSpec.class)) {
//    				  ColorsOut.outappl("state annot found= "  , ColorsOut.CYAN);
//    				  StateSpec annot=m[i].getAnnotation(StateSpec.class);
////    			  Annotation[] mannots  = m[i].getAnnotations();
////        		  for (Annotation annot : mannots) {
////          		    if (annot instanceof StateSpec) {
//          		      //StateSpec p  = (StateSpec) annot;
//          		      ColorsOut.outappl("state ANNOT name= "+ annot.name()  , ColorsOut.CYAN);
//             		}
// 
//    			  
//    			 //
//    		  }
//    		  for (Annotation annot : annotations) {
//    		    if (annot instanceof StateSpec) {
//    		      StateSpec p  = (StateSpec) annot;
//    		      ColorsOut.outappl("state name= "+ p.name()  , ColorsOut.CYAN);
//       		    }
//    		  }
//    		} catch (Exception e) {
//    			ColorsOut.outerr("readAnnots ERROR:" + e.getMessage() )   ;	
//    		}
//    }

	@Override
	protected void setTheInitialState(  ) {	}   //No more necessary for annotations
	protected void setTheInitialState( String stateName ) {
     if( initialState == null  ) {
		initialState= stateName;
		declareAsInitialState( initialState );
	 } else ColorsOut.outerr("Multiple intial states not allowed" );		
	}	
	
	protected void elabStateMethod(Method m, String stateName) {
		if( ! m.getName().equals(stateName)) {
			ColorsOut.outerr(getName() + " | QakActor22FsmAnnot  Method name must be the same as state name" );
		}
		  Vector<String> nextStates = new Vector<String>();
		  Vector<String> msgIds     = new Vector<String>();
		  
		  if( m.isAnnotationPresent(Transition.class)) {
			  Transition t = m.getAnnotation(Transition.class);
			  for( int k=0; k<t.state().length; k++) {
				  String nextState = t.state()[k];
				  nextStates.add(nextState);
				  String msgId     = t.msgId()[k];
				  msgIds.add(msgId);
			  }
		  }
// 		  ColorsOut.outappl("nextStates "+ nextStates.size() , ColorsOut.CYAN);
//		  ColorsOut.outappl("msgIds "+ msgIds.size() , ColorsOut.CYAN);
		  doDeclareState(m,stateName,nextStates,msgIds );
		
	}
	
	protected void doDeclareState(Method curMethod, String stateName, Vector<String> nextStates, Vector<String> msgIds) {
		  declareState( stateName, new StateActionFun() {
				@Override
				public void run( IApplMessage msg ) {
				try {
  					//outInfo("uuuu "+ msg  + " " + this );	
  					curMethod.invoke(  myself, msg   );  //I metodi hanno this come arg implicito
  					for( int j=0; j<nextStates.size();j++ ) {
   						addTransition( nextStates.elementAt(j), msgIds.elementAt(j) );
  					}					
  					nextState();
				} catch ( Exception e) {
						ColorsOut.outerr("wrong execution for:"+ stateName + " - " + e.getMessage());
				}
    			}			        			  
		  });//declareState		
	}
	
	protected void elabAnnotatedMethod(Method m) {
		String functor =  m.getName();		    	 
		Class<?>[] p   =  m.getParameterTypes();
//    		  ColorsOut.outappl("state ANNOT functor="+ functor + " p.length=" + p.length , ColorsOut.CYAN);
//    		  ColorsOut.outappl(""+p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage") , ColorsOut.CYAN);
		if( p.length==0 || p.length>1 || 
    				  ! p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage") ) {
    			  ColorsOut.outerr("wrong arguments for state:"+ functor);
		}else {
			State stateAnnot=m.getAnnotation(State.class);
			if( stateAnnot.initial() )  setTheInitialState(stateAnnot.name());
			//ColorsOut.outappl("state ANNOT name= "+ stateAnnot.name() + " initialState=" + initialState, ColorsOut.CYAN);
			elabStateMethod( m, stateAnnot.name());		  
		}
 	}
	
	@Override
	protected void declareTheStates( ) {
    	try {
 		  Method[] m = this.getClass().getDeclaredMethods( );
		  //ColorsOut.outappl("method: "+ m.length  , ColorsOut.CYAN);		      
  		  for( int i=0; i<m.length; i++ ) {
  			  m[i].setAccessible(true);
 		      if( m[i].isAnnotationPresent(State.class)) //{
 		    	 elabAnnotatedMethod(m[i]);
// 		    	  String functor =  m[i].getName();		    	 
// 		    	  Class<?>[] p   =  m[i].getParameterTypes();
////        		  ColorsOut.outappl("state ANNOT functor="+ functor + " p.length=" + p.length , ColorsOut.CYAN);
////        		  ColorsOut.outappl(""+p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage") , ColorsOut.CYAN);
//        		  if( p.length==0 || p.length>1 || ! p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage") ) {
//        			  ColorsOut.outerr("wrong declaration for state:"+ functor);
//        		  }
// 				  StateSpec stateAnnot=m[i].getAnnotation(StateSpec.class);
// 				  if( stateAnnot.initial() )  setTheInitialState(stateAnnot.name());
//// 					 if( initialState == null  ) {
////	 					 initialState= annot.name();
////	 					 declareAsInitialState( initialState );
//// 				  	} else ColorsOut.outerr("Multiple intial states not allowed" );
// 
// 				  
// 				  ColorsOut.outappl("state ANNOT name= "+ stateAnnot.name() + " initialState=" + initialState, ColorsOut.CYAN);
////        		  final Method curMethod = m[i];
//        		  
//        		  elabStateMethod( m[i], stateAnnot.name());
////        		  Vector<String> nextStates = new Vector<String>();
//        		  Vector<String> msgIds     = new Vector<String>();
//       		  
//        		  if( m[i].isAnnotationPresent(StateTransitionSpec.class)) {
//        			  StateTransitionSpec t = m[i].getAnnotation(StateTransitionSpec.class);
//        			  for( int k=0; k<t.state().length; k++) {
//        				  String nextState = t.state()[k];
//        				  nextStates.add(nextState);
//        				  String msgId     = t.msgId()[k];
//        				  msgIds.add(msgId);
//        			  }
//        		  }
//           		  ColorsOut.outappl("nextStates "+ nextStates.size() , ColorsOut.CYAN);
//          		  ColorsOut.outappl("msgIds "+ msgIds.size() , ColorsOut.CYAN);
//          		  doDeclareState(curMethod,stateAnnot.name(),nextStates,msgIds );
//          		  declareState( stateAnnot.name(), new StateActionFun() {
//        				@Override
//        				public void run( IApplMessage msg ) {
//        					try {
//            					outInfo("uuuu "+ msg  + " " + this );	
//            					curMethod.invoke(  myself, msg   );  //I metodi hanno this come arg implicito
//
//            					for( int j=0; j<nextStates.size();j++ ) {
//            						//addTransition( nextStates.elementAt(0), msgIds.elementAt(0) );
//            						addTransition( nextStates.elementAt(j), msgIds.elementAt(j) );
//            					}
//            					
//            					nextState();
//							} catch ( Exception e) {
//								ColorsOut.outerr("wrong execution for:"+ functor + " - " + e.getMessage());
// 							}
//              				}			        			  
//        		  });//declareState
// 		      }//if
  		  }//for
   		} catch (Exception e) {
  			ColorsOut.outerr("readAnnots ERROR:" + e.getMessage() )   ;	
  		}		
 
	}
}
