package moviewatchlist;

import java.util.Comparator;

import moviewatchlist.Movie;

public class MovieComparatorByRating implements Comparator<Movie>{
	public int compare(Movie a, Movie b) {
		return (int)(b.getRating() * 10 - a.getRating() * 10);
	}
}
