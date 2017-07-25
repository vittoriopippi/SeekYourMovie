package spotlight.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.jfoenix.controls.JFXMasonryPane;

import javafx.application.Platform;
import javafx.scene.Node;
import spotlight.main.MainApp;
import spotlight.util.BarManager;
import spotlight.util.MovieButton;
import spotlight.util.Sys;
import spotlight.view.MovieViewController;

/**
 * Classe runnable che rimuove tutti gli elementi per cui non esiste neanche un
 * persorso
 * 
 * @author SYM
 */
public class CheckButton implements Runnable {

	private JFXMasonryPane myMasonryPane;
	private MovieButton tempBut;

	public CheckButton() {
		super();
		this.myMasonryPane = MovieViewController.myMasonryPane;
	}

	@Override
	public void run() {
//		Boolean flag;
		tempBut = null;
		ArrayList<MovieButton> bArray = new ArrayList<MovieButton>();
		
//		Iterator<Node> iter = myMasonryPane.getChildren().iterator();
//		while (iter.hasNext()) {
//			flag = false;
//			tempBut = (MovieButton) iter.next();
//			for (File f : MainApp.moviePath) {
//				if (tempBut.getAbs_path().contains(f.getAbsolutePath() + Sys.file_separator)) {
//					flag = true;
//					break;
//				}
//			}
//			if (flag) {
//				bArray.add(tempBut);
//			}
//		}
		
		//Versione più veloce ma poco conlaudata, la versione precedente è quella commentata
		ArrayList<Node> childArray = new ArrayList<Node>();
		childArray.addAll(myMasonryPane.getChildren());
		for(File f : MainApp.moviePath) {
			Iterator<Node> iter = childArray.iterator();
			while(iter.hasNext()) {
				tempBut = (MovieButton) iter.next();
				if (tempBut.getAbs_path().contains(f.getAbsolutePath() + Sys.file_separator)) {
					bArray.add(tempBut);
					childArray.remove(tempBut);
				}
			}
		}

		Platform.runLater(() -> {
			myMasonryPane.getChildren().clear();
			myMasonryPane.getChildren().addAll(bArray);
			BarManager.settingCount();
			MainApp.mvController.showMovieDetails(null, null);

		});
	}
}
