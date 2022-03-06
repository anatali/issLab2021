package it.unibo.radarSystem22.actors.domain.support;

import it.unibo.kactor.IApplMessage;
import it.unibo.kactor.MsgUtil;

public class DomainData {
	public static final String ledName        = "led";
	public static final String sonarName 	  = "sonar";
	public static final String radarName 	  = "radar";
	public static final String controllerName = "controller";

	public static final  IApplMessage ledOn    = MsgUtil.buildDispatch(controllerName, DeviceLang.cmd, "turnOn", ledName);
	public static final  IApplMessage ledOff   = MsgUtil.buildDispatch(controllerName, DeviceLang.cmd, "turnOff", ledName);
	public static final  IApplMessage ledState = MsgUtil.buildRequest(controllerName, DeviceLang.req, "getState", ledName);

	public static final  IApplMessage sonarActivate   = MsgUtil.buildDispatch(controllerName, DeviceLang.cmd, "activate", sonarName);
	public static final  IApplMessage sonarDeactivate = MsgUtil.buildDispatch(controllerName, DeviceLang.cmd, "deactivate", sonarName);
	public static final  IApplMessage sonarDistance   = MsgUtil.buildRequest(controllerName, DeviceLang.req, "distance", sonarName);

	public static final  IApplMessage controllerActivate   = MsgUtil.buildDispatch("main", DeviceLang.cmd, "activate", controllerName);

	public static int DLIMIT = 60;
}
