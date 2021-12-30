package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.supports.Colors;


public class RadarGuiProxyAsClient extends ProxyAsClient implements IRadarDisplay {
 
	public RadarGuiProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		super( name, host, entry,protocol );
 	}

	@Override  //from IRadarDisplay
	public void update(String d, String a) {		 
 		String msg= "{ \"distance\" : D , \"angle\" : A }".replace("D",d).replace("A",a);
		try {
			sendCommandOnConnection(msg);
		} catch (Exception e) {
 			Colors.outerr(name+" | update ERROR:" +e.getMessage());;
		}   
 	}



	
	
}
