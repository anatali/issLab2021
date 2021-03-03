/**
 IssAppRobotSupport.java
 ===============================================================
 Abstract robot as object with AppMsg
 Later: robotActor
 ===============================================================
 */
package it.unibo.robotSupports;
import it.unibo.interaction.AppMsg;
import it.unibo.annotations.IssAnnotationUtil;
import it.unibo.interaction.IssAppOperations;
import it.unibo.interaction.IssOperations;

import java.util.HashMap;

public class IssAppRobotSupport implements IssAppOperations {  //
    private IssOperations support;
    private static HashMap<String, Integer> timemap = new HashMap<String, Integer>( );

    public IssAppRobotSupport(Object supportedObj, IssOperations support ){
        this.support   = support;
        IssAnnotationUtil.getMoveTimes( supportedObj, timemap );
    }
    public IssAppRobotSupport( IssOperations support ){
        this.support   = support;

    }
    @Override
    public void forward( AppMsg msg ){
        //System.out.println("        IssAppRobotSupport | forward " + msg);
        String move = msg.getContent();    //payload should be aril
        //System.out.println("        IssAppRobotSupport | forward move=" + move + " to " + msg.getReceiver());
        support.forward( move );
    }

    @Override
    public void request( AppMsg msg ) {
        //System.out.println("        IssAppRobotSupport | request " + msg);
        String move = msg.getContent();    //payload should be aril
        //System.out.println("        IssAppRobotSupport | request move=" + move + " to " + msg.getReceiver());
        support.request( move );

    }

    @Override
    public void reply( AppMsg msg ) {
        String move = msg.getContent();    //payload should be aril
        System.out.println("IssAppRobotSupport | reply move=" + move + " to " + msg.getReceiver());
        support.reply( move );
    }

    @Override
    public String requestSynch( AppMsg msg ) {
        //System.out.println("        IssAppRobotSupport | requestSynch " + msg);
        return support.requestSynch( msg.getContent() );
    }

}
