/**
 UseRobotAril .java
 ===============================================================
 ===============================================================
 */
package it.unibo.robotAppls;
import it.unibo.annotations.ArilRobotSpec;
import it.unibo.interaction.IssOperations;
import it.unibo.interaction.MsgRobotUtil;

@ArilRobotSpec
public class UseRobotAril {
     private IssOperations robotSupport;

     public UseRobotAril(IssOperations support){  //Injected by UniboRobotApplicationStarter
         System.out.println("UseRobotAril | constructor support=" + support  );
         this.robotSupport = support;
     }

     protected void doBasicMoves(){
         robotSupport.forward( MsgRobotUtil.lMsg );
         //Thread.sleep(1000);
         robotSupport.forward( MsgRobotUtil.rMsg );
         //Thread.sleep(1000);
         String answer_w = robotSupport.requestSynch( MsgRobotUtil.wMsg );
         System.out.println("UseRobotAril | constructor answer_w=" + answer_w  );
         String answer_s  = robotSupport.requestSynch( MsgRobotUtil.sMsg );
         System.out.println("UseRobotAril | constructor answer_s=" + answer_s  );
         //Thread.sleep(1000);
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


     public static void main(String args[])  {
         Object appl = RobotApplicationStarter.createInstance(UseRobotAril.class);
         if( appl != null )  ((UseRobotAril)appl).doBasicMoves( );
         //if( appl != null )  ((UseRobotAril)appl).doBoundary(1,"");
     }
}
