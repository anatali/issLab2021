/*
===============================================================
RobotInputController.java
implements the business logic by handling messages received
on the cmdsocket-8091 or from the consoleGui
===============================================================
*/
package it.unibo.resumablebw;
import it.unibo.interaction.IssObserver;
import it.unibo.supports.IssCommSupport;
import org.json.JSONObject;

public class RobotApplInputController implements IssObserver {
private ResumableBoundaryLogic robotBehaviorLogic  ;
private IssCommSupport commSupport;

private boolean robotHalted  = true;

    public RobotApplInputController(IssCommSupport support, boolean usearil, boolean doMap){
        commSupport        = support;
        robotBehaviorLogic = new ResumableBoundaryLogic(support, usearil, doMap);
     }

    @Override
    public void handleInfo(String infoJson) {
        handleInfo( new JSONObject(infoJson) );
    }

    /*
    ENTRY
Handler of the messages sent by WENv over the cmdsocket-8091 to notify:
- the answer to a robot-command move {"endmove":"RESULT", "move":MOVE}
- the information emitted by a sonar { "sonarName": "sonarName", "distance": 1, "axis": "x" }
- a collision between the robot and an obstacle { "collision" : "false", "move": "moveForward"}

Handler of the robotcmd (STOP/RESUME) messages sent by the consoleGui
     */
    @Override
    public synchronized void  handleInfo(JSONObject infoJson) {
        System.out.println("RobotApplInputController | handleInfo:" + infoJson  );
        if( infoJson.has("endmove") )        handleEndMove(infoJson);
        else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        else if( infoJson.has("collision") ) handleCollision(infoJson);
        else if( infoJson.has("robotcmd") )  handleRobotCmd(infoJson);
    }

    protected void handleSonar( JSONObject sonarinfo ){
        String sonarname = (String)  sonarinfo.get("sonarName");
        int distance     = (Integer) sonarinfo.get("distance");
        //System.out.println("RobotInputController | handleSonar:" + sonarname + " distance=" + distance);
    }
    protected void handleCollision( JSONObject collisioninfo ){
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        //String move   = (String) collisioninfo.get("move");
        //System.out.println("RobotInputController | handleCollision move=" + move  );
    }
    protected void handleEndMove(JSONObject endmove ){

        String answer = (String) endmove.get("endmove");
        String move   = (String) endmove.get("move");   //moveForward, ...
        /*
        if(  robotHalted ) {
            robotBehaviorLogic.behaviourHaltedAfterMove(move, answer);
            return;
        }*/
        System.out.println("RobotInputController | handleEndMove:" + move + " answer=" + answer);
        switch( answer ){
            case "true"       : robotBehaviorLogic.boundaryStep( move,false, robotHalted);
                                break;
            case "false"      : robotBehaviorLogic.boundaryStep( move, true, robotHalted  );
                                break;
            case "halted"     : System.out.println("RobotInputController | handleEndMove to do halt" );break;
            case "notallowed" : System.out.println("RobotInputController | handleEndMove to do notallowed" );break;
            default           : System.out.println("RobotInputController | handleEndMove IMPOSSIBLE answer for move=" + move);
        }
    }

    protected void handleRobotCmd( JSONObject robotCmd ){
        String cmd = (String)  robotCmd.get("robotcmd");
        if( cmd.equals( "STOP" ) ){
            robotHalted = true;   //halt the robot, not the move
            System.out.println("RobotApplInputController | handleRobotCmd: robotHalted"    );
        }
        else if( cmd.equals( "RESUME" ) ){
            if( robotHalted ) {
                robotHalted = false;
                robotBehaviorLogic.doBoundaryGoon(   );   //activate the robot
            }else
                System.out.println("RobotApplInputController | handleRobotCmd robot already running"    );
        }
    }
}
