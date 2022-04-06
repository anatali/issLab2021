package unibo.radarSystem22.actors;

import it.unibo.radarSystem22.domain.Distance;
import it.unibo.radarSystem22.domain.concrete.SonarConcrete;
import it.unibo.radarSystem22.domain.interfaces.IDistance;
import it.unibo.radarSystem22.domain.interfaces.ISonar;
import it.unibo.radarSystem22.domain.mock.SonarMock;
import it.unibo.radarSystem22.domain.models.SonarModel;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.domain.utils.ColorsOut;
import it.unibo.radarSystem22.domain.utils.DomainSystemConfig;

public class SonarForActor {

	public static ISonar createSonar(String name) {
		if( DomainSystemConfig.simulation )  return createSonarMock(name);
		else  return createSonarConcrete(name);		
	}

	public static ISonar createSonarMock(String name) {
 		return new SonarMockForActor(name);
	}	
	public static ISonar createSonarConcrete(String name) {
 		return new SonarConcreteForActor(name);
	}


}
