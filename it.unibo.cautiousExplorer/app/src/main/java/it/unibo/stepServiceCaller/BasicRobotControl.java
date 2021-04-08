/*
============================================================
WalkerPath
Sends commands over TCP-8010
============================================================
 */
package it.unibo.stepServiceCaller;

import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.MsgUtil;
import it.unibo.actor0robot.ApplMsgs;
import it.unibo.cautiousExplorer.RobotMovesInfo;

import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.supports.FactoryProtocol;
import it.unibo.supports2021.ActorBasicJava;
import mapRoomKotlin.mapUtil;


public class BasicRobotControl extends ActorBasicJava {

    public static final String startDefaultMsg =
            //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
            ApplMessage.Companion.create("msg(start,dispatch,any,any,do,1)").toString();
    public static final String endDefaultMsg   =
            ApplMessage.Companion.create("msg( end, dispatch, any, any, do, 1 )").toString();
            //MsgUtil.buildDispatch("anysender","end", "do", "anyreceiver" ).toString();

    protected IConnInteraction conn;
     protected RobotMovesInfo moves   = new RobotMovesInfo(false);

    public BasicRobotControl(String name) {
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

    protected void doMove( ) throws Exception {
        String m = ApplMsgs.turnLeftMsg.replace(",","@");
        String cmd    = MsgUtil.buildDispatch("controller","robotmove",m
                ,"basicRobot").toString();
        System.out.println("WalkerPath | doMove cmd:" + cmd);
        conn.sendALine(cmd);
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
                for( int i=1; i<=4; i++) {
                    delay(500);
                    mapUtil.showMap();
                    doMove();
                    updateTripInfo("l");
                 }
                mapUtil.showMap();
                //conn.closeConnection();
                terminate();
             }

        }catch( Exception e){
            System.out.println("WalkerPath ERROR:" + e.getMessage());
        }
    }

    public static void main(String args[]) {
        System.out.println("================================================================");
        System.out.println("BasicRobotControl | main "  ); //+ sysUtil.aboutThreads("main")
        System.out.println("================================================================");
        //Configure the system
        BasicRobotControl controller = new BasicRobotControl("controller");
        controller.send( startDefaultMsg );

    }
}
