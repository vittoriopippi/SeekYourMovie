package spotlight.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import com.jfoenix.controls.JFXMasonryPane;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import spotlight.util.MovieButton;
import spotlight.util.Sys;
import spotlight.view.MovieViewController;

public class Research {
	JFXMasonryPane masPane;
	MovieViewController mvc;

	public Research(MovieViewController mvc) {
		masPane = mvc.myMasonryPane;
		this.mvc = mvc;
	}

	public void find(String newStr) {
		ObservableList<Node> find = masPane.getChildren();
		MovieButton tBut = null;
		
		Boolean flagTitle;
		Boolean flagOriginal;
		Boolean flagFilename;
		Boolean flagGenres;

		for(int i = 0; i < masPane.getChildren().size(); i++){
			tBut = (MovieButton) masPane.getChildren().get(i);
			flagFilename = tBut.getAbs_path().substring(tBut.getAbs_path().lastIndexOf(Sys.file_separator)).toLowerCase().contains(newStr.toLowerCase());
			
			if(tBut != null && tBut.getMovie() != null && tBut.getMovie().getTitle()!=null){
				flagTitle = tBut.getMovie().getTitle().toLowerCase().contains(newStr.toLowerCase());
				flagOriginal = tBut.getMovie().getOriginal_title().toLowerCase().contains(newStr.toLowerCase());
				flagGenres = tBut.getMovie().getGenres().toLowerCase().contains(newStr.toLowerCase());

				if(flagTitle || flagOriginal || flagFilename || flagGenres)
					masPane.getChildren().get(i).setVisible(true);
				else
					masPane.getChildren().get(i).setVisible(false);
			}
			else{
				if(flagFilename)
					masPane.getChildren().get(i).setVisible(true);
				else
					masPane.getChildren().get(i).setVisible(false);
			}
		}
		order();
	}

	public void showAll() {
		for(int i = 0; i < masPane.getChildren().size(); i++){
			masPane.getChildren().get(i).setVisible(true);
		}	
	}
	
	private void order(){
		mvc.getMyMovieScroll().setVvalue(0.);
		
		ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
		for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
			tList.add((MovieButton) n);
		}
		Collections.sort(tList, new Comparator<MovieButton>() {
			@Override
			public int compare(MovieButton s0, MovieButton s1) {
				if(s0 == null && s1 == null)
					return 0;
				if(s0 == null)
					return +1;
				if(s1 == null)
					return -1;

				if(s0.isVisible() && s1.isVisible())
					return 0;
				if(s0.isVisible())
					return -1;
				if(s1.isVisible())
					return +1;

				return 0;
			}	
		});
		MovieViewController.myMasonryPane.getChildren().clear();
		MovieViewController.myMasonryPane.getChildren().addAll(tList);
	}

}
