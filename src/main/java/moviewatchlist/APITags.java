package moviewatchlist;

public enum APITags {
	TITLE("/title/"),
	TITLES("/titles"),
	SEARCH("/search"),
	UTILS("/utils");
	
	String tag;
	
	APITags(String tag) {
		this.tag = tag;
	}
}
