package spotlight.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

import javafx.scene.Node;
import spotlight.main.MainApp;
import spotlight.util.MovieButton;
import spotlight.util.Setting;
import spotlight.util.Sys;
import spotlight.view.MovieViewController;

public class LocalData {

	public static void save(){
		MovieButton tmpButton;
		Movie mv;
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(Setting.LOCALDATA_FILE_PATH));
			for(Node n:MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tmpButton=(MovieButton) n;
				if((mv=tmpButton.getMovie())!=null){
					out.writeObject(mv);
				}
			}
			MainApp.needSave=false;
			out.close();
		} catch(IOException i) {
			i.printStackTrace();
		}
	}

	public static ArrayList<Movie> load(){
		ArrayList<Movie> arrMovie=new ArrayList<Movie>();
		Movie tmp;
		ObjectInputStream in = null;
		if((new File(Setting.LOCALDATA_FILE_PATH)).exists()){
			try{
				in = new ObjectInputStream(new FileInputStream(Setting.LOCALDATA_FILE_PATH));
				while((tmp=(Movie)in.readObject())!=null){
					arrMovie.add(tmp);
				}
			}
			catch(IOException | ClassNotFoundException e) {
				try {
					in.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				MainApp.needSave=false;
				return arrMovie;
			}
		}
		return arrMovie;
	}
	
	
	
	public static void saveSettings(){
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter(new FileWriter(Setting.SETTING_FILE_PATH));
			out.write("HOME=" + Setting.HOME);
			out.newLine();
			out.write("LANGUAGE=" + Setting.LANGUAGE);
			out.newLine();
			out.write("DEEP=" + Setting.DEEP.toString());
			out.newLine();
			out.write("RECURSIVE=" + Setting.RECURSIVE.toString());
			out.newLine();
			out.write("BUTTON_HIGH=" + Setting.BUTTON_HIGH.toString());
			out.newLine();
			if (out != null) out.close();
		} catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	public static void loadSettings(){
		if((new File(Setting.SETTING_FILE_PATH).exists())){
			BufferedReader in = null;
			String tmpString;
			
			try {
				in = new BufferedReader(new FileReader(Setting.SETTING_FILE_PATH));
				tmpString = in.readLine();
//				Setting.HOME = tmpString.substring(tmpString.indexOf('=')+1);	il comando è commentato perchè non è possibile modificare la HOME per ora
				tmpString = in.readLine();
				Setting.LANGUAGE = tmpString.substring(tmpString.indexOf('=')+1);
				tmpString = in.readLine();
				Setting.DEEP = Integer.parseInt(tmpString.substring(tmpString.indexOf('=')+1));
				tmpString = in.readLine();
				if(tmpString.contains("false"))
					Setting.RECURSIVE = false;
				else
					Setting.RECURSIVE = true;
				tmpString = in.readLine();
				Setting.BUTTON_HIGH= Double.parseDouble(tmpString.substring(tmpString.indexOf('=')+1));
			} catch(IOException ex01) {
				ex01.printStackTrace();
			}
		}
	}
	
	/**
	 * Serializes an array of file which are the movie's paths
	 * @param v	The array of file
	 */
	public static void savePath() {
		BufferedWriter out = null;
		String linePath;
		try {
			out = new BufferedWriter(new FileWriter(Setting.PATH_FILE_PATH));
			for (File f : MainApp.moviePath) {
				linePath=f.getAbsolutePath();
				out.write(linePath);
				out.newLine();
			}
			if (out != null) out.close();
		} catch(IOException i) {
			i.printStackTrace();
		}
	}

	/**
	 * De-serializes an array of file which are the movie's paths
	 * @return	The array of file
	 */
	public static void loadPath() {
		if((new File(Setting.PATH_FILE_PATH).exists())){
			BufferedReader in = null;
			String tmpString;
			try {
				in = new BufferedReader(new FileReader(Setting.PATH_FILE_PATH));

				while ((tmpString = in.readLine()) != null) {
	                MainApp.moviePath.add(new File(tmpString));
	            }
			} catch(IOException ex01) {
				ex01.printStackTrace();
			}
		}
	}
	
	public static void clearBackup(){
		File f = new File(Setting.BACKUP_FILE_PATH);
		f.delete();
	}
	
	public static void addMovieBackup(ArrayList<String> bList){
		try(FileWriter fw = new FileWriter(Setting.BACKUP_FILE_PATH, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			    for(String s: bList){
			    	out.println(s);
			    }
			} catch (IOException e) {
			    //exception handling left as an exercise for the reader
			}
	}
	
	public static ArrayList<String> recovrBackup(){
		ArrayList<String> backup = new ArrayList<String>();
		if((new File(Setting.BACKUP_FILE_PATH).exists())){
			BufferedReader in = null;
			String tmpString;
			try {
				in = new BufferedReader(new FileReader(Setting.BACKUP_FILE_PATH));

				while ((tmpString = in.readLine()) != null) {
	                backup.add(tmpString);
	            }
				
			} catch(IOException ex01) {
				ex01.printStackTrace();
			}
		}
		return backup;
	}
	
//	public static void main(String args[]){
//		saveSettings();
//	}
	
}
