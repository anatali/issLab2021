package it.unibo.enablerCleanArch.main.onpc;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import it.unibo.enablerCleanArch.domain.Distance;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.supports.ColorsOut;
 

public class SonarObserverCoap  implements CoapHandler{
private String name;

	public SonarObserverCoap( String name ) {
		this.name  = name;
		ColorsOut.outappl(name + " | CREATED ", ColorsOut.BgGreen);
 	}
	@Override
	public void onLoad(CoapResponse response) {
		String vs   = response.getResponseText();
 		ColorsOut.outappl(name + " | " + vs , ColorsOut.ANSI_PURPLE);
		int v       = Integer.parseInt(vs)  ;
		IDistance d = new Distance(v);
  	}
	@Override
	public void onError() {
 		ColorsOut.outerr(name + " | error"  );	
	}	
}