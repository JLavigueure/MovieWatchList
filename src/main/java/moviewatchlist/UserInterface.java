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
		System.out.println("\nWelcome to MovieWatchList, your rainy day itinerary\n");
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
		cmds.append("[5] Remove movie\n");
		cmds.append("[6] Save and exit\n");
		System.out.println(cmds);
	}
	
	//takes user input and processes accordingly
	private void cmdRouter(String input) throws Exception{
		if(input.equals("")) return;
		if(!input.matches("[1-6]")) {
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
				removeMovie();
				break;	
			case 6:
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
		System.out.println("\nPress enter to return to main menu. Enter 1 to sort list");
		String in = scan.nextLine();
		while(!in.matches("1?")) {
			System.out.println("Command not recognized");
			return;
		}
		if(in.equals("")) return;
		System.out.println("[1] Sort by title\n" 
				+ "[2] Sort by year\n"
				+ "[3] Sort by rating\n"
				+ "[4] Sort by runtime");
		
		in = scan.nextLine();
		while(!in.matches("[1-4]")) {
			System.out.println("Command not recognized, please try again");
			in = scan.next();
		}
		switch(Integer.valueOf(in)) {
		case 0:
			return;
		case 1:
			movies.sort();
			break;
		case 2:
			movies.sortByYear();
			break;
		case 3:
			movies.sortByRating();
			System.out.println("Highest rated to lowest rated\n------------------------------");
			break;
		case 4:
			movies.sortByRuntime();
			System.out.println("Longest to shortest\n-------------------");
			break;
		}
		printMovies();
	}
	
	//searches list by keyword
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
	//searches list year
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
	//search list by genre
	private void searchByGenre() {
		System.out.println("Enter genre");
		String genre = scan.nextLine();
		System.out.println();
		MovieList filtered = movies.filterListByGenre(genre);
		for(Movie m: filtered.getMovies()) 
			System.out.println(m);
	}
	
	//get full movie details
	private void getMovieInfo() {
		//print options
		movies.sort();
		for(Movie m: movies.getMovies()) {
			System.out.println(m);
		}
		
		//get title
		System.out.println("\nEnter movie title");
		String title = scan.nextLine();
		Movie movie = movies.getMovie(title);
		//if match found, print result
		if(movie != null) {
			System.out.println(movie.getFullInfo());
			return;
		}
		//if no match found, search by keyword
		ArrayList<Movie> list = movies.searchByKeyword(title);
		//if no keyword matches found, return
		if(list.isEmpty()) {
			System.out.println(title + " not found");
			return;
		}
		//print list found, and ask for selection
		int index = 1;
		for(Movie m : list) {
			System.out.println("[" + index++ + "] " + m);
		}
		System.out.println("Select a movie. Enter 0 to return.");
		String input = scan.nextLine();
		while(!input.matches("[0-9]+") || Integer.valueOf(input) >= index || Integer.valueOf(input) < 0) {
			System.out.println("Please enter the number that cooresponds to the movie you would like details for. Enter 0 to return.");
			input = scan.nextLine();
		}
		//print details of selected movie or return if 0 is entered
		if(input.equals("0")) return;
		System.out.println(list.get(Integer.valueOf(input)-1).getFullInfo());
	}
	
	//prompts user for title and searches for movies via api, then adds selected movie
	private void query() throws Exception{
		System.out.println("Enter title");
		String title = scan.nextLine();
		printProcessing();
		ArrayList<Movie> results = api.searchByTitle(title);
		if(results.isEmpty()) {
			System.out.println("No results found");
			return;
		}

		int index = 1;
		for(Movie m:results) {
			System.out.println("["+index++ + "] " + m);
		}
		System.out.println("\nWhich movie would you like to add? Enter 0 if none. Enter -1 to filter by year.");

		int input = getIndex(index, true);
		if(input == 0) return;
		if(input == -1) {
			queryByYear(title);
			return;
		}
		Movie selectedMovie = results.get(input-1);
		printProcessing();
		movies.add(api.getFullInfo(selectedMovie));
		System.out.println(selectedMovie + " added to list");
	}
	
	private void queryByYear(String title) throws Exception {
		System.out.println("What year was the movie released?");
		String input = scan.nextLine();
		while(!input.matches("[1-2][0-9][0-9][0-9]") && !input.equals("exit")) {
			System.out.println("Please enter a valid year or exit to return");
			input = scan.nextLine();
		}
		if(input.equals("exit")) return;
		printProcessing();
		ArrayList<Movie> results = api.searchByTitleAndYear(title, Integer.valueOf(input));
		if(results.isEmpty()) {
			System.out.println("No results found");
		}
		int index = 1;
		for(Movie m : results) {
			System.out.println("["+index+"] " + m);
			index++;
		}
		System.out.println("\nWhich movie would you like to add? Enter 0 if none.");
		int selected = getIndex(index, false);
		if(selected == 0) return;
		Movie selectedMovie = results.get(selected-1);
		printProcessing();
		movies.add(api.getFullInfo(selectedMovie));
		System.out.println(selectedMovie + " added to list");
		
	}
	
	//prompts user for title and removes given title
	private void removeMovie() {
		//get title
		System.out.println("Enter title to remove");
		String in = scan.nextLine();
		//if title match found, remove
		if(movies.remove(in)) {
			System.out.println(in + " succesfully removed");
			return;
		}
		//if no exact match, get list by searching by keyword
		ArrayList<Movie> results = movies.searchByKeyword(in);
		//if no results return
		if(results.isEmpty()) {
			System.out.println(in + " not found");
			return;
		}
		//print results
		int index = 1;
		for(Movie m: results) {
			System.out.println("["+ index++ + "] " + m);
		}
		//get user selection
		System.out.println("Select a movie. Enter 0 to return.");
		String input = scan.nextLine();
		while(!input.matches("[0-9]+") || Integer.valueOf(input) >= index || Integer.valueOf(input) < 0) {
			System.out.println("Please enter the number that cooresponds to the movie you would like details for. Enter 0 to return.");
			input = scan.nextLine();
		}
		//remove movie or return if input is 0
		if(input.equals("0")) return;
		Movie toRemove = results.get(Integer.valueOf(input)-1);
		movies.remove(toRemove);
		System.out.println(toRemove + " removed");
	}
	
	//prints processing statement
	private void printProcessing() {
		System.out.println("\nProcessing...\n");
	}
	
	//saves list and exits program
	private void saveAndExit() throws Exception {
		movies.sort();
		movies.saveToFile();
		System.out.println("Saved succesfully");
		System.exit(0);
	}
	
	//takes a max index(qty) and asks user to select a index between 0 to qty. If allowNegative is true, allows -1 as valid return
	private int getIndex(int qty, Boolean allowNegative) {
		System.out.print("Index: ");
		String input = scan.nextLine();
		if(allowNegative) {
			while(!input.matches("-*[0-9]+") || Integer.valueOf(input) < -1 || Integer.valueOf(input) > qty){
				System.out.println("Index does not exist");
				System.out.println("Index: ");
				input = scan.nextLine();
			}
		}else {
			while(!input.matches("[0-9]+") || Integer.valueOf(input) < 0 || Integer.valueOf(input) > qty){
				System.out.println("Index does not exist");
				System.out.println("Index: ");
				input = scan.nextLine();
			}
		}
		return Integer.valueOf(input);
	}
	
}
