package unibo.wenvUsage22.annot;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Vector;

import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.interfaces.StateActionFun;
import unibo.actor22comm.utils.ColorsOut;
import unibo.wenvUsage22.actors.QakActor22Fsm;
import unibo.wenvUsage22.common.ApplData;

public abstract class QakActor2FsmAnnot  extends QakActor22Fsm{
private QakActor2FsmAnnot myself;

	public QakActor2FsmAnnot(String name) {
		super(name);
		myself = this;
		//readAnnots(this);
 	}
    protected void readAnnots(Object element ) {
    	try {
		      ColorsOut.outappl("readAnnots: "+ element  , ColorsOut.CYAN);
    		  //Class<?> clazz            = element.getClass();
    		  Annotation[] annotations  = element.getClass().getAnnotations();
		      ColorsOut.outappl("readAnnots: "+ annotations.length  , ColorsOut.CYAN);
		      Method[] m = element.getClass().getDeclaredMethods( );
		      ColorsOut.outappl("method: "+ m.length  , ColorsOut.CYAN);
		      
    		  for( int i=0; i<m.length; i++ ) {
    			  m[i].setAccessible(true);
    			  if( m[i].getName().startsWith("s0"))
    				  ColorsOut.outappl("  " + m[i]  , ColorsOut.CYAN);
				  if( m[i].isAnnotationPresent(StateSpec.class)) {
    				  ColorsOut.outappl("state annot found= "  , ColorsOut.CYAN);
    				  StateSpec annot=m[i].getAnnotation(StateSpec.class);
//    			  Annotation[] mannots  = m[i].getAnnotations();
//        		  for (Annotation annot : mannots) {
//          		    if (annot instanceof StateSpec) {
          		      //StateSpec p  = (StateSpec) annot;
          		      ColorsOut.outappl("state ANNOT name= "+ annot.name()  , ColorsOut.CYAN);
             		}
 
    			  
    			 //
    		  }
    		  for (Annotation annot : annotations) {
    		    if (annot instanceof StateSpec) {
    		      StateSpec p  = (StateSpec) annot;
    		      ColorsOut.outappl("state name= "+ p.name()  , ColorsOut.CYAN);
       		    }
    		  }
    		} catch (Exception e) {
    			ColorsOut.outerr("readAnnots ERROR:" + e.getMessage() )   ;	
    		}
    }


	protected void declareTheStates( ) {
    	try {
 		  Method[] m = this.getClass().getDeclaredMethods( );
		  ColorsOut.outappl("method: "+ m.length  , ColorsOut.CYAN);		      
  		  for( int i=0; i<m.length; i++ ) {
  			  m[i].setAccessible(true);
 		      if( m[i].isAnnotationPresent(StateSpec.class)) {
 		    	  String functor =  m[i].getName();
 		    	  Class<?>[] p   =  m[i].getParameterTypes();
//        		  ColorsOut.outappl("state ANNOT functor="+ functor + " p.length=" + p.length , ColorsOut.CYAN);
//        		  ColorsOut.outappl(""+p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage") , ColorsOut.CYAN);
        		  if( p.length==0 || p.length>1 || ! p[0].getCanonicalName().equals("it.unibo.kactor.IApplMessage") ) {
        			  ColorsOut.outerr("wrong declaration for state:"+ functor);
        		  }
 				  StateSpec annot=m[i].getAnnotation(StateSpec.class);
        		  ColorsOut.outappl("state ANNOT name= "+ annot.name() , ColorsOut.CYAN);
        		  final Method curMethod = m[i];
        		  
        		  Vector<String> nextStates = new Vector<String>();
        		  Vector<String> msgIds     = new Vector<String>();
       		  
        		  if( m[i].isAnnotationPresent(StateTransitionSpec.class)) {
        			  StateTransitionSpec t = m[i].getAnnotation(StateTransitionSpec.class);
        			  for( int k=0; k<t.state().length; k++) {
        				  String nextState = t.state()[k];
        				  nextStates.add(nextState);
        				  String msgId     = t.msgId()[k];
        				  msgIds.add(msgId);
        			  }
        		  }
           		  ColorsOut.outappl("nextStates "+ nextStates.size() , ColorsOut.CYAN);
          		  ColorsOut.outappl("msgIds "+ msgIds.size() , ColorsOut.CYAN);
          		  
          		  declareState( annot.name(), new StateActionFun() {
        				@Override
        				public void run( IApplMessage msg ) {
        					try {
            					outInfo("uuuu "+ msg  + " " + this );	
            					curMethod.invoke(  myself, msg   );  //I metodi hanno this come arg implicito

            					for( int j=0; j<nextStates.size();j++ ) {
            						//addTransition( nextStates.elementAt(0), msgIds.elementAt(0) );
            						addTransition( nextStates.elementAt(j), msgIds.elementAt(j) );
            					}
            					
            					nextState();
							} catch ( Exception e) {
								ColorsOut.outerr("wrong execution for:"+ functor + " - " + e.getMessage());
 							}
//           					addTransition( functor, ApplData.moveCmdId ); //TODO generare
//        					nextState();
             				}			        			  
        		  });//declareState
 		      }//if
  		  }//for
   		} catch (Exception e) {
  			ColorsOut.outerr("readAnnots ERROR:" + e.getMessage() )   ;	
  		}		
 
		
		//per ciascuno stato dichiarato
		/*
		declareState( "s0", new StateActionFun() {
			@Override
			public void run(IApplMessage msg) {
				//outInfo(""+msg);	
				s0(msg);
				addTransition( "s1", ApplData.moveCmdId );
				nextState();
			}			
		});
		*/
	}
}
