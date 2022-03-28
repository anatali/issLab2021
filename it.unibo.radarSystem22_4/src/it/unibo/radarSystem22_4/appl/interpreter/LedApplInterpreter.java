package it.unibo.radarSystem22_4.appl.interpreter;

 
import it.unibo.radarSystem22.domain.interfaces.ILed;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22_4.comm.interfaces.IApplInterpreter;
import it.unibo.radarSystem22_4.comm.interfaces.IApplMessage;
import it.unibo.radarSystem22_4.comm.utils.CommUtils;
 

/*
 * TODO: il Led dovrebbe essere injected con un metodo o una annotation
 */
public class LedApplInterpreter implements IApplInterpreter  {
private ILed led;
 
	public LedApplInterpreter(  ILed led) {
 		this.led = led;
	}
   
 	protected String elaborate( String payload ) {
		ColorsOut.out("LedApplInterpreter | elaborate payload=" + payload  + " led="+led, ColorsOut.GREEN);
 		if( payload.equals("getState") ) return ""+led.getState() ;
	 	else if( payload.equals("on"))   led.turnOn();
	 	else if( payload.equals("off") ) led.turnOff();	
 		return  payload+"_unknown";
 		
 		
 		
	}
 	@Override
    public String elaborate( IApplMessage message ) {
    String payload = message.msgContent();
      if(  message.isRequest() ) {
        if(payload.equals("getState") ) {
          String ledstate = ""+led.getState();
          IApplMessage reply = CommUtils.prepareReply( message, ledstate);
          return (reply.toString() ); //msg(...)
        }else {
     		String answer = "request_unknown";
            IApplMessage reply = CommUtils.prepareReply( message, answer);
    		return reply.toString();
         }
      }else { //command
    	  return null; //answer not handled
      }
    }   
}
