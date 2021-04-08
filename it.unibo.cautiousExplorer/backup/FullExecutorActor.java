package it.unibo.executor;

import it.unibo.cautiousExplorer.AbstractRobotActor;
import it.unibo.cautiousExplorer.RobotMovesInfo;
import it.unibo.interaction.IJavaActor;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;

import static it.unibo.executor.ApplMsgs.*;

/*
The map is a singleton object, managed by mapUtil
 */
public class FullExecutorActor extends AbstractRobotActor {

    protected enum State {start, moving, endok, endfail};

    protected State curState        = State.start ;
    protected RobotMovesInfo moves  = new RobotMovesInfo(false);
    protected IJavaActor ownerActor ;
    protected String todoPath       = "";
    protected boolean runaway       = false;

    public FullExecutorActor(String name, IJavaActor ownerActor ) {
        super(name );
        this.ownerActor  = ownerActor;
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
        System.out.println("RunawayActor | doMove ... " + moveStep + " totPath="+todoPath);
        if( moveStep == 'w') doStep();
        else if( moveStep == 'l') turnLeft();
        else if( moveStep == 'r') turnRight();
        else if( moveStep == 's') doBackStep();
    }

    protected void fsm(String move, String endmove) {
        System.out.println(myname + " | state=" +
                curState +  " move=" + move + " endmove=" + endmove + " totPath="+todoPath );
        switch (curState) {
            case start: {
                if( move.equals(executorStartId)) {
                    System.out.println("=&=&=&=&=ExecutorActor&=&=&=&=&=&=&=&=&=&=& ");
                    runaway = false;
                }else if( move.equals(runawyStartId)) {
                    System.out.println("=&=&=&=&=ExecutorActor runaway&=&=&=&=&=&=&=&=&=&=& ");
                    runaway = true;
                }
                if (todoPath.length() > 0) {
                    mapUtil.showMap();
                    //waitUser();
                    doMove(todoPath.charAt(0));
                    curState = State.moving;
                } else curState = State.endok;
                break;
            }
            case moving: {
                String moveShort = MoveNameShort.get(move);

                if (endmove.equals("true")){
                    updateTripInfo(moveShort);
                    mapUtil.showMap();
                    todoPath = todoPath.substring(1);   //the first move has been done
                    System.out.println(myname + " | moveShort " + moveShort + " totPath=" + todoPath);
                    if( todoPath.length() > 0){
                        curState = State.moving;
                        doMove( todoPath.charAt(0) );
                    }else{ //totPath.length() == 0
                        microStep();
                        curState = State.endok;
                    }
                }else{  //endmove=false (obstacle)
                    if( runaway ){
                        System.out.println(myname + "|  FATAL ERROR: OUT OF HYPOTHESIS"  );
                    }
                    microBackStep();
                    curState = State.endfail;
                }
                break;
            }//moving
            case endok: {
                System.out.println(myname + " | END OK ---------------- "  );
                if( runaway ) ownerActor.send(runawyEndMsg);
                else ownerActor.send(ApplMsgs.executorendokMsg);
                //terminate();
                mapUtil.showMap();
                resetStateVars();
                break;
            }//end

            case endfail: {
                System.out.println(myname + " | END KO ---------------- "  );
                if( ! runaway ) {
                    try {
                        mapUtil.setObstacle();
                    } catch (Exception e) { //wall
                        System.out.println(myname + " | outside the map " + e.getMessage());
                    }
                    mapUtil.showMap();
                    ownerActor.send(
                            ApplMsgs.executorendkoMsg.replace("PATHDONE", moves.getMovesRepresentation()));
                    this.support.removeActor(this);
                    terminate();
                    //resetStateVars();
                }
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
             System.out.println("ExecutorActor | infoJson:" + msgJson);
             this.todoPath = msgJson.getString(executorStartId);
             fsm(executorStartId, "");
        }else if( msgJson.has(runawyStartId) ) {
             System.out.println("ExecutorActor | runaway infoJson:" + msgJson);
             this.todoPath = msgJson.getString(runawyStartId);
             fsm( runawyStartId, "");
         }else if( msgJson.has(endMoveId) ) {
                 System.out.println("ExecutorActor | infoJson:" + msgJson);
                 fsm(msgJson.getString("move"), msgJson.getString("endmove"));

         }
    }



}
