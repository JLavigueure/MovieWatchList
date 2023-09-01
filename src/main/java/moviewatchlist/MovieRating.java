package moviewatchlist;

public class MovieRating {
	private double rating;
	private int votes;
	
	public MovieRating(double rating, int votes) {
		this.rating = rating;
		this.votes = votes;
	}
	
	public MovieRating() {
		this(0,0);
	}
	
	public double getRating() {
		return rating;
	}
	
	public int getVotes() {
		return votes;
	}
	
	public void setRating(double rate) {
		this.rating = rate;
	}
	
	public void setVotes(int qty) {
		votes = qty;
	}
	
	
}
