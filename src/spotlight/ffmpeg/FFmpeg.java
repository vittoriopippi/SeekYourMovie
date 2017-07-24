package spotlight.ffmpeg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import spotlight.util.Sys;


public class FFmpeg {

	public static JSONObject getData(String moviePath){
		//System.out.println("\n\n"+moviePath+"\n\n");
		Process p = null;
		String line = null;
		JSONObject jobj = null;
		String response = "";
		try{
			if(Sys.os_name.toLowerCase().contains("mac"))
			    p=new ProcessBuilder("/usr/local/Cellar/ffmpeg/3.3.2/bin/ffprobe","-v","quiet","-print_format","json","-show_format","-show_streams",moviePath).start();
			else
				p=new ProcessBuilder("ffprobe","-v","quiet","-print_format","json","-show_format","-show_streams",moviePath).start();
			if(p==null||p.getInputStream()==null)
				return null;
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				response = response.concat(line);
			}
			input.close();

			JSONParser parser = new JSONParser();
			jobj = (JSONObject) parser.parse(response);


		} catch (IOException | ParseException e) {

			return null;
		}

		return jobj;
	}

	//	public static void main(String[] arg){
	//		System.out.println(new Movie(335797,("C:\\Users\\Roberto\\Videos\\Film_Nuovi\\Sing.mkv")));
	//	}
}
