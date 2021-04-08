/*
============================================================
WalkerPath
Sends commands over TCP-8010
============================================================
 */
package it.unibo.stepServiceCaller;

import it.unibo.actor0.*;
import it.unibo.cautiousExplorer.RobotMovesInfo;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.supports.FactoryProtocol;
import it.unibo.supports2021.ActorBasicJava;
import mapRoomKotlin.mapUtil;
import org.json.JSONObject;

public class WalkerPath extends ActorBasicJava {

    public static final String startDefaultMsg =
            //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
            ApplMessage.Companion.create("msg(start,dispatch,any,any,do,1)").toString();
    public static final String endDefaultMsg   =
            ApplMessage.Companion.create("msg( end, dispatch, any, any, do, 1 )").toString();
            //MsgUtil.buildDispatch("anysender","end", "do", "anyreceiver" ).toString();

    protected IConnInteraction conn;
    protected String stepCmd         = "{\"step\":\"350\" }";
    protected String destStepperName = "stepRobot"; //defined by the service
    protected RobotMovesInfo moves   = new RobotMovesInfo(false);

    public WalkerPath(String name) {
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

    protected void updateTripInfo(String move){
        moves.updateMovesRep(move);
        mapUtil.doMove(move);
    }
    protected void doStep() throws Exception {
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
            updateTripInfo("w");
            mapUtil.showMap();
            ActorBasicJava.delay(1000);
            doStep();
        }else{
            System.out.println("WalkerPath obstacle:"  );
            try {
                mapUtil.setObstacle();
            } catch (Exception e) { //wall
                System.out.println(myname + " | outside the map " + e.getMessage());
            }
            mapUtil.showMap();
        }
    }

    @Override
    protected void handleInput(String s) {
        try {
            System.out.println("WalkerPath handleInput:" + s);
            ApplMessage msg = ApplMessage.create(s);
            String msgId    = msg.getMsgId();
            System.out.println("WalkerPath handleInput msg=" + msgId);
            if (msgId.equals("start")) {
                startConn();
                mapUtil.showMap();
                doStep();
            }else if(msgId.equals("stepAnswer")){
                nextMove(msg.getMsgContent());
            }
            /*
                updateTripInfo("w");
                mapUtil.showMap();
                doStep();
            }else if(msgId.equals("stepFail")){
                System.out.println("WalkerPath obstacle:"  );
                try {
                    mapUtil.setObstacle();
                } catch (Exception e) { //wall
                    System.out.println(myname + " | outside the map " + e.getMessage());
                }
                mapUtil.showMap();
            }*/
        }catch( Exception e){
            System.out.println("WalkerPath ERROR:" + e.getMessage());
        }
    }

    public static void main(String args[]) {
        System.out.println("================================================================");
        System.out.println("WalkerPath | main "  ); //+ sysUtil.aboutThreads("main")
        System.out.println("================================================================");
        //Configure the system
        WalkerPath walker = new WalkerPath("walker");
        walker.send( startDefaultMsg );

    }
}
