package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClientSupport;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;
import it.unibo.enablerCleanArch.supports.mqtt.MqttSupport;


public class ProxyAsClient {
private Interaction2021 conn; 
protected String name ;		//could be a uri
protected ProtocolType protocol ;

 
	public ProxyAsClient( String name, String host, String entry, ProtocolType protocol ) {
		try {
			this.name     = name;
			this.protocol = protocol;			 
			setConnection(host,  entry,  protocol);
			Colors.out(name+"  | STARTED conn=" + conn );
		} catch (Exception e) {
			Colors.outerr( name+"  |  ERROR " + e.getMessage());		}
	}
	
	protected void setConnection( String host, String entry, ProtocolType protocol  ) throws Exception {
		if( protocol == ProtocolType.tcp) {
			int port = Integer.parseInt(entry);
			conn = TcpClientSupport.connect(host,  port, 10); //10 = num of attempts
			//Colors.out(name + " |  setConnection "  + conn );
		}else if( protocol == ProtocolType.coap ) {
			conn = new CoapSupport(host,  entry);  
		}else if( protocol == ProtocolType.mqtt ) {
			conn = MqttSupport.getTheSupport();
		}
	}
  	
	public void sendCommandOnConnection( String cmd )  {
 		//Colors.out( name+"  | sendCommandOnConnection " + cmd + " conn=" + conn );
		try {
			conn.forward(cmd);
		} catch (Exception e) {
			Colors.outerr( name+"  | sendCommandOnConnection ERROR=" + e.getMessage()  );
		}
	}
	public String sendRequestOnConnection( String request )  {
 		//Colors.out( name+"  | sendRequestOnConnection request=" + request + " conn=" + conn );
		try {
			String answer = conn.request(request);
			//Colors.out( name+"  | sendRequestOnConnection answer=" + answer  );
			return answer;			
		} catch (Exception e) {
			Colors.outerr( name+"  | sendRequestOnConnection ERROR=" + e.getMessage()  );
			return null;
		}
 	}	
	public Interaction2021 getConn() {
		return conn;
	}
	
	public void close() {
		try {
			conn.close();
			Colors.out(name + " |  CLOSED " + conn  );
		} catch (Exception e) {
			Colors.outerr( name+"  | sendRequestOnConnection ERROR=" + e.getMessage()  );		}
	}
}
