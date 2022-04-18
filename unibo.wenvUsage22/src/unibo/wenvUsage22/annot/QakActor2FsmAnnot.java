package unibo.wenvUsage22.annot;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22;
import unibo.actor22comm.utils.ColorsOut;

public abstract class QakActor2FsmAnnot  extends QakActor22{

	public QakActor2FsmAnnot(String name) {
		super(name);
		readAnnots(this);
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
	@Override
	protected void handleMsg(IApplMessage msg) {
 		
	}

}
