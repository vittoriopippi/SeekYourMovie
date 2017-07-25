package spotlight.util;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Setting {
	public static NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
	public static ArrayList<String> API_KEY_ARRAY = new ArrayList<String>();
	public final static String API_KEY_1 = "7ee68f4670f4cced5ab09d5c609a0b88";	//Roberto
	public final static String API_KEY_2 = "97c4ea62b6282fed208ed2bcc5601f87";	//Vittorio
	public final static String API_KEY_3 = "e7a22bbc3ac65e46c9c24f00d9686f0c";	//Francesca
	public static String LANGUAGE = "it-IT";
	public static Integer DEEP = 3;
	public static Boolean RECURSIVE = false;
	public static Double BUTTON_HIGH = 225.0;
	public static String QUALITY = "w300";
	public static DecimalFormat df = new DecimalFormat("#.##");
	public static ArrayList<MovieButton> TEMP_ARRAY= new ArrayList<MovieButton>();

	public final static String BLACK_STYLE_BACKGROUND = "-fx-background-color: black;";
	public final static String ORANGE_STYLE_SHADOW = "-fx-effect: dropshadow( gaussian , orange , 10 , 0.3 , 0 , 0 );";

	public final static String POSTER_DEFAULT="file:resources/defaultImages/splashImage.jpg";
	public final static String CSS_DEFAULT="file:resources/tryme.css";

	public final static String HOME = Sys.user_home + Sys.file_separator + "Spotlight";

	public final static String POSTER_PATH = HOME + Sys.file_separator + "Poster";
	public final static String SETTING_PATH = HOME + Sys.file_separator + "Setting";
	public final static String DATA_PATH= HOME + Sys.file_separator + "Data";

	public final static String BACKUP_FILE_PATH = Setting.DATA_PATH + Sys.file_separator + "filename_backup.txt";
	public final static String PATH_FILE_PATH = Setting.SETTING_PATH+Sys.file_separator+"path_file.txt";
	public final static String SETTING_FILE_PATH = Setting.SETTING_PATH+Sys.file_separator+"settings.txt";
	public final static String LOCALDATA_FILE_PATH = Setting.DATA_PATH+Sys.file_separator+"localData.txt";

	public static void settingDir(){
		switchKey(); //impostare la chiave

		if(!(new File(SETTING_PATH).exists()))
			CreaDir.creaDir(SETTING_PATH);
		if(!(new File(POSTER_PATH).exists()))
			CreaDir.creaDir(POSTER_PATH);
		if(!(new File(DATA_PATH).exists()))
			CreaDir.creaDir(DATA_PATH);
	}

	public synchronized static void switchKey(){
		if(Sys.user_name.equals("vitto")){
			API_KEY_ARRAY.add(API_KEY_2);
			System.out.println("Impostato API_KEY_2 - Profilo di Vittorio impostato\n");
		} else if(Sys.user_name.equals("Roberto")){
			API_KEY_ARRAY.add(API_KEY_1);
			System.out.println("Impostato API_KEY_1 - Profilo di Roberto impostato\n");
		} else if(Sys.user_name.equals("francesca")){
			API_KEY_ARRAY.add(API_KEY_3);
			System.out.println("Impostato API_KEY_3 - Profilo di Francesca impostato\n");
		} else {
			API_KEY_ARRAY.add(API_KEY_1);
			API_KEY_ARRAY.add(API_KEY_2);
			API_KEY_ARRAY.add(API_KEY_3);
			Collections.shuffle(API_KEY_ARRAY);
			
			if(API_KEY_ARRAY.get(0).equals(API_KEY_1)){
				System.out.println("Impostato API_KEY_1\n");
			} else if(API_KEY_ARRAY.get(0).equals(API_KEY_2)){
				System.out.println("Impostato API_KEY_2\n");
			} else {
				System.out.println("Impostato API_KEY_3\n");
			}
			
		}
	}
}
