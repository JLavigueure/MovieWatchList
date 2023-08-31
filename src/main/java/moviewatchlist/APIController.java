package moviewatchlist;

import java.io.InputStream; 
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
				title +"?exact=false&titleType=movie";
		//build HttpRequest
		HttpRequest request = buildRequest(url);
		//Send request and parse results
		JsonArray results = getResults(request);
		return parseResults(results);
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
	private JsonArray getResults(HttpRequest request) throws Exception{
		HttpResponse<String> response = 
				HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		JsonObject json = new Gson().fromJson(response.body(), JsonObject.class);
		return (JsonArray) json.get("results");
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
			
			//get id
			String id = result.get("id").getAsString();
			//get name
			JsonObject titleText = (JsonObject) result.get("titleText");
			String name = titleText.get("text").getAsString();
			//get year
			int year;
			if(!(result.get("releaseYear") instanceof JsonNull)) {
				JsonObject releaseYear = (JsonObject) result.get("releaseYear");
				year = Integer.valueOf(releaseYear.get("year").getAsString());
			} else {
				year = 0; //no year available
			}
			
			//add movie to list
			movies.add(new Movie(id, name, year));
		}
		return movies;
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
