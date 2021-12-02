package it.unibo.enablerCleanArch.adapters;

import org.json.JSONObject;
import org.json.JSONTokener;

import it.unibo.enablerCleanArch.domain.IRadarDisplay;
import it.unibo.enablerCleanArch.domain.RadarGui;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.TcpServer;

public class RadarGuiAdapterServer extends ApplMessageHandler  {
private IRadarDisplay radar = RadarGui.create();

	public RadarGuiAdapterServer( int port  ) {
		super("RadarGuiAdapterServer");
		try {
 			new TcpServer( name, port,  this );
		} catch (Exception e) {
 			e.printStackTrace();
		} 	
	}

	@Override
	public void elaborate(String message) {
		System.out.println(name+" elaborate message=" + message);
		//message : "{ \"distance\" : d , \"angle\" : a }"
        JSONTokener tokener = new JSONTokener(message);
        JSONObject object   = new JSONObject(tokener);
        
        String distance = ""+object.getInt("distance");
        String angke    = ""+object.getInt("angle");
        
        radar.update(distance, angke);
	}

}
