package it.unibo.radarGui21;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CREATED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.io.FileInputStream;
import java.util.Properties;


public class Distance extends CoapResource {
private String lastMsg    = "0"; //"msg(sonar,event,sonarOnRaspCoap,none,sonar(00),0)";

	public Distance( String name )  {
		super(name);
 		LOGGER.info("Distance " + name + " | created  ");
		//LOGGER.warn("Distance " + name + " | created  ");
 		setObservable(true);
	}

	private void experimentProperties() throws Exception {
		FileInputStream propFile = new FileInputStream( "log4j.properties");
		Properties p             = new Properties(System.getProperties());
		p.load(propFile);

		System.out.println("++++++++++++++++++++++ " + p.getProperty("log4j.defaultInitOverride"));
		//About loggin:
		// 	https://sematext.com/blog/slf4j-tutorial/
		//	https://examples.javacodegeeks.com/enterprise-java/slf4j/slf4j-tutorial-beginners/
		//  https://www.tutorialspoint.com/slf4j/slf4j_overview.htm
		//Logger log = LoggerFactory.getLogger(Distance.class);
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
		System.out.println("Distance " + getName() +
				" | handleGET from:" + exchange.getSourceAddress() + " arg:" + exchange.getRequestText() +
		        " lastMsg = " + lastMsg );
		exchange.respond( lastMsg );
	}

/*
 * POST is NOT idempotent.	Use POST when you want to add a child resource
 */
	@Override
	public void handlePOST(CoapExchange exchange) {
		exchange.accept();		
		if (exchange.getRequestOptions().isContentFormat(MediaTypeRegistry.TEXT_PLAIN)) {
			String xml = exchange.getRequestText();
			exchange.respond(CREATED, xml.toUpperCase());
			
		} else {
			// ...
			exchange.respond(CREATED);
		}
	}
	
/*
 * PUT method is idempotent. Use PUT when you want to modify 
 * RFC-2616 clearly mention that PUT method requests for the enclosed entity 
 * be stored under the supplied Request-URI. 
 * If the Request-URI refers to an already existing resource � 
 * an update operation will happen, otherwise create operation should happen 
 * if Request-URI is a valid resource URI 
 * (assuming client is allowed to determine resource identifier).	
 */

	@Override
	public void handlePUT(CoapExchange exchange) {
		String arg = exchange.getRequestText() ;		
		System.out.println(getName() + " | arg=" + arg + " from " + exchange.getRequestCode() );
		lastMsg = arg;
 		changed();	// notify all CoAp observers		
    	/*
    	 * Notifies all CoAP clients that have established an observe relation with
    	 * this resource that the state has changed by reprocessing their original
    	 * request that has established the relation. The notification is done by
    	 * the executor of this resource or on the executor of its parent or
    	 * transitively ancestor. If no ancestor defines its own executor, the
    	 * thread that has called this method performs the notification.
    	 */
		exchange.respond(CHANGED);
	}

	@Override
	public void handleDELETE(CoapExchange exchange) {
		delete();
		exchange.respond(DELETED);
	}


	public static void testNaive(String uri ) throws  Exception {
		CoapClient client      = new CoapClient( "coap://localhost:5683/"+ uri );
		String v1              = client.get().getResponseText();
		System.out.println("v1=" + v1);

		new DistanceResourceObserver( "localhost:5683", uri) ;

		for( int i =1; i<=5; i++) {
 			String updateMsg  = ""+10*i;
			//String updateMsg  = "msg(sonar,event,sonarOnRaspCoap,none,sonar(10),1)" ;
			CoapResponse resp = client.put(updateMsg, MediaTypeRegistry.TEXT_PLAIN);
			if( resp != null ) System.out.println("updateResource RESPONSE CODE: " + resp.getCode());
			else System.out.println("updateResource FAILURE "  );
			Thread.sleep(500);
		}

		System.out.println("current distance=" + client.get().getResponseText());

	}

	public static void testWithSupport( String resourceUri ) throws  Exception {
		CoapSupport support    = new CoapSupport("coap://localhost:5683", resourceUri);
		String vs              = support.readResource();
		int    v               = Integer.parseInt(vs);
		System.out.println("v=" + v);

		support.observeResource( new DistanceHandlerWithRadarGui( ) );

		Thread.sleep(1500);  //Give time of the DistanceHandlerWithRadarGui to start with RadarGui

		for( int i =1; i<=5; i++) {
			Thread.sleep(500);
			String updateMsg  = "" + (v+5*i);
			//String updateMsg  = "msg(sonar,event,sonarOnRaspCoap,none,sonar(10),1)" ;
			support.updateResource(updateMsg);
		}

		support.removeObserve();
 	}
		public static void main(String[] args) throws  Exception {
		/*
		Create the CoapApplServer
		 */
		CoapApplServer.init();

		/*
		Create distance as the resource of a sonar output-device
		 */
		CoapResource sonarDistance  = new CoapResource("sonar").add( new Distance("distance") );
		CoapApplServer.addCoapResource( sonarDistance, "/devices/output"  );

		String uri            = "devices/output/sonar/distance";


		Distance.testNaive(uri);
		Distance.testWithSupport(uri);

	}

}
