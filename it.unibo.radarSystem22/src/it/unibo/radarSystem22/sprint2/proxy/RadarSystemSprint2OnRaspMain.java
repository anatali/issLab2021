package it.unibo.radarSystem22.sprint2.proxy;

import it.unibo.radarSystem22.IApplication;
import it.unibo.radarSystem22.domain.utils.BasicUtils;
import it.unibo.radarSystem22.sprint1.RadarSystemSprint1Main;

public class RadarSystemSprint2OnRaspMain implements IApplication{

	@Override
	public void doJob(String domainConfig, String systemConfig) {
		 
		
	}

	@Override
	public String getName() {
		return this.getClass().getName() ;  
	}

	public static void main( String[] args) throws Exception {
		BasicUtils.aboutThreads("At INIT with NO CONFIG files| ");
		new RadarSystemSprint2OnRaspMain().doJob(null,null);
  	}
}
