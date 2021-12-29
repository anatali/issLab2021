package it.unibo.enablerCleanArch.enablers.devices;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
 
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;

public class LedApplHandlerMqtt extends LedApplHandler {
private String myTopic;

	public LedApplHandlerMqtt(String name, ILed led) {
		super(name, led);
 	}
	
	@Override  //MqttCallback
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		Colors.outappl(name + " ApplMsgHandler | messageArrived:" + message, Colors.ANSI_PURPLE );
		myTopic = topic;
		try { //Perhaps we receive a structured message
			ApplMessage msgInput = new ApplMessage(message.toString());
			if( msgInput.isRequest() ) {
				Colors.outappl(name + " ApplMsgHandler | REQUEST:" + message, Colors.ANSI_PURPLE );
				elaborate(  msgInput.msgContent(),   MqttSupport.getTheSupport());
				
			}else{ 
				elaborate(  msgInput.msgContent(),   MqttSupport.getTheSupport()) ;
			}
		}catch( Exception e) {
			elaborate(  message.toString(),   MqttSupport.getTheSupport()) ;
		}
	}	
	
	@Override //LedApplHandler
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
 		try {
  			String answer = Utils.buildReply("mqtt", "reply", message, "caller").toString();
  			//String answerTopic = myTopic+"answer";
 			Colors.out(name + " | sendMsgToClient " + answer  , Colors.ANSI_YELLOW);
			conn.reply( answer );
		} catch (Exception e) {
 			Colors.outerr(name + " | ERROR " + e.getMessage());;
		}
 	} 
 
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		Colors.out(name + " | elaborate message=" + message + " conn=" + conn, Colors.ANSI_YELLOW);
// 		if( message.equals("on")) led.turnOn();
// 		else if( message.equals("off") ) led.turnOff();	
// 		else if( message.equals("getState") ) sendMsgToClient(""+led.getState(), conn );
		try {
			ApplMessage msg = new ApplMessage(message);
			if( msg.isReply() ) {
				//TODO: riattivare processo in attesa
			}
		}catch(Exception e) { //not a reply
			super.elaborate(message, conn);
		}
	}
	
 	
}
