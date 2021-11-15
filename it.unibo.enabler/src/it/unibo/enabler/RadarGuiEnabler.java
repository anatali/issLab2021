package it.unibo.enabler;

import radarPojo.radarSupport;

public class RadarGuiEnabler extends TcpEnabler{
 
	public RadarGuiEnabler(int port) throws Exception {
		super("radarGuiEnabler",port);
		radarSupport .setUpRadarGui();
	}

	@Override
	protected void elaborate(String message) {
		//System.out.println("RadarGuiEnabler | elaborate " + message);
		radarSupport.update(message,"90");
	}
	
	public static void main( String[] args) throws Exception {
		RadarGuiEnabler radarGui = new RadarGuiEnabler(8020);
		//radarGui.start();
		EnablerClient c          = new EnablerClient( "localhost", radarGui.getPort() );
		c.forward("30");
	}
	
}
