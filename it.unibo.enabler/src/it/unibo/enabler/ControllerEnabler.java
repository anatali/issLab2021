package it.unibo.enabler;

public class ControllerEnabler extends TcpEnabler{
private Controller ctrl ;

	public ControllerEnabler( int port, Controller ctrl) throws Exception {
		super("ControllerEnabler", port);
		this.ctrl = ctrl;
	}

	@Override
	protected void elaborate(String distance) {
		System.out.println(name + " | distance=" + distance);
		if( distance != null ) {
			ctrl.doWork( distance );
		}
	}
	

}
