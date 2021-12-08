package it.unibo.enablerCleanArch.supports;

import java.util.HashMap;
import it.unibo.enablerCleanArch.domain.ApplMessage;

public class SysMessageHandler extends ApplMessageHandler{
private HashMap<String,ApplMessageHandler> handlerMap = new HashMap<String,ApplMessageHandler>();

	public SysMessageHandler(String name) {
		super(name);
	}

	@Override
	public void elaborate(String message) {
		Colors.out(name+" | elaborate message=" + message );
		//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
		ApplMessage msg = new ApplMessage(message);
		//Colors.out(name+" | elaborate msg=" + msg );
		ApplMessageHandler h = handlerMap.get(msg.msgReceiver());
		if( h != null ) h.elaborate(message);
	}
	
	public void registerHandler(String name, ApplMessageHandler h) {
		handlerMap.put(name, h);
	}

}
