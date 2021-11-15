package it.unibo.enabler;

public class Controller extends Thread implements Runnable{
	public final int DLIMIT = 30;	
	private EnablerClient c_led,c_radarGui ;
	private IEnabler radarGui;
	private IEnabler led;
	
	//TODO: annotation/injection
	public Controller( IEnabler radarGui, IEnabler led) throws Exception {
		this.radarGui = radarGui;
		this.led      = led;
	}

	@Override
	public void run() {
		try {
			c_led         = new EnablerClient( "localhost", led);
			c_radarGui    = new EnablerClient( "localhost", radarGui);
			doWork();
 		} catch (Exception e) {
 			e.printStackTrace();
		}	
	}
	
	private void doWork() {
		try {
			System.out.println("Controller | working ... ");
			int d = 20;
			String msg = "sonarDistance("+d+")";
			c_radarGui.forward(msg);
			if( d < DLIMIT ) c_led.forward(msg);
		} catch (Exception e) {
 			e.printStackTrace();
		}	
		
	}
	
	

}
