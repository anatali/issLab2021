package it.unibo.cautiousExplorer;

import consolegui.ConsoleGuiActor;
import it.unibo.interaction.IJavaActor;
import it.unibo.supports2021.ActorBasicJava;

public class MainCautiosExplorer {

    public static void main(String args[]){
        System.out.println("================================================================");
        System.out.println("CautiousExplorer | main " ); //+ sysUtil.aboutThreads("main")
        System.out.println("================================================================");
    //Configure the system
        IJavaActor explorer = new CautiousExplorerActor("explorer" );

        ConsoleGuiActor console = new ConsoleGuiActor();
        console.registerActor(explorer);

        ActorBasicJava.delay(5000);
    }
}
