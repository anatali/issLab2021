package unibo.wenvUsage22.common;

public class ApplData {

	protected static String crilCmd(String move, int time){
		String crilCmd  = "{\"robotmove\":\"" + move + "\" , \"time\": " + time + "}";
		//ColorsOut.out( "ClientNaiveUsingPost |  buildCrilCmd:" + crilCmd );
		return crilCmd;
	}
	public static String moveForward(int duration)  { return crilCmd("moveForward", duration) ; }
	public static String moveBackward(int duration) { return crilCmd("moveBackward", duration); }
	public static String turnLeft(int duration)     { return crilCmd("turnLeft", duration);     }
	public static String turnRight(int duration)    { return crilCmd("turnRight", duration);    }
	public static String stop(int duration)         { return crilCmd("alarm", duration);        }
	public static String stop( )                    { return crilCmd("alarm", 10);        }

}
