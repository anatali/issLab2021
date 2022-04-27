package unibo.wenvUsage22.annot.walker.alarms;

import it.unibo.kactor.IApplMessage;
import unibo.actor22.QakActor22FsmAnnot;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.State;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.utils.CommUtils;
import unibo.wenvUsage22.common.ApplData;
import unibo.actor22comm.utils.ColorsOut;

public class Sentinel extends Thread {

//	public Sentinel(String name) {
//		super(name);
//		autoMsg( SystemData.activateActor( name, name ) );
// 	}
	@Override 
	public void run() { 
		for( int i = 1; i<=3; i++) {
			CommUtils.delay( 1500 );  //Si blocca su wallDown
			ColorsOut.outappl( "QakActor22 | emit=" + SystemData.fireEvent(  )  , ColorsOut.RED);
			Qak22Util.emitEvent( SystemData.fireEvent(  ) );
			CommUtils.delay( 2000 );
			ColorsOut.outappl( "QakActor22 | emit=" + SystemData.endAlarm(  )  , ColorsOut.RED);
			Qak22Util.emitEvent( SystemData.endAlarm(  ) );
		}
	}

}
