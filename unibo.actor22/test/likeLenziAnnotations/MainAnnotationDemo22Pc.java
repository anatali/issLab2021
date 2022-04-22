package likeLenziAnnotations;
 
import unibo.actor22.Qak22Context;
import unibo.actor22.annotations.*;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@Context22(name = "pcCtx", host = "localhost",    port = "8080")
@Context22(name = "raspCtx",   host = "127.0.0.1", port = "8082")
@Actor22(name = "a1",  contextName = "pcCtx", implement=A1Actor22OnPc.class)
@Actor22(name = "a2",  contextName = "raspCtx" )
public class MainAnnotationDemo22Pc {
    
	public MainAnnotationDemo22Pc() {
        CommSystemConfig.tracing = false;
        Qak22Context.configureTheSystem(this);
        Qak22Context.showActorNames();
    }
    

    public static void main(String[] args) {
        new MainAnnotationDemo22Pc();
        CommUtils.delay(1000);
        
    }
}