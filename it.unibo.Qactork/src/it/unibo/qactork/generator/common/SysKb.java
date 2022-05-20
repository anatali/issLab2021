package it.unibo.qactork.generator.common;
 
import java.net.URL;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.generator.IFileSystemAccessExtension2;

import it.unibo.qactork.QActorSystem;
import it.unibo.qactork.QActorSystemSpec;

/*
 * SINGlETON used to store knowledge about the user model
 */
public class SysKb {
	protected static SysKb sysKb = null;
	protected QActorSystemSpec domainModel;
  
	public static SysKb getSysKb() {
		if (sysKb == null) {
			sysKb = new SysKb();
		}
		return sysKb;
	}

	public SysKb() {
		sysKb = this;
		//System.out.println(" *** SysKb pojo created *** " + sysKb);
	}
	
 
 	public void setDomainModel(QActorSystemSpec dm) {
		domainModel = dm;
	}
	public QActorSystemSpec getDomainModel(  ) {
		return domainModel ;
	}

	public String getActorSystemName( ){
  		return domainModel.getName();
  	}
 
	public static boolean existFile(String fName){
		try {
//			org.eclipse.core.runtime.Platform.
  			URI furi = ((IFileSystemAccessExtension2)GenUtils.curFsa).getURI(fName );
 			URL furl = new URL( ""+furi );
//   			System.out.println(" *** furl=" + furl  );
//  		    InputStream inputStream = 
  		    		furl.openConnection().getInputStream();
  			System.out.println(" *** exists:" + fName );
			return true;
		} catch ( Exception e) {
			System.out.println(" *** DOES NOT exist: " + fName);
			return false;
		}
	}
}
