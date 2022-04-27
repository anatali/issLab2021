package unibo.wenvUsage22.annot.walker;
import unibo.actor22comm.interfaces.GuardFun;
import unibo.actor22comm.utils.ColorsOut;

public class GuardGoonWork {
	protected static GuardFun a ;
  	
	public static void setAction( GuardFun aa ) {
		 a = aa;
	}
 	public static boolean checkValue(   ) {
		return a.check();
	}
 	public boolean eval( ) {
 		boolean b = checkValue();
 		ColorsOut.outappl("GuardEndOfWork eval="+b , ColorsOut.CYAN);
 		return b;
	}

}
