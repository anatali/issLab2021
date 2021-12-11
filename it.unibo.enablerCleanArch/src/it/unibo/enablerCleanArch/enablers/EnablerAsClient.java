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
			//Colors.out(name+"  | STARTED conn=" + conn);
		} catch (Exception e) {
			Colors.outerr( name+"  |  ERROR " + e.getMessage());		}
	}
	
	protected void setConnection( String host, int port, ProtocolType protocol  ) throws Exception {
		if( protocol == ProtocolType.tcp) {
			conn = TcpClient.connect(host,  port, 10); //10 = num of attempts
			//Colors.out(name + " |  setConnection "  + conn );
		}else if( protocol == ProtocolType.coap ) {
			//Coap: attivo un SonarObserver che implementa getVal
		}
	}
  	
	public void sendCommandOnConnection( String val )  {
		//Colors.out( name+"  | sendCommandOnConnection " + val + " conn=" + conn, Colors.GREEN);
		try {
			conn.forward(val);
		} catch (Exception e) {
			Colors.outerr( name+"  | sendCommandOnConnection ERROR=" + e.getMessage()  );
		}
	}
	public String sendRequestOnConnection( String val )  {
		//Colors.out( name+"  | sendRequestOnConnection " + val + " conn=" + conn, Colors.GREEN);
		try {
			conn.forward(val);
			String answer = conn.receiveMsg();
			return answer;
		} catch (Exception e) {
			Colors.outerr( name+"  | sendRequestOnConnection ERROR=" + e.getMessage()  );
			return null;
		}
 	}	
	public Interaction2021 getConn() {
		return conn;
	}
}
