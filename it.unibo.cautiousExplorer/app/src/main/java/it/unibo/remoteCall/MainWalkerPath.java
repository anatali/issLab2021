/*
============================================================
MainWalkerPath

============================================================
 */
package it.unibo.remoteCall;

import it.unibo.actor0.*;
import it.unibo.executor.ApplMsgs;
import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;

import org.json.JSONObject;

public class MainWalkerPath extends AbstractRobotRemote { //
     public MainWalkerPath(String name) {
        super(name);
    }

    protected void nextMove(String answerJsonStr) throws Exception {
        //{"stepDone":"ok" } {"stepFail":"163" }
        System.out.println("WalkerPath nextMove - answerJsonStr=" + answerJsonStr );
        JSONObject answer = new JSONObject(answerJsonStr);
        if( answer.has("stepDone")){
            moves.updateMovesRep("w");
            moves.showMap();
            ActorBasicJava.delay(500);
            doStep();
        }else{
             try {
                //moves.setObstacle();
                moves.showMap();
                String pathSoFar = moves.getJourney();
                System.out.println("WalkerPath obstacle - pathSoFar=" + pathSoFar );
                //Return to home (den)
                turn180("basicRobot"); //Talk with BasicRobotActor
                String pathTodo   =   reverse( pathSoFar  ).replace("l","r") +"ll"; //;
                 System.out.println("WalkerPath obstacle - pathTodo (after turn)=" + pathTodo );
                 moves.showMap();
                 IJavaActor goHome = new WalkToHome("goHome",this);
                 goHome.send(ApplMsgs.runawyStartMsg.replace("PATHTODO", pathTodo));
            } catch (Exception e) { //wall
                System.out.println(myname + " | outside the map " + e.getMessage());
            }
            moves.showMap();
        }
    }



    protected String reverse( String s  ){
        if( s.length() <= 1 )  return s;
        else return reverse( s.substring(1) ) + s.charAt(0) ;
    }
    @Override
    protected void handleInput(String s) {
        try {
            if( ! s.contains("sonarInfo")) System.out.println("WalkerPath handleInput:" + s);
            ApplMessage msg = ApplMessage.create(s);
            String msgId    = msg.getMsgId();
            //System.out.println("WalkerPath handleInput msg=" + msgId);
            if (msgId.equals("start")) {
                //startConn();      //already done by AbstractRobotRemote
                //doMove("l");    //to avoid sonar
                moves.showMap();
                doStep();
            }else if(msgId.equals("stepAnswer")){
                nextMove(msg.getMsgContent());
            }
          }catch( Exception e){
            System.out.println("WalkerPath ERROR:" + e.getMessage());
        }
    }

    @Override
    protected void msgDriven(JSONObject infoJson) {

    }

    public static void main(String args[]) {
        System.out.println("================================================================");
        System.out.println("WalkerPath | main "  ); //+ sysUtil.aboutThreads("main")
        System.out.println("================================================================");
        //Configure the system
        MainWalkerPath walker = new MainWalkerPath("walker");
        walker.send( startDefaultMsg );

    }
}
