package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ApplMessageType;

public class MsgUtil {
private static int msgNum=0;	
//String MSGID, String MSGTYPE, String SENDER, String RECEIVER, String CONTENT, String SEQNUM

	public static ApplMessage buildDispatch(String sender, String msgId, String payload, String dest) {
		try {
			return new ApplMessage(msgId, ApplMessageType.dispatch.toString(),sender,dest,payload,""+(msgNum++));
		} catch (Exception e) {
			Colors.outerr("buildDispatch ERROR:"+ e.getMessage());
			return null;
		}
	}
	
	public static ApplMessage buildRequest(String sender, String msgId, String payload, String dest) {
		try {
			return new ApplMessage(msgId, ApplMessageType.request.toString(),sender,dest,payload,""+(msgNum++));
		} catch (Exception e) {
			Colors.outerr("buildRequest ERROR:"+ e.getMessage());
			return null;
		}
	}
	

}
