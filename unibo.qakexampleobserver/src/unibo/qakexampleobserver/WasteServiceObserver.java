package unibo.qakexampleobserver;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapObserveRelation;
import org.eclipse.californium.core.CoapResponse;
import unibo.comm22.utils.ColorsOut;
import unibo.comm22.utils.CommUtils;

public class WasteServiceObserver {
    private CoapObserveRelation relation = null;
    private CoapClient client = null;
    private String truckRequestStr = "msg(depositrequest, request,python,wasteservice,depositrequest(glass,200),1)";

    public WasteServiceObserver(){
        client = new CoapClient("coap://localhost:8013/ctxwasteservice/wasteservice");
        observe( );
    }

    public void  observe( ) {
        ColorsOut.outappl("Observer observe " + client , ColorsOut.CYAN);
        relation = client.observe(
                new CoapHandler() {
                    @Override public void onLoad(CoapResponse response) {
                        String content = response.getResponseText();
                        ColorsOut.outappl("ActorObserver | value=" + content, ColorsOut.GREEN);
                    }
                    @Override public void onError() {
                        ColorsOut.outerr("OBSERVING FAILED (press enter to exit)");
                    }
                });
    }

    public static void main( String[] args ){
         new WasteServiceObserver();
        CommUtils.delay(60*1000*10);
        ColorsOut.outappl("Observer ENDS " , ColorsOut.CYAN);
    }
}
