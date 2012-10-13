package conf;

public class Config {
	public static final String GUIOntology = "WebGUIRequest";

	public static String getLAgentAddress(){
		return "LAgent"; //TODO: Set to address of LAgent
	}
	
	public static String getUserAgentAddress(){
		return "User_1";
	}
	
	public static String getWorkerAgentAddress(){
		return "Worker_1";
	}
	
	//JADE parameters
	public static String getMainHost(){
		return "localhost";
	}
	
	public static String getPlatformId(){
		return "p-1";
	}
	
	public static String getMainPort(){
		return "1099";
	}
	
	public static String getContainerName(){
		return "cont-1";
	}
	
	public static String getLocalhost(){
		return "localhost";
	}
	
	public static String getLocalPort(){
		return "1100";
	}
	
	public static String getServices(){
		return "jade.core.event.NotificationService";
	}
}
