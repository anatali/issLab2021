package resourceCoap;

import java.util.Scanner;

import it.unibo.kactor.MsgUtil;

public class SimulateSonarData {
	
	private CoapSupport coapSupport ;
	
	public void doJob() {
		coapSupport = new CoapSupport( "coap://localhost:5683","robot/sonar" );
		Scanner in = new Scanner(System.in);
		String data = "";
		while( ! data.equals("q")) {
			System.out.print("INPUT>");
			data = in.nextLine();;
			String m = MsgUtil.buildEvent("simulator", "sonar", "sonar("+data+")").toString();
			if( ! coapSupport.updateResource( m  ) ) 
				System.out.println("EMIT failure"  );
		}
		
	}
	
	public static void main(String[] args) {
		new SimulateSonarData().doJob();
	}

}
