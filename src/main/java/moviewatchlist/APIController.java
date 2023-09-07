package moviewatchlist;

import java.io.InputStream; 
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class APIController {
	private APICredentials key;
	
	//---Constructors---
	public APIController() {
		key = initCredentials();
	}
	
	//initialize API credentials
	public APICredentials initCredentials() {
		try(InputStream in = APICredentials.class.getResourceAsStream("/APIKey.json")){
			String string = new String(in.readAllBytes(), StandardCharsets.UTF_8);
			return new Gson().fromJson(string, APICredentials.class);
		}catch(Exception e) {
			System.out.println("Error retrieving API credentials from resources.");
			System.exit(1);
			return null;
		}
	}
	
	
	//---Search functions---
	
	//Searches by title exactly, returns ArrayList of results
	public ArrayList<Movie> searchByTitle(String title) throws Exception{
		//format title 
		title = formatTitle(title);
		//build API url
		String url = key.getUrl() + APITags.TITLES.tag + APITags.SEARCH.tag + APITags.TITLE.tag +
				title +"?exact=false&sort=year.decr&titleType=movie";
		//build HttpRequest
		HttpRequest request = buildRequest(url);
		//Send request and parse results
		JsonArray results = (JsonArray) getResults(request);
		return parseResults(results);
	}

	public ArrayList<Movie> searchByTitleAndYear(String title, int year) throws Exception{
		//format title 
		title = formatTitle(title);
		//build API url
		String url = key.getUrl() + APITags.TITLES.tag + APITags.SEARCH.tag + APITags.TITLE.tag +
				title +"?exact=false&year=" + year + "&sort=year.decr&titleType=movie";
		//build HttpRequest
		HttpRequest request = buildRequest(url);
		//Send request and parse results
		JsonArray results = (JsonArray) getResults(request);
		return parseResults(results);
	}
	
	//Searches by movie ID string and returns a movie with complete fields
	public Movie getFullInfo(String id) throws Exception{
		//build API url
		String url = key.getUrl() + APITags.TITLES.tag + "/" + id + "?info=base_info";
		//build HttpRequest
		HttpRequest request = buildRequest(url);
		//send request and parse results
		JsonObject result = (JsonObject) getResults(request);
		Movie movie = initMovieFromJson(result);
		parseIdSearchResults(result, movie);
		return movie;
	}
	
	//Takes movie id string from movie obj and calls getFullInfo
	public Movie getFullInfo(Movie movie) throws Exception{
		return getFullInfo(movie.getId());
	}
	
	//Returns Genres object with all possible genres from API
	public Genres getGenres() throws Exception {
		String url = key.getUrl() + APITags.TITLES.tag + APITags.UTILS.tag + "/genres";
		//build request
		HttpRequest request = buildRequest(url);
		//send request and parse results
		JsonArray result = (JsonArray) getResults(request);
		Genres genres = new Genres(); 
		for(JsonElement element : result) {
			if(element.isJsonNull()) continue;
			genres.add(element.getAsString());
		}
		return genres;
		
	}

	//Gives a movie recommendation based off other movies on list
	public Movie getRecommendation(MovieList list) throws Exception{
		//calculate parameters
		int startYear = list.getAverageYear() - list.getStandardDeviationOfYear()-5;
		if(startYear < 0) startYear = 2000;
		int endYear = list.getAverageYear() + list.getStandardDeviationOfYear()+5;
		if(endYear < 1950) endYear = java.time.LocalDate.now().getYear();
		String genre = list.getFavoriteGenre();
		Random r = new Random();
		if(genre == "") {
			String[] genres = getGenres().getArray();
			genre = genres[r.nextInt(genres.length-1)];
		}
		int page = r.nextInt(51);
		String url = key.getUrl() + APITags.TITLES.tag + "?genre=" + genre + "&startYear=" + startYear
				+ "&list=most_pop_movies&page=" + page + "&endYear=" + endYear;
		//build request
		HttpRequest request = buildRequest(url);
		//send request and parse results
		JsonArray result = (JsonArray) getResults(request);
		ArrayList<Movie> results = parseResults(result);
		if(results.isEmpty()) return getRecommendation(list);
		Movie recommended = results.get(r.nextInt(10));
		return getFullInfo(recommended);
	}
	
	//---Helper Methods---
	
	//Takes a url (String) and returns the correct API HttpRequest
	private HttpRequest buildRequest(String url) throws Exception{
		return HttpRequest.newBuilder()
		.uri(new URI(url))
		.header("X-RapidAPI-Key", key.getKey())
		.header("X-RapidAPI-Host", key.getHost())
		.GET()
		.build();
	}
	
	//Sends HTTP request and returns JsonArray of the APIs results
	private JsonElement getResults(HttpRequest request) throws Exception{
		HttpResponse<String> response = 
				HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		JsonObject json = new Gson().fromJson(response.body(), JsonObject.class);
		return json.get("results");
	}
	
	//Takes JsonArray of returned values and returns ArrayList of Movie.
	private ArrayList<Movie> parseResults(JsonArray results){
		ArrayList<Movie> movies = new ArrayList<>();
		//return empty list if no results
		if(results.isEmpty()) return movies;
		
		//iterate over results
		for(JsonElement element: results) {
			//convert each element to object
			JsonObject result = (JsonObject) element.getAsJsonObject();
			//init movie from Json Object and add movie to list
			movies.add( initMovieFromJson(result));
		}
		return movies;
	}
	
	//Takes a json object from api and returns movie with id, name, year
	private Movie initMovieFromJson(JsonObject result) {
		//get id
		String id = result.get("id").getAsString();
		//get name
		JsonObject titleText = (JsonObject) result.get("titleText");
		String name;
		if(!titleText.isJsonNull()) {
			name = titleText.get("text").getAsString();
		}else{
			name = "Unknown title";  //no name available
		}
		//get year
		int year;
		if(!(result.get("releaseYear") instanceof JsonNull)) {
			JsonObject releaseYear = (JsonObject) result.get("releaseYear");
			year = Integer.valueOf(releaseYear.get("year").getAsString());
		} else {
			year = 0; //no year available
		}
		return new Movie(id, name, year);
	}
	
	//Completes all fields in Movie object from api
	private void parseIdSearchResults(JsonObject result, Movie movie) {
		parseRatings(result, movie);
		parseGenres(result, movie);
		parsePlot(result, movie);
		parseRuntime(result, movie);
	}
	
	//Adds result data from json object to movie
	private void parseRatings(JsonObject result, Movie movie) {
		JsonObject json = (JsonObject) result.get("ratingsSummary");
		double rating;
		if(json.get("aggregateRating").isJsonNull()) {
			rating = 0; //no rating available
		}else {
			rating = Double.valueOf(json.get("aggregateRating").toString());
		}
		int voteCount = Integer.valueOf(json.get("voteCount").toString());
		movie.setRatingObj(new MovieRating(rating, voteCount));
	}
	
	//Adds result data from json object to movie
	private void parseGenres(JsonObject result, Movie movie) {
		Genres genres = new Genres();
		if(result.get("genres").isJsonNull()) return;
		JsonObject jsonObj = result.get("genres").getAsJsonObject();
		if(jsonObj.isJsonNull()) return; //no genres exist
		JsonArray jsonArray = jsonObj.get("genres").getAsJsonArray();
		for(JsonElement element : jsonArray) {
			genres.add(element.getAsJsonObject().get("text").getAsString());
		}
		movie.setGenresObj(genres);
	}
	
	private void parsePlot(JsonObject result, Movie movie) {
		if(result.get("plot").isJsonNull()) return; //if no plot exists
		JsonObject plotObj = result.get("plot").getAsJsonObject().get("plotText").getAsJsonObject();
		if(plotObj.isJsonNull()) return;
		String plotText = plotObj.get("plainText").getAsString();
		movie.setPlot(plotText);
	}
	
	private void parseRuntime(JsonObject result, Movie movie) {
		if(result.get("runtime").isJsonNull()) return;
		JsonObject runtimeObj = result.get("runtime").getAsJsonObject(); 
		if(runtimeObj.isJsonNull()) return;
		movie.setRuntime(Integer.valueOf(runtimeObj.get("seconds").getAsString()));
	}
	
	//Fixes capitalization and replaces space characters for API use
	public String formatTitle(String title) {
		return removeSpaces(capitalizeFirstLetter(title));
	}
	
	//Capitalizes first letter of each word
	private String capitalizeFirstLetter(String title) {
		String[] words = title.split(" ");
		StringBuilder out = new StringBuilder();
		for(String word: words) {
			out.append(word.substring(0,1).toUpperCase());
			if(word.length() > 1) out.append(word.substring(1).toLowerCase());
			out.append(" ");
		}
		String capitalized = out.toString().trim();
		if(capitalized.contains("-")) 
			return capitalizeFirstLetterWithHyphen(capitalized);
		return capitalized;
	}
	
	//Capitalizes first letter after each hyphen
	private String capitalizeFirstLetterWithHyphen(String title) {
		String[] words = title.split("-");
		StringBuilder out = new StringBuilder();
		for(int i = 0; i < words.length; i++) {
			out.append(words[i].substring(0, 1).toUpperCase());
			if(words[i].length() > 1) out.append(words[i].substring(1));
			if(i != words.length-1) out.append("-");
		}
		return out.toString();
	}
	
	//Replaces spaces in title with special characters for API URL use
	private String removeSpaces(String title) {
		return title.replaceAll(" ", "%20");
	}
	
	
	
	
	
	
	
	
	
	
}
