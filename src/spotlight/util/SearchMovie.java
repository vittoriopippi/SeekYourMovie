package spotlight.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;

import spotlight.exceptions.ExceptionDialog;
import spotlight.model.Movie;
import spotlight.model.RawMovie;

public class SearchMovie {

	public static Movie TMDb(RawMovie rawMovie) {
		Movie movie = null;
		if(rawMovie.getDataList().size() == 0)
			rawMovie.getDataList().add(null);
		//		System.out.println(rawMovie.getDataList().size() + " anni trovati nel titolo: " + rawMovie.getDataList());
		//		System.out.println("Inizio ricerca del film: " + rawMovie.getRawTitle());

		try {
			movie = new Movie(ResearchMovie(rawMovie,null), rawMovie.getRawTitle());
		} catch (ResourceException | IOException | ParseException e) {
			e.printStackTrace();
		}
		return movie;
	}

	/** Funzione che accetta una lista che viene prima trasformata in una singola stringa,
	 * poi costruito il link per accedere al server rest, parsato l'oggetto ritornato,
	 * riempito un JSONArray(Array di JSONObject) che viene infine ritornato
	 * @param subList
	 * @param year
	 * @return JSONArray
	 * @throws ResourceException
	 * @throws IOException
	 * @throws ParseException
	 */
	private static JSONArray ListToArray(List<String> subList, Integer year) throws ResourceException, IOException, ParseException{
		String tempString;
		ArrayList<String> tempList = new ArrayList<String>();
		/*aggiungo in una lista temporanea tutti gli elementi di una sottolista
		 * che comprende gli elementi che vanno dall'indice i all'indice i+dim estremi inclusi*/
		tempList.addAll(subList);

		//		System.out.println(tempList.size() + "\tLista ottenuta:\t\t" + tempList);

		/*Unisce le stringhe della tempList formando una stringa unica separata da spazi*/
		tempString = UnionString(tempList);
		//		System.out.println("Cerco:\t" + tempString);

		/*costruisco il link*/
		String link = BuildLink.SearchMovie(year,tempString);
		//		System.out.println("Link:\t" + link);
		if(link == null)
			return null;

		String response = ToClient.Response(link);

		if(response != null){
			JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(response);
			//		JSONObject jobj = RestRequest.getObject(link);
			JSONArray filmJSONArray = new JSONArray();
			filmJSONArray.addAll((Collection) jobj.get("results"));
			return filmJSONArray;
		}
		else {
			return null;
		}
	}

	/**Funzione che ritorna l'unione di tutti gli elementi della lista passata spearati da spazi
	 * @param list
	 * @return String
	 */
	private static String UnionString(ArrayList<String> list){
		String temp = "";
		for(int t = 0; t< list.size(); t++){
			temp = temp.concat(list.get(t));
			if(t != list.size()-1)
				temp = temp.concat(" ");
		}
		return temp;
	}

	/**Ricerca ricorsiva delle possibili combinazioni del titolo 
	 * @param rawMovie
	 * @param dim
	 * @param minJArray
	 * @return JSONObject
	 * @throws ResourceException
	 * @throws IOException
	 * @throws ParseException
	 */
	private static Integer ResearchMovie(RawMovie rawMovie, Integer dim) throws ResourceException, IOException, ParseException {

		/*al primo lancio di questa funzione dim == null e minJArray == null*/
		if(dim == null)
			dim = rawMovie.getTitleList().size();
		if(dim < 0)
			return null;
		/*nel caso in cui la dimensione dei segmenti è 0 e ci sono elementi nel minJArray ritorna il primo elemento dell'array*/
		if((Setting.DEEP != 0 && dim <= rawMovie.getTitleList().size() - Setting.DEEP) || dim <= 0){
			return null;
		}

		Integer size = rawMovie.getTitleList().size();

		for(Integer year: rawMovie.getDataList()){
			for(int i = 0; i < size; i++){
				if((i + dim) > size)	//controllo che la sottolista che andrò a creare sia contenuta nella lista originaria
					break;

				JSONArray filmJSONArray = ListToArray(rawMovie.getTitleList().subList(i, i+dim),year);

				if(filmJSONArray == null)
					return null;

				if(filmJSONArray.size() >= 1){
					/*Nel caso è stato trovato un colo risultato oppure nel caso in cui sono tati trovai
					 * più di un risultato ma è la prima ricerca che si fa, questo perchè se alla prima ricerca
					 * si trovano molteplici risultati significa che levando elementi alla sottolista usata per la
					 * ricerca non si fa altro che rimuovere vincoli e quindi tanto vale ritornare il primo risultato*/
					JSONObject jtemp = (JSONObject) filmJSONArray.get(0);
					Integer itemp = Integer.parseInt(jtemp.get("id").toString());
					return itemp;
				}
			}
		}
		return ResearchMovie(rawMovie, dim - 1);
	}

	public static ArrayList<Integer> ArrayTMDb(String title, Integer data){
		ArrayList<Integer> iArray = new ArrayList<Integer>();
		JSONArray filmJSONArray = new JSONArray();

		try {
			String link1 = BuildLink.SearchMovie(data, title);
			String response = ToClient.Response(link1);
			JSONParser parser = new JSONParser();
			JSONObject jobj = (JSONObject) parser.parse(response);

			filmJSONArray.addAll((Collection) jobj.get("results"));

			if(data != null){
				String link2 = BuildLink.SearchMovie(null, title);
				response = ToClient.Response(link2);
				jobj = (JSONObject) parser.parse(response);

				filmJSONArray.addAll((Collection) jobj.get("results"));
			}

			Integer temp = null;
			JSONObject jtemp = null;
			for(Object o: filmJSONArray){
				jtemp = (JSONObject) o;
				if(jtemp.get("id") != null){
					temp = Integer.parseInt(jtemp.get("id").toString());
					iArray.add(temp);
				}
			}
		} catch (ResourceException | ParseException e) {
			ExceptionDialog.show(e);
			e.printStackTrace();
			return null;
		}
		return iArray;
	}

	//	public static void main(String[] args) {
	//		NameFileFinder dl = new NameFileFinder();
	//		dl.listDirectory(new File("C://Users/vitto/Desktop/Film/Film/hard"), false);
	//		long startTime, elapsedTime;
	//
	//		startTime = System.nanoTime();
	//		for(String i: dl.getFilmList()){
	//			System.out.println("##################################################");
	//			System.out.println(Analyze.Filename(i).getTitleList().size() + "\tTitolo analizzato:\t" + Analyze.Filename(i).getTitleList());
	//			System.out.println("\n" + SearchMovie.TMDb(Analyze.Filename(i)));
	//		}
	//		elapsedTime = System.nanoTime() - startTime;
	//		System.out.println("\nCercati " + dl.getFilmList().size() + " film in " + elapsedTime/1000000 + " ms con una media di " + elapsedTime/(1000000*dl.getFilmList().size()) + " ms ad elemento");
	//	}
}
