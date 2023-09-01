package moviewatchlist;

import java.util.ArrayList;

public class Genres {
	private ArrayList<String> genres;
	
	public Genres() {
		this.genres = new ArrayList<String>();
	}
	
	public void add(String s) {
		genres.add(s);
	}
	
	public ArrayList<String> get() {
		return genres;
	}
	
	public String[] getArray() {
		int index = 0;
		String[] array = new String[genres.size()];
		for(String genre: genres) 
			array[index++] = genre;
		return array;
	}
	
	public boolean contains(String genre) {
		return genres.contains(genre.toLowerCase());
	}
}
