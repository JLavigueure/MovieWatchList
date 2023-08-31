package moviewatchlist;


public class APICredentials {
	private String key;
	private String host;
	private String url;
	
	public APICredentials() {
		
	}
	
	//Access methods
	public String getKey() {
		return key;
	}
	
	public String getHost() {
		return host;
	}
	
	public String getUrl() {
		return url;
	}
	
	//Modifiers
	public void setKey(String k) {
		key = k;
	}
	
	public void setHost(String h) {
		host = h;
	}
	
	public void setUrl(String u) {
		url = u;
	}
	
}
