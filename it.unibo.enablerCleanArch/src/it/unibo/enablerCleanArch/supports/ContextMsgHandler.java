package it.unibo.enablerCleanArch.supports;

import java.util.HashMap;
import it.unibo.enablerCleanArch.domain.ApplMessage;

public class ContextMsgHandler extends ApplMsgHandler{
	protected HashMap<String,IApplMsgHandler> handlerMap = new HashMap<String,IApplMsgHandler>();
	//protected HashMap<String,ApplMessage>    requestMap  = new HashMap<String,ApplMessage>();

	public ContextMsgHandler(String name) {
		super(name);
 	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ApplMessage msg      = new ApplMessage(message);
		String dest          = msg.msgReceiver();
		IApplMsgHandler h    = handlerMap.get(dest);
		Colors.out(name +  " | elaborate h="+h.getName() + " since dest="+dest);
		if( dest != null ) h.elaborate(msg.msgContent(), conn);	
	}

	public void addComponent( String name, IApplMsgHandler h) {
		handlerMap.put(name, h);
	}
	public void removeComponent( String name ) {
		handlerMap.remove( name );
	}
}
