/**
 IssRobot.java
 ===============================================================
 Abstract robot as object with AppMsg
 Later: robotActor
 ===============================================================
 */
package it.unibo.interaction;
import java.util.HashMap;

public class IssAppRobotSupport implements IssAppOperations {  //
    private IssOperations support;
    private static HashMap<String, Integer> timemap = new HashMap<String, Integer>( );

    public IssAppRobotSupport(Object supportedObj, IssOperations support ){
        this.support   = support;
        IssAnnotationUtil.getMoveTimes( supportedObj, timemap );
    }

    @Override
    public void forward( AppMsg  msg ){
        System.out.println("IssRobot | forward msg=" + msg);
        String move = msg.getContent();    //payload should be aril
         System.out.println("IssRobot | forward move=" + move + " to " + msg.getReceiver());
         support.forward( move );
    }

    @Override
    public void request( AppMsg msg ) {
        String move = msg.getContent();    //payload should be aril
        System.out.println("IssRobot | request move=" + move + " to " + msg.getReceiver());
        support.request( move );

    }

    @Override
    public void reply( AppMsg msg ) {
        String move = msg.getContent();    //payload should be aril
        System.out.println("IssRobot | reply move=" + move + " to " + msg.getReceiver());
        support.reply( move );
    }

    //@Override
    //public AppMsg requestSynch( AppMsg move ) {    }

}
