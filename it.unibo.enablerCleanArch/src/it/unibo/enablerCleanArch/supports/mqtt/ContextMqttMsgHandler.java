package it.unibo.enablerCleanArch.supports.mqtt;

import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;

/*
  * Il ContextMsgHandler viene invocato dal TcpContextServer (un singleton).
  * Esso gestisce messaggi della forma 'estesa':
 *   msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM ) e 
 *  e ridirige al RECEIVER il CONTENT con la connessione
 *  il RECEIVER elabora il messaggio e invia un msg di risposta sulla connessione 
 *  per i messaggi che costituiscono richieste
 *  
 *  Il ContextMsgHandler potrebbe inviare al RECEIVER  il messaggio in forma estesa
 *  ma il RECEIVER non sarebbe più quello usato nella versione precedente.
 */

public class ContextMqttMsgHandler extends ApplMsgHandler implements IContextMsgHandler{
	protected HashMap<String,IApplMsgHandler> handlerMap = new HashMap<String,IApplMsgHandler>();

	public ContextMqttMsgHandler(String name) {
		super(name);
		ColorsOut.out(name + " |  CREATED:it will dispatch all messages to the proper IApplMsgHandler"  );
		if(  ! RadarSystemConfig.protcolType.equals( ProtocolType.mqtt ) ) { 
			ColorsOut.outerr(name + " | WARNING: ContextMqttMsgHandler requires MQTT" );
		}
 	}
	

 	@Override
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
 		try {
 			ColorsOut.out(name + " | sendMsgToClient message=" + message + " conn=" + conn, ColorsOut.BLUE);
   			String reply = Utils.buildReply("sender", "msgid", message, "dest").toString();
 			conn.forward( reply );
  		} catch (Exception e) {
 			ColorsOut.outerr(name + " | ERROR " + e.getMessage());;
		}
 	} 

 	@Override
 	public void sendAnswerToClient( String message  ) {
 		
 	}

	@Override
	public IApplMsgHandler getHandler( String name ) {
		return handlerMap.get(name);
	}

	@Override
	public void addComponent( String devName, IApplMsgHandler h) {
		ColorsOut.out(name + " | added:" + devName);
		handlerMap.put(devName, h);
	}
	@Override
	public void removeComponent( String devName ) {
		ColorsOut.out(name + " | removed:" + devName);
		handlerMap.remove( devName );
	}
	
	
	//MQTT part 	 MqttCallback
		@Override
		public void connectionLost(Throwable cause) {
			ColorsOut.outerr(name + " | connectionLost cause="+cause);
		}

		@Override
		public void messageArrived(String topic, MqttMessage message)   {
			ColorsOut.out(name + " | messageArrived:" + message + " topic="+topic );
			try { //Perhaps we receive a structured message
				ApplMessage msgInput = new ApplMessage(message.toString());
				//Colors.out(name + " | msgInput:" + msgInput.msgContent() , Colors.ANSI_PURPLE );
				elaborate(msgInput, MqttSupport.getSupport());
 			}catch( Exception e) {
				ColorsOut.outerr(name + " | messageArrived WARNING:"+ e.getMessage() );
 			}
		}
		
		@Override
		public void elaborate( ApplMessage msg, Interaction2021 conn ) {
 			String dest          = msg.msgReceiver();
			ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, ColorsOut.ANSI_PURPLE);
			IApplMsgHandler  h   = handlerMap.get(dest);
			//Colors.out(name +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest, Colors.GREEN);
			//esempio di h: LedApplHandler
			if( dest != null ) h.elaborate( msg , conn);	
		}

	 	@Override
		public void elaborate(String message, Interaction2021 conn) {
			ColorsOut.out(name+" | elaborate:" + message + " conn=" + conn, ColorsOut.ANSI_PURPLE);
			//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
			ApplMessage msg      = new ApplMessage(message);
			elaborate( msg.toString(), conn);
 		}

 
		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			try {
//				Colors.out((name + " | deliveryComplete token=" + token.getMessage() 
//			       + " client=" + token.getClient().getClientId() ), Colors.ANSI_YELLOW);
			} catch (Exception e) {
				ColorsOut.outerr(name + " | deliveryComplete Error:"+e.getMessage());		
			}
		}
	
}
