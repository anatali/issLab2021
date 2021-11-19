package it.unibo.enabler;

import java.net.Socket;
 
public class TcpClient {
	private Interaction2021 connSupport;

	public TcpClient( String host, int port, ApplMessageHandler handler ) throws Exception {
		Socket socket =  new Socket( host, port );	
		connSupport   =  new TcpConnSupport( socket );
		if( handler != null ) activateInput(handler);
		
	}
	
	public void forward( String msg ) throws Exception {
		connSupport.forward(msg);
	}
	
	private void activateInput(ApplMessageHandler handler) {
		new Thread() {
			public void run() {
				try {
					System.out.println("TcpClient | STARTS ...");
					while( true ) {
						String inputMsg = connSupport.receiveMsg();
						handler.elaborate(inputMsg);
					}
				} catch (Exception e) {
 					e.printStackTrace();
				}				
			}
		}.start();
	}
	

}
