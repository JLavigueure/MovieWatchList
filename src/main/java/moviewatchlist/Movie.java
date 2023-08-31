package moviewatchlist;

public class Movie {
	private String id;
	private String title;
	private int year;
	
	//Constructors
	public Movie(String id, String title, int year) {
		this.id = id;
		this.title = title;
		this.year = year;
	}

	//Accessors
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public int getYear() {
		return year;
	}
	
	//Modifiers
	public void setId(String id) {
		this.id = id;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setYear(int year) {
		this.year = year;
	}
	

}
