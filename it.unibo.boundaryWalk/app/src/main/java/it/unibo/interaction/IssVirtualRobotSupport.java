/**
 IssRobotSupport.java
 ===============================================================
 Implements interaction with the virtual robot using the aril
 and the given communication support.
 Adapts the application to the cril
 ===============================================================
 */
package it.unibo.interaction;
import java.util.HashMap;

//@RobotMoveTimeSpec  //could be useful?
public class IssVirtualRobotSupport implements IssOperations{
    private IssOperations support;
    private static HashMap<String, Integer> timemap = new HashMap<String, Integer>( );

    public IssVirtualRobotSupport(Object supportedObj, IssOperations support){
        this.support   = support;
        IssAnnotationUtil.getMoveTimes( supportedObj, timemap );
    }

    //The movetime is takan form the timemap, that is configured via annotations
    protected String translate(String arilMove){
        switch( arilMove ){ //translate into critl move
            case "h" : return "{\"robotmove\":\"alarm\", \"time\": "+ timemap.get("h")+"}";
            case "w" : return "{\"robotmove\":\"moveForward\", \"time\": "+ timemap.get("w")+"}";
            case "s" : return "{\"robotmove\":\"moveBackward\", \"time\": "+ timemap.get("s")+"}";
            case "l" : return "{\"robotmove\":\"turnLeft\", \"time\": "+ timemap.get("l")   + "}";
            case "r" : return "{\"robotmove\":\"turnRight\", \"time\":"+ timemap.get("r")   + "}";
            default  : return "{\"robotmove\":\"alarm\", \"time\": "+ timemap.get("h")+"}" ; //to avoid exceptions
        }
    }

    @Override
    public void forward( String move )   { //move = h | w | s | l | r
        support.forward( translate(move) );
    }

    @Override
    public String requestSynch( String move ) {   //move = h | w | s | l | r
        return support.requestSynch( translate(move) );
    }

    @Override
    public void request( String move ) {
        support.request( translate(move)  );  //the answer is lost ...
    }

    @Override
    public void reply(String msg) {
        //System.out.println( "         IssVirtualRobotSupport | WARNING: reply NOT IMPLEMENTED"  );
    }
}
