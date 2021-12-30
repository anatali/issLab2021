package it.unibo.enablerCleanArch.main;

import it.unibo.enablerCleanArch.supports.Colors;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
 
public class AllMainRadarLed {
public HashMap<String,IApplication> programs = new HashMap<String,IApplication>();
	
	protected void outMenu() {
		for (String i : programs.keySet()) { //
			  System.out.println( ""+i + "    " + programs.get(i).getName() );
		}
 	}
	public void doChoice() {
		try {
			programs.put("1", new LedUsageMain());
			programs.put("2", new SonarUsageMainWithEnablerTcp());		
			programs.put("3", new SonarUsageMainWithContextTcp());
			programs.put("4", new SonarUsageMainWithContextMqtt());
			programs.put("5", new SonarUsageMainCoap());
			programs.put("6", new RadarSystemAllOnPc());
			programs.put("7", new RadarSystemDevicesOnRasp());
			programs.put("8", new RadaSystemMainCoap());
			programs.put("9", new RadarSystemMainOnPcCoap());
			programs.put("a", new RadarSystemDevicesOnRaspMqtt());
			programs.put("A", new RadarSystemMainOnPcMqtt());
			String i = "";
			outMenu();
			Colors.outappl(">>>   ", Colors.ANSI_PURPLE);
 			BufferedReader inputr = new BufferedReader(new InputStreamReader(System.in));
			i =  inputr.readLine();
 			programs.get( i ).doJob("RadarSystemConfig.json");
 		} catch ( Exception e) {
			 Colors.outerr("ERROR:" + e.getMessage() );
		}
		
	}
	public static void main( String[] args) throws Exception {
		Colors.outappl("---------------------------------------------------", Colors.BLUE);
		Colors.outappl("WARNING: this application uses RadarSystemConfig", Colors.BLUE);
		Colors.outappl("---------------------------------------------------", Colors.BLUE);
		new AllMainRadarLed().doChoice();
	}
}
