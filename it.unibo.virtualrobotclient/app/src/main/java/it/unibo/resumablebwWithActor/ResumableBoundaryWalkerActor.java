package it.unibo.resumablebwWithActor;

import it.unibo.robotWithActorJava.RobotMovesInfo;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import org.json.JSONObject;

public class ResumableBoundaryWalkerActor extends ActorBasicJava {
    final String forwardMsg   = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    final String backwardMsg  = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    final String turnLeftMsg  = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg      = "{\"robotmove\":\"alarm\", \"time\": 100}";

    private enum State {start, walking, obstacle, end };
    private IssWsHttpJavaSupport support;
    private State curState       =  State.start ;
    private int stepNum          = 1;
    private RobotMovesInfo moves = new RobotMovesInfo(true);

    private boolean tripStopped  = true;

    public ResumableBoundaryWalkerActor(String name, IssWsHttpJavaSupport support) {
        super(name);
        this.support = support;
    }

    protected void fsm(String move, String endmove){
        System.out.println( myname + " | fsm state=" + curState + " tripStopped=" + tripStopped
                + " stepNum=" + stepNum + " move=" + move + " endmove=" + endmove);
        switch( curState ) {
            case start: {
                //moves.cleanMovesRepresentation();
                moves.showRobotMovesRepresentation();
                doStep();
                curState = State.walking;
                break;
            }
            case walking: {
                 if( move.equals("resume")){
                     doStep();
                 } else if (move.equals("moveForward") && endmove.equals("true")) {
                    //curState = State.walk;
                    moves.updateMovesRep("w");
                    if( ! tripStopped   )  doStep();
                    else{ System.out.println("please resume ..."); }
                 } else if (move.equals("moveForward") && endmove.equals("false")) {
                     curState = State.obstacle;
                     turnLeft();
                 } else {System.out.println("IGNORE answer of turnLeft");
                }
                break;
            }//walk

            case obstacle :
                if( move.equals("resume") ){
                    curState = State.walking;
                    doStep();
                }else if( move.equals("turnLeft") && endmove.equals("true")) {
                    if( stepNum < 4  ) {
                        stepNum++;
                        moves.updateMovesRep("l");
                        moves.showRobotMovesRepresentation();
                        if( ! tripStopped ) {
                            curState = State.walking;
                            doStep();
                        }else System.out.println("please resume ...");
                    }else{  //at home again
                        curState = State.end;
                        turnLeft(); //to force state transition
                    }
                } break;

            case end : {
                if( move.equals("turnLeft") ) {
                    System.out.println("BOUNDARY WALK END");
                    moves.showRobotMovesRepresentation();
                    turnRight();    //to compensate last turnLeft
                }else{
                    System.out.println("RESET ... "  );
                    stepNum        = 1;
                    curState       =  State.start;
                    tripStopped    =  true;
                    //moves           = new RobotMovesInfo(true);
                    moves.cleanMovesRepresentation();
                    moves.showRobotMovesRepresentation();
                }
                break;
            }
            default: {
                System.out.println("error - curState = " + curState);
            }
        }
    }


    @Override
    protected void handleInput(String msg ) {     //called when a msg is in the queue
        //System.out.println( name + " | input=" + msgJsonStr);
        if( msg.equals("startApp"))  fsm("","");
        else msgDriven( new JSONObject(msg) );
    }

    protected void msgDriven( JSONObject infoJson){
        if( infoJson.has("endmove") )        fsm(infoJson.getString("move"), infoJson.getString("endmove"));
        else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        else if( infoJson.has("collision") ) handleCollision(infoJson);
        else if( infoJson.has("robotcmd") )  handleRobotCmd(infoJson);
    }

    protected void handleSonar( JSONObject sonarinfo ){
        String sonarname = (String)  sonarinfo.get("sonarName");
        int distance     = (Integer) sonarinfo.get("distance");
        //System.out.println("RobotApplication | handleSonar:" + sonarname + " distance=" + distance);
    }
    protected void handleCollision( JSONObject collisioninfo ){
        //we should handle a collision  when there are moving obstacles
        //in this case we could have a collision even if the robot does not move
        //String move   = (String) collisioninfo.get("move");
        //System.out.println("RobotApplication | handleCollision move=" + move  );
    }
  
    protected void handleRobotCmd( JSONObject robotCmd ){
        String cmd = (String)  robotCmd.get("robotcmd");
        System.out.println("===================================================="    );
        System.out.println("ResumableBoundaryWalkerActor | handleRobotCmd cmd=" + cmd  );
        System.out.println("===================================================="    );
        if( cmd.equals("STOP") ) {
            tripStopped = true;
        }
        if( cmd.equals("RESUME") && tripStopped ){
            tripStopped = false;
            fsm("resume", "");
        }
    }

    //------------------------------------------------
    protected void doStep(){
        support.forward( forwardMsg);
        delay(1000); //to avoid too-rapid movement
    }
    protected void turnLeft(){
        support.forward( turnLeftMsg );
        delay(500); //to avoid too-rapid movement
    }
    protected void turnRight(){
        support.forward( turnRightMsg );
        delay(500); //to avoid too-rapid movement
    }

}
