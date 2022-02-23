package it.unibo.enablerCleanArch.concur;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.kactor.ActorWrapper;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;

/*
 * Un actor che offre anche una interfaccia come handler per il TcpContextServer
 * Si può fare ma è molto discutibile ... 
 */

public class CounterActorWithDelayHandler extends ActorWrapper implements IApplMsgHandler{ 
	private int n = 2;
	
	public CounterActorWithDelayHandler( String name  ) {
		super( name );
	}

	@Override
	public void doJob( ApplMessage msg ) {
 		Utils.aboutThreads(getName() + " | Before doJob");
		ColorsOut.outappl( getName() + " | " + msg.toString(), ColorsOut.MAGENTA );
 		if( msg.msgId().equals("inc")) { n = n + 1; }
 		else if( msg.msgId().equals("dec")) {
 			int dt = Integer.parseInt( msg.msgContent() );
 			int v = n;
 		    v = v - 1;
 		    Utils.delay(dt);  //the control is given to another client
 		    ColorsOut.outappl(getName() + " | resumes v= " + v, ColorsOut.MAGENTA);
 		    n = v;
 		    ColorsOut.outappl(getName() + " | new value after dec= " + n, ColorsOut.MAGENTA);
 		}
 	}

	@Override
	public void elaborate(String message, Interaction2021 conn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elaborate(it.unibo.enablerCleanArch.domain.ApplMessage message, Interaction2021 conn) {
 		try {
 			int delay = getDecDelayArg(message.msgContent());
 			String msgContent =  ""+delay;
 			it.unibo.kactor.ApplMessage decCmd = MsgUtil.buildDispatch("main","dec",msgContent,"ca");	
 			ColorsOut.out("elaborateForActor sends:" + decCmd);
 			MsgUtil.sendMsg(decCmd, this, null);			 
		}catch( Exception e) {}	
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

	@Override
	public void sendMsgToClient(String message, Interaction2021 conn) {
		try {
 			ColorsOut.out(getName() + " | ApplMsgHandler sendMsgToClient message=" + message + " conn=" + conn, ColorsOut.BLUE);
			conn.forward( message );
		} catch (Exception e) {
 			ColorsOut.outerr(getName() + " | ApplMsgHandler sendMsgToClient ERROR " + e.getMessage());;
		}		
	}

	@Override
	public void sendAnswerToClient(String message, Interaction2021 conn) {
		// TODO Auto-generated method stub
		
	}

	
 

}
