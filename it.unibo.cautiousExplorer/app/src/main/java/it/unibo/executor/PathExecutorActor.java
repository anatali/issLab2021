
package it.unibo.executor;

import it.unibo.cautiousExplorer.AbstractRobotActor;
import it.unibo.cautiousExplorer.RobotMovesInfo;
import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;

import static it.unibo.executor.ApplMsgs.*;

/*
The map is a singleton object, managed by mapUtil
 */
public class PathExecutorActor extends AbstractRobotActor {

    protected enum State {start, nextMove, moving, turning, endok, endfail};

    protected State curState        = State.start ;
    protected RobotMovesInfo moves  = new RobotMovesInfo(false);
    protected IJavaActor ownerActor ;
    protected String todoPath       = "";
    protected String stepMsg        = "{\"ID\":\"350\" }".replace("ID", stepId);
    protected IJavaActor stepper;

    public PathExecutorActor(String name, IJavaActor ownerActor ) {
        super(name );
        this.ownerActor  = ownerActor;
        //stepper          = new StepRobotActor("stepper", this );
    }

    protected void updateTripInfo(String move){
        moves.updateMovesRep(move);
        mapUtil.doMove(move);
    }

    protected void resetStateVars(){
        curState          = State.start;
        moves.cleanMovesRepresentation();
        todoPath       = "";
    }
    protected void doMove(char moveStep){
        System.out.println(myname + " | doMove ... " + moveStep + " todoPath="+todoPath);
        if( moveStep == 'w') doStep();
        else if( moveStep == 'l') turnLeft();
        else if( moveStep == 'r') turnRight();
        else if( moveStep == 's') doBackStep();
    }

    protected void nextMove(){
        if (todoPath.length() > 0) {
            mapUtil.showMap();
            //waitUser();
            char firstMove = todoPath.charAt(0);
            todoPath       = todoPath.substring(1);
            if( firstMove == 'w' ){
                support.removeActor(this);  //avoid to receive info form WEnv
                //IJavaActor stepper = new StepRobotActor("stepper", this );
                ActorBasicJava.delay(300);  //give time to open ws
                stepper.send(stepMsg);
                curState = State.moving;
            } else { //firstMove == 'l'
                curState = State.turning;
                doMove( firstMove );
            }
        } else{ //odoPath.length() == 0
            this.microStep();
            curState = State.endok;
        }
    }

    protected void obstacleFound(){
        System.out.println(myname + " | END KO ---------------- "  );
        mapUtil.showMap();
        try {
            mapUtil.setObstacle();
        } catch (Exception e) { //wall
            System.out.println(myname + " | outside the map " + e.getMessage());
        }
        mapUtil.showMap();
        ownerActor.send(
                ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.getMovesRepresentation()));

        support.removeActor(this);
        terminate();
        //resetStateVars();

    }

    protected void fsm(String move, String endmove) {
        System.out.println(myname + " | state=" +
                curState +  " move=" + move + " endmove=" + endmove + " todoPath="+todoPath );
        switch (curState) {
            case start: {
                if( move.equals(executorStartId)) {
                    stepper = new StepRobotActor("stepper", this );
                    System.out.println("=&=&=&=&=ExecutorActor&=&=&=&=&=&=&=&=&=&=& ");
                }
                nextMove();
                break;
            }

            case turning: {
                System.out.println(myname + " | turning ... endmove= " + endmove );
                String moveShort = MoveNameShort.get(move);
                if (endmove.equals("true")) {
                    updateTripInfo(moveShort);
                    mapUtil.showMap();
                    //waitUser("turning");
                    nextMove();
                }else System.out.println(myname + " | FATAL ERROR " );
                break;
            }//turning
            case moving : {
                System.out.println(myname + " | moving ... " + move  );
                support.registerActor(this);
                if (move.equals(stepDoneId)){
                    updateTripInfo("w");
                    nextMove();
                }else {
                    obstacleFound();
                }
                break;
            }
            case endok: {
                System.out.println(myname + " | END OK ---------------- "  );
                ownerActor.send(ApplMsgs.executorendokMsg);
                mapUtil.showMap();
                support.removeActor(this);
                terminate();
                //resetStateVars();
                break;
            }//end

            case endfail: {
                System.out.println(myname + " | END KO ---------------- "  );
                    try {
                        mapUtil.setObstacle();
                    } catch (Exception e) { //wall
                        System.out.println(myname + " | outside the map " + e.getMessage());
                    }
                    mapUtil.showMap();
                    ownerActor.send(
                            ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.getMovesRepresentation()));

                    support.removeActor(this);
                    terminate();
                    //resetStateVars();
                break;
            }
            default: {
                System.out.println(myname + " | error - curState = " + curState);
            }
        }

    }


/*
======================================================================================
 */
    @Override
    protected void msgDriven( JSONObject msgJson){
         if( msgJson.has(executorStartId) ) {
             System.out.println(myname + " | executorStartId:" + msgJson);
             this.todoPath = msgJson.getString(executorStartId);
             fsm(executorStartId, "");
        } else if( msgJson.has(endMoveId) ) {
                 System.out.println(myname + " | endMoveId:" + msgJson);
                 fsm(msgJson.getString("move"), msgJson.getString(endMoveId));
         }else if( msgJson.has(stepDoneId) ) {
             System.out.println(myname + " | stepDoneId:" + msgJson);
             fsm(stepDoneId, "");
         }else if( msgJson.has(stepFailId) ) {
             System.out.println(myname + " | stepFailed:" + msgJson);
             fsm(stepFailId, msgJson.getString(stepFailId));
         }
    }



}
