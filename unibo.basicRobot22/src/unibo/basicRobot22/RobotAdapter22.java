package unibo.basicRobot22;

 
import unibo.actor22comm.interfaces.IObserver;
import unibo.actor22comm.interfaces.Interaction2021;
import unibo.actor22comm.utils.ColorsOut;
import unibo.actor22comm.ws.WsConnSysObserver;
import unibo.actor22comm.ws.WsConnection;


public class RobotAdapter22 {
protected String actorOwnerName = "";
protected Interaction2021 conn;
protected String robotCmdId = "move";
protected String robotName  = "vr";

	public RobotAdapter22( String actorOwnerName) {
		configure(actorOwnerName);
	}
	
	protected void configure( String actorOwnerName) {
		this.actorOwnerName = actorOwnerName;
		//Dovrebbe distingure tra diversi tipi di robot e usare un supporto che realizza
		ColorsOut.outappl(actorOwnerName + " | ws connecting ...." ,  ColorsOut.YELLOW);
		conn = WsConnection.create("localhost:8091" ); 
		
		IObserver robotMoveObserver = new WsConnApplObserver(actorOwnerName); //genera endMoveOk / endMoveKo
		//IObserver robotMoveObserver = new WsConnSysObserver(actorOwnerName);
		((WsConnection)conn).addObserver(robotMoveObserver);
	    
		ColorsOut.outappl(actorOwnerName + " | conn:" + conn,  ColorsOut.YELLOW);
	}

 
	private  String arilToCril( String cmd  ) {
		switch( cmd ) {
			case "w" : return  ApplData.moveForward(300) ;
			case "s" : return  ApplData.moveBackward(300);		
			case "a" : return  ApplData.turnLeft(300);
			case "d" : return  ApplData.turnRight(300);
			case "h" : return  ApplData.stop(100);
			default: return    ApplData.stop(100);
		}		 
	}

	public void robotMove( String cmd ) {
		try {
			conn.forward( arilToCril(cmd) );
		}catch( Exception e) {
			ColorsOut.outerr( actorOwnerName +  " | robotMove ERROR:" +  e.getMessage() );
		}			
	}

	public void virtualRobotMove( String cmd ) {
		try {
			conn.forward( cmd );
		}catch( Exception e) {
			ColorsOut.outerr( actorOwnerName +  " | robotMove ERROR:" +  e.getMessage() );
		}
	}


}