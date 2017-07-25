package spotlight.model;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import spotlight.util.MovieButton;
import spotlight.view.MovieViewController;

public class DuplicateFinder{


	public static ArrayList<MovieButton> searchDuplicate() {
		ObservableList<Node> masonryButtons =MovieViewController.myMasonryPane.getChildrenUnmodifiable();
		ArrayList<MovieButton> dup=new ArrayList<MovieButton>();
		MovieButton tmpBut1;
		MovieButton tmpBut2;
		boolean first=true; //per aggiunere anche tmpBut1
		for(int i=0;i<masonryButtons.size();i++){
			first=true;
			tmpBut1=(MovieButton) masonryButtons.get(i);
			if(tmpBut1.getMovie()!=null && tmpBut1.getMovie().getTitle()!=null && !dup.contains(tmpBut1)){
				for(int j=i+1;j<masonryButtons.size();j++){
					tmpBut2=(MovieButton) masonryButtons.get(j);
					if(tmpBut2.getMovie()!=null && tmpBut2.getMovie().getTitle()!=null){
						if(tmpBut1.getMovie().getTitle().equals(tmpBut2.getMovie().getTitle()) && !dup.contains(tmpBut2)){ //per il confronto tra stringhe bisogna usare equals
							dup.add(tmpBut2);
							if(first){
								dup.add(tmpBut1);
								first=false;
							}
						}
					}
				}
			}
		}
		return dup;
	}

}
