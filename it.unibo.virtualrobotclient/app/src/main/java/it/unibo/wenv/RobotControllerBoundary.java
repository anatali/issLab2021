/*
===============================================================
RobotObserver.java
handles messages received on the cmdsocket-8091
===============================================================
*/
package it.unibo.wenv;
import it.unibo.interaction.IssObserver;
import it.unibo.interaction.IssOperations;
import it.unibo.interaction.MsgRobotUtil;
import org.json.JSONObject;

public class RobotControllerBoundary implements IssObserver {
private int stepNum      = 1;
private String journey   = "";
private IssOperations rs ;

    public RobotControllerBoundary(IssOperations support){
        rs = support;
    }

    public void start(){
        doRobotAsynchMove( MsgRobotUtil.forwardMsg );
    }

    @Override
    public void handleInfo(String infoJson) {
        handleInfo( new JSONObject(infoJson) );
    }
    @Override
    public void handleInfo(JSONObject infoJson) {
        if( infoJson.has("endmove") ) boundary(infoJson);
    }
//Business logic
    protected void boundary( JSONObject mv ){
        String answer = (String) mv.get("endmove");
        String move   = (String) mv.get("move");
        System.out.println("RobotControllerBoundary | boundary stepNum:" + stepNum + " " + journey);
        if (stepNum <= 4) {
            if( move.equals("turnLeft") ){
                journey = journey + "l";
                stepNum++;
                doRobotAsynchMove( MsgRobotUtil.forwardMsg );
                return;
            }
            if( move.equals("moveForward") && answer.equals("true") ){
                journey = journey + "w";
                doRobotAsynchMove( MsgRobotUtil.forwardMsg );
            }else{ //collision
                doRobotAsynchMove(MsgRobotUtil.turnLeftMsg);
            }
        }else{ //steps ended
            System.out.println("RobotControllerBoundary | boundary journey:" + journey);
        }
    }

//Utility
    protected void doRobotAsynchMove(String jsonMoveStr) {
        System.out.println(jsonMoveStr);
        //"{\"robotmove\":\"...\", \"time\": ...}";
        JSONObject jsonObj = new JSONObject(jsonMoveStr);
        int time = Integer.parseInt( jsonObj.get("time").toString() );
        rs.forward( jsonMoveStr );
        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
        //The answer is handled by the controllers
    }


}
