package it.unibo.enablerCleanArch.supports;
//import java.io.*;

//Install ANSI-Escape in Console form EclipseMarket
public class Colors {
	public static final String ANSI_YELLOW         = "\u001B[33m";
	public static final String ANSI_PURPLE         = "\u001B[35m";
	public static final String ANSI_RESET          = "\u001B[0m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String YELLOW_BACKGROUND   = "\u001B[43m";
	public static final String WHITE_BACKGROUND    = "\u001B[47m";
	public static final String BACKGROUND          = " ";	//WHITE_BACKGROUND
	 
	
	public static final String BLUE     = "\u001B[34m";
	public static final String RED      = "\u001B[31m";
	public static final String GREEN    = "\u001B[32m";
	
	public static final String prefix   = "		";
	
	public static void out( String m, String color ) {		 
		System.out.println(  prefix + BACKGROUND + color + m + ANSI_RESET);
	}
	
	public static void out( String m  ) {		 
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