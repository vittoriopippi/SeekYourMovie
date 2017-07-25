package spotlight.model;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import spotlight.main.MainApp;
import spotlight.util.MovieButton;
import spotlight.util.MovieButtonDuplicate;

public class LoadDuplicate implements Runnable{
	private HBox hBox;


	public LoadDuplicate(HBox hBox) {
		this.hBox=hBox;
	}

	@Override
	public void run() {
		ArrayList<MovieButton> dup=DuplicateFinder.searchDuplicate();
		
		for(MovieButton m:dup){
			MovieButtonDuplicate dupBut=new MovieButtonDuplicate(null);
			dupBut.setMovie(m.getMovie());
			dupBut.setAbs_path(m.getAbs_path());
			Platform.runLater(()->{
				hBox.getChildren().add(dupBut);
			});
		}
		
	}

}
