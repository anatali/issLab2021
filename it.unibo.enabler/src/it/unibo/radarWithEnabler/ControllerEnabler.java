package it.unibo.radarWithEnabler;

import it.unibo.enabler.ApplMessageHandler;
import it.unibo.enabler.TcpEnabler;

/*
 * Elabora i messaggi inviati al Controller
 */
class ControllerMsgHandler extends ApplMessageHandler{
	private Controller ctrl ;
	
	public ControllerMsgHandler(Controller ctrl) {
		this.ctrl = ctrl;
	}
	
	@Override
	protected void elaborate(String distance) {
		System.out.println("ControllerEnablerClientMsgHandler | distance=" + distance);
		if( distance != null ) {
			ctrl.doWork( distance );
		}
	}
}

public class ControllerEnabler extends TcpEnabler{

	public ControllerEnabler( int port, Controller ctrl) throws Exception {
		super("ControllerEnabler", port, new ControllerMsgHandler(ctrl) );
		 
	}


	

}
