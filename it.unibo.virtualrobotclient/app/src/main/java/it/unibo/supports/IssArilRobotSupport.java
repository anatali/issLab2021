/**
 IssArilRobotSupport.java
 ===============================================================
 Implements interaction with the virtual robot using the aril
 and the given communication support.
 Adapts the application to the cril
 ===============================================================
 */
package it.unibo.supports;

import it.unibo.annotations.*;
import it.unibo.interaction.*;

import java.io.IOException;
import java.util.HashMap;

public class IssArilRobotSupport implements IssCommSupport {
    private IssCommSupport support;
    private static HashMap<String, Integer> timemap = new HashMap<String, Integer>( );

    public IssArilRobotSupport(Object supportedObj, IssCommSupport support){
        this.support   = support;
        IssAnnotationUtil.getMoveTimes( supportedObj, timemap );
    }
    public IssArilRobotSupport( String robotConfigFile, IssCommSupport support){
        this.support   = support;
        if( ! IssAnnotationUtil.checkRobotConfigFile(robotConfigFile, timemap) ){
            timemap.put("h", MsgRobotUtil.htime );
            timemap.put("l", MsgRobotUtil.ltime );
            timemap.put("r", MsgRobotUtil.rtime );
            timemap.put("w", MsgRobotUtil.wtime );
            timemap.put("s", MsgRobotUtil.stime );
        };
    }

    //The movetime is takan form the timemap, that is configured via annotations
    protected String translate(String arilMove){
        //System.out.println( "        IssArilRobotSupport | translate:" + arilMove );
        switch( arilMove.trim() ){ //translate into critl move
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
        //System.out.println( "         IssArilRobotSupport | WARNING: reply NOT IMPLEMENTED"  );
    }

    @Override
    public void registerObserver( IssObserver obs ){
       //TODO
    }
    @Override
    public void removeObserver( IssObserver obs ){
        //TODO
    }
    @Override
    public void close(){
        try {
            support.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
