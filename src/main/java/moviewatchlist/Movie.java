package moviewatchlist;

public class Movie implements Comparable<Movie>{
	private String id;
	private String title;
	private int year;
	private MovieRating rating;
	private Genres genres;
	private String plot;
	private int runtime;
	
	
	//Constructors
	public Movie(String id, String title, int year) {
		this.id = id;
		this.title = title;
		this.year = year;
		this.rating = new MovieRating();
		plot = "";
		runtime = 0;
	}

	//Accessors
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getFullInfo() {
		String lineBreak = "\n";
		return (toString() + lineBreak
				+ getGenresToString() + lineBreak
				+ getRatingToString() + lineBreak
				+ getRuntimeInMinutes() + " minutes" + lineBreak
				+ getPlot());
		
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
	
	public String getRatingToString() {
		return "Rating: " + rating.getRating() + "/10 - " + rating.getVotes() + " votes";
	}
	
	public String[] getGenres(){
		if(genres == null) return new String[1];
		return genres.getArray();
	}
	
	public String getGenresToString() {
		String[] genres = getGenres();
		StringBuilder string = new StringBuilder("");
		string.append(genres[0]);
		for(int i = 1; i < genres.length; i++) {
			string.append(", ");
			string.append(genres[i]);
		}
		return string.toString();
	}
	
	public Genres getGenresObj() {
		return genres;
	}
	
	public String getPlot() {
		return plot;
	}
	
	public int getRuntime() {
		return runtime;
	}
	
	public int getRuntimeInMinutes() {
		return runtime/60;
	}
	
	@Override
	public String toString() {
		return title + " (" + year + ")";
	}
	
	public String toStringWithRating() {
		return toString() +"\nRating: " + rating.getRating() + "/10 - " + rating.getVotes() + " votes";
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
	
	public int compareTo(Movie movie) {
		return this.title.compareTo(movie.getTitle());
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
	
	public void setPlot(String plot) {
		this.plot = plot;
	}
	
	public void setRuntime(int seconds) {
		runtime = seconds;
	}
}
