package moviewatchlist;

import java.io.FileWriter;
import java.io.InputStream;
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
	
	//Modifiers
	public void add(Movie movie) {
		movies.add(movie);
	}
	
	
	//Read and write to file
	
	//Loads movie list from file
	public boolean loadFromFile() {
		try(InputStream in = MovieList.class.getResourceAsStream("/MovieList.json")){
			String string = new String(in.readAllBytes(), StandardCharsets.UTF_8);
			JsonArray list = (JsonArray) new Gson().fromJson(string, JsonObject.class).get("movies");
			Gson gson = new Gson();
			for(JsonElement element : list) {
				add(gson.fromJson(element, Movie.class));
			}
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	//Saves current movie list to file, overwrites all previous data.
	public boolean saveToFile() {
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
		try(FileWriter writer = new FileWriter("MovieList.json")){
			writer.write(json.toString());
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
	
}
