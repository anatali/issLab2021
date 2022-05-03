package unibo.actor22.interrupts;

 
import unibo.actor22comm.interfaces.IGuard;
import unibo.actor22comm.utils.ColorsOut;

public class Guard1 implements IGuard{
	protected static int vn ;
	protected static int limit = 3 ;
	
	public static void setValue( int n ) {
		vn = n;
	}
 
 	public boolean eval( ) {
 		boolean b = (vn < limit);
 		ColorsOut.outappl("Guard1 eval="+b , ColorsOut.CYAN);
 		return b;
	}

}
