package it.unibo.stepServiceCaller;
import it.unibo.executor.ApplMsgs;
import it.unibo.executor.PathExecutorActor;
import it.unibo.interaction.IJavaActor;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;



/*
The map is a singleton object, managed by mapUtil
 */
public class WalkToHome extends PathExecutorActor{ //extends PathExecutorActor
    
    public WalkToHome(String name, IJavaActor ownerActor ) {
        super(name, ownerActor );
    }

    protected void fsmdopath(String move, String endmove) {
        System.out.println(myname + " | state=" +
                curState +  " move=" + move + " endmove=" + endmove + " totPath="+todoPath );
        switch (curState) {
            case start: {
                if( move.equals(ApplMsgs.runawyStartId)) {
                    System.out.println(" ------------ WalkToHome -----------  ");
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
                ownerActor.send(ApplMsgs.runawyEndMsg);

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
         if( msgJson.has(ApplMsgs.runawyStartId) ) {
             System.out.println("WalkToHome | runaway infoJson:" + msgJson);
             this.todoPath = msgJson.getString(ApplMsgs.runawyStartId);
             fsmdopath( ApplMsgs.runawyStartId, "");
         }else if( msgJson.has(ApplMsgs.endMoveId) ) {
             System.out.println("WalkToHome | infoJson:" + msgJson);
             fsmdopath(msgJson.getString("move"), msgJson.getString("endmove"));

         }
    }



}
