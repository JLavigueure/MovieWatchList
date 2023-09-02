package moviewatchlist;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
	
	public ArrayList<Movie> searchByKeyword(String keyword) {
		ArrayList<Movie> out = new ArrayList<>();
		for(Movie movie : movies) {
			if(movie.getTitle().toLowerCase().contains(keyword.toLowerCase())) out.add(movie);
		}
		return out;
	}
	
	public Movie getMovie(String title) {
		for(Movie movie : movies) {
			if(movie.getTitle().toLowerCase().equals(title.toLowerCase())) return movie;
		}
		return null;
	}
	
	public MovieList filterListByYear(int start, int end) {
		MovieList out = new MovieList();
		for(Movie m:movies) 
			if(m.getYear() > start && m.getYear() < end) out.add(m);
		return out;
	}

	public MovieList filterListByGenre(String genre) {
		MovieList out = new MovieList();
		for(Movie m : movies) 
			if(m.getGenresObj().contains(genre)) out.add(m);
		return out;
	}
	
	//Modifiers
	public void add(Movie movie) {
		movies.add(movie);
	}
	
	//removes object with given title, returns true if removed, returns false if not found
	public boolean remove(String title) {
		for(Movie m : movies) {
			if(m.getTitle().toLowerCase().equals(title.toLowerCase())) {
				movies.remove(m);
				return true;
			}
		}
		return false;
	}
	
	
	//removes movie object
	public boolean remove(Movie m) {
		return movies.remove(m);
	}
	
	//Read and write to file
	
	//Returns true if file is empty.
	public boolean fileIsEmpty() throws Exception {
		InputStream in = MovieList.class.getResourceAsStream("/movieList.json");
		String string = new String(in.readAllBytes(), StandardCharsets.UTF_8);
		if(string.isBlank()) return true;
		return false;
	}
	
	//Loads movie list from file
	public void loadFromFile() throws Exception {
		InputStream in = MovieList.class.getResourceAsStream("/movieList.json");
		String string = new String(in.readAllBytes(), StandardCharsets.UTF_8);
		JsonArray list = (JsonArray) new Gson().fromJson(string, JsonObject.class).get("movies");
		Gson gson = new Gson();
		for(JsonElement element : list) {
			JsonObject movieObj = (JsonObject) element;
			
			//load ratings
			JsonObject ratingsObj = movieObj.get("rating").getAsJsonObject();
			MovieRating rating = 
					new MovieRating(ratingsObj.get("rating").getAsDouble(), ratingsObj.get("votes").getAsInt());
			
			//load genres
			JsonArray genresObj = movieObj.get("genres").getAsJsonObject().get("genres").getAsJsonArray();
			Genres genres = new Genres();
			for(JsonElement genre : genresObj) {
				genres.add(genre.getAsString());
			}
						
			//load id, title, year and init movie object
			Movie movie = gson.fromJson(element, Movie.class);
			
			//save all loaded info to movie object
			movie.setRatingObj(rating);
			movie.setGenresObj(genres);
			//add movie to list
			add(movie);
		}
	}
	
	//Saves current movie list to file, overwrites all previous data.
	public void saveToFile() throws Exception {
		//build output in String
		Gson gson = new Gson();
		StringBuilder json = new StringBuilder("{\"movies\": [");
		if(!movies.isEmpty()) {
			json.append(gson.toJson(movies.get(0)));
			for(int i = 1; i < movies.size(); i++) {
				json.append(",");
				json.append(gson.toJson(movies.get(i)));
			}
		}
		json.append("]}");
		//Write string to file
		URL url = MovieList.class.getResource("/movieList.json");
		File f = new File(url.toURI());
		FileWriter writer = new FileWriter(f);
		writer.write(json.toString());
		writer.close();
		
	}
	
	
	
}
