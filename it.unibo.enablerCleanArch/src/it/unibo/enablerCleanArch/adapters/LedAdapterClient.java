package it.unibo.enablerCleanArch.adapters;

import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;

/*
 * Adapter for an output device 
 */
public class LedAdapterClient extends EnablerAsClient implements ILed {
private boolean ledStateMirror = false;

	public LedAdapterClient( String name, String host, int port, ProtocolType protocol  ) {
		super(name,host,port, protocol);
		System.out.println(name+" |  STARTS for " + host +":"+port);
	}
/*	
	protected Interaction2021 setConnection( String host, int port  ) throws Exception{
 		return TcpClient.connect( host,port,10 );
 	}
*/	
	@Override
	public void turnOn() { 
 		try {
			sendValueOnConnection( "on" );
			ledStateMirror = true;
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}

	@Override
	public void turnOff() {   
 		try {
			sendValueOnConnection( "off" );
			ledStateMirror = false;
		} catch (Exception e) {
 			e.printStackTrace();
		}
 	}

	@Override
	public boolean getState() {   
		return ledStateMirror;
	}
	@Override
	protected void handleMessagesFromServer(Interaction2021 conn) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
