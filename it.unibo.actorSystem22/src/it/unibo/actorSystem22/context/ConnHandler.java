package it.unibo.actorSystem22.context;

 
import it.unibo.actorComm.interfaces.IApplMsgHandler;
import it.unibo.actorComm.interfaces.Interaction2021;
import it.unibo.actorComm.utils.ColorsOut;
import it.unibo.kactor.Actor22;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import it.unibo.radarSystem22.domain.utils.BasicUtils;

/*
 * Potrei attivare un actor se avessi i meccanismi di Node.js
 * che rilasciano il controllo nella receiveMsg
 */
public class ConnHandler extends Thread{
private Interaction2021 conn;
	public ConnHandler(String name,Interaction2021 conn) {
		super(name);
		this.conn = conn;
		start();
   	}

	@Override
	public void run( ) {
 	//Elaborate the messages sent over the connection	

		try {
			ColorsOut.outappl( getName() + " | STARTS with conn=" + conn, ColorsOut.GREEN );
			while( true ) {
				//ColorsOut.out(getName() + " | waits for message  ...");
			    String msg = conn.receiveMsg();
			    ColorsOut.outappl(getName() + "  |  received:" + msg, ColorsOut.GREEN );
			    if( msg == null ) {
			    	conn.close();
			    	break;
			    } else{ 
			    	//Invia il msg all'attore destinatario che elabora
					IApplMessage m  = new ApplMessage(msg);
			 		String dest     = m.msgReceiver();
					//ColorsOut.out(name +  " | elaborate " + msg.msgContent() + " dest="+dest, ColorsOut.ANSI_PURPLE);
					ColorsOut.outappl(getName() +  " | doJob  dest="+dest, ColorsOut.GREEN );
					//IApplMsgHandler h    = handlerMap.get(dest);
					ActorBasic d = Actor22.getActor(dest);
					if( d == null ) {
						ColorsOut.outerr( dest + " NOT FOUND");
						return;
					}
 					if( m.isRequest() ) {
						ColorsOut.outappl(getName() + " | Handling the request " + m, ColorsOut.GREEN);						
						//Attivo un attore che riceve la risposta					  
						ActorForReply ar = new ActorForReply("ar"+m.msgSender(),  conn);
						Actor22.sendAMsg(m, d); //invio il msg all'attore locale
					}else {
						ColorsOut.outappl(getName() + " | Handling the command " + m, ColorsOut.GREEN);						
						ColorsOut.outappl(getName() + " | command for " + d, ColorsOut.GREEN);						
						
//						IApplMessage cmdOn  = MsgUtil.buildDispatch("main", "cmd", "turnOn",  "led");
//						Actor22.sendAMsg(cmdOn, d);
 						
						d.autoMsg(m,null);
						 
						//this.sendMsg(m.msgId(), m.msgContent(), d);
						//Actor22.sendAMsg(m, d);					
					}
					
					//Una reply va a ...
//					ColorsOut.out(getName() +  " | elaborate  h="+h, ColorsOut.GREEN );
//					ColorsOut.out(getName() +  " | elaborate " + msg.msgContent() + " redirect to handler="+h.getName() + " since dest="+dest, ColorsOut.GREEN );
//					if( dest != null && (! msg.isReply()) ) {
////						if( h instanceof ActorWrapper ) elaborateForActor(msg, (ActorWrapper)h);
////						else 
//							h.elaborate(msg,conn);			
//					}
					

					//handler.elaborate( msg, conn ); 
			    }
			 }
			ColorsOut.out(getName()  + " | BYE"   );
		}catch( Exception e) {
			ColorsOut.outerr( getName()  + "  | ERROR:" + e.getMessage()  );
		}	
	
	
	
	
	}

}
