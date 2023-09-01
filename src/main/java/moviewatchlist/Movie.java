package moviewatchlist;

public class Movie {
	private String id;
	private String title;
	private int year;
	private MovieRating rating;
	private Genres genres;
	
	//Constructors
	public Movie(String id, String title, int year) {
		this.id = id;
		this.title = title;
		this.year = year;
		this.rating = new MovieRating();
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
	
	public MovieRating getRatingObj() {
		return rating;
	}
	
	public double getRating() {
		return rating.getRating();
	}
	
	public String[] getGenres(){
		return genres.getArray();
	}
	
	@Override
	public String toString() {
		return title + " (" + year + ")";
	}
	
	public String toStringWithRating() {
		return toString() +" - " + rating.getRating() +", " + rating.getVotes() + " votes";
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
	
	public void setRatingObj(MovieRating rating) {
		this.rating = rating;
	}
	
	public void setGenresObj(Genres genres) {
		this.genres = genres;
	}
	

}
