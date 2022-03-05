package it.unibo.radarSystem22.actors.domain;

import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;

public class DomainMsg {
	public static final String ledName   = "led";
	public static final String sonarName = "sonar";
	public static final String radarName = "radar";

	public static final  IApplMessage ledOn    = MsgUtil.buildDispatch("main", DeviceLang.cmd, "turnOn", ledName);
	public static final  IApplMessage ledOff   = MsgUtil.buildDispatch("main", DeviceLang.cmd, "turnOff", ledName);
	public static final  IApplMessage ledState = MsgUtil.buildRequest("main", DeviceLang.req, "getState", ledName);

	public static final  IApplMessage sonarActivate   = MsgUtil.buildDispatch("main", DeviceLang.cmd, "activate", sonarName);
	public static final  IApplMessage sonarDeactivate = MsgUtil.buildDispatch("main", DeviceLang.cmd, "deactivate", sonarName);
	public static final  IApplMessage sonarDistance   = MsgUtil.buildRequest("main", DeviceLang.req, "distance", sonarName);
	
}
