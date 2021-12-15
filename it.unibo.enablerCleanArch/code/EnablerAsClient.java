package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;


public class EnablerAsClient {
private Interaction2021 conn; 
protected String name ;		//could be a uri
protected ProtocolType protocol ;
protected CoapSupport coapSupport;
 
	public EnablerAsClient( String name, String host, int port, ProtocolType protocol ) {
		try {
			this.name     = name;
			this.protocol = protocol;
			setConnection(host,  port, protocol);
			Colors.out(name+"  | STARTED conn=" + conn, Colors.ANSI_YELLOW);
		} catch (Exception e) {
			Colors.outerr( name+"  |  ERROR " + e.getMessage());		}
	}
	
	protected void setConnection( String host, int port, ProtocolType protocol  ) throws Exception {
		if( protocol == ProtocolType.tcp) {
			conn = TcpClient.connect(host,  port, 10); //10 = num of attempts
			//Colors.out(name + " |  setConnection "  + conn );
		}else if( protocol == ProtocolType.coap ) {
			coapSupport = new CoapSupport(host, name );
		}
	}
  	
	public void sendCommandOnConnection( String cmd )  {
		Colors.out( name+"  | sendCommandOnConnection " + cmd + " conn=" + conn, Colors.ANSI_YELLOW);
		try {
			if( protocol == ProtocolType.tcp) {
				conn.forward(cmd);
			}else if( protocol == ProtocolType.coap) {
				Colors.out( name+"  | sendCommandOnConnection to uri=" + name+ " val="+cmd, Colors.ANSI_YELLOW);
	 			//coapSupport.updateResource(cmd);
				coapSupport.forward(cmd);
	 		}
		} catch (Exception e) {
			Colors.outerr( name+"  | sendCommandOnConnection ERROR=" + e.getMessage()  );
		}
	}
	public String sendRequestOnConnection( String request )  {
		Colors.out( name+"  | sendRequestOnConnection " + request + " conn=" + conn, Colors.ANSI_YELLOW);
		try {
			if( protocol == ProtocolType.tcp) {
				//conn.forward(request);
				//String answer = conn.receiveMsg();
				String answer = conn.request(request);
				Colors.out( name+"  | sendRequestOnConnection answer=" + answer , Colors.ANSI_YELLOW);
				return answer;
			}else if( protocol == ProtocolType.coap) {
				Colors.out( name+"  | sendRequestOnConnection to uri=" + name + " val="+request, Colors.ANSI_YELLOW);
				//String answer = coapSupport.readResource(request);
				String answer = coapSupport.request(request);
				Colors.out( name+"  | sendRequestOnConnection to uri=" + name + "answer=" + answer, Colors.ANSI_YELLOW);
				return answer;
			}else return null;
		} catch (Exception e) {
			Colors.outerr( name+"  | sendRequestOnConnection ERROR=" + e.getMessage()  );
			return null;
		}
 	}	
	public Interaction2021 getConn() {
		return conn;
	}
}
