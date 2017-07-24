package spotlight.util;

import java.util.ArrayList;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import spotlight.model.Movie;

public class MovieButtonWrapper {

	public static ArrayList<String> wrap(ObservableList<Node> obList){
		MovieButton tempBut;
		ArrayList<String> ret=new ArrayList<String>();
		for(Iterator<Node> iter=obList.iterator();iter.hasNext();){	
			tempBut=(MovieButton)iter.next();
			ret.add(tempBut.getAbs_path());
		}
		return ret;
	}
	
	
	/**Ritorna i path assoluti dei bottoni in cui il film interno è null
	 * @param obList
	 * @return
	 */
	public static ArrayList<String> wrapNull(ObservableList<Node> obList){
		ArrayList<String> tList = new ArrayList<String>();
		MovieButton tempButton;
		
		for(Node n: obList){
			tempButton = (MovieButton) n;
			if(tempButton.getMovie() == null || tempButton.getStyle().contains(Setting.POSTER_DEFAULT))
				tList.add(tempButton.getAbs_path());
		}
		return tList;
	}
	
	public static ArrayList<String> wrapLoad(ArrayList<Movie> mList){
		ArrayList<String> tList = new ArrayList<String>();
		for(Movie m: mList)
			tList.add(m.getMovie_path());
		return tList;
	}
}
