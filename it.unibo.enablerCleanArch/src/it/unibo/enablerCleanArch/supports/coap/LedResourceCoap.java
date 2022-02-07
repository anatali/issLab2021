package it.unibo.enablerCleanArch.supports.coap;
import java.net.InetAddress;
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.domain.ILed;
import it.unibo.enablerCleanArch.supports.ColorsOut;
import it.unibo.enablerCleanArch.supports.IApplMsgHandler;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArchapplHandlers.LedApplLogic;

public class LedResourceCoap extends CoapDeviceResource implements IApplMsgHandler  {
//	private ILed led; 
// 	private IApplMsgHandler ledHandler;
 	private LedApplLogic ledLogic;
	
 	public LedResourceCoap(String name, ILed led   ) {
		super(name, DeviceType.output);
		ledLogic = new LedApplLogic(led) ;
   	}

	@Override
	protected String elaborateGet(String req) {
		ColorsOut.out( getName() + " |  before elaborateGet req:" + req    );
		String answer = "";
		try {
			ApplMessage msg = new ApplMessage( req );
			answer = ledLogic.elaborate( msg  );			
		}catch( Exception e) {
			answer = ledLogic.elaborate( req  );
		}		 
 		return  answer;
	}
	@Override
	protected String elaborateGet(String req, InetAddress callerAddr) {
		return elaborateGet(req);
	}

	@Override
	protected void elaboratePut(String req) {
		ColorsOut.out( getName() + " |  before elaboratePut req:" + req   );
//		if( req.equals( "on") ) led.turnOn();
//		else if( req.equals("off") ) led.turnOff();		
		ledLogic.elaborate( req  );
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
 
	@Override
	public void elaborate(String message, Interaction2021 conn) {
		ColorsOut.outerr("elaborate String is hidden in LedResourceCoap");		
	}

	@Override
	public void elaborate(ApplMessage message, Interaction2021 conn) {
		ColorsOut.outerr("elaborate ApplMessage is hidden in LedResourceCoap");		
	}

	@Override
	public void sendMsgToClient(String message, Interaction2021 conn) {
		//Does nothing since already implemented by the Coap protocol		
	}

	@Override
	public void sendAnswerToClient(String message) {
		//Does nothing since already implemented by the Coap protocol
	}

}
