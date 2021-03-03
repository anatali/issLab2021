/**
 UseRobotVirtual .java
 ===============================================================
 ===============================================================
 */
package it.unibo.robotAppls;
import it.unibo.annotations.VirtualRobotSpec;
import it.unibo.interaction.IssOperations;
import it.unibo.interaction.MsgRobotUtil;

@VirtualRobotSpec
public class UseRobotVirtual {

    private IssOperations robotSupport;

    public UseRobotVirtual(IssOperations support){  //Injected by UniboRobotApplicationStarter
        System.out.println("UseRobotVirtual | constructor support=" + support  );
        this.robotSupport = support;
    }

    public void doBasicMoves() throws Exception{
        robotSupport.forward( MsgRobotUtil.turnLeftMsg );
        Thread.sleep(1000);        //required ONLY if we use websockets
        robotSupport.forward( MsgRobotUtil.turnRightMsg );
        Thread.sleep(1000);      //required ONLY if we use websockets
        String answer_w = robotSupport.requestSynch( MsgRobotUtil.forwardMsg );
        System.out.println("UseRobotVirtual | doBasicMoves answer_w=" + answer_w  );
        String answer_s  = robotSupport.requestSynch( MsgRobotUtil.backwardMsg);
        System.out.println("UseRobotVirtual | doBasicMoves answer_s=" + answer_s  );
        //Thread.sleep(1000);      //required ONLY if we use websockets
    }

    public String doBoundary( int stepNum, String journey){
        if (stepNum > 4) {
            return journey;
        }
        String answer = robotSupport.requestSynch( MsgRobotUtil.forwardMsg );
        while( answer.equals("true") ){
            journey = journey + "w";
            answer = robotSupport.requestSynch( MsgRobotUtil.forwardMsg );
        }
        //collision
        robotSupport.requestSynch( MsgRobotUtil.turnLeftMsg );
        return doBoundary(stepNum + 1, journey + "l");
    }

    public static void main(String args[]) throws Exception{
        Object appl = RobotApplicationStarter.createInstance(UseRobotVirtual.class);
        if( appl != null )  ((UseRobotVirtual)appl).doBasicMoves( );
        //String journey = appl.doBoundary(1,"");
        //System.out.println("UsageRobot | doBoundary BYE journey=" + journey);
    }
}
