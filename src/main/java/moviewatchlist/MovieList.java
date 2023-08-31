package moviewatchlist;

import java.util.ArrayList;
import java.util.List;

public class MovieList {
	private List<Movie> movies;
	
	//---Constructors---
	public MovieList() {
		movies = new ArrayList<>();
	}
	
	public MovieList(List<Movie> list) {
		this();
		for(Movie m: list)
			add(m);
	}
	
	//Accessors
	public List<Movie> getMovies(){
		return movies;
	}
	
	//Modifiers
	public void add(Movie movie) {
		movies.add(movie);
	}
	
	
}
