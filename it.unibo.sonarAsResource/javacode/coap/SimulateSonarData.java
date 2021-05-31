/*
 * SimulateSonarData
 * Reads a value data from the user console
 * Calls coapSupport.updateResourceWithValue( data  ) to PUT on the resource an event
 * 		msg(sonarrobot,event,support,none,sonar(data),1)
 * 
 */
package coap;

import java.util.Scanner;

public class SimulateSonarData {
	
	private CoapSupport coapSupport ;
	
	public void doJob() {
		coapSupport = new CoapSupport( "coap://localhoist:8028","ctxsonarresource/sonarresource" );
		Scanner in  = new Scanner(System.in);
		String data = "";
		while( ! data.equals("q")) {
			System.out.print("INPUT>");
			data = in.nextLine();
			if( ! coapSupport.updateResourceWithValue( data  ) ) System.out.println("EMIT failure"  );
		}
	}
	
	public static void main(String[] args) {
		new SimulateSonarData().doJob();
	}

}
