package it.unibo.robotAppls;

import it.unibo.interaction.IssOperations;
import it.unibo.interaction.MsgRobotUtil;

public class BoundaryLogic {

    public static String doBoundary(int stepNum, String journey, IssOperations robotSupport ){
        if (stepNum > 4) {
            return journey;
        }

        String answer = robotSupport.requestSynch( MsgRobotUtil.ahead.getContent() );
        while( answer.equals("true") ){
            journey = journey + "w";
            answer = robotSupport.requestSynch( MsgRobotUtil.ahead.getContent() );
        }
        //collision
        robotSupport.requestSynch(MsgRobotUtil.left.getContent());
        return doBoundary(stepNum + 1, journey + "l", robotSupport);


    }

}
