package spotlight.model;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import spotlight.model.NameFileFinder;
import spotlight.util.MovieButton;
import spotlight.util.Setting;
import spotlight.util.Sys;
import spotlight.view.MovieViewController;

/**
 * Ogni locandina per cui non viene visualizzato il bottone, viene eliminata
 * dalla cartella Posters
 * 
 * @author SYM
 *
 */

public class CleanImages {
	public static void clean() {
		ObservableList<Node> tList = MovieViewController.myMasonryPane.getChildrenUnmodifiable();
		NameFileFinder nff = new NameFileFinder();
		ArrayList<String> goodImages = new ArrayList<String>();
		nff.findFilenames(new File(Setting.POSTER_PATH), false);
		MovieButton tempButton = null;
		File tFile;

		for (Node n : tList) {
			tempButton = (MovieButton) n;
			if (tempButton.getMovie() != null && tempButton.getMovie().getPoster_path() != null)
				goodImages.add(tempButton.getMovie().getPoster_path().substring(1));
		}

		for (String s : nff.getFilmList()) {
			if (!goodImages.contains(s)) {
				tFile = new File(Setting.POSTER_PATH + Sys.file_separator + s);
				tFile.delete();
			}
		}
	}
}
