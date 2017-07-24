package spotlight.model;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.scene.layout.HBox;
import spotlight.main.MainApp;
import spotlight.util.MovieButtonModify;
import spotlight.util.SearchMovie;

public class DownloadModify implements Runnable{
	private HBox hBox;
	private String title;
	private Integer data;

	public DownloadModify(HBox hBox,String title,Integer data) {
		super();
		this.hBox = hBox;
		this.title=title;
		this.data=data;
	}

	@Override
	public void run() {
		ArrayList<Integer> mArray = SearchMovie.ArrayTMDb(title, data);
		MainApp.mmController.setResultLabel(mArray.size());
		ArrayList<MovieButtonModify> tList = new ArrayList<MovieButtonModify>();
		for(Integer i: mArray){
			MovieButtonModify tempButton=new MovieButtonModify(null,MainApp.mmController);
			tList.add(tempButton);
			Platform.runLater(()->{
				hBox.getChildren().add(tempButton);	
			});
		}
		
		for(Integer i: mArray){
			new DownloadAndUpdate(tList.get(mArray.indexOf(i)),new Movie(i,null),false,false).run();
		}
	}

}
