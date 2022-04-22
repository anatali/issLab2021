package likeLenziAnnotations;
 
import unibo.actor22.Qak22Context;
import unibo.actor22.annotations.*;
import unibo.actor22comm.utils.CommSystemConfig;
import unibo.actor22comm.utils.CommUtils;

@Context22(name = "demoCtx", host = "127.0.0.1", port = "8080")
@Context22(name = "raspi",   host = "localhost",    port = "8082")
@Actor22(name = "a1",  contextName = "demoCtx" )
@Actor22(name = "a2",  contextName = "raspi", implement=DemoActor22OnRaspi.class )
public class MainAnnotationDemo22Raspi {
    
	public MainAnnotationDemo22Raspi() {
        CommSystemConfig.tracing = false;
        Qak22Context.configureTheSystem(this);
        Qak22Context.showActorNames();
    }
    

    public static void main(String[] args) {
        new MainAnnotationDemo22Raspi();
        CommUtils.delay(1000);
        
    }
}