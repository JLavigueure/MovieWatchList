package moviewatchlist;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class APIController {
	private APICredentials key;
	
	public APIController() {
		key = initCredentials();
	}
	
	
	
	
	
	
	
	//initialize credentials
	public APICredentials initCredentials() {
		try(InputStream in = APICredentials.class.getResourceAsStream("/APIKey.json")){
			String string = new String(in.readAllBytes(), StandardCharsets.UTF_8);
			Gson gson = new Gson();
			return gson.fromJson(string, APICredentials.class);
		}catch(Exception e) {
			System.out.println("Error retrieving API credentials from resources.");
			System.exit(1);
			return null;
		}
	}
	
}
