package it.unibo.enablerCleanArch.supports;

import it.unibo.enablerCleanArch.main.RadarSystemConfig;

//Install ANSI-Escape in Console form EclipseMarket
//See 
public class ColorsOut {
	public static final String ANSI_YELLOW         = "\u001B[33m";
	public static final String ANSI_PURPLE         = "\u001B[35m";
	public static final String ANSI_RESET          = "\u001B[0m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String YELLOW_BACKGROUND   = "\u001B[43m";
	public static final String WHITE_BACKGROUND    = "\u001B[47m";
	public static final String BACKGROUND          = "";	//WHITE_BACKGROUND
 	 
	
	public static final String BLACK    = "\u001b[30m";
	public static final String RED      = "\u001B[31m";
	public static final String GREEN    = "\u001B[32m";
	public static final String YELLOW   = "\u001b[33m";
	public static final String BLUE     = "\u001B[34m";
	public static final String MAGENTA  = "\u001b[35m";
	public static final String CYAN     = "\u001b[36m";
	 
	public static final String BgBlack  = "\u001b[40m";
	public static final String BgRed	= "\u001b[41m";
	public static final String BgGreen	= "\u001b[42m";
	public static final String BgYellow	= "\u001b[43m";
	public static final String BgBlue	= "\u001b[44m";
	public static final String BgMagenta= "\u001b[45m";
	public static final String BgCyan   = "\u001b[46m";
	public static final String BgWhite  = "\u001b[47m";
	                                                
	public static final String prefix   = "		";
	
	public static void out( String m, String color ) {	
		if( RadarSystemConfig.tracing )
		System.out.println(  prefix + BACKGROUND + color + m + ANSI_RESET);
	}
	
	public static void outappl( String m, String color  ) {		 
		System.out.println(  BACKGROUND + color + m + ANSI_RESET);
	}
	public static void out( String m  ) {	
		//if( RadarSystemConfig.tracing )
		System.out.println(  prefix + BACKGROUND + BLUE + m + ANSI_RESET);
	}
	public static void outerr( String m  ) {		 
		System.out.println(  prefix + BACKGROUND + RED + m + ANSI_RESET);
	}

	public static void outm( String m, String color ) {		 
		System.out.println(   BACKGROUND + color + m + ANSI_RESET);
	}
	
	public static  void delay( int dt ) {
		try {
			Thread.sleep(dt);
		} catch (InterruptedException e) {
				e.printStackTrace();
		}		
	}

}

	

/*
BLACK	\u001B[30m	BLACK_BACKGROUND	\u001B[40m
RED	    \u001B[31m	RED_BACKGROUND	    \u001B[41m
GREEN	\u001B[32m	GREEN_BACKGROUND	\u001B[42m
YELLOW	\u001B[33m	YELLOW_BACKGROUND	\u001B[43m
BLUE	\u001B[34m	BLUE_BACKGROUND	    \u001B[44m
PURPLE	\u001B[35m	PURPLE_BACKGROUND	\u001B[45m
CYAN	\u001B[36m	CYAN_BACKGROUND	     \u001B[46m
WHITE	\u001B[37m	WHITE_BACKGROUND	\u001B[47m
*/