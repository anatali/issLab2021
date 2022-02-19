package it.unibo.enablerCleanArch.concur;
  
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMsgHandler;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
 
 
public class CounterApplHandler extends ApplMsgHandler {
private CounterWithDelay counter
;
	public CounterApplHandler( String name, CounterWithDelay counter ) {
		 super(name);
		 this.counter = counter;
	}
	
	@Override
	public void elaborate(String cmd, Interaction2021 conn) {		
		ColorsOut.out(name + " | elaborate cmd: "+cmd);
		elaborate( cmd );
	} 
	
	protected String elaborate( String cmd ) {
		String answer=null;
		try {
 			Struct cmdT     = (Struct) Term.createTerm(cmd);
			String cmdName  = cmdT.getName();
			if( cmdName.equals("dec")) {
				int delay = Integer.parseInt(cmdT.getArg(0).toString());
				ColorsOut.out(name + " | dec delay="+delay);
				counter.dec(delay);	
				answer = ""+counter.getVal();
			}
		}catch( Exception e) {
 		}	
		return answer;
	}
	
	@Override
	public void elaborate( ApplMessage msg, Interaction2021 conn ) {
		ColorsOut.out(name + " | elaborate ApplMessage: "+msg);
 		String answer = elaborate( msg.msgContent() );
		if( msg.isRequest() ) {
			ApplMessage  reply = Utils.prepareReply(msg, answer);
			sendAnswerToClient(reply.toString());			
		}
	}

}
