package it.unibo.enablerCleanArch.supports.coap;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.supports.Colors;

public class ObserverNaive  implements CoapHandler{
private String name;
	public ObserverNaive( String name ) {
		this.name = name;
	}
	@Override
	public void onLoad(CoapResponse response) {
 		Colors.outappl(name + " | " + response.getResponseText(), Colors.ANSI_PURPLE);
	}
	@Override
	public void onError() {
 		Colors.outerr(name + " | error"  );	
	}	
}