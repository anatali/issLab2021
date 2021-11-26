package it.unibo.radarGui21;


public class RadarGuiCoap {
private CoapSupport coapSupport;
private Boolean polling = true;


	public RadarGuiCoap( boolean polling  ) throws Exception {
		this.polling = polling;

 		//IRadarGui radar = RadarGui.create();
/*
		String path = "robot/sonar";
		Distance.createCoapResource(path);
		coapSupport = new CoapSupport("coap://localhost:5683", path);

		 
		if( polling ) doJobPolling(radar);
		//else coapSupport.observeResource( new DistanceHandler(radar) );
		
		new DistanceResourceObserver( "localhost", path) ;
 */
	}
	
	private void doJobPolling(IRadarGui radar) throws Exception {
		new Thread(){
			public void run(){
				while( true ) {
					try {
						String msg = coapSupport.readResource();
						DistanceHandlerWithRadarGui.showDataOnGui(msg, radar);
						Thread.sleep(500);
					}catch( Exception e){
						break;
					}
				}
			}
		}.start();
	}
	
 	public CoapSupport getCoapSupport(){
		return coapSupport;
	}


	public static void main(String[] args) throws Exception{
		RadarGuiCoap appl = new RadarGuiCoap( false );

		//simulateData()
		for( int i =1; i<=9; i++) {
			Thread.sleep(1000);
			appl.getCoapSupport().updateResource(""+10*i);
		}

	}
//curl http://curl.haxx.se/docs/httpscripting.html
//curl http://curl.haxx.se/docs/httpscripting.html
}
