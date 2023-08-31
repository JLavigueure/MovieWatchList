package moviewatchlist;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class MovieTest {
	
	@Test
	public void equalsNull() {
		Movie m = new Movie("TT123", "Superman", 2002);
		assertFalse(m.equals(null));
	}
	@Test
	public void equalsDifferentClass() {
		Movie m = new Movie("TT123", "Superman", 2002);
		assertFalse(m.equals(""));
	}
	@Test
	public void equalsDifferentId() {
		Movie a = new Movie("TT123", "Superman", 2002);
		Movie b = new Movie("TT124", "Superman", 2002);
		assertFalse(a.equals(b));
	}
	@Test
	public void equalsDifferentTitle() {
		Movie a = new Movie("TT123", "Superman", 2002);
		Movie b = new Movie("TT123", "Batman", 2002);
		assertFalse(a.equals(b));
	}
	@Test
	public void equalsDifferentYear() {
		Movie a = new Movie("TT123", "Superman", 2002);
		Movie b = new Movie("TT123", "Superman", 2001);
		assertFalse(a.equals(b));
		assertFalse(b.equals(a));
	}
	@Test
	public void equalsSelf() {
		Movie a = new Movie("TT123", "Superman", 2002);
		assertTrue(a.equals(a));
	}
	@Test
	public void equalsIdenticalMovie() {
		Movie a = new Movie("TT123", "Superman", 2002);
		Movie b = new Movie("TT123", "Superman", 2002);
		assertTrue(a.equals(b));
	}
}
