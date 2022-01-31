package it.unibo.enablerCleanArch.concur;
  
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
 
 
public class CounterHandler extends ApplMsgHandler {
private CounterWithDelay c = new CounterWithDelay();
	public CounterHandler( String name ) {
		 super(name);
	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.out(name + " | elaborate: "+message);
		try {
			ApplMessage msg = new ApplMessage(message);
			ColorsOut.out(name + " | elaborate: "+msg);
			String cmd      = msg.msgContent();
			Struct cmdT     = (Struct) Term.createTerm(cmd);
			String cmdName  = cmdT.getName();
			if( cmdName.equals("dec")) {
				elaborateDec(cmdT);	
				if( msg.isRequest() ) {
					String reply = "answer_from_" + name;
	 				ColorsOut.out(name + " | reply="+reply );					
					//sendMsgToClient( msg.msgId(),   "replyToDec", msg.msgSender(), reply);
	 				sendMsgToClient(  reply, conn ) ;
				}
		}
		}catch( Exception e) {
			Struct cmdT     = (Struct) Term.createTerm(message);
			elaborateDec(   cmdT );
		}	
 	} 
	public void elaborate( ApplMessage message, Interaction2021 conn ) {}
	
	protected void elaborateDec( Struct cmdT ) {
		int delay = Integer.parseInt(cmdT.getArg(0).toString());
		ColorsOut.out(name + " | dec delay="+delay);
		c.dec(delay);			
	}

	@Override
	public void sendAnswerToClient(String message) {
		// TODO Auto-generated method stub
		
	}

 

}
