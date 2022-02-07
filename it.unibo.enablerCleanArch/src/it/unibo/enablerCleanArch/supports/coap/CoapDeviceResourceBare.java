package it.unibo.enablerCleanArch.supports.coap;
import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.Response;
import org.eclipse.californium.core.server.resources.CoapExchange;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import static org.eclipse.californium.core.coap.CoAP.ResponseCode.*;

import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;


public abstract class CoapDeviceResourceBare extends CoapResource {

	public CoapDeviceResourceBare(String name, DeviceType dtype)  {
		super(name);
		setObservable(true); 
		CoapApplServer coapServer = CoapApplServer.getTheServer();  //SINGLETION
 		if( dtype==DeviceType.input )        coapServer.addCoapResource( this, CoapApplServer.inputDeviceUri);
 		else if( dtype==DeviceType.output )  coapServer.addCoapResource( this, CoapApplServer.lightsDeviceUri);
 		//LOGGER.info("CoapDeviceResource " + name + " | created  ");
	}
 
 
	protected boolean examineGETOptions(CoapExchange exchange) {
		boolean observe1Found = false;
			List<Option> opts = exchange.getRequestOptions().asSortedList();
			//Colors.out( getName() + "handleGET options size=" + opts.size() );
			Iterator<Option> iter = opts.iterator();
			while( iter.hasNext() ) {
				Option option = iter.next();
				ColorsOut.out( getName() + " handleGET options=" + option );
				//if( option.toString().contains("Observe: 1")) exchange.respond("BYE");
				observe1Found =  true;
			}		
			return observe1Found;
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
 		//examineGETOptions(exchange);
		String query = exchange.getQueryParameter("q"); 
		if( query == null ) {
			ColorsOut.out( getName() + " handleGET request=" + exchange.getRequestText() );
			String answer = elaborateGet( exchange.getRequestText(), exchange.getSourceAddress() );
			
			
			if( answer.length() < 500000 ) {
				ColorsOut.out( getName() + " handleGET request answer=" + answer , ColorsOut.GREEN );
				exchange.respond(answer);
			}
			else {
				ColorsOut.out( getName() + " handleGET  long answer=" + answer.length() , ColorsOut.RED ); 
				exchange.respond("long answer");
			}
			
		}else{  //query != null
 			ColorsOut.out( getName() + " handleGET query=" + query);
 			String answer = elaborateGet( exchange.getQueryParameter("q"), exchange.getSourceAddress() );
 			
 			
 			if( answer.length() < 500000 ) {
 				if( answer.length() < 1000) ColorsOut.out( getName() + "handleGET q-query answer=" + answer , ColorsOut.GREEN );
 				else ColorsOut.out( getName() + "handleGET q-long answer=" + answer.length() , ColorsOut.RED );
 				exchange.respond(answer);
 			}
			else {
				ColorsOut.out( getName() + "handleGET q-long answer=" + answer.length() , ColorsOut.RED ); 
	  			exchange.respond("too long");
	  		}

		}		
	}

/*
 * POST is NOT idempotent.	Use POST when you want to add a child resource
 */
	@Override
	public void handlePOST(CoapExchange exchange) {
 	}
 	@Override
	public void handlePUT(CoapExchange exchange) {
 		ColorsOut.out(getName() + " | handlePUT addr=" + exchange.getSourceAddress(), ColorsOut.BgYellow );
 		String arg = exchange.getRequestText() ;
		elaboratePut( arg, exchange.getSourceAddress() );
		//changed();
		ColorsOut.out(getName() + " | handlePUT arg=" + arg + " CHANGED="+ CHANGED );
		exchange.respond(""+CHANGED);
	}

 	protected abstract String elaborateGet(String req);
 	protected abstract String elaborateGet(String req, InetAddress callerAddr);
	protected abstract void elaboratePut(String req);	
 	protected abstract void elaboratePut(String req, InetAddress callerAddr);


	@Override
	public void handleDELETE(CoapExchange exchange) {
		delete();
		exchange.respond(DELETED);
	}

}
