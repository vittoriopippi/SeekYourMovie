package spotlight.util;

import java.io.File;
import java.util.ArrayList;

import com.jfoenix.controls.JFXMasonryPane;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import spotlight.util.MovieButton;
import spotlight.model.LocalData;
import spotlight.model.Movie;
import spotlight.view.MovieViewController;
import spotlight.view.RootLayoutController;

public class Rename implements Runnable{
	private JFXMasonryPane mPane;
	static String msg;
	String err;
	static Integer counter = 0;
	Boolean toRecover;
	public Rename(Boolean toRecover) {
		super();
		this.toRecover = toRecover;
		mPane = MovieViewController.myMasonryPane;
	}

	@Override
	public void run() {
		if(toRecover)
			recoverBackup();
		else
			renameAll();
	}

	public void recoverBackup(){
		ArrayList<String> backup = LocalData.recovrBackup();
		String newFilename;	//filename che verrà rinominato nell'oldFilename
		String oldFilename;
		MovieButton tButton = null;
		Movie tMovie = null;
		File tempFile;
		msg = "";
		counter = 0;

		for(Node n: MovieViewController.myMasonryPane.getChildren()){
			tButton = (MovieButton) n;
			tMovie = tButton.getMovie();
			if(backup.contains(tButton.getAbs_path())){	//tButton.getAbs_path() è il newFilename(indice pari)
				newFilename = tButton.getAbs_path();
				oldFilename = backup.get(backup.indexOf(newFilename)-1);

				tempFile = new File(newFilename);	//carico il nuovo filename
				if(tempFile.exists() && tempFile.renameTo(new File(oldFilename)) && tMovie != null){
					msg = msg.concat("\nRinominato il Film \"" + tMovie.getTitle() + "\" con successo:\n"); 
					msg = msg.concat("PRIMA\t" + newFilename + "\n"); //rinomino con il vecchio
					msg = msg.concat("DOPO\t" + oldFilename + "\n"); 
					tMovie.setMovie_path(oldFilename);
					tButton.setMovie(tMovie);
					counter++;
				}else{
					msg = msg.concat("\nImpossibile rinominare il Film \"" + tMovie.getTitle() + "\"\n"); 
				}
				tButton.setAbs_path(oldFilename);
			}
		}
		LocalData.clearBackup();
		Platform.runLater(()->{
			Rename.showMessage("Rinomina effettuata","Tutti i Film sono stati rinominati:","","Sono stati rinominati " + counter + " su " + MovieViewController.myMasonryPane.getChildren().size() + msg);
		});
	}

	public void renameAll(){
		//		System.out.println("Sono il thread di rinomina e sono stato eseguito");
		ArrayList<Node> newList = new ArrayList<Node>();
		ArrayList<String> backup = new ArrayList<String>();
		MovieButton tempButton = null;
		Movie tempMovie = null;
		File tempFile = null;
		counter = 0;
		msg = "";
		err = "\nFilm che non sono stati rinominati perchè non trovati:\n";
		newList.addAll(mPane.getChildrenUnmodifiable());

		LocalData.clearBackup();

		for(Node n: newList){
			tempButton = (MovieButton) n;	//prendo il bottone temporaneo
			tempMovie = tempButton.getMovie();	//prendo il film temporaneo

			if(tempMovie != null){	//se il film temporaneo non è nullo
				tempFile = new File(tempButton.getAbs_path());	//instanzio un file temporaneo
				//msg = msg.concat("\nIl FIlm " + tempMovie.getTitle() + " è stato rinominato:");
				//System.out.println("creato file temporaneo");
				if(tempFile.exists() && tempMovie.getTitle() != null){
					String tempTitle = tempMovie.getTitle().replaceAll("[\\/:*?\"<>|]", "");

					String absDest;

					if(tempMovie.getRelease_date() != null)
						absDest = tempFile.getParent() + Sys.file_separator + tempTitle + " " 
								+ tempMovie.getRelease_date().substring(0, 4) 
								+ tempButton.getAbs_path().substring(tempButton.getAbs_path().lastIndexOf('.'));
					else
						absDest = tempFile.getParent() + Sys.file_separator + tempTitle  
						+ tempButton.getAbs_path().substring(tempButton.getAbs_path().lastIndexOf('.'));

					if(!tempButton.getAbs_path().equals(absDest) && tempFile.renameTo(new File(absDest))){
						msg = msg.concat("\nIl FIlm \"" + tempMovie.getTitle() + "\" è stato rinominato:");
						msg = msg.concat("\n PRIMA\t" + tempButton.getAbs_path());
						msg = msg.concat("\n DOPO\t" + absDest + "\n");
						backup.add(tempButton.getAbs_path());
						backup.add(absDest);
						counter++;
					} else {
						msg = msg.concat("\nIl FIlm \"" + tempMovie.getTitle() + "\" aveva già il Filename corretto\n");
					}
					tempButton.setAbs_path(absDest);
					tempMovie.setMovie_path(absDest);
					tempButton.setMovie(tempMovie);
					n = tempButton;
				} else {
					err = err.concat(tempButton.getAbs_path() + "\n");
				}
			} else {
				err = err.concat(tempButton.getAbs_path() + "\n");
			}

		}
		LocalData.addMovieBackup(backup);
		//		Platform.runLater(() ->{
		//			mPane.getChildren().clear();
		//			mPane.getChildren().addAll(newList);
		//		});
		if(counter!=newList.size())
			Platform.runLater(()->{
				Rename.showMessage("Rinomina effettuata","Tutti i Film sono stati rinominati:","","Sono stati rinominati " + counter + " su " + newList.size() + msg.concat(err));
			});
		else
			Platform.runLater(()->{
				Rename.showMessage("Rinomina effettuata","Tutti i Film sono stati rinominati:","","Sono stati rinominati " + counter + " su " + newList.size() + msg);
			});
	}

	public static void showMessage(String title,String header,String labelText,String msg){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		//		alert.setContentText("Could not find file blabla.txt!");

		Label label = new Label(labelText);

		TextArea textArea = new TextArea(msg);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.getDialogPane().getStylesheets().add(Setting.CSS_DEFAULT);
		alert.getDialogPane().getStyleClass().add("custom-dialog");

		alert.showAndWait();
	}
}
