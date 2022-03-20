package it.unibo.radarSystem22_4.appl.interpreter;
import it.unibo.radarSystem22.domain.interfaces.ISonar;

import it.unibo.radarSystem22_4.comm.interfaces.IApplInterpreter;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.utils.ColorsOut;
 

public class SonarApplInterpreter implements IApplInterpreter{
private	ISonar sonar;

	public SonarApplInterpreter(ISonar sonar) {
		this.sonar = sonar;
	}

 
	@Override
	public String elaborate(IApplMessage message) {
		ColorsOut.out("SonarApplInterpreter | elaborate " + message, ColorsOut.BLUE);
		String payloead = message.msgContent();
		if( payloead.equals("getDistance")) {
			//ColorsOut.out(name+ " | elaborate getDistance="  , ColorsOut.BLUE);
			return ""+sonar.getDistance().getVal();
 		}else if( payloead.equals("activate")) {
			sonar.activate();
		}else if( payloead.equals("deactivate")) {
			sonar.deactivate();
		}else if( payloead.equals("isActive")) {
			ColorsOut.out("SonarApplInterpreter | isActive " + sonar.isActive(), ColorsOut.BLUE);
			return ""+sonar.isActive();
 		}
 		return payloead+"_done";
	}
	

}
