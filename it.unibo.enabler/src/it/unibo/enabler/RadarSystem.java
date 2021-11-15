package it.unibo.enabler;

public class RadarSystem {
	
	private Controller cntrl;
	private TcpEnabler rge;
	private TcpEnabler le;
	
	public void setup() throws Exception {
		le    = new LedEnabler(8010);
		rge   = new RadarGuiEnabler(8012);
		cntrl = new Controller( rge, le );
		le.start();
		rge.start();		
		cntrl.start();
	}
	
	public static void main( String[] args) throws Exception {
		new RadarSystem().setup();
		Thread.sleep(3000);
		System.out.println("BYE  ");
	}

}
