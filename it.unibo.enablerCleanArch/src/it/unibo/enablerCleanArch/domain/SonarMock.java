package it.unibo.enablerCleanArch.domain;


public class SonarMock implements ISonar{
private static int sonarVal = 90;

	public static ISonar create() {
		SonarMock sonar = new SonarMock();
		sonar.activate();
		return sonar;
	}
	
	 
	private  void activate() {
		new Thread() {
			public void run() {
				try { Thread.sleep(4500);//for starting the gui ...
				} catch (InterruptedException e) { e.printStackTrace(); }   
				while( true ) {
					sonarVal--;
					if( sonarVal < 0 ) break;
					delay(100);
				}//
				System.out.println("SonarMock | ENDS");
			}
		}.start();
	}

	@Override
	public int getVal() {
		return sonarVal;
	}
	
	private void delay( int dt ) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}

}
