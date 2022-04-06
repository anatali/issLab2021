package unibo.radarSystem22.actors;

import it.unibo.kactor.IApplMessage;
import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import unibo.actor22.Qak22Util;
import unibo.actor22comm.events.EventMsgHandler;

/*
 * Agisce come emettitore di eventi di distanza
 */
public class SonarMockForActor extends SonarMock{
private IApplMessage distanceEvent ;
private String name;	

	public SonarMockForActor( String name ) {
		this.name = name;
	}
	protected void updateDistance( int d ) {
		curVal = new Distance( d );
		distanceEvent = Qak22Util.buildEvent(name, ApplData.evDistance, ""+d );
		Qak22Util.sendAMsg(distanceEvent, EventMsgHandler.myName);
		ColorsOut.out(name + " | updateDistance "+ d, ColorsOut.BLUE);
	}	
	
}
