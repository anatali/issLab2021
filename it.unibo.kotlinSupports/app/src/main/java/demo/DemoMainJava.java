package demo;

import it.unibo.actor0.ActorBasicKotlin;
import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.DispatchType;
import it.unibo.actor0.MsgUtil;
import it.unibo.actorAppl.AbstracMainForActor;
import it.unibo.interaction.IJavaActor;
import it.unibo.robotService.ApplMsgs;
import it.unibo.robotService.BasicStepRobotActor;
import it.unibo.supports.NaiveActorKotlinObserver;


public class DemoMainJava extends AbstracMainForActor {
    @Override
    public void mainJob() {
        System.out.println("myscope=" +  getMyscope());
        ActorBasicKotlin a = new NaiveActorObserverJava(
                "a",getMyscope(), DispatchType.single,
                10 );
        IJavaActor b = new BasicStepRobotActor("stepRobot", a, getMyscope(), "localhost");
        String stepMsg = "{\"step\":\"350\" }";
        String turnLeftMsg = "{\"robotmove\":\"turnLeft\" @ \"time\": 300}";
        //ApplMessage m  = MsgUtil.buildDispatch("main", ApplMsgs.stepId, stepMsg, "stepRobot");
        ApplMessage m  = MsgUtil.buildDispatch("main", "move", turnLeftMsg, "stepRobot");
        b.registerActor(a);
        b.send( m.toString() );

        b.send( m.toString() );
    }

    public static void main(String[] args){
        DemoMainJava appl = new DemoMainJava();
        appl.mainJob();
    }
}
