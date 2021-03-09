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
        //Thread.sleep(1000);
        robotSupport.forward( MsgRobotUtil.turnLeftMsg );
        Thread.sleep(400);        //required ONLY if we use websockets
        robotSupport.forward( MsgRobotUtil.turnRightMsg );
        //robotSupport.forward( MsgRobotUtil.rMsg );   //WRONG payload
        Thread.sleep(400);
        robotSupport.forward( MsgRobotUtil.forwardMsg );
        Thread.sleep(800);
        robotSupport.forward( MsgRobotUtil.backwardMsg );
        Thread.sleep(800);
        String answerForward = robotSupport.requestSynch( MsgRobotUtil.forwardMsg );
        System.out.println("UseRobotVirtual | doBasicMoves answerForward=" + answerForward  );
        Thread.sleep(800);
        String answerBackward  = robotSupport.requestSynch( MsgRobotUtil.backwardMsg);
        System.out.println("UseRobotVirtual | doBasicMoves answerBackward=" + answerBackward );
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
        if( appl != null ) {
            //String journey = appl.doBoundary(1,"");
            //System.out.println("UsageRobot | doBoundary BYE journey=" + journey);
        }

    }
}
