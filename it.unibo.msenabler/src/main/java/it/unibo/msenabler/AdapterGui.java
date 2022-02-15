package it.unibo.msenabler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.supports.ColorsOut;

public class AdapterGui {
//	private boolean sonarDataOn = false;
	private String raspAddr     = "unkown";
    private boolean webCamActive= false;
    private boolean applStarted = false;
    private String ledState     = "false";
    private String appName;
	
    public void setApplName(Model model, String appName) {
    	this.appName = appName;
    	model.addAttribute("arg", appName);
    	setModelValues(model,"entry");
    }
	public void setLedState(Model model, String ledState, String cmd) {
		ColorsOut.out("AdapterGui updateLed ledState=" + ledState  );
        model.addAttribute("ledgui",   cmd);
        model.addAttribute("ledstate", ledState);
        setModelValues(model,"Led " + ledState);
	}
	
 	public void setApplStarted( Model model, String addr ) {
		ColorsOut.out("AdapterGui setApplAddress " + addr  );
		raspAddr    = addr;
		applStarted = true;
		setModelValues(model,"setApplAddress");
	}
	public void setSonarDistance( Model model, String d ) {
        ColorsOut.out("HumanEnablerController sonar d=" + d + " sonarDelay=" + RadarSystemConfig.sonarDelay );
        model.addAttribute("sonardistance",d);
        setModelValues(model,"Distance");
	}
	public void setSonarOn( Model model ) {
		setModelValues(model,"sonardataon");
	}
	public void setSonarOff( Model model ) {
		setModelValues(model,"sonardataoff");
	}
 
	public void setModelValues(Model model, String info){
	    model.addAttribute("arg", appName+" - action=" + info);
	    model.addAttribute("applicationAddr",  raspAddr);
	    model.addAttribute("webCamActive",     webCamActive);
	    model.addAttribute("applStarted",      applStarted);
	    model.addAttribute("ledState",         ledState);
	}

}
