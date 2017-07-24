package spotlight.model;

import spotlight.util.BarManager;
import spotlight.util.MovieButton;
import spotlight.util.Setting;
import spotlight.util.Sys;

public class DownloadAndUpdate implements Runnable{
	MovieButton mButton;
	Movie tempMovie;
	Boolean overwrite;
	Boolean showMsg;

	public DownloadAndUpdate(MovieButton mButton, Movie tempMovie) {
		super();
		this.mButton = mButton;
		this.tempMovie = tempMovie;
		this.overwrite = false;
		this.showMsg = true;
	}

	public DownloadAndUpdate(MovieButton mButton, Movie tempMovie, Boolean overwrite, Boolean showMsg) {
		super();
		this.mButton = mButton;
		this.tempMovie = tempMovie;
		this.overwrite = overwrite;
		this.showMsg = showMsg;
	}

	@Override
	public void run() {
		if(tempMovie.getPoster_path()!=null)
			new SaveImage("https://image.tmdb.org/t/p/w300" + tempMovie.getPoster_path(), Setting.POSTER_PATH, tempMovie.getPoster_path().substring(1),overwrite).run();
		mButton.setMovie(tempMovie);
		if(showMsg){
			if(tempMovie.getTitle()!=null)
				BarManager.addMessage("Trovato: " + tempMovie.getTitle());
			else if(tempMovie.getMovie_path()!=null)
				BarManager.addMessage("Non trovato: "+tempMovie.getMovie_path());
			else
				BarManager.addNull();
		}
	}
}
