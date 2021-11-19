package it.unibo.enabler;


class NaiveServerMsgHandler extends ApplMessageHandler{
	protected void elaborate(String msg) {
		System.out.println("NaiveServerMsgHandler | " + msg );
		this.conn.forward("Some answer from NaiveServerMsgHandler");
	}
}

class NaiveClientMsgHandler extends ApplMessageHandler{
	protected void elaborate(String msg) {
		System.out.println("NaiveClientMsgHandler | " + msg );
		
	}
}

public class TcpEnablerTest {
	private ApplMessageHandler hs = new NaiveServerMsgHandler();	
	private ApplMessageHandler hc = new NaiveClientMsgHandler();	
	public void doWork() throws Exception {		
 		new TcpEnabler( "enablerServer", 8033 , hs );
 		
 		TcpClient c1 = new TcpClient("localhost", 8033, hc); 
 		c1.forward("Connection from c1");
	}

	public static void main( String[] args) throws Exception {
		new TcpEnablerTest().doWork();
	}
}
