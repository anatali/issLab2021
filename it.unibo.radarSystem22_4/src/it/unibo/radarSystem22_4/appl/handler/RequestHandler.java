package it.unibo.radarSystem22_4.appl.handler;

import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.ApplMsgHandler;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.interfaces.Interaction2021;

public class RequestHandler extends ApplMsgHandler {

	public RequestHandler(String name) {
		super(name);
 	}

	@Override
	public void elaborate(IApplMessage message, Interaction2021 conn) {
 		ColorsOut.outappl(name + " elaborate "+message, ColorsOut.GREEN);
	}

}
