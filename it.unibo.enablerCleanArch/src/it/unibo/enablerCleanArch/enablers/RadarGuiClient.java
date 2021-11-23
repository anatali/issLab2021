package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.domain.IRadarGui;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;

public class RadarGuiClient extends EnablerAsClient implements IRadarGui {
 
	public RadarGuiClient( String name, String host, int port ) {
		super( name, host, port );
 	}

	@Override
	protected Interaction2021 setProtocolClient(String host, int port) throws Exception {
		return TcpClient.connect( host, port);
	}
	
	@Override  //from IRadarGui
	public void update(String d, String a) {		 
 		String msg= "{ \"distance\" : D , \"angle\" : A }".replace("D",d).replace("A",a);
		try {
			sendValueOnConnection(msg);
		} catch (Exception e) {
 			e.printStackTrace();
		}   
 	}


	
	
}
