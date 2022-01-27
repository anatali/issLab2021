package it.unibo.enablerCleanArch.supports;

import java.util.HashMap;
import it.unibo.enablerCleanArch.domain.ApplMessage;

public class SysMessageHandler extends ApplMessageHandler{
	private static int num = 1;
	
private HashMap<String,ApplMessageHandler> handlerMap = new HashMap<String,ApplMessageHandler>();
private HashMap<String,ApplMessage>    requestMap     = new HashMap<String,ApplMessage>();
 
	public SysMessageHandler(String name) {
		super(name+num++);
	}

	@Override
	public void elaborate(String message) {
		Colors.out(name+" | elaborate message=" + message + " conn=" + conn);
		//msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
		ApplMessage msg      = new ApplMessage(message);
		ApplMessageHandler h = handlerMap.get(msg.msgReceiver());
		if( h != null ) {
			//h.setConn(  conn ); 
			requestMap.put(msg.msgId()+msg.msgSender(), msg);
			if( msg.isRequest() ) msg.setConn(conn);  //memorizzo la connessione nel messaggio (per aliens)
			Colors.out(name+" | redirect " + msg + " to " + h.name + " conn=" + conn, Colors.GREEN  );
			h.elaborate(message);
		}
	}
	
	public void answer( String reqId,  String reqestmsgId, String caller, String  msg) {
		ApplMessage reqMsg = requestMap.remove( reqId+caller );  //Messaggio di richiesta
		Colors.out(name + " | answer finds:" + reqMsg);
		if( reqMsg == null ){
			Colors.outerr("INCONSISTENT: no request found");
		}else {
			String destName      = reqMsg.msgSender();
			ApplMessage replyMsg = new ApplMessage(reqMsg.msgId(), "reply", reqMsg.msgSender(), destName, msg, "1");
			Colors.out(name + " | answer replyMsg:" + replyMsg);
			Interaction2021 connToCaller = reqMsg.getConn();
			Colors.out(name + " | answer connToCaller:" + connToCaller, Colors.RED);
			try {
				connToCaller.forward( replyMsg.toString() );
			} catch (Exception e) {Colors.outerr("replyreq ERROR " + e.getMessage());	}
		}
	}
	
	public void registerHandler(String name, ApplMessageHandler h) {
		handlerMap.put(name, h);
	}
	public void unregisterHandler( String name ) {
		handlerMap.remove( name );
	}
}
