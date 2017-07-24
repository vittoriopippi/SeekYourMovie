package spotlight.model;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import spotlight.util.Sys;

public class SaveImage implements Runnable{
	String link;
	String destination;
	String filename;
	Boolean overwrite;
	
	public SaveImage(String link, String destination, String filename) {
		super();
		this.link = link;
		this.destination = destination;
		this.filename = filename;
		this.overwrite = false;
	}

	public SaveImage(String link, String destination, String filename, Boolean overwrite) {
		super();
		this.link = link;
		this.destination = destination;
		this.filename = filename;
		this.overwrite = overwrite;
	}

	@Override
	public void run() {
		try {
			if(overwrite || !(new File(destination + Sys.file_separator + filename).exists())){

				Integer reader;
				URL source = new URL(link);
				BufferedInputStream in = new BufferedInputStream(source.openStream());
				FileOutputStream out = new FileOutputStream(destination + Sys.file_separator + filename);

				while((reader = in.read()) != -1){
					out.write(reader);;
				}
				out.close();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}
