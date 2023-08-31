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
	
	@Override
	public String toString() {
		return title + " (" + year + ")";
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == this) return true;
		if(!(o instanceof Movie)) return false;
		Movie other = (Movie) o;
		return this.id.equals(other.id) &&
				this.title.equals(other.title) &&
				this.year == other.year;
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
