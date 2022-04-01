package it.unibo.actorComm.utils;
 
public class BasicUtils {
 	
	public static void showSystemInfo(){

		System.out.println(
			"COMPUTER | memory="+ Runtime.getRuntime().totalMemory() +
					" num of processors=" +  Runtime.getRuntime().availableProcessors());
		System.out.println(
			"AT START | num of threads="+ Thread.activeCount() +" currentThread=" + Thread.currentThread() );
	}

 
	public static void delay(int n) {
		try {
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void waitTheUser() {
		try {			
			int v = -1;
			while( v != '0' ) {
				if( v == 13 || v == 10 )  {}
				else ColorsOut.outappl("Please hit 0 to restart " , ColorsOut.WHITE_BACKGROUND);
				v=System.in.read();		
 				if( v == '0') break;
			}
		} catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	public static void aboutThreads(String msg)   { 
		String tname    = Thread.currentThread().getName();
		String nThreads = ""+Thread.activeCount() ;
		ColorsOut.outappl( msg + " curthread=T n=N".replace("T", tname).replace("N", nThreads), ColorsOut.YELLOW );
	}

 
	
}
