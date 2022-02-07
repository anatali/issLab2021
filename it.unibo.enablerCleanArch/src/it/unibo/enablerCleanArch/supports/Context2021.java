package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.coap.CoapContextServer;
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
			//CoapApplServer.getTheServer();
			ctx = new CoapContextServer(id,entry);
			ctx.activate();
			break;
		}
		default:
			break;
		}
		return ctx;
	}//create with args
	
	
	
	public static IContext create() {
		String clientId = null;
		String entry    = null;
		switch( RadarSystemConfig.protcolType ) {
			case tcp : {
				clientId = "tcpCtx";
				entry    = ""+RadarSystemConfig.ctxServerPort;
				break;
			}
			case mqtt : {
				clientId = "pc4";
				entry    = "pctopic";
				break;
			}
			case coap : {
				clientId = RadarSystemConfig.pcHostAddr;
				entry    = "";
				break;
			}
			default:
				break;
			}//switch
 
		return create(clientId,entry);   //activates! 
	}//create

}	