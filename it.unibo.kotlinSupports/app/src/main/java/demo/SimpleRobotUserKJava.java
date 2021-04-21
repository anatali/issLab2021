package demo;

import it.unibo.actor0.ActorBasicKotlin;
import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.MsgUtil;
import it.unibo.robotService.BasicStepRobotActor;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.channels.SendChannel;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public class SimpleRobotUserKJava extends AbstractActorKJava {
    private ActorBasicKotlin robot;
    private String stepMsg        = "{\"step\":\"350\" }";
    private String turnLeftMsg    = "{\"robotmove\":\"turnLeft\" @ \"time\": 300}";
    private ApplMessage turnleft  = MsgUtil.buildDispatch("main", "move", turnLeftMsg, "stepRobot");
    private ApplMessage dostep    = MsgUtil.buildDispatch("main", "step", stepMsg, "stepRobot");

    public SimpleRobotUserKJava(@NotNull String name, @NotNull CoroutineScope scope ) {
        super( name, scope );
        robot  = new BasicStepRobotActor("stepRobot", this, scope, "localhost");
    }

    protected void doMoves(){
        robot.send( dostep.toString() );
        robot.send( turnleft.toString() );
        //robot.send( turnleft.toString() );
        //robot.send( turnleft.toString() );
    }
    @Override
    protected void handleMsg(ApplMessage msg, SendChannel<ApplMessage> myactor) {
        println("SimpleRobotUserKJava | " + msg   );
        if( msg.getMsgId().equals("start")) {
            doMoves();
        }else if( msg.getMsgId().equals("stepAnswer")   ){
            JSONObject answerJson = new JSONObject( msg.getMsgContent() );
            if( answerJson.has("stepDone")){
                  robot.send( turnleft.toString() );
            }
        }else if( msg.getMsgId().equals("endmove")   ){
            JSONObject answerJson = new JSONObject( msg.getMsgContent().replace("@",",") );
            if( ! answerJson.getString("endmove").equals("notallowed")){
                robot.terminate();
                this.terminate();
            }
        }
    }


}
