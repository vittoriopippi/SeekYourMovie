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

public class CheckButton implements Runnable{

	private JFXMasonryPane myMasonryPane;
	private MovieButton tempBut;

	public CheckButton() {
		super();
		this.myMasonryPane = MovieViewController.myMasonryPane;
	}

	@Override
	public void run() {
		Boolean flag;
		tempBut=null;
		ArrayList<MovieButton> bArray = new ArrayList<MovieButton>();
		for(Iterator<Node> iter=myMasonryPane.getChildren().iterator();iter.hasNext();){
			flag=false;
			tempBut=(MovieButton) iter.next();
			for(File f:MainApp.moviePath){
				if(tempBut.getAbs_path().contains(f.getAbsolutePath()+Sys.file_separator)){
					flag=true;
					break;
				}
			}
			if(flag){
//				System.out.println("\n\n\n"+tempBut.getAbs_path()+"\n\n\n");
				bArray.add(tempBut);
			}
		}
		Platform.runLater(()->{
			myMasonryPane.getChildren().clear();
			myMasonryPane.getChildren().addAll(bArray);
			BarManager.settingCount(); 				//<------------------
			MainApp.mvController.showMovieDetails(null,null);
			
		});
	}
}
