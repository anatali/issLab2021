package it.unibo.enabler;

public class LedEnabler extends TcpEnabler{

	public LedEnabler(int port) throws Exception {
		super("ledEnabler",port);
	}


	@Override
	protected void elaborate(String message) {
		System.out.println("LedEnabler | elaborate " + message);
		
	}
	
	public static void main( String[] args) throws Exception {
		LedEnabler sys = new LedEnabler(8010);
	
	}



	
}
