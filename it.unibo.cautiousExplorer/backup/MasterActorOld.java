package it.unibo.executor;

import it.unibo.cautiousExplorer.AbstractRobotActor;
import it.unibo.interaction.IJavaActor;
import org.json.JSONObject;

public class MasterActorOld extends AbstractRobotActor {


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

    public MasterActorOld(String name) {
        super(name);
    }


    protected void startNewJournewy(){
        numSpiral++;
        System.out.println(myname + " | startNewJournewye" +  " numSpiral=" + numSpiral );
        if( numSpiral >= 4 ) {
            terminate();
        }
        curPathTodo = getSpiralPath(numSpiral);
        String msg  = ApplMsgs.executorstartMsg.replace("PATHTODO", curPathTodo);
        System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
        IJavaActor executor = new ExecutorActor("executor", this);
        waitUser("start new journay");
        executor.send(msg);
    }

    protected void returnToDen(String result){
        String pathStillTodo = curPathTodo.replaceFirst(result,"");
        System.out.println(myname + " | pathStillTodo=" + pathStillTodo + " over:"+curPathTodo);
        //working = false;
        curPathTodo = reverse( result  ) +"ll"; //.replace("l","r")
        String msg  = ApplMsgs.runawyStartMsg.replace("PATHTODO", curPathTodo);
        System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
        waitUser("return to den");
        IJavaActor runaway = new RunawayActor("runaway", this);
        runaway.send(msg);
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
            engaged = true;
            startNewJournewy();
            /*
            System.out.println(myname + " | mJson=" + mJson );
            //String pathTodo = mJson.getString(ApplMsgs.activateId);
            curPathTodo     = getSpiralPath(numSpiral);
            String msg      = ApplMsgs.executorstartMsg.replace("PATHTODO", curPathTodo);
            System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral );
            IJavaActor executor = new ExecutorActor("executor", this);
            executor.send(msg);*/
        }//if not working, the message is lost. We could store it in a local queue
        else if( mJson.has(ApplMsgs.runawyEndId)){
            startNewJournewy();
            /*
            System.out.println(myname + " | Back to home" +  " numSpiral=" + numSpiral );
            numSpiral++;
            if( numSpiral >= 4 ) {
                 terminate();
            }
            curPathTodo = getSpiralPath(numSpiral);
            String msg  = ApplMsgs.executorstartMsg.replace("PATHTODO", curPathTodo);
            System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
            IJavaActor executor = new ExecutorActor("executor", this);
            //this.waitUser();
            executor.send(msg);*/
        }

        else if( mJson.has(ApplMsgs.executorEndId)){
            System.out.println(myname + " | mJson=" + mJson );
            String result        = mJson.getString(ApplMsgs.executorEndId);
            System.out.println(myname + " | result of execution=" + result );
             if( result.equals("ok")){ //Executor has done a spiral
                 startNewJournewy();
                 /*
                 numSpiral++;
                 curPathTodo = getSpiralPath(numSpiral);
                 String msg  = ApplMsgs.executorstartMsg.replace("PATHTODO", curPathTodo);
                 System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
                 IJavaActor executor = new ExecutorActor("executor", this);
                 //this.waitUser();
                 executor.send(msg);*/
             }
             else{ //Executor has found an obstacle
                 returnToDen(  result );
                 /*
                 String pathStillTodo = curPathTodo.replaceFirst(result,"");
                 System.out.println(myname + " | pathStillTodo=" + pathStillTodo + " over:"+curPathTodo);
                 //working = false;
                 curPathTodo = reverse( result  ) +"ll"; //.replace("l","r")
                 String msg  = ApplMsgs.runawyStartMsg.replace("PATHTODO", curPathTodo);
                 System.out.println(myname + " | msg=" + msg + " numSpiral=" + numSpiral);
                 this.waitUser();
                 IJavaActor runaway = new RunawayActor("runaway", this);
                 runaway.send(msg);*/
             }
        }
    }//msgdriven


}
