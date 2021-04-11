/*
============================================================
AbstractRobotRemote
 
============================================================
 */
package it.unibo.remoteCall;

 
import it.unibo.actor0.ApplMessage;
import it.unibo.executor.ApplMsgs;
import it.unibo.interaction.IJavaActor;
import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.supports.FactoryProtocol;
import it.unibo.supports2021.ActorBasicJava;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractRobotRemote extends ActorBasicJava {

    protected enum State {start, nextMove, moving, turning, endok, endfail};

    public static final String startDefaultMsg =
            //msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
            ApplMessage.Companion.create("msg(start,dispatch,any,any,{\"start\":\"do\"},1)").toString();
    public static final String endDefaultMsg   =
            ApplMessage.Companion.create("msg( end, dispatch, any, any, {\"end\":\"do\"}, 1 )").toString();

    protected IConnInteraction conn;
    protected int moveInterval          = 500;   //to avoid too-rapid movement
    protected String stepCmd            = "{\"step\":\"340\" }";
    protected TripInfo moves            = new TripInfo();
    protected String destStepperName    = "stepRobot"; //defined by the service
    protected String destBasicRobotName = "basicRobot"; //defined by the service

    protected final Map<String, String> MoveNameShort = new HashMap<String, String>();
    protected final Map<String, String> MoveJsonCmd   = new HashMap<String, String>();

    protected ConnectionReader reader;

    public AbstractRobotRemote( String name  ) {
        super(name );

        MoveNameShort.put("moveForward","w");
        MoveNameShort.put("moveBackward","s");
        MoveNameShort.put("turnLeft","l");
        MoveNameShort.put("turnRight","r");
        MoveNameShort.put("alarm","h");

        MoveJsonCmd.put("w", ApplMsgs.forwardMsg.replace(",","@"));
        MoveJsonCmd.put("s", ApplMsgs.backwardMsg.replace(",","@"));
        MoveJsonCmd.put("l", ApplMsgs.turnLeftMsg.replace(",","@"));
        MoveJsonCmd.put("r", ApplMsgs.turnRightMsg.replace(",","@"));
        MoveJsonCmd.put("h", ApplMsgs.haltMsg.replace(",","@"));

        startConn();
    }

    protected void startConn()   {
        try {
            FactoryProtocol fp =  new FactoryProtocol(null, "TCP", "walker"); //MsgUtil.getFactoryProtocol(Protocol.TCP);
            System.out.println("    --- " +myname + " AbstractRobotRemote | fp:" + fp);
            conn = fp.createClientProtocolSupport("localhost", 8010);
            System.out.println("    --- " +myname + " AbstractRobotRemote | connected:" + conn);
            reader = new ConnectionReader("reader", conn);
            reader.registerActor(this);
            reader.send( startDefaultMsg );
        }catch( Exception e ){ e.printStackTrace(); }
    }

    protected void doMove(String moveShort, String dest)  { //Talk with BasicRobotActor
        try {
            String msg =
                    ApplMessage.Companion.create("msg(robotmove,dispatch,SENDER,DEST,CMD,1)").toString()
                            .replace("SENDER", myname)
                            .replace("DEST", dest)
                            .replace("CMD", MoveJsonCmd.get(moveShort));
            System.out.println("    --- " +myname + " AbstractRobotRemote | doMove msg:" + msg);
            conn.sendALine(msg);
            moves.updateMovesRep(moveShort);
            delay(moveInterval); //to avoid too-rapid movement
        }catch( Exception e ){ e.printStackTrace(); }
    }
    protected void engageBasicRobot( boolean doengage)  { //Talk with BasicRobotActor
        try {
            String cmd = doengage ? "engage" : "release";
            String msg =
                    ApplMessage.Companion.create("msg(MSGID,dispatch,SENDER,DEST,do,1)").toString()
                            .replace("MSGID", cmd)
                            .replace("SENDER", myname)
                            .replace("DEST", destBasicRobotName);
            System.out.println("    --- " +myname + " AbstractRobotRemote | engageBasicRobot msg:" + msg);
            conn.sendALine(msg);
        }catch( Exception e ){ e.printStackTrace(); }
    }

    protected void doStep()   { //Talk with StepRobotActor
        try {
            String msg = //MsgUtil.buildDispatch("walker", "step", stepCmd, destStepperName).toString();
                    ApplMessage.Companion.create("msg(step,dispatch,walker,DEST,CMD,1)").toString()
                            .replace("DEST", destStepperName)
                            .replace("CMD",stepCmd);
            System.out.println("    --- " +myname + " AbstractRobotRemote | doStep msg:" + msg);
            conn.sendALine(msg);
            delay(moveInterval); //to avoid too-rapid movement
        }catch( Exception e ){ e.printStackTrace(); }

    }
    protected void microStep() {
        try {
            String msg =
                    ApplMessage.Companion.create("msg(robotmove,dispatch,walker,DEST,CMD,1)").toString()
                            .replace("DEST", destBasicRobotName)
                            .replace("CMD", ApplMsgs.microStepMsg.replace(",", "@"));
            System.out.println("    --- " + myname + " AbstractRobotRemote | microStep msg:" + msg);
            conn.sendALine(msg);
            delay(moveInterval); //to avoid too-rapid movement
        }catch( Exception e ){ e.printStackTrace(); }
    }

    protected void turn180(String dest){
            doMove("l", dest);
            doMove("l", dest);
    }


/*
======================================================================================
 */
    @Override
    protected void handleInput(String info ) {
        System.out.println("    --- " + myname + " AbstractRobotRemote | handleInput:" + info );
        String perhapsJson = "";
        try{
            if( info.startsWith("msg")){    //Answer from the remote actor
                ApplMessage m   = ApplMessage.create( info );
                perhapsJson     = m.getMsgContent().replace("@",",");
                JSONObject obj  =  new JSONObject( perhapsJson );
                msgDriven( obj );
            }else{
                perhapsJson    = info;
                JSONObject obj = new JSONObject( perhapsJson );
                msgDriven( obj );
            }
        }catch(Exception e){
                System.out.println("    --- " +myname + " AbstractRobotRemote | sorry for:" + perhapsJson );
        }

    }


    abstract protected void  msgDriven( JSONObject infoJson);
}
