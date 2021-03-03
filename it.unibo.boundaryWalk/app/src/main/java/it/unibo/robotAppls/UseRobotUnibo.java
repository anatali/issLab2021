/**
 UseRobotUnibo .java
 ===============================================================
 ===============================================================
 */
package it.unibo.robotAppls;

import it.unibo.annotations.UniboRobotSpec;
import it.unibo.interaction.IssAppOperations;
import it.unibo.interaction.MsgRobotUtil;

@UniboRobotSpec
public class UseRobotUnibo {
     private IssAppOperations robotSupport;

     public UseRobotUnibo(IssAppOperations support){  //Injected by UniboRobotApplicationStarter
         //System.out.println("UseRobotUnibo | constructor support=" + support  );
         this.robotSupport = support;
     }

     protected void doJob(){
         System.out.println("UseRobotUnibo | doJob "  );
         robotSupport.forward( MsgRobotUtil.left );
         //Thread.sleep(1000);
         robotSupport.forward( MsgRobotUtil.right );
         //Thread.sleep(1000);
         robotSupport.requestSynch( MsgRobotUtil.ahead );
         robotSupport.requestSynch( MsgRobotUtil.back );
         //Thread.sleep(1000);
     }

    public String doBoundary( int stepNum, String journey){
        if (stepNum > 4) {
            return journey;
        }
        String answer = robotSupport.requestSynch( MsgRobotUtil.ahead );
        while( answer.equals("true") ){
            journey = journey + "w";
            answer = robotSupport.requestSynch( MsgRobotUtil.ahead );
        }
        //collision
        robotSupport.requestSynch(MsgRobotUtil.left);
        return doBoundary(stepNum + 1, journey + "l");
    }


     public static void main(String args[])  {
         //UniboRobotApplicationStarter.run( UseRobotUnibo.class );
         Object appl = RobotApplicationStarter.createInstance(UseRobotUnibo.class);
         if( appl != null )  ((UseRobotUnibo)appl).doBoundary(1,"");
     }
}
