package spotlight.view;

import java.io.File;

import javafx.beans.property.SimpleStringProperty;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spotlight.main.MainApp;
import spotlight.model.CheckButton;
import spotlight.model.MovieLoader;
import spotlight.util.Setting;

public class MoviePathEditorController {
	@FXML
	private TableView<File> pathTable;
	@FXML
	private TableColumn<File,String> pathColumn;

	private Stage dialogStage;

	// Reference to the main application.
	private MainApp mainApp;

	/**
	 * The constructor.
	 * The constructor is called before the initialize() method.
	 */
	public MoviePathEditorController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		// Add observable list data to the table
		pathTable.setItems(MainApp.moviePath);
		// Initialize the person table with the two columns.
		pathColumn.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getAbsolutePath()));
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		this.dialogStage.getIcons().add(new Image("file:resources/defaultImages/Sym.png"));
	}

	/**
	 * Called when the user clicks on the delete button.
	 */
	@FXML
	private void handleDeletePath() {
		int selectedIndex = pathTable.getSelectionModel().getSelectedIndex();

		if (selectedIndex<0) {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(dialogStage);
			alert.setTitle("Nessuna selezione");
			alert.setHeaderText("Nessun percorso selezionato");
			alert.setContentText("Selezionare un percorso");
			
			alert.getDialogPane().getStylesheets().add(Setting.CSS_DEFAULT);
			alert.getDialogPane().getStyleClass().add("custom-dialog");
			
			alert.showAndWait();
		}
//		else if (MainApp.moviePath.size()==1) {
//			// Nothing selected.
//			Alert alert = new Alert(AlertType.WARNING);
//			alert.initOwner(dialogStage);
//			alert.setTitle("Errore");
//			alert.setHeaderText("Impossibile eliminare il percorso");
//			alert.setContentText("DEVE essere presente almeno un percorso di ricerca");
//			alert.showAndWait();
//		}
		else{
			pathTable.getItems().remove(selectedIndex);
			new Thread(new CheckButton()).start();
		}

	}

	/**
	 * Opens a DirectoryChooser to let the user add a new movie_path.
	 */
	@FXML
	private void handleAddPath() {
		//la classe che ci servirà per selezionare le cartelle
		DirectoryChooser dirChooser = new DirectoryChooser();
		dirChooser.setTitle("Spotlight - Aggiungi percorso di ricerca...");
		File selectedDirectory=dirChooser.showDialog(dialogStage);
		if(selectedDirectory!=null && !MainApp.moviePath.contains(selectedDirectory)){
			MainApp.moviePath.add(selectedDirectory);
			new Thread(new MovieLoader(selectedDirectory.getAbsolutePath())).start();
			
//			System.out.println("Cartella selezionata: "+selectedDirectory);
		}
//		else
//			System.out.println("Nessuna cartella selezionata");
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}




}
