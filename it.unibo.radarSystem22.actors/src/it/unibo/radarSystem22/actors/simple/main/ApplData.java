package it.unibo.radarSystem22.actors.simple.main;

import it.unibo.actorComm.ProtocolType;
import it.unibo.actorComm.utils.CommUtils;
import it.unibo.kactor.IApplMessage;

public class ApplData {
	public static final String ledName        = "led";
	public static final String controllerName = "controller";

	public static final String comdLedon   = "turnOn";
	public static final String comdLedoff  = "turnOff";
	public static final String reqLedState = "getState";

	public static final String cmdActivate  = "activate";
	public static final String cmdDectivate = "deactivate";
	
	public static final int ctxPort          = 8018;
	public static final ProtocolType protocol= ProtocolType.tcp;
	
	public static final IApplMessage turnOnLed    = CommUtils.buildDispatch(controllerName, "cmd", comdLedon,   ledName);
	public static final IApplMessage turnOffLed   = CommUtils.buildDispatch(controllerName, "cmd", comdLedoff,  ledName);
	public static final IApplMessage getState     = CommUtils.buildRequest(controllerName,  "ask", reqLedState, ledName);
	
	public static final  IApplMessage activateCrtl = CommUtils.buildDispatch("main", "cmd", cmdActivate, controllerName);
	
	
	
}
