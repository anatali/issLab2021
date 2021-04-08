package it.unibo.supports2021.usage;

import it.unibo.interaction.IJavaActor;
import it.unibo.interaction.MsgRobotUtil;
import it.unibo.supports2021.ActorBasicJava;
import it.unibo.supports2021.IssWsHttpJavaSupport;
import it.unibo.supports2021.usage.NaiveActorObserver;


public class RobotActorController extends ActorBasicJava {

    public RobotActorController(String name){
        super(name);
    }
    @Override
    protected void handleInput(String info) {
        System.out.println( myname + " | " + info + " " + aboutThreads() );
        if( info.equals("testHttp")) testHttp();
        else if( info.equals("testWs")) testWs();
        else if( info.equals("testObservers")) testObservers();
    }

    public void testHttp(){
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForHttp( "localhost:8090" );
        support.requestSynch( MsgRobotUtil.forwardMsg );
        support.requestSynch( MsgRobotUtil.backwardMsg );
        //support.forward( MsgRobotUtil.turnLeftMsg );
        support.close();
    }
    public void testWs(){
        IssWsHttpJavaSupport support = IssWsHttpJavaSupport.createForWs("localhost:8091" );

        IJavaActor obs = new NaiveActorObserver("obs",0);
        support.registerActor(obs);

        //String answer = support.requestSynch( MsgRobotUtil.turnRightMsg );
        //System.out.println("RobotActorController | testWs answer=" + answer);
        support.forward( MsgRobotUtil.turnLeftMsg );
        support.forward( MsgRobotUtil.turnRightMsg );   //notallowed
        ActorBasicJava.delay(1000);
        support.close();
    }

    public void testObservers(){
        IssWsHttpJavaSupport support   = IssWsHttpJavaSupport.createForWs("localhost:8091" );
        while( ! support.isOpen() ){  //busy form of waiting
            System.out.println("RobotActorController | testObservers support opening ... " );
            ActorBasicJava.delay(100);
        }
        NaiveActorObserver[] observers = new NaiveActorObserver[5];

        for( int i = 0; i<5; i++){
            observers[i] = new NaiveActorObserver("a"+i, i);
            support.registerActor( observers[i] );
        }

        support.forward( MsgRobotUtil.turnLeftMsg );

        ActorBasicJava.delay(1000);

        for( int i = 0; i<5; i++){
            support.removeActor(observers[i]);
            observers[i].terminate();
        }
        support.close();
    }

}
