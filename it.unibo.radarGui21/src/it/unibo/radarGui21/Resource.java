package it.unibo.radarGui21;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CHANGED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.CREATED;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.DELETED;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import java.io.FileInputStream;
import java.util.Properties;


public class Resource extends CoapResource {


private String lastMsg    = "msg(sonar,event,sonarOnRaspCoap,none,sonar(00),0)";

	public Resource( String name ) throws Exception{
		super(name);
 		LOGGER.info("Resource " + name + " | created  ");
		//LOGGER.warn("Resource " + name + " | created  ");
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
		//Logger log = LoggerFactory.getLogger(Resource.class);
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
		System.out.println("Resource " + getName() +
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
		System.out.println("Resource " + getName() + " | PUT arg=" + arg + " from " + exchange.getRequestCode() );
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
	
 
	private static CoapServer server = new CoapServer();

	public static void createCoapResource(String path) throws Exception {
		String[] items = path.split("/");
		Resource root  = new Resource(items[0]);
		CoapResource r = root;
		for( int i=1; i < items.length; i++) {
			r = r.add( new Resource(items[i]) );			
		}
		server.add( r );   
		server.start();	
	}
	
	public static void stopServer() {
		server.stop();
	}
	
	public static void main(String[] args) throws  Exception {
		/*
		CoapServer server = new CoapServer();
		server.add( 
				new Resource("robot").add(
					new Resource("sonar") )  //robot/sonar
		);
		server.start();
		*/
		String path = "robot/sonar";
		Resource.createCoapResource(path);
		CoapSupport support = new CoapSupport("coap://localhost:5683", path);

		//support.updateResource("msg(sonar,event,sonarOnRaspCoap,none,sonar(10),1)");

		for( int i =1; i<=5; i++) {
			support.updateResource(""+10*i);
			Thread.sleep(1000);
		}
		
		String v = support.readResource();
		System.out.println("v=" + v);
		//support.updateResource("");
		
		//Resource.stopServer();
	}

}
