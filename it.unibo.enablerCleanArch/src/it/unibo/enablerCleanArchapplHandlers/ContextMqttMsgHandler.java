package it.unibo.enablerCleanArchapplHandlers;

import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.IContextMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;

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
		Colors.out(name + " |  RadarSystemConfig.protcolType=" +  RadarSystemConfig.protcolType);
		Colors.out(name + " |  test=" +  (RadarSystemConfig.protcolType == ProtocolType.mqtt));
		if(  ! RadarSystemConfig.protcolType.equals( ProtocolType.mqtt ) ) { 
			Colors.outerr(name + " | WARNING: ContextMqttMsgHandler requires MQTT" );
		}
 	}
	

 	@Override
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
 		try {
 			Colors.out(name + " | sendMsgToClient message=" + message + " conn=" + conn, Colors.BLUE);
   			String reply = Utils.buildReply("sender", "msgid", message, "dest").toString();
 			conn.forward( reply );
  		} catch (Exception e) {
 			Colors.outerr(name + " | ERROR " + e.getMessage());;
		}
 	} 


	@Override
	public IApplMsgHandler getHandler( String name ) {
		return handlerMap.get(name);
	}

	@Override
	public void addComponent( String name, IApplMsgHandler h) {
		Colors.out("ContextMsgHandler added:" + name);
		handlerMap.put(name, h);
	}
	@Override
	public void removeComponent( String name ) {
		Colors.out("ContextMsgHandler removed:" + name);
		handlerMap.remove( name );
	}
	
	
	//MQTT part 	 MqttCallback
		@Override
		public void connectionLost(Throwable cause) {
			Colors.outerr(name + " | connectionLost cause="+cause);
		}

		@Override
		public void messageArrived(String topic, MqttMessage message)   {
			Colors.outappl(name + " | messageArrived:" + message + " topic="+topic, Colors.ANSI_PURPLE );
			try { //Perhaps we receive a structured message
				ApplMessage msgInput = new ApplMessage(message.toString());
				Colors.outappl(name + " | msgInput:" + msgInput.msgContent() , Colors.ANSI_PURPLE );
				if( msgInput.isRequest() ) {
					//Preparo connessione per spedire risposta 
					String sender=msgInput.msgSender();
					//MqttSupport conn = MqttSupport.getSupport();
					//conn.connect(name+"Answer", topic+sender+"answer", RadarSystemConfig.mqttBrokerAddr);  
					elaborateRequest(  msgInput.toString(),   name+"Answer", topic+sender+"answer" ) ;
 				}else  elaborate(  msgInput.toString(),   null) ;
 			}catch( Exception e) {
				Colors.outerr(name + " | messageArrived WARNING:"+ e.getMessage() );
 			}
		}
		
		@Override
		public void elaborate( ApplMessage message, Interaction2021 conn ) {
			Colors.outerr(name+ " | I should be never here");
		}

	 	@Override
		public void elaborate(String message, Interaction2021 conn) {
			Colors.out(name+" | elaborate:" + message + " conn=" + conn, Colors.ANSI_PURPLE);
			//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
			ApplMessage msg      = new ApplMessage(message);
			String dest          = msg.msgReceiver();
			Colors.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, Colors.ANSI_PURPLE);
			IApplMsgHandler  h   = handlerMap.get(dest);
			//Colors.out(name +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest, Colors.GREEN);
			if( dest != null ) h.elaborate( msg , conn);	
		}

	 	/*
		 * non posso dare una nuova connessione al broker dallo stesso PC
		 */
		protected void elaborateRequest( String message, String clientid, String topic  ) {
			Colors.out(name+" | elaborate:" + message + " conn=" + conn, Colors.ANSI_PURPLE);
			//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
			ApplMessage msg      = new ApplMessage(message);
			String dest          = msg.msgReceiver();
			Colors.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, Colors.ANSI_PURPLE);
			IApplMsgHandler  h   = handlerMap.get(dest);
			//Colors.out(name +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest, Colors.GREEN);
			if( dest != null ) h.elaborate( msg , MqttSupport.getSupport());
			//il msg contiene implicitamente la topic di risposta e il sender della risposta (l'attuale receiver)
		}

		@Override
		public void deliveryComplete(IMqttDeliveryToken token) {
			try {
//				Colors.outappl("MqttSupportCallback | deliveryComplete token=" 
//			       + token.getMessage() + " client=" + token.getClient().getClientId() , Colors.ANSI_YELLOW);
			} catch (Exception e) {
				Colors.outerr(name + " | deliveryComplete Error:"+e.getMessage());		
			}
		}
	
}
