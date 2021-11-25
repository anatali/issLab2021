package coap;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.CoapServer;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.kactor.ApplMessage;
 

class SonarDataHandler implements CoapHandler {
	public SonarDataHandler ( ) {		 
	}
	@Override public void onLoad(CoapResponse response) {
		 String msg = response.getResponseText();
		 ApplMessage m = new ApplMessage( msg );
		 //System.out.println("SonarDataHandler " + m.msgContent());	//sonar(d)
		 String distance = ((Struct) Term.createTerm(m.msgContent())).getArg(0).toString();
		 radarPojo.radarSupport.update(distance,"0");
  	}					
	@Override public void onError() {
		System.err.println("SonarDataHandler  |  FAILED (press enter to exit)");
	}
}

public class RadarGuiCoap {
private CoapSupport coapSupport;
private Boolean polling = false;

	public RadarGuiCoap(   ) {
		createCoapResource();
		radarPojo.radarSupport.setUpRadarGui();
		System.out.println("RadarGuiCoap STARTED ");
		delay( 2000 ); //give time to show the GUI 
		coapSupport = new CoapSupport("coap://localhost:5683", "robot/sonar");
		if( polling ) doJobPolling();
		else doObserve();
	}
	
	private void  createCoapResource(){
		CoapServer server = new CoapServer();
		server.add( 
				new Resource("robot").add(
					new Resource("sonar") )  //robot/sonar
		);
		server.start();		
	}

	private void doObserve() {
		coapSupport.observeResource( new SonarDataHandler() );
		//radarPojo.radarSupport.update("90","90");	
	}
	
	private void doJobPolling() {
		while( true ) {
			showSonarValue();
 			delay( 500 );
		}
	}
	
	private void showSonarValue() {
		 String msg = coapSupport.readResource();
		 ApplMessage m = new ApplMessage( msg );
		 //System.out.println("doJobPolling " + m.msgContent());	//sonar(d)
		 String distance = ((Struct) Term.createTerm(m.msgContent())).getArg(0).toString();
		 radarPojo.radarSupport.update(distance,"90");		
	}
	
	private void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
 			e.printStackTrace();
		}
	}

	public static void main(String[] args){
		//RadarGuiCoap appl = 
				new RadarGuiCoap();		 
  	}

}
