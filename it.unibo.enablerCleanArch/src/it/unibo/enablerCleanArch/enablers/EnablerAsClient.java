package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;


public abstract class EnablerAsClient {
private Interaction2021 conn; 
protected String name ;	

	public EnablerAsClient( String name, String host, int port, ProtocolType protocol ) {
		try {
			this.name = name;
			setConnection(host,  port, protocol);
			Colors.out(name+"  | STARTED conn=" + conn);
		} catch (Exception e) {
			Colors.outerr( name+"  |  ERROR " + e.getMessage());		}
	}
	
	protected void setConnection( String host, int port, ProtocolType protocol  ) throws Exception {
		if( protocol == ProtocolType.tcp) {
			conn = TcpClient.connect(host,  port, 10);
			startHandlerMessagesFromServer( conn );
			//Colors.out(name + " |  setConnection "  + conn );
		}else if( protocol == ProtocolType.coap ) {
			//Coap: attivo un SonarObserver che implementa getVal
		}
	}
	
		
	protected void startHandlerMessagesFromServer( Interaction2021 conn) {
		new Thread() {
			public void run() {
				try {
					handleMessagesFromServer(conn);
				} catch (Exception e) {
					Colors.outerr( "SonarClient | handleMessagesFromServer  ERROR " + e.getMessage());
				}				
			}
		}.start();
	}
	
	protected abstract void handleMessagesFromServer( Interaction2021 conn ) throws Exception;
	
	protected void sendValueOnConnection( String val ) throws Exception{
		//Colors.out( name+"  | sendValueOnConnection " + val  );
		conn.forward(val);
	}
	
	public Interaction2021 getConn() {
		return conn;
	}
}
