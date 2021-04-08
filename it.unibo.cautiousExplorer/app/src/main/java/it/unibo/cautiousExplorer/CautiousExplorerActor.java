package it.unibo.cautiousExplorer;

import it.unibo.interaction.IJavaActor;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;

public class CautiousExplorerActor extends AbstractRobotActor {

    private enum State {start, exploring, turning, obstacle, atHomeAgain, end };

    private State curState       = State.start ;
    private boolean tripStopped  = true;
    protected RobotMovesInfo moves = new RobotMovesInfo(false);
    //protected RobotMovesInfo map   = new RobotMovesInfo(true);
    protected boolean goingBackToHome   = false;
    protected String returnPath    = "";

    protected IJavaActor goBackActor ;
    protected int numStep          = 0;
    protected int maxNumStep       = 1;
    protected int numOfLeftTurn    = 0;
    protected int numOfSpiral      = 1;
    protected int maxnumOfSpiral   = 5;
    public CautiousExplorerActor(String name ) {
        super(name );
    }

    protected void resetStateVars(){
        goingBackToHome    = false;
        numStep       = 0;
        numOfLeftTurn = 0;
        moves.cleanMovesRepresentation();
    }
    protected void continueWalk(){
        if( ! tripStopped   )  doStep();
        else{ System.out.println("please resume ..."); }
    }

    protected void updateTripInfo(String move){
        moves.updateMovesRep(move);
        mapUtil.doMove(move);
        //map.updateMovesRep(move);
    }



