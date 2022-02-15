package it.unibo.enablerCleanArch.supports.coap;
import java.net.InetAddress;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.IApplInterpreter;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.domain.LedApplInterpreter;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;

public class LedResourceCoap extends ApplResourceCoap   {
 	private IApplInterpreter ledInterpr;
	
 	public LedResourceCoap(String name, IApplInterpreter ledInterpr   ) {
		super(name, DeviceType.output);
		this.ledInterpr = ledInterpr;
		ColorsOut.out( getName() + " | LedResourceCoap CREATED ledInterpr="  + ledInterpr   );
   	}

	@Override
	protected String elaborateGet(String req) {
		ColorsOut.out( getName() + " |  before elaborateGet req:" + req    );
		String answer = "";
		try {
			ApplMessage msg = new ApplMessage( req );
			answer = ledInterpr.elaborate( msg  );			
		}catch( Exception e) {
			answer = ledInterpr.elaborate( req  );
		}		 
 		return  answer;
	}
	@Override
	protected String elaborateGet(String req, InetAddress callerAddr) {
		return elaborateGet(req);
	}

	@Override
	protected void elaboratePut(String req) {
		ColorsOut.out( getName() + " |  elaboratePut req:" + req   );
		String answer = "";
		try {
			ApplMessage msg = new ApplMessage( req );
			answer = ledInterpr.elaborate( msg  );			
		}catch( Exception e) {
			answer = ledInterpr.elaborate( req  );
		}		 
 		 
		
//		if( req.equals( "on") ) led.turnOn();
//		else if( req.equals("off") ) led.turnOff();		
		//Colors.out( getName() + " |  after elaboratePut :" + led.getState()  );
	}  
	@Override
	protected void elaboratePut(String req, InetAddress callerAddr) {
		ColorsOut.out( getName() + " | before elaboratePut req:" + req + " callerAddr="  + callerAddr  );
		elaboratePut(req);
	}

//    protected String elaborate(String message) {
//    	if( message.equals("on")) led.turnOn();
//        else if( message.equals("off") ) led.turnOff();
//        else if( message.equals("getState") ) sendMsgToClient(""+led.getState(), conn );
//    	return "";
//    }
//    protected String elaborate(ApplMessage message) {
//    	String payload = message.msgContent();
//        if( message.isRequest() ) {
//            if(payload.equals("getState") ) {
//                     String ledstate = ""+led.getState();
//                     ApplMessage reply = prepareReply( message, ledstate);
//                     sendAnswerToClient(reply.toString());
//                }
//        }else if( message.isReply() ) {
//
//        }else elaborate(payload, conn); //call to previous version
//   	return "";
//    }
 

}
