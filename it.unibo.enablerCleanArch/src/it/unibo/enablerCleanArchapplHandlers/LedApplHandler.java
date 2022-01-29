package it.unibo.enablerCleanArchapplHandlers;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IDevice;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;


/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplHandler extends ApplMsgHandler {
ILed led;

	public LedApplHandler(String name ) {
		super( name );
	}
	public LedApplHandler(String name, ILed led) {
		super(name);
		this.led = led;
	}
	
	public void setTheDevice( ILed dev ) {
		led = dev;
	}
	@Override
	public void elaborate( ApplMessage message, Interaction2021 conn ) {
		String payload = message.msgContent();
		String sender  = message.msgSender();
		String receiver= message.msgReceiver();
		if( message.isRequest() ) {
			if(payload.equals("getState") ) {
				String ledstate = ""+led.getState();
				ApplMessage reply = Utils.buildReply("led", "ledanswer", ledstate, message.msgSender()) ;
				
				//sendMsgToClient(reply, conn );  
				//E' una reply al client - La connessione mqtt dovrebbe consoscere la topic
				
				sendAnswerToClient(reply.toString());
			}
		}else if( message.isReply() ) {
			
		}else elaborate(payload, conn);
	}
	
 	@Override
	public void elaborate(String message, Interaction2021 conn) {
		Colors.out(name + " | elaborate message=" + message + " conn=" + conn + " led="+led, Colors.GREEN);
 		if( message.equals("on")) led.turnOn();
 		else if( message.equals("off") ) led.turnOff();	
 		else if( message.equals("getState") ) sendMsgToClient(""+led.getState(), conn );
	}

}
