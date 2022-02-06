package it.unibo.enablerCleanArch.supports.mqtt;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedModel;
import it.unibo.enablerCleanArch.enablers.LedProxyAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArchapplHandlers.LedApplHandler;

public class MqttSupportDemoLedMain {
  
private MqttSupport mqtt;
private IContextMsgHandler ctxH;

public void simulateReceiver(String name) {
	new Thread() {
		public void run() {
		try {
 			ColorsOut.outappl("receiver STARTS with " + mqtt, ColorsOut.GREEN);
			String inputMNsg = mqtt.receiveMsg();
			ColorsOut.outappl("receiver RECEIVED:"+ inputMNsg, ColorsOut.BLACK);
 		} catch (Exception e) {
			ColorsOut.outerr("receiver  | Error:" + e.getMessage());
	 	}
		}//run
	}.start();
}

 

protected void endMqttSupport() {
	mqtt.disconnect();
}

public void doJob() {
 	mqtt = MqttSupport.createSupport("demoLed",MqttSupport.topicInput);
 	ctxH = mqtt.getHandler();
	
	//Configure the system (proxy site)
	ILed leddev = LedModel.create();
	ctxH.addComponent( "led", new LedApplHandler("ledH",leddev) );
  	ILed led = new LedProxyAsClient("ledpxy", RadarSystemConfig.pcHostAddr, MqttSupport.topicInput, ProtocolType.mqtt );
	
  	//Operate
	ColorsOut.outappl("doJob | STARTS ... " , ColorsOut.BLACK);
 	
  	led.turnOn();
	boolean ledState = led.getState();
	ColorsOut.outappl("doJob | ledState =" + ledState, ColorsOut.BLACK);
	Utils.delay(1000);
	
	led.turnOff();
	ledState = led.getState();
	ColorsOut.outappl("doJob | ledState =" + ledState, ColorsOut.BLACK);
 	Utils.delay(1000);
 	
 	endMqttSupport();   
	ColorsOut.outappl("doJob | ENDS" , ColorsOut.BLACK);
		 
 }

 
 
	public static void main(String[] args) throws Exception  {
		//See https://www.catchpoint.com/network-admin-guide/mqtt-broker
		//RadarSystemConfig.mqttBrokerAddr  = "tcp://localhost:1883";  
		//RadarSystemConfig.mqttBrokerAddr  = "tcp://broker.hivemq.com:1883";
		//RadarSystemConfig.mqttBrokerAddr  = "tcp://mqtt.eclipse.org:1883";  //NO
		//RadarSystemConfig.mqttBrokerAddr    = "tcp://test.mosca.io:1883"; //NO
		RadarSystemConfig.mqttBrokerAddr  = "tcp://test.mosquitto.org";
		RadarSystemConfig.simulation  = true;
		RadarSystemConfig.protcolType = ProtocolType.mqtt;
		RadarSystemConfig.ledGui      = true;
		RadarSystemConfig.tracing     = true;
		RadarSystemConfig.pcHostAddr  = "localhost";
		
		MqttSupportDemoLedMain sys    = new MqttSupportDemoLedMain();			
		sys.doJob();
 		//sys.doSendReceive();
  		//sys.doRequestRespond();
  		
  		System.exit(0);
 	}

}
