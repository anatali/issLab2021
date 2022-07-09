package unibo.qakexampleobserver;

//import org.apache.log4j.BasicConfigurator;
import unibo.comm22.utils.ColorsOut;


public class WasteServiceCaller {
    private String truckRequestStr = "msg(depositrequest, request,python,wasteservice,depositrequest(glass,200),1)";

    public WasteServiceCaller(){

         doTruckRequest() ;
     }
    
    protected void doTruckRequest() {
		try{
			ConnTcp connTcp   = new ConnTcp("localhost", 8013);
			String answer     = connTcp.request(truckRequestStr);
 			ColorsOut.outappl("doTruckRequest answer=" + answer , ColorsOut.GREEN);
			//connTcp.close();
		}catch(Exception e){
			ColorsOut.outerr("doTruckRequest ERROR:" + e.getMessage());

		}    	
    }

 
    public static void main( String[] args ){
        //BasicConfigurator.configure(); //For slf4j
        new WasteServiceCaller();
        //CommUtils.delay(60*1000*10);
        ColorsOut.outappl("WasteServiceCaller ENDS " , ColorsOut.CYAN);
    }
}
