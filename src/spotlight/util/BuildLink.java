package spotlight.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import spotlight.exceptions.ExceptionDialog;

public class BuildLink {
	
	/**Costruisce il link basandosi sui dati forniti 
	 * @param year
	 * @param filmName
	 * @param language
	 * @return String
	 */
	public static String SearchMovie(Integer year,String filmName){
		if(filmName == null)
			return null;
		String link = null;
		if(year == null){
			try {
				link = "https://api.themoviedb.org/3/search/movie?&include_adult=true&page=1&query="
						+ URLEncoder.encode(filmName, "UTF-8")
						+ "&language="
						+ Setting.LANGUAGE
						+ "&api_key="
						+ Setting.API_KEY_ARRAY.get(0);
			} catch (UnsupportedEncodingException e) {
				ExceptionDialog.show(e);
				e.printStackTrace();
				return null;
			} 
		} else {
			try {
				link = "https://api.themoviedb.org/3/search/movie?year="
						+ year.toString()
						+ "&include_adult=true&page=1&query="
						+ URLEncoder.encode(filmName, "UTF-8")
						+ "&language="
						+ Setting.LANGUAGE	
						+ "&api_key="
						+ Setting.API_KEY_ARRAY.get(0);
			} catch (UnsupportedEncodingException e) {
				ExceptionDialog.show(e);
				e.printStackTrace();
				return null;
			}
		}
		return link;
	}
	
	/**Genera il link per trovare i dettagli di un film
	 * @param id
	 * @param language
	 * @return String
	 */
	public static String GetDetails(Integer id){
		if(id == null)
			return null;
		String link = null;
		link = "https://api.themoviedb.org/3/movie/"
				+ id.toString()
				+ "?api_key="
				+ Setting.API_KEY_ARRAY.get(0)
				+ "&language="
				+ Setting.LANGUAGE;
		return link;
	}
}
