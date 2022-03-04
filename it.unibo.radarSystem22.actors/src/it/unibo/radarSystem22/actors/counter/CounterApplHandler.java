package it.unibo.radarSystem22.actors.counter;
  
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.comm2022.ApplMsgHandler;
import it.unibo.comm2022.interfaces.IApplMessage;
import it.unibo.comm2022.interfaces.Interaction2021;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
 
 
public class CounterApplHandler extends ApplMsgHandler {
private CounterActorWithDelay ca;

 	public CounterApplHandler( String name, CounterActorWithDelay counter ) {
		 super(name);
 		 ca = counter;
	}
	
	@Override
	public void elaborate(String cmd, Interaction2021 conn) {		
		ColorsOut.out(name + " | (not used) elaborate cmd: "+cmd);
	} 
	
	@Override
	public void elaborate( IApplMessage msg, Interaction2021 conn ) {
		elaborateForActor( msg );
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
	
 	
	protected void elaborateForActor( IApplMessage cmd ) {
		ColorsOut.outappl(name + " | elaborateForActor ApplMessage: "+cmd, ColorsOut.GREEN);		
 		try {
 			int delay = getDecDelayArg(cmd.msgContent());
 			String msgContent =  ""+delay;
 			it.unibo.kactor.ApplMessage decCmd = MsgUtil.buildDispatch("main","dec",msgContent,"ca");	
 			ColorsOut.out(name + " | elaborateForActor sends:" + decCmd);
 			MsgUtil.sendMsg(decCmd, ca, null);
			 
		}catch( Exception e) {}	
	}
}
