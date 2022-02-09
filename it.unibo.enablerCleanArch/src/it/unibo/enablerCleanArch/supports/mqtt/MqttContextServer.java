package it.unibo.enablerCleanArch.supports.mqtt;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContext;

public class MqttContextServer implements IContext{
private MqttSupport mqtt ; //Singleton
private String clientId;
private String topic;

	public MqttContextServer(String clientId, String topic) {
		this.clientId = clientId;
		this.topic    = topic;
		ColorsOut.out("MqttContextServer CREATED");
	}
	@Override
	public void activate() {
		mqtt = MqttSupport.createSupport( clientId, topic );
		mqtt.connectToBroker(clientId,  RadarSystemConfig.mqttBrokerAddr);
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
		mqtt.getHandler().addComponent(name, h);		
	}
	@Override
	public void removeComponent(String name) {
		mqtt.getHandler().removeComponent(name);		
	}
}
