package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;

public class RadarGuiClient extends EnablerAsClient implements IRadarDisplay {
 
	public RadarGuiClient( String name, String host, int port, ProtocolType protocol ) {
		super( name, host, port,protocol );
 	}
/*
	@Override
	protected Interaction2021 setConnection(String host, int port) throws Exception {
		return TcpClient.connect( host, port, 10);
	}
*/	
	@Override  //from IRadarGui
	public void update(String d, String a) {		 
 		String msg= "{ \"distance\" : D , \"angle\" : A }".replace("D",d).replace("A",a);
		try {
			sendValueOnConnection(msg);
		} catch (Exception e) {
 			e.printStackTrace();
		}   
 	}
@Override
protected void handleMessagesFromServer(Interaction2021 conn) throws Exception {
	// TODO Auto-generated method stub
	
}


	
	
}
