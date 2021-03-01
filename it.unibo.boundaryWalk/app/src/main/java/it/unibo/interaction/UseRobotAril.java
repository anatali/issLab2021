/**
 UseVirtualRobot.java
 ===============================================================
 Use the (virtual) robot by exploiting Java annotation on the class
 to configure the protocol (HTTP or WS) and the move times.

 HOWEVER, the configuration can also be set (WITH PRIORITY over the class)
    by the file IssProtocolConfig.txt for the protocol
 by the file IssRobotConfig.txt       for the move times
 ===============================================================
 */
package it.unibo.interaction;


/** //Interaction based on websocket */
@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.WS,
        url="localHost:8091"
)


/* //Interaction based on HTTP
@IssProtocolSpec(
        protocol = IssProtocolSpec.issProtocol.HTTP,
        url      = "http://localHost:8090/api/move"
)
*/

@RobotMoveTimeSpec( ltime = 300, wtime=200 )
public class UseRobotAril {

    private IssOperations robotSupport;

    //Factory method
    public static UseRobotAril create(){
        UseRobotAril obj          = new UseRobotAril();  //appl-object
        IssOperations commSupport = IssCommsFactory.create( obj  );
        obj.robotSupport          = new IssVirtualRobotSupport( obj, commSupport ); //'inject'
        //In the future we could use different robots and thus different robotSupport,
        return obj;  //return the created appl-object
    }

    public void doJob() throws Exception{
        System.out.println("UsageRobot | doJob START"  );
        robotSupport.forward( "r" );
        Thread.sleep(1000);        //required ONLY if we use websockets
        String answer = robotSupport.requestSynch( "r" );
        System.out.println("UsageRobot | doJob answer to r= " + answer);
        Thread.sleep(1000);      //required ONLY if we use websockets
        robotSupport.request( "l" );    //the answer is sent but we do not wait
        Thread.sleep(1000);      //required ONLY if we use websockets
    }



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
    public static void main(String args[]) throws Exception{
        UseRobotAril appl = UseRobotAril.create();
        appl.doJob();
        //String journey = appl.doBoundary(1,"");
        //System.out.println("UsageRobot | doBoundary BYE journey=" + journey);
    }
}
