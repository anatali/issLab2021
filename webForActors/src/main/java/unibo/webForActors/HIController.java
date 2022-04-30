package unibo.webForActors;

import connQak.ConnQakBase;
import it.unibo.kactor.IApplMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import unibo.Robots.MainBasicRobot;
import unibo.Robots.MainRobotCleaner;
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.ProtocolType;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@Controller
public class HIController {
    private static final String robotCmdId = "move";
    private static  String robotName  = "";

    private Interaction2021 conn;

    public HIController(){
        ColorsOut.outappl("HIController: CREATE"   , ColorsOut.WHITE_BACKGROUND);
        //createRobotCleaner();
        createBasicRobot();
    }

    protected void createRobotCleaner(){
        robotName  = MainRobotCleaner.myName;
        CommUtils.aboutThreads("Before start - ");
        MainRobotCleaner appl = new MainRobotCleaner( );
        appl.doJob();
        //appl.terminate();
    }
    protected void createBasicRobot(){
        CommSystemConfig.tracing = true;
        robotName  = MainBasicRobot.myName;
        CommUtils.aboutThreads("Before start - ");
        MainBasicRobot appl = new MainBasicRobot( );
        appl.doJob();
        //appl.terminate();
    }
    private IApplMessage moveAril( String cmd  ) {
        //ColorsOut.outappl("HIController | moveAril cmd:" + cmd , ColorsOut.BLUE);
        switch( cmd ) {
            case "w" : return CommUtils.buildDispatch("webgui", robotCmdId, "w", robotName);
            case "s" : return CommUtils.buildDispatch("webgui", robotCmdId, "s",robotName);
            case "a" : return CommUtils.buildDispatch("webgui", robotCmdId, "a",    robotName);
            case "d" : return CommUtils.buildDispatch("webgui", robotCmdId, "d",   robotName);
            case "h" : return CommUtils.buildDispatch("webgui", robotCmdId, "h",       robotName);
            default: return CommUtils.buildDispatch("webgui",   robotCmdId, "h",       robotName);
        }
    }
    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("arg", appName);
        return "RobotNaiveGui";
    }

    @PostMapping("/configure")
    public String configure(Model viewmodel  , @RequestParam String move, String addr ){
        ColorsOut.outappl("HIController | configure:" + move, ColorsOut.BLUE);
        ConnQakBase connToRobot = ConnQakBase.create( ProtocolType.tcp );
        conn = connToRobot.createConnection(addr, 8020);
        return "RobotNaiveGui";
    }

    @PostMapping("/robotmove")
    public String doMove(Model viewmodel  , @RequestParam String move ){
                     //String @RequestParam(name="move", required=false, defaultValue="h")robotmove  )  {
        //sysUtil.colorPrint("HIController | param-move:$robotmove ", Color.RED)
        ColorsOut.outappl("HIController | doMove:" + move + " robotName=" + robotName, ColorsOut.BLUE);
        if( move.equals("t")){
            Qak22Util.sendAMsg( SystemData.startSysCmd("hicontroller",robotName) );
        }else{
            try {
                String msg = moveAril(move).toString();
                ColorsOut.outappl("HIController | doMove msg:" + msg , ColorsOut.BLUE);
                conn.forward( msg );
            } catch (Exception e) {
                ColorsOut.outerr("HIController | doMove ERROR:"+e.getMessage());
                //e.printStackTrace();
            }
        }
        return "RobotNaiveGui";
    }

    @ExceptionHandler
    public ResponseEntity handle(Exception ex) {
        HttpHeaders responseHeaders = new HttpHeaders();
        return new ResponseEntity(
                "BaseController ERROR " + ex.getMessage(),
                responseHeaders, HttpStatus.CREATED);
    }
}
