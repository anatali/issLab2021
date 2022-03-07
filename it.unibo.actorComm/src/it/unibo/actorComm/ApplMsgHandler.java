package it.unibo.actorComm;

import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.actorComm.utils.CommSystemConfig;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;

/*
 * TODO: omettere la parte MqttCallback che viene realizzata da ContextMqttMsgHandler
 */
public abstract class ApplMsgHandler  implements IApplMsgHandler {  
protected String name;
   
 	public ApplMsgHandler( String name  ) {  
		this.name = name;
		//Colors.out(name + " | CREATING ... ", Colors.BLUE );
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
 	
// 	public void sendMsgToClient( IApplMessage message, Interaction2021 conn  ) {
// 		sendMsgToClient( message.toString(), conn );
// 	}
	
 	@Override
 	public void sendAnswerToClient( String reply, Interaction2021 conn   ) {
		sendMsgToClient(reply, conn);		
 	}
 	
 	//@Override
 	public void sendAnswerToClient( String reply  ) {
		ColorsOut.out(name + " | ApplMsgHandler sendAnswerToClient reply=" + reply, ColorsOut.BLUE);
		try {
			if( CommSystemConfig.protcolType == ProtocolType.mqtt) {
				//MqttConnection.getSupport().reply(reply);  //TODO
			}else {
				ColorsOut.outerr(name + " | ApplMsgHandler sendAnswerToClient not implemented for  " 
							+ CommSystemConfig.protcolType);
			}
		} catch (Exception e) {
			ColorsOut.outerr(name + " | ApplMsgHandler sendAnswerToClient ERROR " + e.getMessage());
 		}
  	}
 	
 	public abstract void elaborate(String message, Interaction2021 conn) ;
 	
}
