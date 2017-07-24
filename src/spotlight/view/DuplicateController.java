package spotlight.view;


import java.io.File;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import spotlight.main.MainApp;
import spotlight.model.CheckDuplicate;
import spotlight.model.LoadDuplicate;
import spotlight.model.Movie;
import spotlight.util.BarManager;
import spotlight.util.MovieButton;
import spotlight.util.MovieButtonDuplicate;
import spotlight.util.Setting;
import spotlight.util.Sys;

public class DuplicateController {
	private MainApp mainApp;
	private Movie dMovie;

	public static MovieButton actualMButton;

	@FXML
	private Button removeButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Text filename;
	@FXML
	private Text fileSize;
	@FXML
	private Text title;
	@FXML
	private Text nominal_runtime;
	@FXML
	private Text effective_runtime;
	@FXML
	private Text codec;
	@FXML
	private Text resolution;
	@FXML
	private Text proportion;
	@FXML
	private Text format;
	@FXML
	private Text fps;
	@FXML
	private Text bps;
	@FXML
	private Text encoder;

	@FXML
	private ImageView poster;
	@FXML
	private HBox hBox; 

	private Stage dialogStage;

	public DuplicateController() {
	}

	@FXML
	private void initialize() {
		removeButton.disableProperty().set(true);
		deleteButton.disableProperty().set(true);
		setMovie(null,null);
		new Thread(new LoadDuplicate(hBox)).start();
	}

	public void setMovie(Movie movie, String abs_path) {
		dMovie=movie;
		Double size;
		Double Mbps;
		Integer durata;
		if (movie!= null) {
			removeButton.disableProperty().set(false);
			deleteButton.disableProperty().set(false);

			if(movie.getTitle() != null)
				title.setText(movie.getTitle());
			else
				title.setText("--");
			
			if(movie.getRuntime()!=null)
				nominal_runtime.setText(movie.getRuntime().toString()+" min");
			else
				nominal_runtime.setText("--");
			
			if(movie.getDuration()!=null){
				durata=movie.getDuration()/60;
				effective_runtime.setText(durata.toString()+" min");
			}else
				effective_runtime.setText("--");
			
			if(movie.getCodec_long_name()!=null)
				codec.setText(movie.getCodec_long_name());
			else
				codec.setText("--");
			
			if(movie.getWidth()!=null && movie.getHeight()!=null)
				resolution.setText(movie.getWidth().toString()+"x"+movie.getHeight().toString());
			else
				resolution.setText("--");
			
			if(movie.getDisplay_aspect_ratio()!=null)
				proportion.setText(movie.getDisplay_aspect_ratio());
			else
				proportion.setText("--");
			
			if(movie.getFormat_long_name()!=null)
				format.setText(movie.getFormat_long_name());
			else
				format.setText("--");
			
			if(movie.getAvg_frame_rate()!=null)
				fps.setText(movie.getAvg_frame_rate()+" fps");
			else
				fps.setText("--");
			
			if(movie.getBit_rate()!=null){
				Mbps=movie.getBit_rate()/Math.pow(1024., 2);
				bps.setText(Setting.df.format(Mbps)+ "Mbps");
			}else
				bps.setText("--");
			
			if(movie.getEncoder()!=null)
				encoder.setText(movie.getEncoder());
			else
				encoder.setText("--");
			

			if(movie.getPoster_path() != null)
				poster.setImage(new Image("file:"+Setting.POSTER_PATH+Sys.file_separator+movie.poster_path().substring(1)));
			else
				poster.setImage(new Image(Setting.POSTER_DEFAULT));
		} else {
			removeButton.disableProperty().set(true);
			deleteButton.disableProperty().set(true);
			
			title.setText("--");
			nominal_runtime.setText("--");
			effective_runtime.setText("--");
			codec.setText("--");
			resolution.setText("--");
			proportion.setText("--");
			format.setText("--");
			fps.setText("--");
			bps.setText("--");
			encoder.setText("--");
			poster.setImage(new Image(Setting.POSTER_DEFAULT));
		}
		if(abs_path != null){
			filename.setText(abs_path);
			size=new Double(new File(abs_path).length());
			if(size/Math.pow(1024., 2)>=800){
				size=size/Math.pow(1024., 3);
				fileSize.setText(Setting.df.format(size)+" GB");
			}else{
				size=size/Math.pow(1024., 2);
				fileSize.setText(Setting.df.format(size)+" MB");
			}
		}
		else{
			filename.setText("--");
			fileSize.setText("--");
		}
	}

	@FXML
	private void handleAnnul(){
		dialogStage.close();
	}

	/**
	 * La funzione permette di rimuovere il film tra quelli mostrati nel mesonrypane
	 */
	@FXML
	public void handleRemove(){
		Platform.runLater(()->{
			for(Node n:hBox.getChildren()){
				MovieButtonDuplicate tmp1=(MovieButtonDuplicate) n;
				if(tmp1.getMovie().getMovie_path().equals(dMovie.getMovie_path())){
					hBox.getChildren().remove(tmp1);
					break;
				}
			}

			for(Node n:MovieViewController.myMasonryPane.getChildren()){
				MovieButton tmp1=(MovieButton) n;
				if(tmp1.getAbs_path().equals(dMovie.getMovie_path())){
					MovieViewController.myMasonryPane.getChildren().remove(tmp1);
					break;
				}
			}
			BarManager.settingCount(); 				//<------------------
			setMovie(null,null);
			new Thread(new CheckDuplicate(hBox)).start();
			MainApp.mvController.showMovieDetails(null, null);
		});
	}

	/**
	 * La funzione permette di ELIMINARE definitivamente il film selezionato
	 */
	@FXML
	public void handleDelete(){
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
		if (result.get() == buttonTypeOne){
			Platform.runLater(()->{
				String fileDel=dMovie.getMovie_path();
				for(Node n:hBox.getChildren()){
					MovieButtonDuplicate tmp1=(MovieButtonDuplicate) n;
					if(tmp1.getMovie().getMovie_path().equals(dMovie.getMovie_path())){
						hBox.getChildren().remove(tmp1);
						break;
					}
				}

				for(Node n:MovieViewController.myMasonryPane.getChildren()){
					MovieButton tmp1=(MovieButton) n;
					if(tmp1.getAbs_path().equals(dMovie.getMovie_path())){
						MovieViewController.myMasonryPane.getChildren().remove(tmp1);
						break;
					}
				}
				BarManager.settingCount(); 				//<------------------
				setMovie(null,null);
				new Thread(new CheckDuplicate(hBox)).start();
				System.out.println(fileDel);
				new File(fileDel).delete();
				MainApp.mvController.showMovieDetails(null, null);
			});
		}
		else if (result.get() == buttonTypeThree){
			handleRemove();
		}
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

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
