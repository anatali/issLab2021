package it.unibo.enablerCleanArch.supports.mqtt;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IDistance;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedModel;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;

public class MqttSupportDemoLedMain {
//private String topic      = MqttSupport.topicOut;
private String brokerAddr = RadarSystemConfig.mqttBrokerAddr;  

private final String caller    = "demo";
private final String requestId = "query";

//String sender, String msgId, String payload, String dest
private String helloMsg  = Utils.buildDispatch(caller, "cmd",    "hello",    "ANYONE").toString();
 
private MqttSupport mqtt;
private ContextMqttMsgHandler ctxH;

public void simulateReceiver(String name) {
	new Thread() {
		public void run() {
		try {
 			Colors.outappl("receiver STARTS with " + mqtt, Colors.GREEN);
			String inputMNsg = mqtt.receiveMsg();
			Colors.outappl("receiver RECEIVED:"+ inputMNsg, Colors.BLACK);
 		} catch (Exception e) {
			Colors.outerr("receiver  | Error:" + e.getMessage());
	 	}
		}//run
	}.start();
}

protected void init() {
 	mqtt = MqttSupport.getSupport();
	mqtt.connectToBroker("demo", RadarSystemConfig.mqttBrokerAddr);	
	ctxH = new ContextMqttMsgHandler( "ctxH"  );
	mqtt.subscribe(MqttSupport.topicOut, ctxH);
}

protected void end() {
	//mqtt.unsubscribe( topic );	
	mqtt.disconnect();
}

public void doJob() {
	init();
	ILed leddev = LedModel.create();
	ctxH.addComponent( "led", new LedApplHandler("ledH",leddev) );
 	//try {
		String host = RadarSystemConfig.pcHostAddr;
		ILed led    = new LedProxyAsClient("ledpxy", host, MqttSupport.topicOut, ProtocolType.mqtt );
		led.turnOn();
 		Utils.delay(1000);
		boolean ledState = led.getState();
		Colors.outappl("doJob| ledState =" + ledState, Colors.BLACK);
		Utils.delay(1000);
		led.turnOff();
		ledState = led.getState();
		Colors.outappl("doJob| ledState =" + ledState, Colors.BLACK);
 		Utils.delay(1000);
		end();  //Se no si ha poi un  connectionLost  
		Colors.outappl("doJob BYE BYE" , Colors.BLACK);
// 	} catch (Exception e) {
//		e.printStackTrace();
//	}
}

 
 
	public static void main(String[] args) throws Exception  {
		//RadarSystemConfig.mqttBrokerAddr  = "tcp://localhost:1883";  
		//RadarSystemConfig.mqttBrokerAddr  = "tcp://broker.hivemq.com";
		//RadarSystemConfig.mqttBrokerAddr  = "tcp://mqtt.eclipse.org:1883";  //NO
		
		RadarSystemConfig.mqttBrokerAddr  = "tcp://test.mosquitto.org";
		RadarSystemConfig.simulation  = true;
		RadarSystemConfig.protcolType = ProtocolType.mqtt;
		RadarSystemConfig.ledGui      = true;
		RadarSystemConfig.testing     = false;
		RadarSystemConfig.pcHostAddr  = "localhost";
		MqttSupportDemoLedMain sys = new MqttSupportDemoLedMain();	
		
		sys.doJob();
 		//sys.doSendReceive();
  		//sys.doRequestRespond();
  		
  		System.exit(0);
 	}

}
