package it.unibo.enablerCleanArch.supports.mqtt;
 
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IApplMsgHandlerMqtt;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class SonarDataObserverHandler implements IApplMsgHandlerMqtt{
	private String name;
	private ILed led;
	private int lastDistanceVal = 0;
	private boolean ledUsageTeminated = true;
	
	public SonarDataObserverHandler(String name, ILed led ) {  
		this.name    = name;
		this.led = led;
 	}
  	@Override
	public void connectionLost(Throwable cause) {
  		ColorsOut.out(name + " | connectionLost " + cause);
		
	}
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		ColorsOut.out(name + " | messageArrived " + message);
		lastDistanceVal = Integer.parseInt(message.toString());
//		ColorsOut.out(name + " | distance= " + lastDistanceVal + " led=" + led);
//		if( d < 10 ) led.turnOn();	//RadarSystemConfig.DLIMIT
//		else led.turnOff();	
		//TODO: emissione evento!
		if( ledUsageTeminated ) {
			ledUsageTeminated = false;
			new Thread() {
				public void run() {
					if( lastDistanceVal < 10 ) led.turnOn();	//RadarSystemConfig.DLIMIT
					else led.turnOff();		
					ColorsOut.out("********************* ");
					ledUsageTeminated = true;
				}
			}.start();
		}
	}
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		ColorsOut.out(name + " | deliveryComplete= " + lastDistanceVal  );
//		try {
//			ColorsOut.out(name + " | deliveryComplete " + token.getMessage() );
//			new Thread() {
//				public void run() {
//					if( lastDistanceVal < 10 ) led.turnOn();	//RadarSystemConfig.DLIMIT
//					else led.turnOff();						
//				}
//			}.start();
//		} catch (MqttException e) {
// 			e.printStackTrace();
//		}	
	}
	
	@Override
	public String getName() {
 		return name;
	}
	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate " + message);
		
	}
	@Override
	public void elaborate(ApplMessage message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate ApplMessage:" + message);
		
	}
	@Override
	public void sendMsgToClient(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | sendMsgToClient:" + message);
		
	}
	@Override
	public void sendAnswerToClient(String message) {
		ColorsOut.out(name + " | sendAnswerToClient:" + message);
	}
}