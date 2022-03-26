package it.unibo.actorComm;

import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.IApplMessage;

public abstract class ApplMsgHandler  implements IApplMsgHandler {  
protected String name;
   
 	public ApplMsgHandler( String name  ) {  
		this.name = name;
	}
 	
 	public String getName() {
		return name;
	}	 
   	
 	//Warning: le risposte sono messaggi 'unstructured'
 	public void sendMsgToClient( String message, Interaction2021 conn  ) {
 		try {
 			ColorsOut.out(name + " | ApplMsgHandler sendMsgToClient message=" + message + " conn=" + conn, ColorsOut.BLUE);
			conn.forward( message );
		} catch (Exception e) {
 			ColorsOut.outerr(name + " | ApplMsgHandler sendMsgToClient ERROR " + e.getMessage());;
		}
 	} 
 	
 	
 	@Override
 	public void sendAnswerToClient( String reply, Interaction2021 conn   ) {
 		try {
			conn.reply(reply);
		} catch (Exception e) {
			ColorsOut.outerr(name + " | ApplMsgHandler sendAnswerToClient ERROR " + e.getMessage() );
		}
 	}
 	
 	//@Override
// 	public void sendAnswerToClient( String reply  ) {
//		ColorsOut.out(name + " | ApplMsgHandler sendAnswerToClient reply=" + reply, ColorsOut.BLUE);
//		try {
//			if( CommSystemConfig.protcolType == ProtocolType.mqtt) {
//				//TODO
//			}else {
//				ColorsOut.outerr(name + " | ApplMsgHandler sendAnswerToClient not implemented for  " 
//							+ CommSystemConfig.protcolType);
//			}
//		} catch (Exception e) {
//			ColorsOut.outerr(name + " | ApplMsgHandler sendAnswerToClient ERROR " + e.getMessage());
// 		}
//  	}
 	
 	public abstract void elaborate(IApplMessage message, Interaction2021 conn) ;
 	
}
