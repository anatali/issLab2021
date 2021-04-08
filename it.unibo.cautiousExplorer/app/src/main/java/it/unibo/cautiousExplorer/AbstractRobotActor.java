package it.unibo.cautiousExplorer;
import it.unibo.interaction.IJavaActor;
import org.json.JSONObject;
import it.unibo.supports2021.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class AbstractRobotActor extends ActorBasicJava {

    protected int moveInterval = 500;   //to avoid too-rapid movement
    protected IssWsHttpJavaSupport support;
    protected Console cnsl     = System.console();  //returns null in an online IDE
    protected final Map<String, String> MoveNameShort = new HashMap<String, String>();

    public AbstractRobotActor( String name ) {
        super(name);
        support = IssWsHttpJavaSupport.createForWs("localhost:8091" );
        support.registerActor(this);
        MoveNameShort.put("moveForward","w");
        MoveNameShort.put("moveBackward","s");
        MoveNameShort.put("turnLeft","l");
        MoveNameShort.put("turnRight","r");
        MoveNameShort.put("alarm","h");
    }


    //------------------------------------------------
    protected void doStep(){
        support.forward( ApplMsgs.forwardMsg);
        delay(moveInterval); //to avoid too-rapid movement
    }
    protected void microStep(){
        support.forward( ApplMsgs.microStepMsg);
        delay(moveInterval); //to avoid too-rapid movement
    }
    protected void doBackStep(){
        support.forward( ApplMsgs.backwardMsg );
        delay(moveInterval); //to avoid too-rapid movement
    }
    protected void microBackStep(){
        support.forward( ApplMsgs.littleBackwardMsg );
        delay(moveInterval); //to avoid too-rapid movement
    }
    protected void turnLeft(){
        support.forward( ApplMsgs.turnLeftMsg );
        delay(moveInterval); //to avoid too-rapid movement
    }
    protected void turnRight(){
        support.forward( ApplMsgs.turnRightMsg );
        delay(moveInterval); //to avoid too-rapid movement
    }


    protected void doMove(char moveStep){
        if( moveStep == 'w') doStep();
        else if( moveStep == 'l') turnLeft();
        else if( moveStep == 'r') turnRight();
        else if( moveStep == 's') doBackStep();
    }


    protected long getCurrentTime(){
        return System.currentTimeMillis();
    }

    protected long getDuration( long start){
        long d = System.currentTimeMillis() - start;
        return d;
    }
    //StartTime = getCurrentTime()  Duration = getDuration(StartTime)

    protected void reactivate(IJavaActor actor){
        actor.send(ApplMsgs.resumeMsg);
    }

    protected void waitUser(String prompt){
        System.out.print(">>>  " + prompt + " >>>  ");
        Scanner scanner = new Scanner(System.in);
        try { scanner.nextInt(); } catch (Exception e) { e.printStackTrace(); }
    }
/*
======================================================================================
 */
    @Override
    protected void handleInput(String infoJson) {
        //System.out.println("AbstractRobotActor | infoJson:" + infoJson);
        msgDriven( new JSONObject(infoJson) );
    }

    abstract protected void  msgDriven( JSONObject infoJson);


}
