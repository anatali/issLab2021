package it.unibo.enablerCleanArch.enablers;
import it.unibo.enablerCleanArch.supports.Colors;
import it.unibo.enablerCleanArch.supports.Interaction2021;
import it.unibo.enablerCleanArch.supports.TcpClient;
import it.unibo.enablerCleanArch.supports.coap.CoapSupport;


public class EnablerAsClient {
private Interaction2021 conn; 
protected String name ;		//could be a uri
protected ProtocolType protocol ;
//protected CoapSupport coapSupport;
 
	public EnablerAsClient( String name, String host, String entry, ProtocolType protocol ) {
		try {
			this.name     = name;
			this.protocol = protocol;			 
			setConnection(host,  entry, protocol);
			Colors.out(name+"  | STARTED conn=" + conn, Colors.GREEN);
		} catch (Exception e) {
			Colors.outerr( name+"  |  ERROR " + e.getMessage());		}
	}
	
	protected void setConnection( String host, String entry, ProtocolType protocol  ) throws Exception {
		if( protocol == ProtocolType.tcp) {
			int port = Integer.parseInt(entry);
			conn = TcpClient.connect(host,  port, 10); //10 = num of attempts
			//Colors.out(name + " |  setConnection "  + conn );
		}else if( protocol == ProtocolType.coap ) {
			//name = uri
			conn = new CoapSupport(host,  entry); //CoapApplServer.inputDeviceUri+"/led"
		}
	}
  	
	public void sendCommandOnConnection( String cmd )  {
		Colors.out( name+"  | sendCommandOnConnection " + cmd + " conn=" + conn, Colors.GREEN);
		try {
			/*
			if( protocol == ProtocolType.tcp) {
				conn.forward(cmd);
			}else if( protocol == ProtocolType.coap) {
				Colors.out( name+"  | sendCommandOnConnection to uri=" + name+ " val="+cmd, Colors.GREEN);
	 			//coapSupport.updateResource(cmd);
				coapSupport.forward(cmd);
	 		}*/
			conn.forward(cmd);
		} catch (Exception e) {
			Colors.outerr( name+"  | sendCommandOnConnection ERROR=" + e.getMessage()  );
		}
	}
	public String sendRequestOnConnection( String request )  {
		Colors.out( name+"  | sendRequestOnConnection request=" + request + " conn=" + conn, Colors.GREEN);
		try {
			/*
			if( protocol == ProtocolType.tcp) {
				//conn.forward(request);
				//String answer = conn.receiveMsg();
				String answer = conn.request(request);
				Colors.out( name+"  | sendRequestOnConnection answer=" + answer , Colors.GREEN);
				return answer;
			}else if( protocol == ProtocolType.coap) {
				Colors.out( name+"  | sendRequestOnConnection to uri=" + name + " val="+request, Colors.GREEN);
				//String answer = coapSupport.readResource(request);
				String answer = coapSupport.request(request);
				Colors.out( name+"  | sendRequestOnConnection to uri=" + name + "answer=" + answer, Colors.GREEN);
				return answer;
			}else return null;
			*/
			String answer = conn.request(request);
			Colors.out( name+"  | sendRequestOnConnection answer=" + answer , Colors.GREEN);
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
