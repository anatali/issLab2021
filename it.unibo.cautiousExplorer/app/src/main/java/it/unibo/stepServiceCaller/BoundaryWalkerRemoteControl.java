/*
============================================================
BoundaryWalkerRemoteControl

============================================================
 */
package it.unibo.stepServiceCaller;

import it.unibo.actor0.ApplMessage;
import it.unibo.executor.ApplMsgs;
import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;
import org.json.JSONObject;

public class BoundaryWalkerRemoteControl extends AbstractRobotRemote { //
    private int count = 0;

    public BoundaryWalkerRemoteControl(String name) {
        super(name);
    }

    protected void nextMove(String answerJsonStr) throws Exception {
        //{"stepDone":"ok" } {"stepFail":"163" }
        System.out.println("BoundaryWalkerRemoteControl | nextMove - answerJsonStr=" + answerJsonStr );
        JSONObject answer = new JSONObject(answerJsonStr);
        if( answer.has("stepDone")){
            moves.updateMovesRep("w");
            moves.showMap();
            ActorBasicJava.delay(500);
            doStep();
        }else if( answer.has("stepFail")){
             //try {
                moves.setObstacle();
                String pathSoFar = moves.getJourney();
                System.out.println("BoundaryWalkerRemoteControl | obstacle - pathSoFar=" + pathSoFar + " count=" + count);
                 moves.showMap();
                 doMove("l");
                 /*
                //ActorBasicJava.delay(500);
                if( count > 4 ) {
                    doMove("l");
                    terminate();
                }else{
                    count++;
                    doStep();
                }*/
              //} catch (Exception e) { //wall
               // System.out.println(myname + " | outside the map " + e.getMessage());
            //}
            moves.showMap();
        }else {
            System.out.println("BoundaryWalkerRemoteControl | todo ... ");
        }
    }
    protected void turn180(){
        try {
            ActorBasicJava.delay(800);
            doMove("l");  //does also moves.updateMovesRep("l");
            //moves.updateMovesRep("l");
            ActorBasicJava.delay(800);
            doMove("l");
            //moves.updateMovesRep("l");
            ActorBasicJava.delay(500);
        }catch( Exception e ){ e.printStackTrace();}
    }


    protected String reverse( String s  ){
        if( s.length() <= 1 )  return s;
        else return reverse( s.substring(1) ) + s.charAt(0) ;
    }
    @Override
    protected void handleInput(String s) {
        try {
            if( ! s.contains("sonarInfo")) System.out.println("BoundaryWalkerRemoteControl handleInput:" + s);
            ApplMessage msg = ApplMessage.create(s);
            String msgId    = msg.getMsgId();
            //System.out.println("BoundaryWalkerRemoteControl handleInput msg=" + msgId);
            if (msgId.equals("start")) {
                //startConn();      //already done by AbstractRobotRemote
                //doMove("l");    //to avoid sonar
                moves.showMap();
                doStep();
            }else if(msgId.equals("stepAnswer")){
                nextMove(msg.getMsgContent());
            }
          }catch( Exception e){
            System.out.println("BoundaryWalkerRemoteControl ERROR:" + e.getMessage());
        }
    }

    @Override
    protected void msgDriven(JSONObject infoJson) {

    }

    public static void main(String args[]) {
        System.out.println("================================================================");
        System.out.println("BoundaryWalkerRemoteControl | main "  ); //+ sysUtil.aboutThreads("main")
        System.out.println("================================================================");
        //Configure the system
        BoundaryWalkerRemoteControl walker = new BoundaryWalkerRemoteControl("walker");
        walker.send( startDefaultMsg );

    }
}
