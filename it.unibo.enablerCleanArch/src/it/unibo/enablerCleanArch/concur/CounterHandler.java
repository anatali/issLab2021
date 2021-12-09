package it.unibo.enablerCleanArch.concur;
  
import it.unibo.enablerCleanArch.domain.ApplMessage;
import it.unibo.enablerCleanArch.supports.ApplMessageHandler;
 
public class CounterHandler extends ApplMessageHandler {
private CounterWithDelay c = new CounterWithDelay();
	public CounterHandler( String name ) {
		 super(name);
	}

	@Override
	public void elaborate(String message) {
		ApplMessage msg = new ApplMessage(message);
		if( msg.msgContent().equals("dec(10)")) c.dec(10);	
 		if( msg.msgContent().equals("dec(0)")) c.dec(0);
	}

}
