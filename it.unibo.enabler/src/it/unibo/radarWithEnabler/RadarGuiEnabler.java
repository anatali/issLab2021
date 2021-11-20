package it.unibo.radarWithEnabler;

import it.unibo.enabler.ApplMessageHandler;
import it.unibo.enabler.TcpClient;
import it.unibo.enabler.TcpEnabler;
import radarPojo.radarSupport;

class RadarGuiEnablerClientMsgHandler extends ApplMessageHandler{

	public RadarGuiEnablerClientMsgHandler() {
		radarSupport.setUpRadarGui();
	}
	
	@Override
	protected void elaborate(String message) {
		//System.out.println("RadarGuiEnabler | elaborate " + message);
		radarSupport.update(message,"90");
	}

}

public class RadarGuiEnabler extends TcpEnabler{
 
	public RadarGuiEnabler(int port) throws Exception {
		super("radarGuiEnabler",port, new RadarGuiEnablerClientMsgHandler() );
	}


	
	public static void main( String[] args) throws Exception {
		new RadarGuiEnabler(8020);
		//radarGui.start();
		TcpClient c  = new TcpClient( "localhost", 8020, null );
		c.forward("30");
	}
	
}
