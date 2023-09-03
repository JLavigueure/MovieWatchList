package moviewatchlist;

import java.util.Comparator;

import moviewatchlist.Movie;

public class MovieComparatorByRuntime implements Comparator<Movie>{
	public int compare(Movie a, Movie b) {
		return b.getRuntime() - a.getRuntime();
	}
}
