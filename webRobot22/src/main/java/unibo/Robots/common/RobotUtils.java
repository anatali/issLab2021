package unibo.Robots.common;
/*
import unibo.actor22comm.coap.CoapConnection;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.tcp.TcpClientSupport;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommUtils;
*/
import unibo.comm22.coap.CoapConnection;
import unibo.comm22.interfaces.Interaction2021;
import unibo.comm22.tcp.TcpClientSupport;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;

public class RobotUtils {
    public static final String robotCmdId       = "move";
    public static final String basicrobotCmdId  = "cmd";
    public static final int robotPort           = 8020;
    //public static final String robotPortStr     = ""+robotPort;
    private static Interaction2021 conn;


    public static void connectWithRobotUsingTcp(String addr){
        //ColorsOut.outappl("HIController | connectWithRobotUsingTcp addr:" + addr , ColorsOut.CYAN);
        //ConnQakBase connToRobot = ConnQakBase.create( ProtocolType.tcp );
        //conn = connToRobot.createConnection(addr, RobotUtils.robotPort);
        try {
            conn = TcpClientSupport.connect(addr, robotPort, 10);
            ColorsOut.outappl("HIController | connect Tcp conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("RobotUtils | connectWithRobotUsingTcp ERROR:"+e.getMessage());
        }
    }
    public static CoapConnection connectWithRobotUsingCoap(String addr){
        //ColorsOut.outappl("HIController | connec Coap addr:" + addr , ColorsOut.BLUE);
        //ConnQakBase connToRobot = ConnQakBase.create( ProtocolType.tcp );
        //conn = connToRobot.createConnection(addr, RobotUtils.robotPort);
        try {
            String ctxqakdest       = "ctxbasicrobot";
            String qakdestination 	= "basicrobot";
            String path   = ctxqakdest+"/"+qakdestination;
            conn = new CoapConnection(addr+":"+robotPort, path);
            ColorsOut.outappl("HIController | connect Coap conn:" + conn , ColorsOut.CYAN);
        }catch(Exception e){
            ColorsOut.outerr("RobotUtils | connectWithRobotUsingTcp ERROR:"+e.getMessage());
        }
        return (CoapConnection) conn;
    }
    public static  String moveAril(String robotName, String cmd  ) {
        //ColorsOut.outappl("HIController | moveAril cmd:" + cmd , ColorsOut.BLUE);
        switch( cmd ) {
            case "w" : return ""+CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(w)", robotName);
            case "s" : return ""+CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(s)", robotName);
            case "turnLeft" : return ""+CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(a)", robotName);
            case "l" : return ""+CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(a)", robotName);
            case "r" : return ""+CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(d)", robotName);
            case "h" : return ""+CommUtils.buildDispatch("webgui", basicrobotCmdId, "cmd(h)", robotName);
            case "p" : return ""+CommUtils.buildRequest("webgui", "step", "step(345)", robotName);

            case "start"  : return ""+CommUtils.buildDispatch("webgui", robotCmdId, "start",  robotName);
            case "stop"   : return ""+CommUtils.buildDispatch("webgui", "stop", "do",   robotName);
            case "resume" : return ""+CommUtils.buildDispatch("webgui", "resume", "do", robotName);
            default:   return ""+CommUtils.buildDispatch("webgui",   robotCmdId, "h",robotName);
        }
    }
/*
    public static void startRobot( String sender, String robotName ){
        Qak22Util.sendAMsg( SystemData.startSysCmd(sender,robotName) );
    }
*/
    public static void sendMsg(String robotName, String cmd){
        try {
            String msg =  moveAril(robotName,cmd);
            ColorsOut.outappl("RobotUtils | sendMsg msg:" + msg + " conn=" + conn, ColorsOut.BLUE);
            conn.forward( msg );
        } catch (Exception e) {
            ColorsOut.outerr("RobotUtils | sendMsg ERROR:"+e.getMessage());
        }
    }


}
