package it.unibo.qaktest;

import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.Utils;
import it.unibo.kactor.ActorBasic;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;
import kotlin.*;
import kotlin.coroutines.*;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.GlobalScope;

public class QakTest extends ActorBasic {
	
	public QakTest(String name, CoroutineScope scope) {
		super( name,  scope, false,false, false,50);
	}

	@Override
	public Object actorBody(IApplMessage msg, Continuation<? super Unit> arg1) {
//		createMsglogFile();
 		ColorsOut.out( msg.toString() );
 		if( msg.msgContent().equals("msg1(hello2)")) {
 			IApplMessage m = MsgUtil.buildDispatch("a","msg2","msg2(hello)","a");
 			this.autoMsg( m, arg1 );			
 		}
		return null;
	}

	public static void main( String[] args) {
//		kotlinx.coroutines.runBlocking {
//			
//		};
		QakTest a = new QakTest("qaktest", GlobalScope.INSTANCE);
		MsgUtil.sendMsg("msg1", "msg1(hello1)", a, null);
		Utils.delay(500);
		MsgUtil.sendMsg("msg1", "msg1(hello2)", a, null);
		MsgUtil.sendMsg("msg1", "msg1(hello3)", a, null);
		
		ColorsOut.out("END");
	}
}
