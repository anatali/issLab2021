package it.unibo.enablerCleanArch.enablers.devices;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;

public class ClientApplHandlerMqtt extends ApplMsgHandler {
private String myTopic;
private Interaction2021 conn;

	public ClientApplHandlerMqtt(String name, Interaction2021 conn ) {
		super( name );
		this.conn = conn;
 	}
	
	@Override  //MqttCallback
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		//Colors.outappl(name + " ApplMsgHandler | messageArrived:" + message, Colors.GREEN );
		myTopic = topic;
		try { //Perhaps we receive a structured message
			ApplMessage msgInput = new ApplMessage(message.toString());
			if( msgInput.isRequest() ) {
				Colors.outappl(name + " ApplMsgHandler | REQUEST:" + message + " conn="+conn, Colors.RED );
				elaborate(  msgInput.msgContent(),   conn);				
			}else{ 
				elaborate(  msgInput.msgContent(),   conn) ;
			}
		}catch( Exception e) {
			elaborate(  message.toString(),  conn) ;
		}
	}	
	
	//Non dovrebbe mai essere usato
	@Override //LedApplHandler
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
 		try {
  			String answer = Utils.buildReply("mqtt", "reply", message, "caller").toString();
  			//String answerTopic = myTopic+"answer";
 			Colors.out(name + " | sendMsgToClient " + answer  , Colors.RED);
			conn.reply( answer );
		} catch (Exception e) {
 			Colors.outerr(name + " | ERROR " + e.getMessage());;
		}
 	} 
 
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		Colors.out(name + " | elaborate message=" + message + " conn=" + conn, Colors.RED);
// 		if( message.equals("on")) led.turnOn();
// 		else if( message.equals("off") ) led.turnOff();	
// 		else if( message.equals("getState") ) sendMsgToClient(""+led.getState(), conn );
		try {
			ApplMessage msg = new ApplMessage(message);
			if( msg.isReply() ) {
				//TODO: riattivare processo in attesa
			}else {
				Colors.out(name + " | elaborate message put in queue  conn=" + conn, Colors.RED);
				((MqttSupport)conn).getQueue().put(message);
			}
		}catch(Exception e) { //not a structured
			Colors.out(name + " | elaborate message put in queue  conn=" + conn, Colors.RED);
			try {
				((MqttSupport)conn).getQueue().put(message);
			} catch (InterruptedException e1) {
				Colors.outerr(name + " | elaborate ERROR:" + e.getMessage());
			}
		}
	}
	
 	
}
