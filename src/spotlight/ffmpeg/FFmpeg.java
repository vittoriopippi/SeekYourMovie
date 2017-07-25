package spotlight.ffmpeg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import spotlight.exceptions.ExceptionDialog;
import spotlight.util.Sys;

public class FFmpeg {

	/**
	 * Funzione che a seconda del sistema operativo utilizza ffprobe per analizzare
	 * le informazioni di un file video e le riporta in JSON
	 * 
	 * @param moviePath
	 *            path assoluto del file video da analizzare
	 * @return il JSONObject ritornato dalla richiesta, oppure null nel caso in cui
	 *         non si è riusciti ad utilizzare ffprobe
	 */
	public static JSONObject getData(String moviePath) {
		Process p = null;
		String line = null;
		JSONObject jobj = null;
		// Utilizzando uno StringBuilder è possibile fare l'append di una stringa più
		// velocemente
		StringBuilder response = new StringBuilder();
		try {
			if (Sys.os_name.toLowerCase().contains("mac"))
				p = new ProcessBuilder("/usr/local/Cellar/ffmpeg/3.3.2/bin/ffprobe", "-v", "quiet", "-print_format",
						"json", "-show_format", "-show_streams", moviePath).start();
			else
				p = new ProcessBuilder("ffprobe", "-v", "quiet", "-print_format", "json", "-show_format",
						"-show_streams", moviePath).start();

			if (p == null || p.getInputStream() == null)
				return null;

			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));

			while ((line = input.readLine()) != null) {
				response.append(line);
			}
			input.close();

			JSONParser parser = new JSONParser();
			jobj = (JSONObject) parser.parse(response.toString());

		} catch (IOException e) {
			return null;
		} catch (ParseException e) {
			ExceptionDialog.show(e, "Errore durante il \"parsing\" del JSONObject ritornato da l'exec");
		}
		return jobj;
	}
}
