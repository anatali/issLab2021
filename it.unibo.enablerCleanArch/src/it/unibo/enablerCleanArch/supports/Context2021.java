package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.mqtt.MqttContextServer;
import it.unibo.enablerCleanArch.supports.tcp.TcpContextServer;

public class Context2021 {

	public static IContext create(String id, String entry ) {
	IContext ctx = null;
	ProtocolType protocol = RadarSystemConfig.protcolType;
	
	ColorsOut.out("Context2021 CREATE support for protocol=" + protocol);
		switch( protocol ) {
		case tcp : {
			ctx=new TcpContextServer(id, entry);
			ctx.activate();
			break;
		}
		case mqtt : {
			ctx= new MqttContextServer( id, entry);
			ctx.activate();
			break;
		}
		case coap : {
			CoapApplServer.getTheServer();
			break;
		}
		default:
			break;
		}
		return ctx;
	}//create
}
