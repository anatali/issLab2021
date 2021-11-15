package it.unibo.enabler;

public class RadarGuiEnabler extends TcpEnabler{

	public RadarGuiEnabler(int port) throws Exception {
		super("radarGuiEnabler",port);
	}


	@Override
	protected void elaborate(String message) {
		System.out.println("RadarGuiEnabler | elaborate " + message);
		
	}
	
	public static void main( String[] args) throws Exception {
		RadarGuiEnabler sys = new RadarGuiEnabler(8010);
	
	}
	
}
