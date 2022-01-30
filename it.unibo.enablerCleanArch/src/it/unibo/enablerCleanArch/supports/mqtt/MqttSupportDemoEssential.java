package it.unibo.enablerCleanArch.supports.mqtt;

import java.util.concurrent.BlockingQueue;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;

public class MqttSupportDemoEssential {
private String topic      = MqttSupport.topicOut;
private String brokerAddr = RadarSystemConfig.mqttBrokerAddr; // : 1883  OPTIONAL

//String sender, String msgId, String payload, String dest
private String helloMsg  = Utils.buildDispatch("demo", "cmd",   "hello",    "anyone").toString();
private String aRequest  = Utils.buildRequest("demo",  "query", "getTime",  "anyone").toString();
private String aReply    = Utils.buildReply("demo",  "answer", "ANSWER",    "anyone").toString();

private BlockingQueue<String> blockingQueue = null;

public void doSendReceive() {
	MqttSupport mqtt    = MqttSupport.getSupport();
	MqttAnswerHandler h = new MqttAnswerHandler(blockingQueue);
	mqtt.connectToBroker("demo", RadarSystemConfig.mqttBrokerAddr);

	mqtt.subscribe(topic, h);
	
	try {
		mqtt.forward(helloMsg);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
 
	public static void main(String[] args) throws Exception  {
		MqttSupportDemoEssential sys = new MqttSupportDemoEssential();	
 		sys.doSendReceive();
  		//sys.doRequest();
 	}

}
