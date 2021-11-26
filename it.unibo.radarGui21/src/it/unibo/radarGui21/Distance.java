package it.unibo.radarGui21;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CREATED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResource;
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
 * If the Request-URI refers to an already existing resource – 
 * an update operation will happen, otherwise create operation should happen 
 * if Request-URI is a valid resource URI 
 * (assuming client is allowed to determine resource identifier).	
 */

	@Override
	public void handlePUT(CoapExchange exchange) {
		String arg = exchange.getRequestText() ;		
		System.out.println("Distance " + getName() + " | PUT arg=" + arg + " from " + exchange.getRequestCode() );
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
	
 
	
	public static void main(String[] args) throws  Exception {
		CoapApplServer.init();
		String path      = "sonar/distance";
		CoapResource dr  = new Distance("distance");
		CoapApplServer.addCoapResource( dr, null );
		CoapSupport support = new CoapSupport("coap://localhost:5683", path);

		//new DistanceResourceObserver( "localhost", path) ;
		
		//support.updateResource("msg(sonar,event,sonarOnRaspCoap,none,sonar(10),1)");
/* 
		for( int i =1; i<=5; i++) {
			support.updateResource(""+10*i);
			Thread.sleep(1000);
		}
*/ 		
		String v = support.readResource();
		System.out.println("v=" + v);
	 
		 
		String path1      = "sonar/values";
		CoapResource dr1  = new Distance("values");
		CoapApplServer.addCoapResource( dr1, null );
		//CoapSupport support1 = new CoapSupport("coap://localhost:5683", path1);
		String url =  "coap://localhost:5683/"+ path1;
		CoapClient client = new CoapClient( url );
		String v1 = client.get().getResponseText();
		System.out.println("v1=" + v1);
		 
		
		CoapApplServer.getResource("");
	}

}
