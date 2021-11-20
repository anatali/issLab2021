package it.unibo.enablerCleanArch.adapters;

import it.unibo.enablerCleanArch.domain.ISonar;
import it.unibo.enablerCleanArch.domain.SonarMock;
import it.unibo.enablerCleanArch.main.RadarSystemConfig;
import it.unibo.enablerCleanArch.domain.SonarConcrete;

public class SonarAdapter implements ISonar{
private ISonar sonar;

	public SonarAdapter(ISonar sonar) {
		if( RadarSystemConfig.SonareRemote)  {
			//sonar = new SonarMock();
		}else { 
			this.sonar = sonar;
		}
	}

	@Override
	public int getVal() {
		return sonar.getVal();
	}

}
