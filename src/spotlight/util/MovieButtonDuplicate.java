package spotlight.util;


import java.io.File;

import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import spotlight.main.MainApp;



public class MovieButtonDuplicate extends MovieButton{
	
public MovieButtonDuplicate(String filename) {
		super(null, MainApp.mvController);
		this.setMinWidth(125);
		this.setPrefSize(125, 200);
		setContMenu();
		setOnClick();
		
	}
	
	private void setOnClick(){
		this.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 1){
		            	MainApp.dController.setMovie(movie, movie.getMovie_path());
		            }else if(mouseEvent.getClickCount() == 2){
		            	System.out.println("Passato");
						PlayMovie();
					}
		        }
		    }
		});
	}

	@Override
	protected void setContMenu() {
		ContextMenu cm = new ContextMenu();

		MenuItem riproduci = new MenuItem("Riproduci");
		riproduci.setOnAction(event -> {
			PlayMovie();
		});

		MenuItem rimuovi = new MenuItem("Rimuovi");
		rimuovi.setOnAction(event -> {
			MainApp.dController.setMovie(movie, movie.getMovie_path());
			MainApp.dController.handleRemove();
		});
		
		MenuItem elimina = new MenuItem("Elimina");
		elimina.setOnAction(event -> {
			MainApp.dController.setMovie(movie, movie.getMovie_path());
			MainApp.dController.handleDelete();
		});
		
		cm.getItems().addAll(riproduci,rimuovi,elimina);

		this.setContextMenu(cm);
	}

	

}
