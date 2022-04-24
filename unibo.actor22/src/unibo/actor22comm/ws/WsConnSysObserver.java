package unibo.actor22comm.ws;

import java.util.Observable;

import org.json.JSONObject;

import it.unibo.kactor.IApplMessage;
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.MoveTimer;
import unibo.actor22comm.SystemData;

public class WsConnSysObserver implements IObserver{
	private String ownerActor = null;
	private MoveTimer timer;
	
	public WsConnSysObserver( String ownerActor ) {
		this.ownerActor = ownerActor;
		timer = new MoveTimer();
	}
	
	//Invocata prima di ogni send su WS. Lo stop lo fa update
	public void startMoveTime() {
		timer.startTime();
	}
	
	@Override
	public void update(Observable source, Object data) {
//		ColorsOut.out("WsConnSysObserver update/2 receives:" + data);
		timer.stopTime();	
		update( data.toString() );
		
	}
	@Override
	public void update(String data) {
		String duration = ""+timer.getDuration();
		ColorsOut.out("WsConnSysObserver update receives:" + data + " duration=" + duration, ColorsOut.BLUE);
		JSONObject dJson = new JSONObject(""+data);
		boolean resultMoveOk =  dJson.has("endmove") && dJson.getBoolean("endmove");
		 
		if( resultMoveOk ) {
			if( dJson.getString("move").contains("turn")) return; //AVOID TO send info about turn
			propagateEndMoveOk( dJson.getString("move") );
		}
		else { //endmove false since interrupted or collision
			boolean resultMoveKo = dJson.has("collision")  || ! dJson.getBoolean("endmove") ;
			if( resultMoveKo ) //
				Qak22Util.sendAMsg(SystemData.endMoveKo(ownerActor,dJson.getString("collision"),duration));
			else ColorsOut.outerr("WsConnSysObserver perhaps sonar data:" + data);
		} 
	}
	
	protected void propagateEndMoveOk( String data ) {
		IApplMessage m;
		if( ownerActor == null ) m = SystemData.endMoveOkEvent( data );
		else m = SystemData.endMoveOk(ownerActor,data);		
		Qak22Util.sendAMsg(m);
	}
	protected void propagateEndMoveKo(String data,String duration) {
		IApplMessage m;
		if( ownerActor == null ) m = SystemData.endMoveKoEvent( data );
		else m = SystemData.endMoveKo(ownerActor,data,duration);		
		Qak22Util.sendAMsg(m);
	}
	
}//WsConnSysObserver	
		
		
		
		
		
		
		
//		if( ownerActor == null ) {
//			IApplMessage ev = Qak22Util.buildEvent( "wsconn", SystemData.wsEventId, data  );
//			Qak22Util.emitEvent( ev );
//		}else { //invio l'evento direttamente a ownerActor 
//			//IApplMessage evMsg = Qak22Util.buildDispatch( "wsconn", SystemData.wsEventId, data, ownerActor  );
//			JSONObject dJson = new JSONObject(""+data);
//			boolean resultMoveOk =  dJson.has("endmove") && dJson.getBoolean("endmove");
//			if( resultMoveOk ) {
// 				Qak22Util.sendAMsg(SystemData.endMoveOk(ownerActor,dJson.getString("move")));
//			}else {
//				boolean resultMoveKo = dJson.has("collision") && dJson.getBoolean("endmove");
//				if( resultMoveKo )
//					Qak22Util.sendAMsg(SystemData.endMoveKo(ownerActor,dJson.getString("move"),"0"));
//			}
//			//Qak22Util.sendAMsg(evMsg);
//		}
		
		
//		JSONObject d = new JSONObject(""+data);
//		ColorsOut.outappl("ClientUsingWs update/2 collision=" + d.has("collision"), ColorsOut.MAGENTA);
 


