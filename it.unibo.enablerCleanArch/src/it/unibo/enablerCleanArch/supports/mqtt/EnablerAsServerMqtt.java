package it.unibo.enablerCleanArch.supports.mqtt;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.coap.CoapApplServer;
import it.unibo.enablerCleanArch.supports.mqtt.MqttConnection;
import it.unibo.enablerCleanArch.supports.tcp.TcpServer;
 
/*
 * Attiva un server relativo al protocollo specificato (se non null)
 * lasciando la gestione dei messaggi inviati dai client alle classi specializzate
 * che si possono avvalere del metodo sendCommandToClient 
 * per inviare comandi e/o risposte a un client
 */
 
public class EnablerAsServerMqtt   {  
 
protected String name;
protected ProtocolType protocol;
protected TcpServer serverTcp;
protected boolean isactive = false;

 	
 	public EnablerAsServerMqtt( String name, String topic, IApplMsgHandler handler )   { //, String handlerClassName
		this.name     			= name;
		MqttConnection mqtt        = MqttConnection.getSupport();
		//mqtt.connectMqtt(name,topic, handler);
		ColorsOut.out(name+" |  CREATED  MqttSupport  handler="+handler);
	}
  	
 	public String getName() {
 		return name;
	}
 	public boolean isActive() {
 		return isactive;
 	}
 
 
 	public void stop() {
 	
 	}
  	 
}
