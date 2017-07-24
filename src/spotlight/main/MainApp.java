package spotlight.main;

import java.io.File;
import java.io.IOException;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import spotlight.model.LocalData;
import spotlight.model.Movie;
import spotlight.model.MovieLoader;
import spotlight.util.ConnectionManager;
import spotlight.util.OnClosing;
import spotlight.util.Setting;
import spotlight.view.DuplicateController;
import spotlight.view.ModifyMovieController;
import spotlight.view.MoviePathEditorController;
import spotlight.view.MovieViewController;
import spotlight.view.CheckFFmpegController;
import spotlight.view.RootLayoutController;
import spotlight.view.SettingsController;

public class MainApp extends Application {
	public static boolean researching;
	public static boolean needSave=true;
	private Stage primaryStage;
	private BorderPane rootLayout;
	public static MovieViewController mvController;
	public static ModifyMovieController mmController;
	public static DuplicateController dController;

	public static ObservableList<File> moviePath=FXCollections.observableArrayList();

	private ObservableMap<IntegerProperty,Movie> movieData=FXCollections.observableHashMap();

	@Override
	public void start(Stage primaryStage){
		this.primaryStage=primaryStage;
		this.primaryStage.setTitle("SEEK YOUR MOVIE");
		
		// Set the application icon.
	    this.primaryStage.getIcons().add(new Image("file:resources/defaultImages/Sym.png"));

		//Creo nella home dell'utente la seguente gerarchia:
		//- Spotligh:
		//			--Path: path_file.txt --> contiene un elenco dei path inseriti dall'utente
		//			--Poster: -->contiene i poster dei film trovati
		//			--Data: --> contiene tutte le info di tutti i film mostrati a video
		Setting.settingDir();

		//Carico i path precedentemente inseriti dall'utente (se ve ne sono)
		LocalData.loadPath();
		LocalData.loadSettings();


		initRootLayout();
		showMovieView();
		
		new Thread(new ConnectionManager()).start();;

		new Thread(new MovieLoader()).start();
		researching=true;

		//Salvataggio dati!!
		this.primaryStage.setOnCloseRequest(event->{
			OnClosing.close();
		});
	}


	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/spotlight/view/RootLayout.fxml")); // se inizio con "/", parte da src, quindi devo mettere: "/package/sottocartelle.../file.fxml"
			rootLayout = (BorderPane) loader.load();

			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);


			// Give the controller access to the main app.
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);


			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Mostra i film sul tab "myMovieTab" del RootLayout
	 */
	public void showMovieView() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/spotlight/view/MovieView.fxml"));
			AnchorPane movieView = (AnchorPane) loader.load();

			// Imposta l'AnchorPane al centro del RootLayout
			rootLayout.setCenter(movieView);

			// Give the controller access to the main app.
			MovieViewController controller = loader.getController();
			controller.setMainApp(this);
			mvController=controller;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void showMoviePathEditor() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/spotlight/view/MoviePathEditor.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Aggiungi Percorso");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			MoviePathEditorController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showMovieModify() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/spotlight/view/ModifyMovie.fxml"));
			AnchorPane movieModify = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Modifica Film");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(movieModify);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);

			// Set the person into the controller.
			ModifyMovieController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			
			mmController=controller;

			//dialogStage.showAndWait();
			dialogStage.show(); //Fondamentale
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showDuplicate() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/spotlight/view/Duplicate.fxml"));
			AnchorPane movieDuplicate = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Film duplicati");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(movieDuplicate);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);

			DuplicateController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			
			dController=controller;

			//dialogStage.showAndWait();
			dialogStage.show(); //Fondamentale
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showSettings() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/spotlight/view/Settings.fxml"));
			AnchorPane settings = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Preferenze");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(settings);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);

			// Set the person into the controller.
			SettingsController controller = loader.getController();
			controller.setDialogStage(dialogStage);

//			dialogStage.showAndWait();
			dialogStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showCheckFFmpeg() {
		try {
			// Load person overview.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("/spotlight/view/CheckFFmpeg.fxml"));
			AnchorPane noFFmpeg = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setTitle("Verifica installazione FFmpeg");
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(noFFmpeg);
			dialogStage.setScene(scene);
			dialogStage.setResizable(false);

			// Set the person into the controller.
			CheckFFmpegController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			dialogStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns the main stage.
	 * @return
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Returns the data as an observable hash map of Persons. 
	 * @return
	 */
	public ObservableMap<IntegerProperty,Movie> getMovieData() {
		return movieData;
	}


	public BorderPane getRootLayout(){
		return rootLayout;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
