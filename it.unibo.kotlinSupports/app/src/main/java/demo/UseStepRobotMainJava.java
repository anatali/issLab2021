package demo;

import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.MsgUtil;
import it.unibo.actorAppl.AbstracMainForActor;
import it.unibo.interaction.IUniboActor;
import kotlinx.coroutines.CoroutineScope;


public class UseStepRobotMainJava extends AbstracMainForActor {
//AbstracMainForActor is provided by the support

    private String startMsgStr    = "{\"start\":\"ok\" }";
    private String endMsgStr      = "{\"end\":\"ok\" }";
    private ApplMessage startMsg  = MsgUtil.buildDispatch("main", "start", startMsgStr, "any");
    private ApplMessage endMsg    = MsgUtil.buildDispatch("main", "end", endMsgStr, "any");

    @Override
    public void mainJob(CoroutineScope scope) {
        System.out.println("UseStepRobotMainJava  | mainJob scope=" +  scope );

        IUniboActor worker = new SimpleRobotUserKJava("worker", scope);
        worker.send( startMsg.toString() );

        //ActorBasicKotlin obs = new NaiveActorObserverKJava( "a2", scope);
        //IJavaActor robot     = new BasicStepRobotActor("stepRobot", obs, scope, "localhost");
        //robot.registerActor(obs);
        //robot.send( turnleft.toString() );
        //robot.send( turnleft.toString() );

        //robot.send( dostep.toString() );
        //robot.send( turnleft.toString() );
        //WARNING: we must wait for the answer before doing another step ...
        /*
        robot.send( dostep.toString() );

        robot.send( turnleft.toString() );
        robot.send( turnleft.toString() );
        robot.send( turnleft.toString() );
        */
    }

    public static void main(String[] args){
        UseStepRobotMainJava appl = new UseStepRobotMainJava();
        appl.startmain();
        //System.out.println("main | ENDS"   );
        System.exit(0); //otherwise we wait for graceful termination of ws-okhttp
    }

}
