package moviewatchlist;

import java.util.Comparator;

import moviewatchlist.Movie;

public class MovieComparatorByYear implements Comparator<Movie>{
	public int compare(Movie a, Movie b) {
		return b.getYear() - a.getYear();
	}
}
