/**
 IssRobot.java
 ===============================================================
 Abstract robot as object with AppMsg
 Later: robotActor
 ===============================================================
 */
package it.unibo.interaction;
import java.util.HashMap;

//@RobotMoveTimeSpec
public class IssRobot { //implements IssOperations{
    //private IssOperations support;
    //private Object supported;
    private static HashMap<String, Integer> timemap = new HashMap<String, Integer>( );

    public IssRobot( ){ //(Object supportedObj, IssOperations support){
        //this.support   = support;
        //IssAnnotationUtil.getMoveTimes( supportedObj, timemap );
      }


    //@Override
    public void forward( AppMsg  msg )   { //AppMsg
         String move = msg.getContent();
         System.out.println("IssRobot | forward move=" + move);
    }

    //@Override
    public String request( AppMsg move ) {
        return "";
    }

}
