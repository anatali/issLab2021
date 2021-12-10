package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.server.resources.Resource;
import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarModel;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
 

public class CoapSonarResource extends CoapDeviceResource {  
private ISonar sonar; 
private String curVal = "-1";

	public CoapSonarResource( String name  ) {  
		super( name, deviceType.input )  ;
		sonar = SonarModel.create();
		getSonarValues();
		System.out.println( getName() + " |  CREATED"   );	
 	}
	
	private void getSonarValues() {
		new Thread() {
			public void run() {
				sonar.activate();
				while( sonar.isActive() ) {
					int v = sonar.getVal();
					elaborateAndNotify(  v );
				}
			}
		}.start();
	}
	
	 // CoapDeviceResource
		@Override
		protected String elaborateGet(String req) {
			//System.out.println( getName() + " |  elaborateGet:" + req  );		
			return  curVal;
		}

		@Override
		protected void elaboratePut(String arg) {
 			//System.out.println( getName() + " |  elaboratePut:" + arg  );		
			if( arg.equals("stop")) sonar.deactivate(); 	
 			changed();	// notify all CoAp observers
		}
		
		protected void elaborateAndNotify(int arg) {
 			//System.out.println( getName() + " |  elaborateAndNotify:" + arg  );		
			curVal= ""+arg;
			changed();	// notify all CoAp observers
		}

	 
	public static void main(String[] args) throws  Exception {
	//CONFIGURATION
	RadarSystemConfig.simulation = true;
 	//CoapApplServer.init();  //(SINGLETON) lo fa la risorsa creata
	//Create sonar resource
	Resource sonarRes = new CoapSonarResource("sonar") ;  //  
	//CoapApplObserver obs = 
		new CoapApplObserver( "localhost", 
			CoapApplServer.inputDeviceUri+"/sonar" ,
			new SonarMessageHandler( null ) );	//TODO
 	//USAGE
	String uri = CoapApplServer.inputDeviceUri+"/"+sonarRes.getName();
	System.out.println("uri= " + uri );
	CoapSupport cps = new CoapSupport("localhost", uri );
 	
 
	for( int i= 1; i<=5; i++) {
		String vs = cps.readResource();		//invia GET	
		System.out.println("vs=" + vs );
		Thread.sleep(800);
	}
	
	Thread.sleep(500);
 
	cps.updateResource("stop");

	//Altro modo per leggere i dati
	CoapClient client  = new CoapClient( "coap://localhost:5683/"+ uri ); //+"?value=10"
	CoapResponse answer = client.get();
	System.out.println("answer=" + answer.getCode() );
	System.out.println("valore finale=" + answer.getResponseText() );
 

	String vs = cps.readResource();		//invia GET	
	System.out.println("\nvs final=" + vs );
 
	}

}
 	/*
	protected void elaborate(String arg) {
		//System.out.println(getName() + " | elaborate arg=" + arg );
		try {
			JSONObject jsonObj = new JSONObject( arg );
			String model = jsonObj.getString("model");
			int    limit = jsonObj.getInt("limit");
			//System.out.println(getName() + " | moodel=" + model + " limit="+limit+" from " + exchange.getRequestCode() );
			this.model    = model;
			limitDistance = limit;
		}catch( Exception e) {
			limitDistance = Integer.parseInt(arg);
		}
		handler.elaborate(arg);
	}
	*/	


 
