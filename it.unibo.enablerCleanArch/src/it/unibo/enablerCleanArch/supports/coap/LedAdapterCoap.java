package it.unibo.enablerCleanArch.supports.coap;
 
import it.unibo.enablerCleanArch.domain.ILed;

/*
 * Adapter for an output device 
 */
public class LedAdapterCoap implements ILed {
private boolean ledStateMirror = false;
//private CoapClient client;
private CoapSupport cps;

	public LedAdapterCoap( String hostAddr, String resourceUri  ) {
 		System.out.println( "LedAdapterCoap |  STARTS for " + hostAddr +" resourceName=>"+resourceUri);
		//client  = new CoapClient( "coap://localhost:5683/"+ resourceUri ); //+"?value=10"
		cps = new CoapSupport(hostAddr, resourceUri );
		//CoapResponse answer = client.get();
	}
 	
	@Override
	public void turnOn() { 
 		try {
 			cps.updateResource( "on" );
			ledStateMirror = true;
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}

	@Override
	public void turnOff() {   
 		try {
 			cps.updateResource( "off" );
			ledStateMirror = false;
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}

	@Override
	public boolean getState() {   
		return ledStateMirror;
		/*
		try {
			return cps.readResource(   ).equals("true");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

	}

}
