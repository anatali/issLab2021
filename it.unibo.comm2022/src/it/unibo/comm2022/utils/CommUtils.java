package it.unibo.comm2022.utils;

import it.unibo.comm2022.ApplMessage;
import it.unibo.comm2022.interfaces.IApplMessage;
 

public class CommUtils {

	public static String getContent( String msg ) {
		String result = "";
		try {
			IApplMessage m = new ApplMessage(msg);
			result        = m.msgContent();
		}catch( Exception e) {
			result = msg;
		}
		return result;	
	}

}
