package demo;

import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.MsgUtil;
import it.unibo.actorAppl.AbstracMainForActor;
import it.unibo.interaction.IUniboActor;
import it.unibo.robotService.ApplMsgs;
import kotlinx.coroutines.CoroutineScope;



public class DemoMainJava extends AbstracMainForActor {
//AbstracMainForActor is provided by the support

    @Override
    public void mainJob(CoroutineScope scope) {
        System.out.println("mainJob | scope=" +  scope );

        IUniboActor a1 = new SimpleRobotUserKJava("a1", scope); //new SimpleActorKJava("a1", scope);
        IUniboActor a2 = new NaiveActorObserverKJava( "a2", scope);
        a1.registerActor(a2);

        String endMsg        = "{\"endKJava\":\"ok\" }";
        ApplMessage endKJava = MsgUtil.buildDispatch("main", "endKJava", endMsg, "any");

        //String turnLeftMsg = "{\"robotmove\":\"turnLeft\" @ \"time\": 300}";
        //ApplMessage m      = MsgUtil.buildDispatch("main", "move", turnLeftMsg, "a1");
        //System.out.println("mainJob | send " + m );

        ApplMessage m1 = ApplMsgs.INSTANCE.stepRobot_l("mainJava");
        System.out.println("mainJob | send " + m1 );
        a1.send( m1.toString() );


        //a1.send( endKJava.toString() );
        //a2.send( endKJava.toString() );

    }

    public static void main(String[] args){
        DemoMainJava appl = new DemoMainJava();
        appl.startmain();
    }

}
        /*
        IJavaActor b = new BasicStepRobotActor("stepRobot", a, getMyscope(), "localhost");
        String stepMsg = "{\"step\":\"350\" }";
        String turnLeftMsg = "{\"robotmove\":\"turnLeft\" @ \"time\": 300}";
        //ApplMessage m  = MsgUtil.buildDispatch("main", ApplMsgs.stepId, stepMsg, "stepRobot");
        ApplMessage m  = MsgUtil.buildDispatch("main", "move", turnLeftMsg, "stepRobot");
        b.registerActor(a);
        b.send( m.toString() );

        b.send( m.toString() );
         */