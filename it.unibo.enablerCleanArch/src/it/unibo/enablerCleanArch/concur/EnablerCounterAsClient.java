package it.unibo.enablerCleanArch.concur;

import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class EnablerCounterAsClient extends EnablerAsClient{

	public EnablerCounterAsClient(String name, String host, int port ) {
		super(name, host, port, ProtocolType.tcp);
	}

	@Override
	protected void handleMessagesFromServer(Interaction2021 conn) throws Exception {
	}

	public static void main( String[] args) throws Exception {
		EnablerCounterAsServer server  = new EnablerCounterAsServer("serverCounter");
		EnablerCounterAsClient client1 = new EnablerCounterAsClient(
						"client1","localhost",EnablerCounterAsServer.port);
		EnablerCounterAsClient client2 = new EnablerCounterAsClient(
				"client2","localhost",EnablerCounterAsServer.port);
		
		 
		client1.sendValueOnConnection("dec(0)");
		client2.sendValueOnConnection("dec(0)");
		
 
	}
}
