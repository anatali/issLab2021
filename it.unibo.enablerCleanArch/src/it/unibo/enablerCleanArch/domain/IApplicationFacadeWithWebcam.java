package it.unibo.enablerCleanArch.domain;
  

public interface IApplicationFacadeWithWebcam extends IApplicationFacade{ // IApplication
 	public void takePhoto( String fName  );	
	public void sendCurrentPhoto();
	public void startWebCamStream(   );	
	public void stopWebCamStream(   );	
	public String getImage(String fName);
	public void storeImage(String encodedString, String fName);
}
