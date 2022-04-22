package likeLenziAnnotations;
 
import unibo.actor22.Qak22Context;
import unibo.actor22.Qak22Util;
import unibo.actor22.annotations.*;
import unibo.actor22.common.ApplData;
import unibo.actor22comm.SystemData;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@Context22(name = "demoCtx", host = "localhost",    port = "8080")
@Context22(name = "raspi",   host = "127.0.0.1", port = "8082")
@Actor22(name = "a1",  contextName = "demoCtx", implement=DemoActor22OnPc.class)
@Actor22(name = "a2",  contextName = "raspi" )
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