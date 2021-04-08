package it.unibo.supports2021;

import it.unibo.interaction.IJavaActor;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class ActorBasicJava extends Thread implements IJavaActor {
    private boolean goon = true;
    private Vector<IJavaActor> actorobservers      = new Vector<IJavaActor>();
    private BlockingQueue<String> bqueue           = new LinkedBlockingQueue<String>(10);
    protected String myname ;

    public ActorBasicJava(String name){
        this.myname = name ;
        this.setName( "th_"+name );   //set the name of the thread
        start();
    }

    //------------------------------------------------
    //Utility ops to communicate with another, known actor
    protected void forward(@NotNull String msg, @NotNull it.unibo.supports2021.ActorBasicJava dest) {
        dest.send(msg);
    }
    protected void request(@NotNull String msg, @NotNull it.unibo.supports2021.ActorBasicJava dest) { dest.send(msg); }
    protected void reply(@NotNull String msg, @NotNull it.unibo.supports2021.ActorBasicJava dest) { dest.send(msg);   }

    protected abstract void handleInput(String info);

    @Override
    public String myname(){
        return myname;
    }

    @Override
    public synchronized void send( String msg ){
        try {
            bqueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------
    @Override
    public void run() {
        while (goon){
            waitInputAndElab();
        }
        //System.out.println( name + " | waitInputAndElab END "  );
    }

    public void terminate(){
        //System.out.println(name +  " | terminate "   );
        goon = false;
        send("bye");
    }

    protected void waitInputAndElab() {
        try {
            //System.out.println(name +  " | waitInputAndElab ... "  );
            String info = bqueue.take();
             if (goon) handleInput(info);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




//---------------------------------------------------------------------------
    @Override
    public void registerActor(@NotNull IJavaActor obs) {
        actorobservers.add(obs);
    }

    @Override
    public void removeActor(@NotNull IJavaActor obs) { actorobservers.remove(obs);   }

    protected void updateObservers(String info ){
         //System.out.println(name + " update " + actorobservers.size() );
         actorobservers.forEach( v -> { v.send(info); } );
    }


//-----------------------------------------------------
    public static String aboutThreads(){
        return "curThread="+Thread.currentThread().getName() +" nthreads="+ Thread.activeCount() ;
    }
    public static void delay(int dt){
        try {
            Thread.sleep(dt);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}