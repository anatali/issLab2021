package it.unibo.radarGui21;
import org.eclipse.californium.core.CoapServer;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
 

public class RadarGuiCoap {
private CoapSupport coapSupport;
private Boolean polling = false;

	public RadarGuiCoap(   ) throws Exception {
		//createCoapResource();
		radarPojo.radarSupport.setUpRadarGui();
		System.out.println("RadarGuiCoap STARTED ");
		delay( 2000 ); //give time to show the GUI 
		coapSupport = new CoapSupport("coap://localhost:5683", "robot/sonar");
		if( polling ) doJobPolling(); 
		else coapSupport.observeResource( new DistanceHandler() );
	}

	/*
	private void  createCoapResource(){
		CoapServer server = new CoapServer();
		server.add( 
				new Resource("robot").add(
					new Resource("sonar") )  //robot/sonar
		);
		server.start();		
	}
 */
	
	private void doJobPolling() throws Exception {
		while( true ) {
			 String msg  = coapSupport.readResource();
			 ApplMessage m = new ApplMessage( msg );
			 //System.out.println("doJobPolling " + m.msgContent());	//sonar(d)
			 String distance = ((Struct) Term.createTerm(m.msgContent())).getArg(0).toString();
			 radarPojo.radarSupport.update(distance,"90");		
 			 delay( 500 );
		}
	}
	
 
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
 			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception{
		//RadarGuiCoap appl = 
				new RadarGuiCoap();		 
  	}

}
