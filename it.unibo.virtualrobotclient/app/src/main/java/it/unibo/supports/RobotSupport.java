/*
===============================================================
RobotSupport.java
Utility class that should be used by a robot controller
(e.g. RobotControllerBoundary)

This resource implements asynch IssOperations so to avoid move not allowed
===============================================================
*/
package it.unibo.supports;

import it.unibo.interaction.IssObserver;
import it.unibo.interaction.IssOperations;
import it.unibo.interaction.MsgRobotUtil;
import org.json.JSONObject;

public class RobotSupport implements IssCommSupport{
    private IssCommSupport rs;

    public RobotSupport(IssCommSupport rs){
        this.rs = rs;
    }

    public void request(String jsonMoveStr ) {
        //if( rs instanceof  IssWsSupport )   doRobotMoveDelay(jsonMoveStr,rs);
        //else if( rs instanceof  IssHttpSupport ) rs.request(jsonMoveStr);
        forward( jsonMoveStr );
    }
    public void forward(String jsonMoveStr ) {
        if( rs instanceof  IssWsSupport ) doRobotMoveAndDelay(jsonMoveStr,rs);
        else if( rs instanceof  IssHttpSupport ) rs.request(jsonMoveStr);
    }
    public void reply( String msg ){
        rs.forward( msg );
    }
    public String requestSynch( String msg ){
        return rs.requestSynch( msg );  //if( rs instanceof  IssWsSupport ) wait the answer
    }

    //Utility === requestSynch? NO!
    //Since a controller must have the possibility of capture other 'events'
    public static void doRobotMoveAndDelay(String jsonMoveStr, IssOperations rs) {
        System.out.println(jsonMoveStr);
        //"{\"robotmove\":\"...\", \"time\": ...}";
        JSONObject jsonObj = new JSONObject(jsonMoveStr);
        int time = Integer.parseInt( jsonObj.get("time").toString() );
        rs.forward( jsonMoveStr );  //The answer is handled by the controllers
        try { Thread.sleep(time+100); } catch (InterruptedException e) { e.printStackTrace(); }
    }

    public static String doBoundarySynch(int stepNum, String journey, IssOperations rs) {
        if (stepNum > 4) {
            return journey;
        }
        String answer = rs.requestSynch( MsgRobotUtil.wMsg );
        while( answer.equals("true") ){
            journey = journey + "w";
            answer = rs.requestSynch( MsgRobotUtil.wMsg );
        }
        //collision
        rs.requestSynch(MsgRobotUtil.lMsg);
        return doBoundarySynch(stepNum + 1, journey + "l", rs);
    }

//---------------------------------------------------------------
    public void registerObserver( IssObserver obs ){
        rs.registerObserver( obs );
    }
    public void removeObserver( IssObserver obs ){
        rs.removeObserver( obs );
    }
    public void close(){
        rs.close();
    }

}
