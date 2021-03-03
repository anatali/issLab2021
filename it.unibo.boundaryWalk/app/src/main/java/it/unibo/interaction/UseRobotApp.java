/**
 UseRobotApp .java
 ===============================================================
 Use the (virtual) robot by exploiting Java annotation on the class
 to configure the protocol (HTTP or WS) and the move times.

 The interaction with the robot is based on high-level operations
 (forward, request) with payload of type AppMsg.
 ===============================================================
 */
package it.unibo.interaction;


/** //Interaction based on websocket
@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.WS,
        url="localHost:8091"
)
 */

/* //Interaction based on HTTP */
@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.HTTP,
        url      = "http://localHost:8090/api/move"
)
@RobotMoveTimeSpec( ltime = 300, wtime=400 )
public class UseRobotApp {

    private IssAppOperations robotSupport;

    //Factory method
    public static UseRobotApp create(){
        UseRobotApp obj                  = new UseRobotApp();  //appl-object
        IssOperations commSupport        = IssCommsFactory.create( obj  );
        IssOperations arilRobotSupport   = new IssArilRobotSupport( obj, commSupport ); //'inject'
        IssAppOperations rs              = new IssAppRobotSupport( obj, arilRobotSupport );
        obj.robotSupport                 = rs;
        //In the future we could use different robots and thus different robotSupport,
        return obj;  //return the created appl-object
    }

    public void doJob() throws Exception{
        System.out.println("UsageRobot | doJob START "    );
        robotSupport.forward( MsgRobotUtil.left );
        Thread.sleep(1000);
        robotSupport.forward( MsgRobotUtil.right );
        Thread.sleep(1000);
        robotSupport.requestSynch( MsgRobotUtil.ahead );
        robotSupport.requestSynch( MsgRobotUtil.back );
        //Thread.sleep(1000);
    }


/*
    public String doBoundary( int stepNum, String journey){
        //int stepNum = 1;
        if (stepNum > 4) {
             return journey;
        }
        String answer = robotSupport.requestSynch( "w" );
        while( answer.equals("true") ){
            journey = journey + "w";
            answer = robotSupport.requestSynch( "w" );
        }
        //collision
        robotSupport.requestSynch("l");
        return doBoundary(stepNum + 1, journey + "l");
    }

 */
    public static void main(String args[]) throws Exception{
        UseRobotApp appl = UseRobotApp.create();
        appl.doJob();
        //String journey = appl.doBoundary(1,"");
        //System.out.println("UsageRobot | doBoundary BYE journey=" + journey);
    }
}
