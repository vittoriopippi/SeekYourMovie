package spotlight.util;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import spotlight.main.MainApp;
import spotlight.model.CleanImages;
import spotlight.model.LocalData;
import spotlight.view.MovieViewController;

public class OnClosing {
	
	public static void close(){
		if(MainApp.needSave || MovieViewController.myMasonryPane.getChildrenUnmodifiable().size()==0){ //<-------------------
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Conferma salvataggio");
			alert.setHeaderText("Premendo \"Si\" verrano salvate tutte le informazioni dei film mostrati a video");
			alert.setContentText("Vuoi effettuare il salvataggio?");

			ButtonType buttonTypeOne = new ButtonType("Si");
			ButtonType buttonTypeTwo = new ButtonType("No");
		
			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

			alert.getDialogPane().getStylesheets().add(Setting.CSS_DEFAULT);
			alert.getDialogPane().getStyleClass().add("custom-dialog");
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == buttonTypeOne){
				LocalData.save();
				//Elimino locandine che non sono riferite ai film salvati
				CleanImages.clean();
			}
		}
		//Salvataggio Path
		LocalData.savePath();
		//Elimino locandine che non sono mostrate a video
		//CleanImages.clean();
		System.exit(0);
	}

}
