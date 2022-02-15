package it.unibo.enablerCleanArch.main.onpc;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
//import it.unibo.enablerCleanArch.domain.Distance;
//import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.IObserver;
import it.unibo.enablerCleanArch.supports.ColorsOut;
 

public class SonarObserverCoap  implements CoapHandler{
private String name;
private IObserver h;

	public SonarObserverCoap( String name, IObserver h ) {
		this.name  = name;
		this.h     = h;
		ColorsOut.outappl(name + " | CREATED ", ColorsOut.BgGreen);
 	}
	@Override
	public void onLoad(CoapResponse response) {
 		String vs   = response.getResponseText();
 		ColorsOut.outappl(name + " | " + vs , ColorsOut.ANSI_PURPLE);
//		int v       = Integer.parseInt(vs)  ;
//		IDistance d = new Distance(v);
		h.update( vs );  //un oggetto che dovrebbe inviare l'info sulla websocket
  	}
	@Override
	public void onError() {
 		ColorsOut.outerr(name + " | error"  );	
	}	
}