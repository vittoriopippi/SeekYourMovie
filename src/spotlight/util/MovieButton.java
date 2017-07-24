package spotlight.util;

import java.io.File;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import spotlight.main.MainApp;
import spotlight.model.Movie;
import spotlight.player.MoviePlayer;
import spotlight.view.ModifyMovieController;
import spotlight.view.MovieViewController;
import spotlight.view.RootLayoutController;

public class MovieButton extends Button implements Comparable<MovieButton>{
	Movie movie;
	String abs_path;
	MovieViewController mvController;
	ModifyMovieController mmController;

	public MovieButton(String filename,MovieViewController mvController){
		super();
		movie = null;
		this.abs_path = filename;
		this.mvController=mvController;
		this.setText("");
		String STANDARD_BUTTON_STYLE = "-fx-background-image: url("+Setting.POSTER_DEFAULT+");"
				+ "-fx-background-size: stretch;";
		String HOVERED_BUTTON_STYLE = "-fx-background-image: url("+Setting.POSTER_DEFAULT+");"
				+ "-fx-effect: dropshadow( gaussian , black , 10 , 0.3 , 0 , 0 );"
				+ "-fx-background-size: stretch;";
		this.setMyStyle(STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
		setContMenu();
		setOnClick();
	}

	protected void setContMenu() {
		ContextMenu cm = new ContextMenu();

		MenuItem modifica = new MenuItem("Modifica");
		modifica.setOnAction(event -> {
			mvController.mainApp.showMovieModify();
			mmController=MainApp.mmController;
			mmController.actualMButton=this; ///ogni ModifyMovieController ha il riferimento al movie button su cui è stato chiamato
			mmController.setOldMovie(movie, abs_path);
		});

		MenuItem rinomina = new MenuItem("Rinomina");
		rinomina.setOnAction(event -> {
			File tempFile = new File(abs_path);	//instanzio un file temporaneo
			if(tempFile.exists()&& movie!=null && movie.getTitle() != null ){
				String tempTitle = movie.getTitle().replaceAll("[\\/:*?\"<>|]", "");

				String absDest;

				if(movie.getRelease_date() != null)
					absDest = tempFile.getParent() + Sys.file_separator + tempTitle + " " 
							+ movie.getRelease_date().substring(0, 4) 
							+ abs_path.substring(abs_path.lastIndexOf('.'));
				else
					absDest = tempFile.getParent() + Sys.file_separator + tempTitle  
					+ abs_path.substring(abs_path.lastIndexOf('.'));
				tempFile.renameTo(new File(absDest));
				abs_path=absDest;
				movie.setMovie_path(absDest);
				mvController.showMovieDetails(movie,abs_path);
			}
		});

		MenuItem riproduci = new MenuItem("Riproduci");
		riproduci.setOnAction(event -> {
			PlayMovie();
		});

		MenuItem rimuovi = new MenuItem("Rimuovi");
		rimuovi.setOnAction(event -> {
			MovieViewController.myMasonryPane.getChildren().remove(this);
			BarManager.settingCount(); 				//<------------------
			mvController.showMovieDetails(null, null);
		});

		MenuItem elimina=new MenuItem("Elimina");
		elimina.setOnAction(event -> {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Conferma eliminazione");
			alert.setHeaderText("Premendo \"Si\" il file associato al film selezionato verrà definitivamenete eliminato!\n\n"+
					"Consiglio: se vuoi solo che il film sia escluso da quelli mostrati, seleziona \"Rimuovi\"");
			alert.setContentText("Vuoi eliminare definitivamente il file?");

			ButtonType buttonTypeOne = new ButtonType("Si");
			ButtonType buttonTypeTwo = new ButtonType("No");
			ButtonType buttonTypeThree = new ButtonType("Rimuovi");

			alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo,buttonTypeThree);

			alert.getDialogPane().getStylesheets().add(Setting.CSS_DEFAULT);
			alert.getDialogPane().getStyleClass().add("custom-dialog");
			
			Optional<ButtonType> result = alert.showAndWait();
			Platform.runLater(()->{
				if (result.get() == buttonTypeOne){

					MovieViewController.myMasonryPane.getChildren().remove(this);
					BarManager.settingCount(); 				//<------------------
					new File(abs_path).delete();
					MainApp.mvController.showMovieDetails(null, null);

				}
				else if (result.get() == buttonTypeThree){
					MovieViewController.myMasonryPane.getChildren().remove(this);
					BarManager.settingCount(); 				//<------------------
				}
			});
		});

		cm.getItems().addAll(modifica,rinomina,riproduci,new SeparatorMenuItem(),rimuovi,new SeparatorMenuItem(),elimina);

		this.setContextMenu(cm);
	}

	//	public MovieButton(Movie film,MovieViewController mvController) {
	//		super();
	//		this.mvController=mvController;
	//		this.setText("");
	//		setMovie(film);
	//		this.abs_path = movie.getMovie_path();
	//		setContMenu();
	//		setOnClick();
	//	}

	private void setOnClick(){
		this.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent mouseEvent) {
				if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
					if(mouseEvent.getClickCount() == 1){
						mvController.showMovieDetails(movie,abs_path);
					}else if(mouseEvent.getClickCount() == 2){
						PlayMovie();
					}
				}
			}
		});
	}

	public Movie getMovie() {
		return movie;
	}

	public String getAbs_path() {
		return abs_path;
	}

	public void setMovie(Movie film) {
		//MainApp.needSave=true;
		if(film.getPoster_path()!=null){
			String STANDARD_BUTTON_STYLE = "-fx-background-image: url(file:" +Setting.POSTER_PATH.replace(Sys.file_separator, "/") + film.getPoster_path() + ");"+ "-fx-background-size: stretch;";
			String HOVERED_BUTTON_STYLE = "-fx-background-image: url(file:" + Setting.POSTER_PATH.replace(Sys.file_separator, "/") + film.getPoster_path() + ");"+ "-fx-effect: dropshadow( gaussian , black , 10 , 0.3 , 0 , 0 );"
					+ "-fx-background-size: stretch;";
			this.setMyStyle(STANDARD_BUTTON_STYLE, HOVERED_BUTTON_STYLE);
		}
		this.movie = film;
	}

	public void setAbs_path(String abs_path) {
		this.abs_path = abs_path;
	}

	protected void PlayMovie(){
		System.out.println(abs_path);
		new MoviePlayer().play(abs_path,movie!=null?movie.getTitle():null); //<-- Modificato
	}

	public void setMyStyle(String STANDARD_BUTTON_STYLE, String HOVERED_BUTTON_STYLE) {
		Node node = this;
		node.setStyle(STANDARD_BUTTON_STYLE);
		node.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				node.setStyle(HOVERED_BUTTON_STYLE);
			}
		});
		node.setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override public void handle(MouseEvent mouseEvent) {
				node.setStyle(STANDARD_BUTTON_STYLE);
			}
		});
	}


	// permette di ordinare i bottoni in ordine ascendente in base al titolo
	@Override
	public int compareTo(MovieButton mb) {
		if(mb == null)
			return -1;
		if(this.movie == null && mb.getMovie() == null)
			return 0;
		if(this.movie == null)
			return +1;
		if(mb.getMovie() == null)
			return -1;
		String t1 = movie.getTitle();
		String t2 = mb.getMovie().getTitle();
		return t1.compareTo(t2);
	}
}
