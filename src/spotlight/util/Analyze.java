/**
 * 
 */
package spotlight.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import spotlight.model.NameFileFinder;
import spotlight.model.RawMovie;

/**
 * @author vitto
 *
 */
public class Analyze {
	
	/**
	 * 
	 */
	
	public static RawMovie Filename(String filename){
		RawMovie rawMovie = new RawMovie(filename);
		
		rawMovie.setDataList(Year(filename));
		rawMovie.setTitleList(Title(filename,rawMovie.getDataList()));
		
		return rawMovie;
	}
	
	private static ArrayList<String> Title(String filename, ArrayList<Integer> yearList) {
		/*lista che verrrà riempita con tutti i pezzetti che comporranno il titolo*/
		ArrayList<String> titleList = new ArrayList<String>();
		/*lista contenente tutte le parti "inutili" del titolo che vanno eliminate*/
		ArrayList<String> junkList = new JunkTextTitle().getList();
		/*lista temporanea per eliminare gli elementi di una ArrayList (secondo me si può fare in qualche modo più efficiente)*/
		ArrayList<String> tempList = new ArrayList<String>();
		
		/*Essendo un direttorio assoluto vado a prendere l'ultima parte del path fino all'ultima occorrenza del file separetor*/
		filename = filename.substring(filename.lastIndexOf(Sys.file_separator)+1);
		
		/*elimino l'estensione del file*/
		filename = filename.substring(0, filename.lastIndexOf('.'));
		
		/*sostituisce i punti, i trattini e gli underscore con degli spazi*/
		filename = filename.replace('.', ' ');
		filename = filename.replace('-', ' ');
		filename = filename.replace('_', ' ');
		filename = filename.replace(',', ' ');
		
		/*trasforma tutto in minuscolo*/
		filename = filename.toLowerCase();
		
		/*Se c'è qualcosa tra le parentesi tonde viene rimosso*/
		if(filename.contains("[") && filename.contains("]") && filename.indexOf("[") < filename.indexOf("]")){
			filename = filename.substring(0, filename.indexOf("[")) + filename.substring(filename.indexOf("]") + 1, filename.length());
		}
		
		/*Se c'è qualcosa tra le parentesi tonde viene rimosso*/
		if(filename.contains("(") && filename.contains(")") && filename.indexOf("(") < filename.indexOf(")")){
			filename = filename.substring(0, filename.indexOf("(")) + filename.substring(filename.indexOf(")") + 1, filename.length());
		}
		
		/*for utilizzato per rimuovere i doppi spazi, secondo me è inutile.
		 * è ottimizzabile facendo un controllo di tutti gli indici pari e controllare se sono uguali ad ' ' e poi in caso
		 * controllare i caratteri adiacenti
		 */
		/*for(int i = 0; i < filename.length(); i++){
			if(i != filename.length()-1 && filename.charAt(i) == ' ' && filename.charAt(i+1) == ' '){
				filename = filename.substring(0, i) + filename.substring(i+1);
			}
		}*/
		
		/*Divido il filename in sottostringhe utilizzando il carattere ' ' come separatore e trasformo tutto in un ArrayList */
		String[] items = filename.split(" ");
		Collections.addAll(titleList, items);
		
		/*creo l'iteratore per poter modificare l'ArrayList*/
//		Iterator<String> it = junkList.iterator();
		
		for(String jText: junkList){
			if(titleList.contains(jText)){
				/*nel caso in cui sia presente svuoto un ArrayList temporanea*/
				tempList.clear();
				/*aggiungo all'ArrayList temporanea il sotto array formato da tutti gli elementi fino all'elemento trovato
				 * scartando tutti gli elementi successivi*/
				tempList.addAll(titleList.subList(0, titleList.indexOf(jText)));
				/*svuoto titleList*/
				titleList.clear();
				/*ripopolo titleList con la sottolista di prima*/
				titleList.addAll(tempList);
			}
		}
		
//		/*per ogni elemento dell'ArrayList junkList controllo se è presente nell'ArrayList titleList*/
//		while(it.hasNext()){
//			String jText = (String) it.next();
//			if(titleList.contains(jText)){
//				/*nel caso in cui sia presente svuoto un ArrayList temporanea*/
//				tempList.clear();
//				/*aggiungo all'ArrayList temporanea il sotto array formato da tutti gli elementi fino all'elemento trovato
//				 * scartando tutti gli elementi successivi*/
//				tempList.addAll(titleList.subList(0, titleList.indexOf(jText)));
//				/*svuoto titleList*/
//				titleList.clear();
//				/*ripopolo titleList con la sottolista di prima*/
//				titleList.addAll(tempList);
//			}
//		}
		
		/*nel caso ci sia un solo anno e titleList contiene un elemento con scitto quell'anno viene eliminato dalla lista*/
		if(yearList.size() == 1 && titleList.contains(yearList.get(0).toString()))
			titleList.remove(titleList.indexOf(yearList.get(0).toString()));
		
		while(titleList.contains(""))
			titleList.remove("");
		
		return titleList;
	}

	private static ArrayList<Integer> Year(String filename){
		/*creo una lista dove vado ad inserire tutte le possibili date trovate*/
		ArrayList<Integer> yearList = new ArrayList<Integer>();
		/*espressione regolare che trova tutti i numeri formati da 4 cifre*/
		String regexData = "[0-9]{4}";
		String matched;
		
		/*non ho la più pallida idea di cosa sia*/
		Pattern pattern = Pattern.compile(regexData);
		Matcher matcher = pattern.matcher(filename);
		
		/*crea una specie di iteratore dove viene eseguito questo ciclo per ogni occorrenza di un numero di 4 cifre nel nome*/
		while (matcher.find()){
			/*l'occorrenza trovata viene utilizzata tramite la funzione matcher.group() che poi viene assegnata alla String matched*/
			matched = matcher.group();
			/*parso la data in un numero*/
			Integer dataInt = Integer.parseInt(matched);
			/*controllo che il numero sia compreso tra la data del primo film mai realizzato fino all'anno corrente*/
			if( dataInt != null && dataInt >= 1891 && dataInt <= new GregorianCalendar().get(Calendar.YEAR)){
				/*se rispetta i requisiti viene aggiunto alla lista*/
				yearList.add(dataInt);
			}
		}
		
		/*nel caso vengono trovati 2 o più date possibili avverte di un possibile errore*/
		if(yearList.size() > 1){
			System.out.println("POSSIBILE ERRORE GENERATO\t" + filename);
			for(Integer i: yearList){
				System.out.println(i);
			}
		}
		
		return yearList;
	}
	
//	public static void main(String[] args) {
//		NameFileFinder dl = new NameFileFinder();
//		dl.listDirectory(new File("C://Users/vitto/Desktop/Film"), true);
//		for(String i: dl.getFilmList()){
//			System.out.println(Analyze.Filename(i));
//		}
//	}

}
