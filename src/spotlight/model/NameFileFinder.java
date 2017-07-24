package spotlight.model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import spotlight.util.Sys;

public class NameFileFinder {   
	private ArrayList<String> filmList;
	/**
	 * List a directory
	 * @param f			The directory to be explored
	 * @param recursive	True for recursive mode
	 */
	public NameFileFinder() {
		super();
		filmList = new ArrayList<String>();
	}
	
	public void listDirectory(File f, boolean recursive) {
		/*se il file non è una directory esce immediatamente*/
		if (!f.isDirectory()) return;
		File[] items = f.listFiles();

		/*scorre la lista di file e se non è una directory viene tagliata l'estensione ed aggiunto il nome alla lista
		 * altrimenti se è una directory viene rilanciata la funzione in quella directory se recursive è true*/
		
		for (File item : items) {
			if (!item.isDirectory() && (item.getName().endsWith(".3gp") || 
										item.getName().endsWith(".asf") ||
										item.getName().endsWith(".avi") ||
										item.getName().endsWith(".divx") ||
										item.getName().endsWith(".flv") ||
										item.getName().endsWith(".swf") ||
										item.getName().endsWith(".mpeg") ||
										item.getName().endsWith(".ogm") ||
										item.getName().endsWith(".wmv") ||
										item.getName().endsWith(".mp4") ||
										item.getName().endsWith(".mov") ||
										item.getName().endsWith(".mkv") ||
										item.getName().endsWith(".nbr") ||
										item.getName().endsWith(".rm") ||
										item.getName().endsWith(".vob") ||
										item.getName().endsWith(".sfd") ||
										item.getName().endsWith(".webm"))){
				filmList.add(f.getAbsolutePath() + Sys.file_separator + item.getName());
			}
			if (recursive && item.isDirectory()) listDirectory(item, true);
		}
	}
	
	public void findFilenames(File f, boolean recursive) {
		/*se il file non è una directory esce immediatamente*/
		if (!f.isDirectory()) return;
		File[] items = f.listFiles();

		/*scorre la lista di file e se non è una directory viene tagliata l'estensione ed aggiunto il nome alla lista
		 * altrimenti se è una directory viene rilanciata la funzione in quella directory se recursive è true*/
		
		for (File item : items) {
			if (!item.isDirectory()){
				filmList.add(item.getName());
			}
			if (recursive && item.isDirectory()) listDirectory(item, true);
		}
	}

	public ArrayList<String> getFilmList() {
		return filmList;
	}

	public void setFilmList(ArrayList<String> filmList) {
		this.filmList = filmList;
	}

//	public static void main(String[] args) {
//		NameFileFinder dl = new NameFileFinder();
//		dl.listDirectory(new File("F://Venom/MEDIA"), false);
//		for(String i: dl.getFilmList()){
//			System.out.println(i);
//		}
//	}

}
