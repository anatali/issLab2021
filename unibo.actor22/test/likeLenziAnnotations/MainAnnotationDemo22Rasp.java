package likeLenziAnnotations;
 
import unibo.actor22.Qak22Context;
import unibo.actor22.annotations.*;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@Context22(name = "pcCtx", host = "127.0.0.1", port = "8080")
@Context22(name = "raspCtx",   host = "localhost",    port = "8082")
@Actor22(name = "a1",  contextName = "pcCtx" )
@Actor22(name = "a2",  contextName = "raspCtx", implement=A2Actor22OnRasp.class )
public class MainAnnotationDemo22Rasp {
    
	public MainAnnotationDemo22Rasp() {
        CommSystemConfig.tracing = false;
        Qak22Context.configureTheSystem(this);
        Qak22Context.showActorNames();
    }
    

    public static void main(String[] args) {
        new MainAnnotationDemo22Rasp();
        CommUtils.delay(1000);
        
    }
}