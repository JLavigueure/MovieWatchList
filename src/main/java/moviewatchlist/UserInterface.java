package moviewatchlist;

import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {
	private Scanner scan;
	private MovieList movies;
	private APIController api;
	
	public UserInterface(Scanner s, MovieList list) {
		scan = s;
		movies = list;
		api = new APIController();
	}
	
	public void start() {
		try {
			printIntro();
			checkPreviousData();
			String in = "";
			while(true) {
				printCmds();
				in = scan.nextLine();
				cmdRouter(in);
		}
		
		}catch(Exception e) {
			System.out.println("Fatal error\n" + e.getMessage() + "\n" + e.getStackTrace() + "\n" + e.getLocalizedMessage());
			System.exit(1);
		}
	}
	
	//prints intro message
	public void printIntro() {
		System.out.println("Welcome to MovieWatchList, your rainy day itinerary\n");
	}
	
	//checks if previous data exists, and asks user if they would like to load if exists
	private void checkPreviousData() throws Exception {
		if(movies.fileIsEmpty()) return;
		System.out.println("We found some movies in your history, would you like to load these?"
				+ "\nNote: Starting a new list will overwrite this data");
		System.out.print("Load Data? [Yes][No]");
		String in = formatInput(scan.nextLine());
		while(!in.toLowerCase().equals("yes") && !in.toLowerCase().equals("no")) {
			System.out.println("Command not recognized");
			System.out.print("Load Data? [Yes][No]");
			in = formatInput(scan.nextLine());
		}
		if(in.equals("yes")) {
			movies.loadFromFile();
			System.out.println("\nData successfully loaded.");
		}
	}
	
	//prints basic commands
	private void printCmds() {
		StringBuilder cmds = new StringBuilder();
		cmds.append("\nEnter a number:\n");
		cmds.append("[1] Print list\n");
		cmds.append("[2] Search list\n");
		cmds.append("[3] Get movie info\n");
		cmds.append("[4] Add Movie\n");
		cmds.append("[5] Save and exit\n");
		System.out.println(cmds);
	}
	
	//takes user input and processes accordingly
	private void cmdRouter(String input) throws Exception{
		if(input.equals("")) return;
		if(!input.matches("[1-5]")) {
			System.out.println("Command not recognized.");
			return;
		}
		switch(Integer.valueOf(input)){
			case 1:
				printMovies();
				break;
			case 2:
				System.out.println("[1] Search by keyword \n[2] Search by year \n[3] Search by genre");
				String in = scan.nextLine();
				if(!in.matches("[1-3]")) {
					System.out.println("Command not recognized");
					break;
				}
				switch(Integer.valueOf(in)){
					case 1: 
						searchByKeyword();
						break;
					case 2:
						searchByYear();
						break;	
					case 3:
						searchByGenre();
						break;
				}
				break;
			case 3: 
				getMovieInfo();
				break;
			case 4:
				query();
				break;
			case 5:
				saveAndExit();	
		}
	}
	
	//returns given string trimmed and toLowerCase
	private String formatInput(String s) {
		return s.toLowerCase().trim();
	}
	
	//prints current list of movies
	private void printMovies() {
		if(movies.getMovies().isEmpty()) {
			System.out.println("Your list is empty.");
			return;
		}
		for(Movie m:movies.getMovies()) {
			System.out.println(m);
		}
	}
	
	//prints current list of movies with rating data
	private void printMoviesWithRatings() {
		if(movies.getMovies().isEmpty()) {
			System.out.println("Your list is empty.");
			return;
		}
		for(Movie m: movies.getMovies()) {
			System.out.println(m.toStringWithRating());
		}
	}
	
	//searches movie by keyword
	private void searchByKeyword() {
		System.out.println("\nEnter keyword");
		String keyword = scan.nextLine();
		ArrayList<Movie> list = movies.searchByKeyword(keyword);
		if(list.isEmpty()) {
			System.out.println("No results found");
			return;
		}
		for(Movie m : list) {
			System.out.println(m);
		}
	}
	
	private void getMovieInfo() {
		System.out.println("Enter movie title");
		String title = scan.nextLine();
		Movie movie = movies.getMovie(title);
		if(movie == null) {
			System.out.println("Movie not found");
			return;
		}
		System.out.println(movie.getFullInfo());
	}
	
	private void searchByYear() {
		System.out.println("Enter beginning year");
		String start = scan.nextLine();
		while(!start.matches("[1-2][0-9][0-9][0-9]")) {
			System.out.println("Please enter a valid year");
			start = scan.nextLine();
		}
		System.out.println("Enter ending year");
		String end = scan.nextLine();
		while(!end.matches("[1-2][0-9][0-9][0-9]")) {
			System.out.println("Please enter a valid year");
			end = scan.nextLine();
		}
		System.out.println();
		MovieList filtered = movies.filterListByYear(Integer.valueOf(start), Integer.valueOf(end));
		for(Movie m : filtered.getMovies()) 
			System.out.println(m);
		
	}
	
	private void searchByGenre() {
		System.out.println("Enter genre");
		String genre = scan.nextLine();
		System.out.println();
		MovieList filtered = movies.filterListByGenre(genre);
		for(Movie m: filtered.getMovies()) 
			System.out.println(m);
	}
	
	//prompts user for title and searches for movies via api
	private void query() throws Exception{
		System.out.println("Enter title");
		String input = scan.nextLine();
		printProcessing();
		ArrayList<Movie> results = api.searchByTitle(input);
		if(results.isEmpty()) {
			System.out.println("No results found");
			return;
		}

		int index = 1;
		for(Movie m:results) {
			System.out.println("["+index++ + "] " + m);
		}
		System.out.println("\nWhich movie would you like to add? Enter 0 if none.");
		System.out.print("Index: ");
		input = scan.nextLine();
		while(!input.matches("[0-9]+") || Integer.valueOf(input) < 0 || Integer.valueOf(input) > index){
			System.out.println("Index does not exist");
			System.out.println("Index: ");
			input = scan.nextLine();
		}
		if(input.equals("0")) return;
		Movie selectedMovie = results.get(Integer.valueOf(input)-1);
		printProcessing();
		movies.add(api.getFullInfo(selectedMovie));
		System.out.println(selectedMovie + " added to list");
	}
	
	private void printProcessing() {
		System.out.println("\nProcessing...\n");
	}
	
	//saves list and exits program
	private void saveAndExit() throws Exception {
		movies.saveToFile();
		System.out.println("Saved succesfully");
		System.exit(0);
	}
	
}
