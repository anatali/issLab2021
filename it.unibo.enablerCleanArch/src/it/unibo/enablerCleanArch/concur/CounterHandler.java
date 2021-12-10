package it.unibo.enablerCleanArch.concur;
  
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.SysMessageHandler;
import it.unibo.enablerCleanArch.supports.TcpContextServer;
 
public class CounterHandler extends ApplMessageHandler {
private CounterWithDelay c = new CounterWithDelay();
	public CounterHandler( String name ) {
		 super(name);
	}

	@Override
	public void elaborate(String message) {
		ApplMessage msg = new ApplMessage(message);
		Colors.out(name + " | elaborate: "+msg);
		String cmd      = msg.msgContent();
		Struct cmdT     = (Struct) Term.createTerm(cmd);
		String cmdName  = cmdT.getName();
		if( cmdName.equals("dec")) {
			int delay = Integer.parseInt(cmdT.getArg(0).toString());
			Colors.out(name + " | dec delay="+delay);
			c.dec(delay);	
			if( msg.isRequest() ) {
				String reply = "answer from " + name;
				SysMessageHandler sysMsgHandler = TcpContextServer.getSysHandler();
				Colors.out(name + " | reply="+reply );
				
				sysMsgHandler.answer( msg.msgId(),   "replyToDec", msg.msgSender(), reply);
			}
			/*
			try {
				if( conn != null ) conn.forward( "answer from " + name + " "  );
			} catch (Exception e) {
 				e.printStackTrace();
			}*/
		}
 	}

}
