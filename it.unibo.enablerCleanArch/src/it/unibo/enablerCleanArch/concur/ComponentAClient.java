package it.unibo.enablerCleanArch.concur;

import android.net.wifi.WifiConfiguration.Protocol;
import it.unibo.enablerCleanArch.enablers.EnablerAsClient;
import it.unibo.enablerCleanArch.enablers.ProtocolType;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class ComponentAClient  extends EnablerAsClient{

	public ComponentAClient(String name, String host, int port ) {
		super(name, host, port, ProtocolType.tcp);
	}

	@Override
	protected void handleMessagesFromServer(Interaction2021 conn) throws Exception {
	}

}
