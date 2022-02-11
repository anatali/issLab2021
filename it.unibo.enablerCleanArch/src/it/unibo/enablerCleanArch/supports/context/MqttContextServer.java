package it.unibo.enablerCleanArch.supports.context;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandlerMqtt;
import it.unibo.enablerCleanArch.supports.mqtt.ContextMqttMsgHandler;
import it.unibo.enablerCleanArch.supports.mqtt.MqttConnection;

public class MqttContextServer implements IContext{
private MqttConnection mqtt ; //Singleton
private IContextMsgHandlerMqtt ctxMsgHandler;	
private String clientId;
private String topic;

	public MqttContextServer(String clientId, String topic) {
		this.clientId = clientId;
		this.topic    = topic;
		ctxMsgHandler = new ContextMqttMsgHandler("ctxH");
		
		ColorsOut.out("MqttContextServer CREATED");
	}
	@Override
	public void activate() {
		mqtt = MqttConnection.createSupport( clientId, topic );
		mqtt.connectToBroker(clientId,  RadarSystemConfig.mqttBrokerAddr);
		//mqtt.subscribe(clientId, topic, ctxMsgHandler);   //?????
		mqtt.subscribe( topic, ctxMsgHandler );	
 		ColorsOut.out("MqttContextServer activated");
  	}
	@Override
	public void deactivate() {		 
		ColorsOut.out("MqttContextServer deactivate");
		mqtt.disconnect();
		mqtt = null;
 	}
	@Override
	public void addComponent(String name, IApplMsgHandler h) {
		//mqtt.getHandler().addComponent(name, h);		
		ctxMsgHandler.addComponent(name, h);	
	}
	@Override
	public void removeComponent(String name) {
		//mqtt.getHandler().removeComponent(name);		
		ctxMsgHandler.removeComponent(name);		
	}
}
