package unibo.Robots.common;
import connQak.ConnQakBase;
import it.unibo.kactor.IApplMessage;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;

public class RobotUtils {
    public static final String robotCmdId       = "move";
    public static final String basicrobotCmdId  = "cmd";
    public static final int robotPort           = 8020; //8083 is the cleaner robot
    public static final String robotPortStr     = ""+robotPort;
    private static Interaction2021 conn;


    public static void connectWithRobot(String addr){
        ConnQakBase connToRobot = ConnQakBase.create( ProtocolType.tcp );
        conn = connToRobot.createConnection(addr, RobotUtils.robotPort);
    }
    public static void connectWithRobotUsingCoap(String addr){
        ConnQakBase connToRobot = ConnQakBase.create( ProtocolType.tcp );
        conn = connToRobot.createConnection(addr, RobotUtils.robotPort);
    }
    public static  IApplMessage moveAril(String robotName, String cmd  ) {
        //ColorsOut.outappl("HIController | moveAril cmd:" + cmd , ColorsOut.BLUE);
        switch( cmd ) {
            case "w" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(w)", robotName);
            case "s" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(s)", robotName);
            case "l" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(a)", robotName);
            case "r" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(d)", robotName);
            case "h" : return CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(h)", robotName);
            case "p" : return CommUtils.buildRequest("webgui", "step", "step(345)", robotName);

            case "start"  : return CommUtils.buildDispatch("webgui", robotCmdId, "start",  robotName);
            case "stop"   : return CommUtils.buildDispatch("webgui", "stop", "do",   robotName);
            case "resume" : return CommUtils.buildDispatch("webgui", "resume", "do", robotName);
            default:   return CommUtils.buildDispatch("webgui",   robotCmdId, "h",robotName);
        }
    }

    public static void startRobot( String sender, String robotName ){
        Qak22Util.sendAMsg( SystemData.startSysCmd(sender,robotName) );
    }

    public static void sendMsg(String robotName, String cmd){
        try {
            String msg =  moveAril(robotName,cmd).toString();
            ColorsOut.outappl("RobotUtils | doMove msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            conn.forward( msg );
        } catch (Exception e) {
            ColorsOut.outerr("RobotUtils | doMove ERROR:"+e.getMessage());
        }
    }


}
