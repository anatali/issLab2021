/*
============================================================
MainWalkerPath

============================================================
 */
package it.unibo.stepServiceCaller;

import it.unibo.actor0.*;
import it.unibo.executor.ApplMsgs;
import it.unibo.interaction.IJavaActor;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.supports.FactoryProtocol;
import it.unibo.supports2021.ActorBasicJava;

import org.json.JSONObject;

public class MainWalkerPath extends ActorBasicJava {

    public static final String startDefaultMsg =
            //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
            ApplMessage.Companion.create("msg(start,dispatch,any,any,do,1)").toString();
    public static final String endDefaultMsg   =
            ApplMessage.Companion.create("msg( end, dispatch, any, any, do, 1 )").toString();
            //MsgUtil.buildDispatch("anysender","end", "do", "anyreceiver" ).toString();

    protected IConnInteraction conn;
    protected String stepCmd            = "{\"step\":\"350\" }";
    protected String destStepperName    = "stepRobot"; //defined by the service
    protected String destBasicRobotName = "basicRobot"; //defined by the service

    protected TripInfo moves            = new TripInfo();
    public MainWalkerPath(String name) {
        super(name);
    }

    protected void startConn() throws Exception {
        FactoryProtocol fp =  new FactoryProtocol(null, "TCP", "walker"); //MsgUtil.getFactoryProtocol(Protocol.TCP);
        System.out.println("WalkerPath | fp:" + fp);
        conn = fp.createClientProtocolSupport("localhost", 8010);
        System.out.println("WalkerPath | connected:" + conn);
        ConnectionReader reader = new ConnectionReader("reader", conn);
        reader.registerActor(this);
        reader.send(startDefaultMsg);
    }

    protected void turnLeft() throws Exception { //Talk with BasicRobotActor
        String msg =
                ApplMessage.Companion.create("msg(robotmove,dispatch,walker,DEST,CMD,1)").toString()
                .replace("DEST", destBasicRobotName)
                .replace("CMD",ApplMsgs.turnLeftMsg.replace(",","@"));
        System.out.println("WalkerPath | turnLeft msg:" + msg);
        conn.sendALine(msg);
    }

    protected void doStep() throws Exception { //Talk with StepRobotActor
        String msg = //MsgUtil.buildDispatch("walker", "step", stepCmd, destStepperName).toString();
                ApplMessage.Companion.create("msg(step,dispatch,walker,DEST,CMD,1)").toString()
                        .replace("DEST", destStepperName).replace("CMD",stepCmd);
        System.out.println("WalkerPath | doStep msg:" + msg);
        conn.sendALine(msg);

    }

    protected void nextMove(String answerJsonStr) throws Exception {
        //{"stepDone":"ok" } {"stepFail":"163" }
        JSONObject answer = new JSONObject(answerJsonStr);
        if( answer.has("stepDone")){
            moves.updateMovesRep("w");
            moves.showMap();
            ActorBasicJava.delay(1000);
            doStep();
        }else{
             try {
                moves.setObstacle();
                String pathSoFar = moves.getJourney();
                System.out.println("WalkerPath obstacle - pathSoFar=" + pathSoFar );
                //Return to home (den)
                turn180(); //Talk with BasicRobotActor
                String pathTodo   =   reverse( pathSoFar  ).replace("l","r") +"ll"; //;
                IJavaActor goHome = new WalkToHome("goHome",this);
                goHome.send(ApplMsgs.runawyStartMsg.replace("PATHTODO", pathTodo));
            } catch (Exception e) { //wall
                System.out.println(myname + " | outside the map " + e.getMessage());
            }
            moves.showMap();
        }
    }
    protected void turn180(){
        try {
            turnLeft();
            moves.updateMovesRep("l");
            ActorBasicJava.delay(300);
            turnLeft();
            moves.updateMovesRep("l");
            ActorBasicJava.delay(300);
        }catch( Exception e ){ e.printStackTrace();}
    }


    protected String reverse( String s  ){
        if( s.length() <= 1 )  return s;
        else return reverse( s.substring(1) ) + s.charAt(0) ;
    }
    @Override
    protected void handleInput(String s) {
        try {
            System.out.println("WalkerPath handleInput:" + s);
            ApplMessage msg = ApplMessage.create(s);
            String msgId    = msg.getMsgId();
            //System.out.println("WalkerPath handleInput msg=" + msgId);
            if (msgId.equals("start")) {
                startConn();
                moves.showMap();
                doStep();
            }else if(msgId.equals("stepAnswer")){
                nextMove(msg.getMsgContent());
            }
          }catch( Exception e){
            System.out.println("WalkerPath ERROR:" + e.getMessage());
        }
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
