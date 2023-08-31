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
	
	
}