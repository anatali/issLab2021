package it.unibo.supports2021.usage.basic;

import it.unibo.actor0.ApplMessage;
import it.unibo.actor0.sysUtil;
import it.unibo.supports2021.ActorBasicJava;
import org.jetbrains.annotations.NotNull;

public class NaiveActorBasicJava extends ActorBasicJava {

    public NaiveActorBasicJava(@NotNull String name) { super(name);
    }

    protected void handleInput(String info){
        if( info.equals("end") ) this.terminate();
        else System.out.println( myname + " | " + info + " " + sysUtil.aboutThreads(myname));

    }

 }
