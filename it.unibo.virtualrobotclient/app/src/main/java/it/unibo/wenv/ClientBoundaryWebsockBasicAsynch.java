/*
===============================================================
ClientBoundaryWebsockBasicAsynch.java
Use the cril language and the support specified in the
configuration file WebsocketBasicConfig.txt

GOAL: use request instead of requestSynch
and handle the information sent by WEnv over the cmdSocket-8091
===============================================================
*/
package it.unibo.wenv;
import it.unibo.annotations.IssProtocolSpec;
import it.unibo.interaction.IssObserver;
import it.unibo.interaction.IssOperations;
import it.unibo.supports.IssCommsSupportFactory;

@IssProtocolSpec( configFile ="WebsocketBasicConfig.txt" )
public class ClientBoundaryWebsockBasicAsynch {
    private IssOperations support;
    private IssObserver   controller;
    //Factory method

    public static ClientBoundaryWebsockBasicAsynch createAndRun(){
        ClientBoundaryWebsockBasicAsynch obj = new ClientBoundaryWebsockBasicAsynch();
        IssOperations support                = new IssCommsSupportFactory().create( obj  );
        obj.setCommSupport(support);
        //support.registerObserver( new RobotObserver() );    //!!!!
        obj.controller = new RobotControllerBoundary(support);
        support.registerObserver( obj.controller );
        return obj;
    }

    protected void setCommSupport(IssOperations support){
        this.support = support;
    }

    public static void main(String args[]){
        ClientBoundaryWebsockBasicAsynch appl = ClientBoundaryWebsockBasicAsynch.createAndRun();
        try {
            RobotControllerBoundary ctrl = (RobotControllerBoundary) appl.controller;
            ctrl.start();
            Thread.sleep(30000);
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}
