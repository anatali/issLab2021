package it.unibo.comm2022.example;
  
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.comm2022.utils.ColorsOut;
import it.unibo.comm2022.utils.CommUtils;

 
 
public class CounterApplHandler extends ApplMsgHandler {
private CounterWithDelay counter;
 

	public CounterApplHandler( String name, CounterWithDelay counter ) {
		 super(name);
		 this.counter = counter;
 	}
 
	
	@Override
	public void elaborate(String cmd, Interaction2021 conn) {		
		ColorsOut.out(name + " | (not used) elaborate cmd: "+cmd);
	} 
	
	@Override
	public void elaborate( IApplMessage msg, Interaction2021 conn ) {
		elaborateForObject( msg );
	}
	
	protected int getDecDelayArg(String cmd) throws Exception{
		Struct cmdT     = (Struct) Term.createTerm(cmd);
		String cmdName  = cmdT.getName();
		if( cmdName.equals("dec")) {
			int delay = Integer.parseInt(cmdT.getArg(0).toString());
			//ColorsOut.outappl(name + " | dec delay="+delay, ColorsOut.GREEN);
			return delay;
 		}else return 0;		
	}
	
	protected void elaborateForObject( IApplMessage msg  ) {
		ColorsOut.outappl(name + " | elaborateForObject ApplMessage: "+msg, ColorsOut.GREEN);		
		String answer=null;
		try {
			String cmd =  msg.msgContent();
 			int delay   = getDecDelayArg(cmd);
 			counter.dec(delay);	
			answer = ""+counter.getVal();
			//ColorsOut.outappl(name + " | elaborate ApplMessage answer: "+answer, ColorsOut.GREEN);
			if( msg.isRequest() ) {
				IApplMessage  reply = CommUtils.prepareReply(msg, answer);
				sendAnswerToClient(reply.toString());			
			}
		}catch( Exception e) {}	
	}
	
}
