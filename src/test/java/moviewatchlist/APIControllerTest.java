package moviewatchlist;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class APIControllerTest{
	
	@Test
	public void initCredentials(){
		assertEquals(
				new APIController().initCredentials(), 
				new APICredentials("TESTKEY", "host.test.com", "https://TESTURL.com/TEST/"));
	}
	
	@Test
	public void formatTitleFixesCapitlization1() {
		assertEquals("Sharknado", new APIController().formatTitle("sHaRKnaDO"));
	}
	
	@Test
	public void formatTitleFixesCapitlization2() {
		assertEquals("Scarface", new APIController().formatTitle("ScarFACE"));
	}
	
	@Test
	public void formatTitleFixesCapitlization3() {
		assertEquals("Inception", new APIController().formatTitle("inCePtIOn"));
	}
	
	@Test
	public void formatTitleFixesCapitlizationWithHyphens1() {
		assertEquals("Spider-Man", new APIController().formatTitle("spider-man"));
	}
	
	@Test
	public void formatTitleFixesCapitlizationWithHyphens2() {
		assertEquals("The-Great-Pyramids-Of-Giza", new APIController().formatTitle("THe-Great-pyRAMids-of-gIZA"));
	}
	
	@Test
	public void formatTitleRemovesSpaces1() {
		assertEquals("Last%20Seen%20Alive", new APIController().formatTitle("Last Seen Alive"));
	}
	
	@Test
	public void formatTitleRemovesSpaces2() {
		assertEquals("12%20Strong", new APIController().formatTitle("12 Strong"));
	}
	
	@Test
	public void formatTitleRemovesSpaces3() {
		assertEquals("The%20Adventures%20Of%20Buckaroo%20Banzai%20Across%20The%208th%20Dimension", new APIController()
				.formatTitle("The Adventures Of Buckaroo Banzai Across The 8th Dimension"));
	}
	
	
	
}