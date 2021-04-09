/*
============================================================
PathExecutorForRemote
Executes a given path (e.g. wwwl) by using the StepRobotActor
============================================================
 */
package it.unibo.stepServiceCaller;

import it.unibo.actor0.ApplMessage;
import it.unibo.executor.ApplMsgs;
import it.unibo.executor.NaiveObserver;
import it.unibo.interaction.IJavaActor;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;


import static it.unibo.executor.ApplMsgs.*;


public class PathExecutorForRemote extends AbstractRobotRemote {

    protected enum State {start, nextMove, moving, turning, endok, endfail};

    protected State curState        = State.start ;
    protected IJavaActor ownerActor ;
    protected String todoPath       = "";

    public PathExecutorForRemote(String name, IJavaActor ownerActor ) {
        super( name );
        this.ownerActor  = ownerActor;
        MoveNameShort.put("moveForward","w");
        MoveNameShort.put("moveBackward","s");
        MoveNameShort.put("turnLeft","l");
        MoveNameShort.put("turnRight","r");
        MoveNameShort.put("alarm","h");
    }


    protected void resetStateVars(){
        curState       = State.start;
        //moves.cleanMovesRepresentation();
        todoPath       = "";
    }


    protected void nextMove(){
        if (todoPath.length() > 0) {
            mapUtil.showMap();
            //waitUser();
            char firstMove = todoPath.charAt(0);
            todoPath       = todoPath.substring(1);
            if( firstMove == 'w' ){
                //support.removeActor(this);  //avoid to receive info form WEnv
                //IJavaActor stepper = new StepRobotActor("stepper", this );
                doStep();
                //ActorBasicJava.delay(300);  //give time to open ws
                //stepper.send(stepMsg);
                curState = State.moving;
            } else { //firstMove == 'l'
                curState = State.turning;
                doMove( ""+firstMove );
            }
        } else{ //todoPath.length() == 0
            microStep();
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
        if( ownerActor != null ) {
            ownerActor.send(
                    ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.getJourney()));
        }
        //support.removeActor(this);
        //terminate();
        resetStateVars();

    }

    protected void fsm(String move, String arg) {
        System.out.println(myname + " | state=" +
                curState +  " move=" + move + " arg=" + arg + " todoPath="+todoPath );
        switch (curState) {
            case start: {
                if( move.equals(executorStartId)) {
                      System.out.println("--- PathExecutorForRemote todoPath=" + todoPath);
                      todoPath = arg;
                }
                nextMove();
                break;
            }

            case turning: {
                System.out.println(myname + " | turning ... arg= " + arg );
                String moveShort = MoveNameShort.get(move);
                if (arg.equals("true")) {
                    moves.updateMovesRep(moveShort);
                    moves.showMap();
                    //waitUser("turning");
                    nextMove();
                }else System.out.println(myname + " | FATAL ERROR " );
                break;
            }//turning

            case moving : {
                System.out.println(myname + " | moving ... " + move  );
                //support.registerActor(this);
                if (move.equals(stepDoneId)){
                    moves.updateMovesRep("w");
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
                //support.removeActor(this);
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
                            ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.getJourney()));

                    //support.removeActor(this);
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
protected void handleInput(String info ) {
    System.out.println(myname + " | handleInput:" + info );
    String infoJson = info;
    if( info.startsWith("msg")){    //Answer from the remote actor
        ApplMessage m  = ApplMessage.create( info );
        infoJson       =  m.getMsgContent();
    }
    msgDriven( new JSONObject(infoJson) );
}


    protected void msgDriven( JSONObject msgJson){
         if( msgJson.has(executorStartId) ) {
             System.out.println(myname + " | executorStartId:" + msgJson);
             String todoPath = msgJson.getString(executorStartId);
             fsm(executorStartId, todoPath);
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

    public static void main(String args[]) {
        System.out.println("================================================================");
        System.out.println("PathExecutorForRemote | main "  ); //+ sysUtil.aboutThreads("main")
        System.out.println("================================================================");
        //Configure the system
        NaiveObserver obs            = new NaiveObserver("obs");
        PathExecutorForRemote pathexec = new PathExecutorForRemote("pathexec", obs);
        String executorStartMsg      = executorstartMsg.replace("PATHTODO", "ww");
        pathexec.send( executorStartMsg );

    }


}
