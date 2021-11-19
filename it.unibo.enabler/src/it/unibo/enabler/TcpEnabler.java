package it.unibo.enabler;
import java.net.ServerSocket;
import java.net.Socket;

public  class TcpEnabler extends Thread  {
private int port;
private int count = 1;
private ApplMessageHandler handler;

protected String name;

	public TcpEnabler( String name, int port, ApplMessageHandler handler ) throws Exception {
		this.name      = name;	//name is inherited from Thread
		this.port      = port;
		this.handler   = handler;
		
		System.out.println(name + " | CREATED on port=" + port);	 
		this.start();
	}
	
	public int getPort() { return port; }
	
	@Override
	public void run() {
		try {
			ServerSocket serversock     = new ServerSocket( port );
			while( true ) {
				System.out.println(name + " | waits on port=" + port);	 
				Interaction2021 conn  = waitConn( serversock );
				handler.setConn(conn);
				new TcpMessageHandler( name+count++, conn, handler);
				//activateWork( connSupport );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private Interaction2021 waitConn( ServerSocket serverSocket ) throws Exception{ 
 		int timeOut = 600000;  //msecs
		serverSocket.setSoTimeout(timeOut);
		Socket sock = serverSocket.accept();	
		//System.out.println(name + " | waitConn has created socket: " + sock);
		return new TcpConnSupport(sock) ;
	}

/*	
	protected void activateWork(IConnInteraction connSupport) {
		new Thread() {
			public void run() {
				work( connSupport );
			}
		}.start();
	}
	
	protected abstract void elaborate( String message );
	
	private void work(IConnInteraction connSupport) {
		try {
			while( true ) {
				//System.out.println(name + " | waits for message on ...");
			    String msg = connSupport.receiveALine();
				//System.out.println(name + " | received:" + msg );
			    if( msg == null ) connSupport.closeConnection();
			    else elaborate( msg );
			}
		}catch( Exception e) {
			
		}
	}*/	
		

 	
}
