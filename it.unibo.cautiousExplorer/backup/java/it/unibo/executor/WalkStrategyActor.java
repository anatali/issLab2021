package it.unibo.executor;

import it.unibo.cautiousExplorer.AbstractRobotActor;
import it.unibo.interaction.IJavaActor;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;

public class WalkStrategyActor extends AbstractRobotActor {


    private int numSpiral   = 0;
    private boolean engaged = false;
    String curPathTodo = "";

    private String getSpiralPath( int n){
        String path  = "";
        String ahead = "";
        for( int i=1; i<=n; i++){
            ahead = ahead + "w";
        }
        path = ahead+"l"+ahead+"l"+ahead+"l"+ahead+"l";
        return path;
    }

    public WalkStrategyActor(String name) {
        super(name);
    }


    protected void startNewJourney(){
        numSpiral++;
        System.out.println(myname + " | startNewJourney" +  " numSpiral=" + numSpiral );
        if( numSpiral >= 5 ) {
            System.out.println(myname + " | TERMINATES"   );
            terminate();
            return;
        }
        curPathTodo = getSpiralPath(numSpiral);
        String msg  = ApplMsgs.executorstartMsg.replace("PATHTODO", curPathTodo);
        System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
        IJavaActor executor = new PathExecutorActor("executor", this);
        waitUser("start new journey");
        executor.send(msg);
    }

    protected void returnToDen( String result){
        String pathStillTodo = curPathTodo.replaceFirst(result,"");
        System.out.println(myname + " | pathStillTodo=" + pathStillTodo + " over:"+curPathTodo);
        String pathTodo = reverse( result  ).replace("l","r") +"ll"; //
        String msg      = ApplMsgs.runawyStartMsg.replace("PATHTODO", pathTodo);
        System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
        turn180();
        //if( pathTodo.startsWith("l") )
        mapUtil.showMap();
        waitUser("return to den");
        support.removeActor(this);
        IJavaActor runaway = new RunawayActor("runaway", this);
        runaway.send(msg);
    }
    protected void turn180(){
        turnLeft();
        mapUtil.doMove("l");
        turnLeft();
        mapUtil.doMove("l");
    }


    protected String reverse( String s  ){
        if( s.length() <= 1 )  return s;
        else return reverse( s.substring(1) ) + s.charAt(0) ;
    }
/*
-----------------------------------------------------------------------------
 */
    @Override
    protected void msgDriven(JSONObject mJson) {
        if( mJson.has(ApplMsgs.activateId) && !engaged){  //while working no more
            engaged = true; //if engaged, the message is lost. We could store it in a local queue
            startNewJourney();
        }
        else if( mJson.has(ApplMsgs.executorEndId)){
             String result        = mJson.getString(ApplMsgs.executorEndId);
             System.out.println(myname + " | result of journey=" + result );
             if( result.equals("ok")){ //Executor has done a spiral
                 startNewJourney();
             }
             else{ //Executor has found an obstacle
                 returnToDen(  result );
             }
        }
        else if( mJson.has(ApplMsgs.runawyEndId)){
            support.registerActor(this);
            startNewJourney();
        }

    }//msgdriven


}
