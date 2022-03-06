package it.unibo.radarSystem22.actors.counter;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.proxy.ProxyAsClient;
import it.unibo.actorComm.utils.BasicUtils;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.utils.ColorsOut;

public class CounterActorCaller  {
	public static final int ctxServerPort   = 7070;
	private String delay        = "1000";  

	private String decCmdStr = "msg( dec, dispatch, main, counter, DELAY, 1 )"
		      .replace("DELAY", delay);
	private String decReqStr =  "msg( dec, request, main, counter, DELAY, 1 )"
		      .replace("DELAY", delay);
	private ProxyAsClient pxyCaller;
	private String name ;
	
	public CounterActorCaller(String name) {
		this.name = name;
  		ColorsOut.outappl(name + " | CREATED"  , ColorsOut.CYAN);		
 		pxyCaller = new ProxyAsClient("pxyCaller","localhost", 
 				""+SharedCounterMain.ctxServerPort, ProtocolType.tcp);
 	}
		
	public void doCommand(   ) {
		IApplMessage msg = new ApplMessage( decCmdStr.replace("NAME", name));
		if( isRemote( msg.msgReceiver() ) ) {
			ColorsOut.outappl(name + " | doJob " + msg  , ColorsOut.CYAN);		
	 		pxyCaller.sendCommandOnConnection( msg.toString() );		
		}		
	}
	public void doRequest(  ) {
		IApplMessage msg = new ApplMessage( decReqStr.replace("NAME", name));
		if( isRemote( msg.msgReceiver() ) ) {
			ColorsOut.outappl(name + " | doJob " + msg  , ColorsOut.CYAN);		
	 		String answer = pxyCaller.sendRequestOnConnection( msg.toString() );		
			ColorsOut.outappl(name + " | answer=" + answer, ColorsOut.CYAN);	
		}
	}
	
	protected boolean isRemote( String actorName) {
		ColorsOut.outappl(name + " | " + actorName + " is REMOTE "  , ColorsOut.CYAN);		
		return true;
	}


	public static void main(String[] args) {
		CounterActorCaller ac1 = new CounterActorCaller("ac1") ;
		ac1.doCommand();
 		ac1.doRequest();
		//BasicUtils.delay(3000);
  		ColorsOut.outappl( "CounterActorCaller | BYE"  , ColorsOut.CYAN);		
  	}



}
