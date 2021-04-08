package it.unibo.executor;
/*
==============================================================================
Accept a  ApplMsgs.stepMsg to move ahead the robot for a given time.

Returns a ApplMsgs.stepDoneMsg if the move is done with success.
Returns a ApplMsgs.stepFailMsg with TIME=DT if the move is interrupted
by an obstacle after time DT. In this case it moves back the robot for time DT
==============================================================================
 */
import it.unibo.cautiousExplorer.AbstractRobotActor;
import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorMsgs;
import it.unibo.supports2021.TimerActor;
import org.json.JSONObject;

import static it.unibo.executor.ApplMsgs.*;

/*
The map is a singleton object, managed by mapUtil

 */
public class StepRobotActor extends AbstractRobotActor {

    protected enum State {start, moving, obstacle, end };

    protected IJavaActor ownerActor = null;
    protected State curState  = State.start ;
    protected TimerActor timer;
    protected int plannedMoveTime = 0;
    protected String backMsg = "";
    protected String answer  = "";
    private long StartTime = 0;


    public StepRobotActor(String name, IJavaActor ownerActor ) {
        super(name  );
        this.ownerActor = ownerActor;
    }

    private void resetVariables(){
        curState  = State.start ;
        plannedMoveTime = 0;
        backMsg = "";
        answer  = "";
        StartTime = 0;
    }

    protected void fsmstep( String move, String arg) {
        System.out.println(myname + " | state=" + curState +  " move=" + move + " arg=" + arg  );
        switch (curState) {
            case start: {
                if( move.equals( stepId )) {
                    StartTime = this.getCurrentTime();
                    timer     = new TimerActor("t0", this);
                    timer.send(ActorMsgs.startTimerMsg.replace("TIME", arg));
                    plannedMoveTime = Integer.parseInt(arg);
                    String attemptStepMsg = "{\"robotmove\":\"moveForward\", \"time\": TIME}"
                            .replace("TIME", ""+(plannedMoveTime+100));
                    support.forward( attemptStepMsg );
                    curState = State.moving;
                    break;
                }
            }
            case moving: {
                 String dt = ""+this.getDuration( StartTime ); //effective duration of the move
                 if (move.equals(ActorMsgs.endTimerId)){    //time elapsed
                     //support.removeActor(this);
                     answer = stepDoneMsg;
                     support.forward(haltMsg); //send move halted when move terminates
                     //endOfJob = true;
                     curState = State.end;  //consume  move halted
                 }else if (move.equals("collision")){
                     support.forward(haltMsg); //send move=halted
                     timer.kill();
                     //int todoTime   = plannedMoveTime - Integer.parseInt(dt);
                     answer  = stepFailMsg.replace("TIME", dt);
                     backMsg = "{\"robotmove\":\"moveBackward\", \"time\": BACKT}".replace("BACKT",dt);
                     System.out.println(myname + " | answer="+ answer  + " backMsg=" + backMsg);
                     curState = State.obstacle;  //to capture  {"endmove":"halted","move":"moveForward"}
                 }
                break;
            }//moving
            case obstacle: {
                if( arg.equals( "halted" )) {
                    System.out.println(myname + " | obstacle  arg=" +  arg);
                    support.forward(backMsg);
                    curState = State.end;
                }
                break;
            }
            case end: {
                if( move.equals( "moveBackward" ) && arg.equals( "true")  ||
                        move.equals( "moveForward" ) && arg.equals( "halted")) {
                    System.out.println(myname + " | end  arg=" + arg);
                    ownerActor.send(answer);
                } else if(move.equals( "collision" ) ) { //the last step was ok but with a collision
                    System.out.println(myname + " | collision move  ? answer=" + answer);
                    if( answer.equals(  stepDoneMsg ) ) ownerActor.send(answer);
                    else ownerActor.send(stepFailMsg.replace("TIME", "10"));
                }else {
                    System.out.println(myname + " | FATAL error - curState = " + curState);
                }
                //support.removeActor(this);
                //terminate();
                resetVariables();
                curState = State.start;
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
        if( ! msgJson.has("sonarName")) {
            System.out.println(myname + " | msgDriven (no sonar):" + msgJson);
        }
        if( msgJson.has(stepId) ) {
             //System.out.println("StepRobotActor |  msgJson:" + msgJson);
             String time = msgJson.getString(stepId);
             fsmstep( stepId, time);
         }else if( msgJson.has(ActorMsgs.endTimerId) ) {
             fsmstep(ActorMsgs.endTimerId, "");
         }else if( msgJson.has("endmove") ) {
            fsmstep(msgJson.getString("move"), msgJson.getString("endmove") );
         }else if( msgJson.has("collision") ) {  //put as the last
            fsmstep("collision", "");
         }
    }


}
