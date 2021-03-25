package it.unibo.robotWithActorJava;


import it.unibo.interaction.IssCommActorSupport;
import it.unibo.supports2021.ActorBasicJava;
import org.json.JSONObject;

public class BoundaryWalkerActor extends ActorBasicJava {
    final String forwardMsg = "{\"robotmove\":\"moveForward\", \"time\": 350}";
    final String backwardMsg = "{\"robotmove\":\"moveBackward\", \"time\": 350}";
    final String turnLeftMsg = "{\"robotmove\":\"turnLeft\", \"time\": 300}";
    final String turnRightMsg = "{\"robotmove\":\"turnRight\", \"time\": 300}";
    final String haltMsg = "{\"robotmove\":\"alarm\", \"time\": 100}";

    private enum State {start, walking, obstacle, end };
    private IssCommActorSupport support;
    private State curState       =  State.start ;
    private int stepNum          = 1;
    private RobotMovesInfo moves = new RobotMovesInfo(true);

    public BoundaryWalkerActor(String name, IssCommActorSupport support) {
        super(name);
        this.support = support;
    }
/*
    public void startJob(){
        curState       =  State.start;
        //fsm("","");
    }
*/
    public void reset(){
        stepNum        = 1;
        curState       =  State.start;
        System.out.println("RobotBoundaryLogic | FINAL MAP:"  );
        moves.showRobotMovesRepresentation();
        moves.getMovesRepresentationAndClean();
        moves.showRobotMovesRepresentation();
    }


    protected void fsm(String move, String endmove){
        System.out.println( myname + " | fsm state=" + curState + " stepNum=" + stepNum + " move=" + move + " endmove=" + endmove);
        switch( curState ) {
            case start: {
                //moves.getMovesRepresentationAndClean();
                moves.showRobotMovesRepresentation();
                doStep();
                curState = State.walking;
                break;
            }
            case walking: {
                if (move.equals("moveForward") && endmove.equals("true")) {
                    //curState = State.walk;
                    moves.updateMovesRep("w");
                    if (stepNum <= 4) doStep();
                    else {
                        curState = State.end;
                        turnLeft();
                    }
                } else if (move.equals("moveForward") && endmove.equals("false")) {
                    curState = State.obstacle;
                    turnLeft();
                } else {System.out.println("IGNORE answer of turnLeft");
                }
                    //if (move.equals("turnLeft") && endmove.equals("true")) doStep();
                break;
            }//walk

            case obstacle :
                if( move.equals("turnLeft") && endmove.equals("true")) {
                    if( stepNum == 4) { reset(); return; }
                    stepNum++;
                    moves.updateMovesRep("l");
                    moves.showRobotMovesRepresentation();
                    curState = State.walking;
                    doStep();
                } break;

            case end : {
                System.out.println("END");
                moves.showRobotMovesRepresentation();
            }
            default: System.out.println("error");
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
        System.out.println("RobotApplication | handleRobotCmd cmd=" + cmd  );
        System.out.println("===================================================="    );
    }

    //------------------------------------------------
    protected void doStep(){
        support.forward( forwardMsg);
        delay(1000); //to avoid too-rapid movement
    }
    protected void turnLeft(){
        support.forward( turnLeftMsg );
        delay(1000); //to avoid too-rapid movement
    }

}