    protected void fsmSpiral(String move, String endmove  ) {
        System.out.println(myname + " | fsmSpiral state=" +
                curState +  " move=" + move + " endmove=" + endmove);
        switch (curState) {
            case start: {
                if( move.equals("resume") ){
                    //map.showRobotMovesRepresentation();
                    mapUtil.showMap();
                    resetStateVars();
                    //waitUser();
                    doStep();
                    curState = State.exploring;
                };
                break;
            }
            case exploring: {
                if( move.equals("resume")){
                    doStep();
                }else if (move.equals("moveForward") && endmove.equals("true")) {
                    updateTripInfo("w");
                    numStep++;
                    if( numStep < maxNumStep) continueWalk( );
                    else{
                        turnLeft();
                        curState = State.turning;
                    }
                } else if (move.equals("moveForward") && endmove.equals("false")) {
                    mapUtil.setObstacle();
                    curState = State.obstacle;
                    //turnLeft();
                    microStep(); //just to continue ...
                }
                break;
            }//exploring

            case turning : {
                /*
                if (move.equals("resume")) { //???
                    curState = State.turning;
                    doStep();
                    return;
                }*/
                if (move.equals("turnLeft") && endmove.equals("true")) {
                    numOfLeftTurn++;
                    updateTripInfo("l");
                    if( numOfLeftTurn < 4 ) {
                        numStep = 0;
                        doStep();
                        curState = State.exploring;
                    }else{  //back to home either ok or runaway
                        curState = State.atHomeAgain;
                        this.microStep(); //just to continue ...
                    }
                }
                break;
            }//turning

            case atHomeAgain:{
                System.out.println(myname + "SPIRALDONE:" + numOfSpiral);
                numOfSpiral++;
                mapUtil.showMap();
                moves.showRobotMovesRepresentation();
                delay(1000);
                System.out.println(myname + "ANOTHERSPIRAL: " + numOfSpiral);
                if( numOfSpiral < maxnumOfSpiral ) {
                    resetStateVars();
                    maxNumStep++; //equal numOfSpiral?
                    curState = State.exploring;
                    doStep();
                }
                break;
            }//atHomeAgain

            case obstacle : {   //here with a microstep
                //if (move.equals("turnLeft") ) { //&& endmove.equals("true")
                    //updateTripInfo("l");

                    //if( moves.getMovesRepresentation().    .turnLeft();
                    //updateTripInfo("l"); //do not handle endmove
                    returnPath = moves.getMovesRepresentation();
                    System.out.println(myname + " | back to home along the same path ...");
                    //map.showRobotMovesRepresentation();
                    mapUtil.showMap();
                    moves.showRobotMovesRepresentation();

                    //waitUser();


                    goingBackToHome = true;
                    goBackActor = new RunawayActor("runAway", moves.getMovesRepresentation(), this);
                    goBackActor.send(ApplMsgs.goBackMsg);
               // }
                break;
            }//obstacle

            default: {
                System.out.println(myname + " | error - curState = " + curState);
            }
        }

    }

/*
    protected void fsm(String move, String endmove) {
        System.out.println(myname + " | fsm state=" + curState +  " move=" + move + " endmove=" + endmove);
        switch (curState) {
            case start: {
                if( move.equals("resume") ){
                    map.showRobotMovesRepresentation();
                    doStep();
                    curState = State.exploring;
                 };
                break;
            }
            case exploring: {
                if( move.equals("resume")){
                    doStep();
                }else if (move.equals("moveForward") && endmove.equals("true")) {
                    updateTripInfo("w");
                    continueWalk();
                } else if (move.equals("moveForward") && endmove.equals("false")) {
                    curState = State.obstacle;
                    turnLeft();
                }
                break;
            }//exploring

            case obstacle : {
                if (move.equals("resume")) {
                    curState = State.exploring;
                    doStep();
                } else if (move.equals("turnLeft") && endmove.equals("true")) {
                    updateTripInfo("l");
                    turnLeft();
                    updateTripInfo("l"); //do not handle endmove
                    returnPath = moves.getMovesRepresentation();
                    System.out.println("back to home along the same path ...");
                    map.showRobotMovesRepresentation();
                    moves.showRobotMovesRepresentation();

                    goingBackToHome = true;
                    goBackActor = new RunawayActor("runAway",moves.getMovesRepresentation(), map);
                    goBackActor.send(goBackMsg);
                }
                break;
            }//obstacle

            default: {
                System.out.println("error - curState = " + curState);
            }
        }

    }

*/
/*
======================================================================================
 */
    @Override
    protected void msgDriven( JSONObject infoJson){
        if( infoJson.has("resume") ) {
            curState = State.atHomeAgain;
            fsmSpiral("resume","");
        }
        if( goingBackToHome ){   //does nothing (avoid to handle messages sent by WEnv
            return;
        }
        System.out.println(myname + "  | infoJson:" + infoJson);
        if( infoJson.has("endmove") )
            fsmSpiral(infoJson.getString("move"), infoJson.getString("endmove") );
        else if( infoJson.has("sonarName") ) handleSonar(infoJson);
        else if( infoJson.has("collision") ) handleCollision(infoJson);
        else if( infoJson.has("robotcmd") )  handleRobotCmd(infoJson);
    }
/*
    protected void stepToHome(){
        if( returnPath.length() == 0 ){
            System.out.println("CautiousExplorerActor | AT HOME AGAIN "  );
            map.showRobotMovesRepresentation();
        }else{
            System.out.println("CautiousExplorerActor | going to home "  + returnPath  );
            returnPath = returnPath.substring(1);
            doStep();
        }
    }
*/
    protected void handleSonar( JSONObject sonarinfo ){
        String sonarname = (String)  sonarinfo.get("sonarName");
        int distance     = (Integer) sonarinfo.get("distance");
        System.out.println( myname + " | handleSonar:" + sonarname + " distance=" + distance);
    }
    protected void handleCollision( JSONObject collisioninfo ){
        //System.out.println("CautiousExplorerActor | handleCollision move=" + move  );
    }

    protected void handleRobotCmd( JSONObject robotCmd ){
        String cmd = (String)  robotCmd.get("robotcmd");
        System.out.println("===================================================="    );
        System.out.println(myname + " | handleRobotCmd cmd=" + cmd  );
        System.out.println("===================================================="    );
        if( cmd.equals("STOP") ) {
            tripStopped = true;
        }
        if( cmd.equals("RESUME") && tripStopped ){
            tripStopped = false;
            fsmSpiral("resume", "");
        }
    }
}
