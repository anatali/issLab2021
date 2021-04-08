package it.unibo.executor;

import it.unibo.interaction.IJavaActor;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;

import static it.unibo.executor.ApplMsgs.*;

/*
The map is a singleton object, managed by mapUtil
 */
public class RunawayActor extends PathExecutorActor {


    public RunawayActor(String name, IJavaActor ownerActor ) {
        super(name, ownerActor );
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

    protected void fsmrunaway(String move, String endmove) {
        System.out.println(myname + " | state=" +
                curState +  " move=" + move + " endmove=" + endmove + " totPath="+todoPath );
        switch (curState) {
            case start: {
                if( move.equals(runawyStartId)) {
                    System.out.println("=&=&=&=&=RunawayActor runaway&=&=&=&=&=&=&=&=&=&=& ");
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
                        System.out.println(myname + "|  FATAL ERROR: OUT OF HYPOTHESIS"  );

                        support.removeActor(this);
                        terminate();
                 }
                break;
            }//moving
            case endok: {
                System.out.println(myname + " | END OK ---------------- "  );
                ownerActor.send(runawyEndMsg);

                support.removeActor(this);
                terminate();
                //mapUtil.showMap();
                //resetStateVars();
                break;
            }//end


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
         if( msgJson.has(runawyStartId) ) {
             System.out.println("RunawayActor | runaway infoJson:" + msgJson);
             this.todoPath = msgJson.getString(runawyStartId);
             fsmrunaway( runawyStartId, "");
         }else if( msgJson.has(endMoveId) ) {
             System.out.println("RunawayActor | infoJson:" + msgJson);
             fsmrunaway(msgJson.getString("move"), msgJson.getString("endmove"));

         }
    }



}
