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
			programs.put("4", new SonarUsageMainCoap());
			programs.put("5", new RadarSystemAllOnPc());
			programs.put("6", new RadarSystemDevicesOnRasp());
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
		new AllMainRadarLed().doChoice();
	}
}
