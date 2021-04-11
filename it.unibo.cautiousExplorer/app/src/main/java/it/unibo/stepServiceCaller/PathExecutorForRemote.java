/*
============================================================
PathExecutorForRemote
Executes a given path (e.g. www) by using the StepRobotActor
Ends when the path is terminated or when an obstacle is found
============================================================
 */
package it.unibo.stepServiceCaller;

import it.unibo.actor0.ApplMessage;
import it.unibo.executor.ApplMsgs;
import it.unibo.executor.NaiveObserver;
import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;
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
        todoPath       = "";
    }


    protected void nextMove(){
        if (todoPath.length() > 0) {
            moves.showMap();
            //waitUser();
            char firstMove = todoPath.charAt(0);
            todoPath       = todoPath.substring(1);
            if( firstMove == 'w' ){
                 doStep();
                 curState = State.moving;
            } else { //firstMove == 'l'
                curState = State.turning;
                doMove( ""+firstMove );
            }
        } else{ //todoPath.length() == 0
            microStep();
            //doMove("h");  //do not force last state change since there is no answer from Wenv
            curState = State.endok; //to force state change
        }
        ActorBasicJava.delay(300);
    }

    protected void obstacleFound(){
        System.out.println(myname + " | END KO ---------------- "  );
        moves.showMap();
        try {
            //moves.setObstacle();
        } catch (Exception e) { //wall
            System.out.println(myname + " | outside the map " + e.getMessage());
        }
        moves.showMap();
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
                    todoPath = arg;
                    System.out.println("--- PathExecutorForRemote todoPath=" + todoPath);
                }
                nextMove();
                break;
            }

            case turning: {
                //System.out.println(myname + " | turning  move=" + move  + " arg=" + arg);
                String moveShort = MoveNameShort.get(move);
                if (arg.equals("true")) {
                    moves.showMap();
                    moves.showJourney();
                    //waitUser("turning");
                    nextMove();
                }else if (arg.equals("halted")) {

                }else System.out.println(myname + " | FATAL ERROR " );
                break;
            }//turning

            case moving : {
                if (move.equals(stepDoneId)){
                    moves.updateMovesRep("w");
                    nextMove();
                }else if (arg.equals("halted")){

                }else obstacleFound();
                break;
            }

            case endok: {
                System.out.println(myname + " | END OK ---------------- "  );
                ownerActor.send(ApplMsgs.executorendokMsg);
                moves.showMap();
                //support.removeActor(this);
                terminate();
                //resetStateVars();
                break;
            }//end

            case endfail: {
                System.out.println(myname + " | END KO ---------------- "  );
                    try {
                        //moves.setObstacle();
                    } catch (Exception e) { //wall
                        System.out.println(myname + " | outside the map " + e.getMessage());
                    }
                    moves.showMap();
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
    System.out.println(myname + " | handleInput info=" + info );
    String infoJson = info;
    if( info.startsWith("msg")){    //Answer from the remote actor or step
        ApplMessage m  = ApplMessage.create( info );
        infoJson       =  m.getMsgContent().replace("@",",");
        System.out.println(myname + " | handleInput infoJson=" + infoJson );
    }
    msgDriven( new JSONObject(infoJson) );
}

    protected void msgDriven( JSONObject msgJson){
         //System.out.println(myname + " | msgDriven:" + msgJson);
         if( msgJson.has(executorStartId) ) {
             System.out.println(myname + " | executorStartId:" + msgJson);
             String todoPath = msgJson.getString(executorStartId);
             fsm(executorStartId, todoPath);
         }
         //Sent by the BasicRobotActor (NOT BY the StepRobotActor)for turn moves
         else if( msgJson.has(endMoveId) ) {
             //System.out.println(myname + " | endMoveId:" + msgJson);
             String moveResult = msgJson.getString(endMoveId);
             String moveDone   = msgJson.getString("move");
             fsm( moveDone, moveResult);
         }
         else if( msgJson.has(stepDoneId) ) {
            //System.out.println(myname + " | stepDoneId:" + msgJson);
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
        NaiveObserver obs              = new NaiveObserver("obs");
        PathExecutorForRemote pathexec = new PathExecutorForRemote("pathexec", obs);
        String executorStartMsg         = executorstartMsg.replace("PATHTODO", "wwwwlwwwwlwwwwlwwwwl");
        pathexec.send( executorStartMsg );

    }


}
