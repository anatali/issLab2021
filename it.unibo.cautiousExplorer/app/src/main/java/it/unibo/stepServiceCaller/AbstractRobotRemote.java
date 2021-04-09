
package it.unibo.stepServiceCaller;

 
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
            ApplMessage.Companion.create("msg(start,dispatch,any,any,do,1)").toString();
    public static final String endDefaultMsg   =
            ApplMessage.Companion.create("msg( end, dispatch, any, any, do, 1 )").toString();

    protected IConnInteraction conn;
    protected int moveInterval          = 500;   //to avoid too-rapid movement
    protected String stepCmd            = "{\"step\":\"350\" }";
    protected TripInfo moves            = new TripInfo();
    protected String destStepperName    = "stepRobot"; //defined by the service
    protected String destBasicRobotName = "basicRobot"; //defined by the service

    protected final Map<String, String> MoveNameShort = new HashMap<String, String>();
    protected final Map<String, String> MoveJsonCmd   = new HashMap<String, String>();

    protected IJavaActor ownerActor ;
    protected String todoPath       = "";
    protected String stepMsg        = "{\"ID\":\"350\" }".replace("ID", ApplMsgs.stepId);


    public AbstractRobotRemote( String name  ) {
        super(name );
        this.ownerActor  = ownerActor;
        MoveNameShort.put("moveForward","w");
        MoveNameShort.put("moveBackward","s");
        MoveNameShort.put("turnLeft","l");
        MoveNameShort.put("turnRight","r");
        MoveNameShort.put("alarm","h");

        MoveJsonCmd.put("w", ApplMsgs.forwardMsg.replace(",","@"));
        MoveJsonCmd.put("s", ApplMsgs.backwardMsg.replace(",","@"));
        MoveJsonCmd.put("l", ApplMsgs.turnLeftMsg.replace(",","@"));
        MoveJsonCmd.put("l", ApplMsgs.turnRightMsg.replace(",","@"));
        MoveJsonCmd.put("h", ApplMsgs.haltMsg.replace(",","@"));

        startConn();
    }

    protected void startConn()   {
        try {
            FactoryProtocol fp =  new FactoryProtocol(null, "TCP", "walker"); //MsgUtil.getFactoryProtocol(Protocol.TCP);
            System.out.println("AbstractRobotRemote | fp:" + fp);
            conn = fp.createClientProtocolSupport("localhost", 8010);
            System.out.println("AbstractRobotRemote | connected:" + conn);
            ConnectionReader reader = new ConnectionReader("reader", conn);
            reader.registerActor(this);
            reader.send( startDefaultMsg );
        }catch( Exception e ){ e.printStackTrace(); }
    }
/*
    protected void turnLeft()   { //Talk with BasicRobotActor
        String msg =
                ApplMessage.Companion.create("msg(robotmove,dispatch,walker,DEST,CMD,1)").toString()
                        .replace("DEST", destBasicRobotName)
                        .replace("CMD",ApplMsgs.turnLeftMsg.replace(",","@"));
        System.out.println("AbstractRobotRemote | turnLeft msg:" + msg);
        conn.sendALine(msg);
    }
*/
    protected void doMove(String moveShort)  { //Talk with BasicRobotActor
        try {
            String msg =
                    ApplMessage.Companion.create("msg(robotmove,dispatch,walker,DEST,CMD,1)").toString()
                            .replace("DEST", destBasicRobotName)
                            .replace("CMD", MoveJsonCmd.get(moveShort));
            System.out.println("AbstractRobotRemote | doMove msg:" + msg);
            conn.sendALine(msg);
            delay(moveInterval); //to avoid too-rapid movement
        }catch( Exception e ){ e.printStackTrace(); }
    }

    protected void doStep()   { //Talk with StepRobotActor
        try {
            String msg = //MsgUtil.buildDispatch("walker", "step", stepCmd, destStepperName).toString();
                    ApplMessage.Companion.create("msg(step,dispatch,walker,DEST,CMD,1)").toString()
                            .replace("DEST", destStepperName).replace("CMD",stepCmd);
            System.out.println("AbstractRobotRemote | doStep msg:" + msg);
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
            System.out.println("AbstractRobotRemote | microStep msg:" + msg);
            conn.sendALine(msg);
            delay(moveInterval); //to avoid too-rapid movement
        }catch( Exception e ){ e.printStackTrace(); }
    }




/*
======================================================================================
 */
    @Override
    protected void handleInput(String infoJson) {
        msgDriven( new JSONObject(infoJson) );
    }


    abstract protected void  msgDriven( JSONObject infoJson);
}
