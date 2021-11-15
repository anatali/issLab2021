package it.unibo.enabler;

public class RadarSystem {
 	
	public void setup() throws Exception {
		IEnabler le     = new LedEnabler(8010);
		IEnabler rge    = new RadarGuiEnabler(8012);
		IEnabler se     = new SonarEnabler(8014);
		
		Controller ctrl = new Controller( rge, le, se );
		
		IEnabler ce     = new ControllerEnabler( 8016, ctrl );
	}
	
	public static void main( String[] args) throws Exception {
		new RadarSystem().setup();
	}

}
