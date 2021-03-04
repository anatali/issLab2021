/**
 UseRobotAril .java
 ===============================================================
 Use the (virtual) robot by exploiting Java annotation on the class
 to configure the protocol (HTTP or WS) and the move times.

 HOWEVER, the configuration can also be set (WITH PRIORITY over the class)
    by the file IssProtocolConfig.txt for the protocol
 by the file IssRobotConfig.txt       for the move times
 ===============================================================
 */
package it.unibo.interactionExamples;


import it.unibo.annotations.IssProtocolSpec;
import it.unibo.annotations.RobotMoveTimeSpec;
import it.unibo.interaction.IssOperations;
import it.unibo.interaction.MsgRobotUtil;
import it.unibo.robotSupports.IssArilRobotSupport;
import it.unibo.robotSupports.IssCommsSupportFactory;

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

@RobotMoveTimeSpec( ltime = 200, wtime=400 )
public class UseRobotArilHeavy {

    private IssOperations robotSupport;

    //Factory method
    public static UseRobotArilHeavy create(){
        UseRobotArilHeavy obj     = new UseRobotArilHeavy();  //appl-object
        IssOperations commSupport = IssCommsSupportFactory.create( obj  );
        obj.robotSupport          = new IssArilRobotSupport( obj, commSupport ); //'inject'
        //In the future we could use different robots and thus different robotSupport,
        return obj;  //return the created appl-object
    }

    public void doJob() throws Exception{
        System.out.println("UsageRobot | doJob START"  );
        robotSupport.forward( MsgRobotUtil.rMsg );
        Thread.sleep(1000);        //required ONLY if we use websockets
        String answer = robotSupport.requestSynch(MsgRobotUtil.rMsg );
        System.out.println("UsageRobot | doJob answer to r= " + answer);
        Thread.sleep(1000);      //required ONLY if we use websockets
        robotSupport.request( MsgRobotUtil.lMsg );    //the answer is sent but we do not wait
        Thread.sleep(1000);      //required ONLY if we use websockets
    }

    public String doBoundary( int stepNum, String journey){
        if (stepNum > 4) {
             return journey;
        }
        String answer = robotSupport.requestSynch( MsgRobotUtil.wMsg );
        while( answer.equals("true") ){
            journey = journey + "w";
            answer = robotSupport.requestSynch( MsgRobotUtil.wMsg );
        }
        //collision
        robotSupport.requestSynch( MsgRobotUtil.lMsg );
        return doBoundary(stepNum + 1, journey + "l");
    }

    public static void main(String args[]) throws Exception{
        UseRobotArilHeavy appl = UseRobotArilHeavy.create();
        //appl.doJob();
        String journey = appl.doBoundary(1,"");
        System.out.println("UsageRobot | doBoundary BYE journey=" + journey);
    }
}
