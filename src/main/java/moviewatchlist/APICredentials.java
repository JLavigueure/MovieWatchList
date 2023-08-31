package moviewatchlist;


public class APICredentials {
	private String key;
	private String host;
	private String url;
	
	//Constructors
	public APICredentials() {
	}
	
	public APICredentials(String key, String host, String url) {
		this.key = key;
		this.host = host;
		this.url = url;
	}
	
	//Accessors
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
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof APICredentials)) return false;
		APICredentials other = (APICredentials) o;
		return(this.key.equals(other.key) &&
				this.host.equals(other.host) &&
				this.url.equals(other.url));
	}
	
}
