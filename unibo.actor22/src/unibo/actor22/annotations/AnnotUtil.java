package unibo.actor22.annotations;

import java.io.FileInputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import unibo.actor22.Qak22Context;
import unibo.actor22comm.ProtocolInfo;
import unibo.actor22comm.utils.ColorsOut;
 

public class AnnotUtil {
/*
-------------------------------------------------------------------------------
RELATED TO Actor22
-------------------------------------------------------------------------------
*/
	
	public static void createActorLocal(Object element) {
        Class<?> clazz            = element.getClass();
        Annotation[] annotations  = clazz.getAnnotations();
         for (Annotation annot : annotations) {
        	 if (annot instanceof ActorLocal) {
        		 ActorLocal a = (ActorLocal) annot;
        		 for( int i=0; i<a.name().length; i++) {
        			 String name     = a.name()[i];
        			 Class  impl     = a.implement()[i];
            		 try {
						impl.getConstructor( String.class ).newInstance( name );
	            		 ColorsOut.outappl( "CREATED LOCAL ACTOR: "+ name, ColorsOut.MAGENTA );
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
							| InvocationTargetException | NoSuchMethodException | SecurityException e) {
 						e.printStackTrace();
					}
         		 }
        	 }
         }
		
	}
	public static void  createProxyForRemoteActors(Object element) {
        Class<?> clazz            = element.getClass();
        Annotation[] annotations  = clazz.getAnnotations();
        for (Annotation annot : annotations) {
        	 if (annot instanceof ActorRemote) {
        		 ActorRemote a = (ActorRemote) annot;
        		 for( int i=0; i<a.name().length;i++) {
        			 String name     = a.name()[i];
        			 String host     = a.host()[i];
        			 String port     = a.port()[i];
        			 String protocol = a.protocol()[i];        			 
        			 Qak22Context.setActorAsRemote(name, port, host, ProtocolInfo.getProtocol(protocol));
            		 ColorsOut.outappl(
            				 "CREATE REMOTE ACTOR PROXY:"+ name + " host:" + host + " port:"+port
            						 + " protocol:" + protocol, ColorsOut.MAGENTA);        			 
        		 }
        	 }
         }
		
	}
	
/*
-------------------------------------------------------------------------------
RELATED TO PROTOCOLS
-------------------------------------------------------------------------------
 */
 
     public static ProtocolInfo checkProtocolConfigFile( String configFileName ) {
        try {
            System.out.println("IssAnnotationUtil | checkProtocolConfigFile configFileName=" + configFileName);
            FileInputStream fis = new FileInputStream(configFileName);
            Scanner sc = new Scanner(fis);
            String line = sc.nextLine();
            //System.out.println("IssAnnotationUtil | line=" + line);
            String[] items = line.split(",");

            String protocol = AnnotUtil.getProtocolConfigInfo("protocol", items[0]);
            System.out.println("AnnotationUtil | protocol=" + protocol);

            String url = AnnotUtil.getProtocolConfigInfo("url", items[1]);
            //System.out.println("IssAnnotationUtil | url=" + url);
             return null;
        } catch (Exception e) {
            System.out.println("AnnotationUtil | WARNING:" + e.getMessage());
            return null;
        }
    }

    //Quite bad: we will replace with Prolog parser
    //  line example: http://localHost:8090/api/move
    //functor era Prolog msg( )
    public static String getProtocolConfigInfo(String functor, String line){
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        ColorsOut.outappl("line:                " + line, ColorsOut.CYAN);
        String content = null;
        if( matcher.find()) {
            int end   = matcher.end() ;
            int start = matcher.start();
            ColorsOut.outappl("start:                   " + start, ColorsOut.CYAN);
            ColorsOut.outappl("end:                     " + end,   ColorsOut.CYAN);
            ColorsOut.outappl("group:                     " + matcher.group(),   ColorsOut.CYAN);
            String a = line.substring(start+1,end-1);
            ColorsOut.outappl("a:                     " + a,   ColorsOut.CYAN);
//            content = line.substring( end, line.indexOf(")") )
//                    .replace("\"","")
//                    .replace("(","").trim();
        }
        return content;
    }
    
    
    public static void readProtocolAnnotation(Object element) {
        try {
            Class<?> clazz            = element.getClass();
            Annotation[] annotations  = clazz.getAnnotations();
             for (Annotation annot : annotations) {
                 if (annot instanceof ProtocolSpec) {
                	ProtocolSpec p  = (ProtocolSpec) annot;
                    ColorsOut.outappl("Tipo del protocollo: " + p.protocol(), ColorsOut.CYAN);
                    ColorsOut.outappl("Url del protocollo:  " + p.url(), ColorsOut.CYAN);
                    //http://localHost:8090/api/move
                    String v = getProtocolConfigInfo("\\w*://[a-zA-Z]*:\\d*/\\w*/\\w*", p.url());
                    ColorsOut.outappl("v:                   " + v, ColorsOut.CYAN);
               }
            }
        } catch (Exception e) {
        	ColorsOut.outerr("AnnotationUtil | readAnnotation ERROR:" + e.getMessage());
        }
    }
    
    


    /*
-------------------------------------------------------------------------------
RELATED TO ROBOT MOVES
-------------------------------------------------------------------------------
 */
 
 
    //Used also by IssArilRobotSupport
    public static boolean checkRobotConfigFile(
        String configFileName, HashMap<String, Integer> mvtimeMap){
        try{
            //spec( htime( 100 ),  ltime( 300 ), rtime( 300 ),  wtime( 600 ), wstime( 600 ) ).
            //System.out.println("IssAnnotationUtil | checkRobotConfigFile configFileName=" + configFileName);
            FileInputStream fis = new FileInputStream(configFileName);
            Scanner sc = new Scanner(fis);
            String line = sc.nextLine();
            //System.out.println("IssAnnotationUtil | checkRobotConfigFile line=" + line);
            String[] items = line.split(",");
            mvtimeMap.put("h", getRobotConfigInfo("htime", items[0] ));
            mvtimeMap.put("l", getRobotConfigInfo("ltime", items[1] ));
            mvtimeMap.put("r", getRobotConfigInfo("rtime", items[2] ));
            mvtimeMap.put("w", getRobotConfigInfo("wtime", items[3] ));
            mvtimeMap.put("s", getRobotConfigInfo("stime", items[4] ));
            //System.out.println("IssAnnotationUtil | checkRobotConfigFile ltime=:" + mvtimeMap.get("l"));
            return true;
        } catch (Exception e) {
            System.out.println("IssAnnotationUtil | checkRobotConfigFile WARNING:" + e.getMessage());
            return false;
        }

    }

    protected static Integer getRobotConfigInfo(String functor, String line){
        Pattern pattern = Pattern.compile(functor);
        Matcher matcher = pattern.matcher(line);
        String content = "0";
        if(matcher.find()) {
            int end = matcher.end() ;
            content = line.substring( end, line.indexOf(")") )
                    .replace("\"","")
                    .replace("(","").trim();
            //System.out.println("IssAnnotationUtil | getRobotConfigInfo functor=" + functor + " v=" + Integer.parseInt(content));
        }
        return Integer.parseInt( content );
    }
 
 
}
