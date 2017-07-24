package spotlight.player;

import spotlight.util.Sys;
import java.io.*;

public class MoviePlayer{
	private String player; //permette di avere info sul player in uso
	private String moviePath; //PATH del file video da riprodurre
	private String title; //titolo del file video da riprodurre
	public boolean playing=false; // permette di sapere in ogni momemnto (public) se e' in riproduzione un file video
	private Process p;// permette di avviare il processo di riproduzione del file video 

	/**
	 * Il costruttore, sulla base del SO corrente, seleziona il player per riprodurre i file video:
	 * WINDOWS:
	 * - VLC: C:\Program Files (x86)\VideoLAN\VLC\vlc.exe
	 * - WindowsMediaPlayer: C:\Program Files (x86)\Windows Media Player\wmplayer.exe (Solo se non e' installa VLC)
	 * 
	 * MAC_OS:
	 * - ...
	 * 
	 * UNIX:
	 * - ...
	 */
	public MoviePlayer() {
		super();
		this.moviePath = null;
		this.title=null;
		String OS=Sys.os_name.toLowerCase();
		
		//WINDOWS
		if(OS.contains("win")){	
			File pl=new File("C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe");	// Verifico se e' installa VLC ...
			if(pl.exists())	// ...se installato lo uso per riprodurre i film
				this.player = "\"C:\\Program Files (x86)\\VideoLAN\\VLC\\vlc.exe\""; //basta inserire il PATH di un player qualsiasi
			else		// ...se non installato uso il player di sistema
				this.player = "\"C:\\Program Files (x86)\\Windows Media Player\\wmplayer.exe\""; // possibili problemi per la riproduzione di file con estensione ".mkv"
		}
		
		//MAC_OS
		else if (OS.contains("mac")){
				this.player = null;	//Non importa quale player scegliere viene utilizzato quello di default
		}
		
		//UNIX
		else if(OS.contains("nix") || OS.contains("nux") || OS.contains("aix") ) {	
			File pl=new File("/usr/bin/vlc");	// Verifico se e' installa VLC ...
			if(pl.exists())		// ...se installato lo uso per riprodurre i film
				this.player = "vlc"; //basta inserire il PATH di un player qualsiasi
			else		// ...se non installato uso il player di sistema
				this.player = "mplayer"; // possibili problemi per la riproduzione di file con estensione ".mkv"
		
		}

	}

	/**
	 * Permette di avviare la riproduzione di un file video.
	 * Se e' gia' in riproduzione un file video, questo viene arrestato e si riproduce il "nuovo" file video
	 * situato al PATH indicato come parametro
	 * 
	 * @param moviePath - PATH del file video da riprodurre
	 * @param title - Titolo del file video da riprodurre
	 */
	public void play(String moviePath, String title) {
		if(moviePath!=null){
			this.moviePath=moviePath;
			this.title=title;
			if(playing){ // se si sta gi� guardando un file video, lo arresto e avvia una nuova riproduzione
				p.destroy();
				try {
					Thread.sleep(500); //Aspetta mezzo secondo secondo prim
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			playing=true;
			try{
				if(Sys.os_name.toLowerCase().contains("win")) {
					p= Runtime.getRuntime().exec(player+"\""+this.moviePath+"\"");
				}else if(Sys.os_name.toLowerCase().contains("mac")) {
					new ProcessBuilder("open",this.moviePath).start();
				} else {
					new ProcessBuilder(player,this.moviePath).start();  //va bene anche per windows
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Permette di arrestare l'"eventuale" riproduzione di un file video.
	 */
	public void stop(){
		if(playing){
			playing=false;
			p.destroy();
		}
	}

	/**
	 * Ritorna il titolo del file video in riproduzione
	 * 
	 * @return title - Titolo del file video in riproduzione
	 */
	public String getTitle() {
		return title;
	}

}
