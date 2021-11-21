package it.unibo.enablerCleanArch.enablers;

/*
 *  
 */
import it.unibo.enablerCleanArch.domain.IRadarGui;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;

public class RadarGuiClient implements IRadarGui {
private Interaction2021 conn; 

	public RadarGuiClient(  ) {
		try {
			conn = TcpClient.connect( RadarSystemConfig.pcHostAddr, RadarSystemConfig.radarGuiPort);
		} catch (Exception e) {
			System.out.println( "RadarGuiClient |  ERROR " + e.getMessage());
		}
 	}

	@Override
	public void update(String d, String a) {		 
		try {
			String msg= "{ \"distance\" : D , \"angle\" : A }".replace("D",d).replace("A",a);
			conn.forward(msg);   
		} catch (Exception e) {
			System.out.println( "RadarGuiClient |  ERROR " + e.getMessage());
		}		
	}
	
	
}
