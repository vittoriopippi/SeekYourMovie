package spotlight.util;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class Check {

	/**Test della connessione fatta provando a connettersi al dominio "http://www.google.com"
	 * @return true nel caso la connessione è avventuna con successo, false in caso contrario
	 */
	public static Boolean Connection() {
		try {
			URL url = new URL("http://www.google.com");
			URLConnection conn = url.openConnection();
			conn.connect();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
