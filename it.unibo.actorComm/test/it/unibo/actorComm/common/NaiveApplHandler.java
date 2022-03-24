package it.unibo.actorComm.common;


import it.unibo.actorComm.ApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.IApplMessage;

public class NaiveApplHandler extends ApplMsgHandler {

	public NaiveApplHandler(String name) {
		super(name);
		ColorsOut.out(name + " | CREATED "  );
	}
 
	@Override
	public void elaborate( IApplMessage message, Interaction2021 conn ) {
 		ColorsOut.out(name + " | elaborate " + message + " conn=" + conn);
 		//sendMsgToClient("answerTo_"+message , conn);		
 		this.sendAnswerToClient("answerTo_"+message , conn);
	}

//	@Override
//	public void elaborate(String message, Interaction2021 conn) {
//		System.out.println(name + " | elaborate " + message + " conn=" + conn);
//		this.sendMsgToClient("answerTo_"+message, conn);
//  	}

}
