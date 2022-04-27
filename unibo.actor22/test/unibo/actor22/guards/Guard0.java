package unibo.actor22.guards;

 
import unibo.actor22comm.utils.ColorsOut;

public class Guard0 {
	protected static int vn ;
  	
	public static void setValue( int n ) {
		vn = n;
	}
	public static boolean checkValue(   ) {
		return vn > 0 ;
	}
 	public boolean eval( ) {
 		boolean b = checkValue();
 		ColorsOut.outappl("Guard0 eval="+b , ColorsOut.CYAN);
 		return b;
	}

}
