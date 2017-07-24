package spotlight.view;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import spotlight.main.MainApp;
import spotlight.model.DownloadModify;
import spotlight.model.Movie;
import spotlight.util.MovieButton;
import spotlight.util.Setting;
import spotlight.util.Sys;

public class ModifyMovieController {
	private MainApp mainApp;
	private Movie newMovie=null,oldMovie;

	public static MovieButton actualMButton;
	
	@FXML
	private Label result;
	
	@FXML
	private ScrollPane modifyMovieScroll;

	@FXML
	private Button ApplyButton;
	@FXML
	private Text oldFilename;
	@FXML
	private Text oldTitle;
	@FXML
	private Text oldOriginalTitle;
	@FXML
	private TextArea oldOverview;
	@FXML
	private Text newTitle;
	@FXML
	private Text newOriginalTitle;
	@FXML
	private Text newRuntime;
	@FXML
	private  Text genresText;
	@FXML
	private Text newOverview;
	@FXML
	private Text newVote;
	@FXML
	private Text newVoteCount;
	@FXML
	private Text newDate;
	@FXML
	private Text newTagline;
	@FXML
	private Text newRevenue;
	@FXML
	private Text newBudget;
	@FXML
	private ImageView oldPoster;
	@FXML
	private ImageView newPoster;
	@FXML
	private TextField textTitle;
	@FXML
	private TextField textData;
	@FXML
	private HBox hBox;

	private Stage dialogStage;

	public ModifyMovieController() {
	}

	@FXML
	private void initialize() {
		ApplyButton.disableProperty().set(true);
		result.setText("Risultati:");
		setNewMovie(null,null);
		setOldMovie(null,null);
	}

	public void setNewMovie(Movie movie, String abs_path) {
		modifyMovieScroll.setVvalue(0.);
		newMovie=movie;
		if (movie!= null) {
			ApplyButton.disableProperty().set(false);
			if(movie.getTitle() != null)
				newTitle.setText(movie.getTitle());
			else
				newTitle.setText("--");

			if(movie.getOriginal_title() != null)
				newOriginalTitle.setText(movie.getOriginal_title());
			else
				newOriginalTitle.setText("--");

			if(movie.getRuntime() != null)
				newRuntime.setText(movie.getRuntime().toString());
			else
				newRuntime.setText("--");

			if(movie.getGenres() != null)
				genresText.setText(movie.getGenres());
			else
				genresText.setText("--");
			
			if(movie.getOverview() != null)
				newOverview.setText(movie.getOverview());
			else
				newOverview.setText("--");

			if(movie.getVote_average() != null)
				newVote.setText(movie.getVote_average().toString());
			else
				newVote.setText("--");

			if(movie.getRelease_date() != null)
				newDate.setText(movie.getRelease_date().toString());
			else
				newDate.setText("--");

			if(movie.getTagline() != null)
				newTagline.setText(movie.getTagline());
			else
				newTagline.setText("--");

			if(movie.getRevenue() != null)
				newRevenue.setText("$ "+Setting.nf.format(movie.getRevenue()));
			else
				newRevenue.setText("--");

			if(movie.getBudget() != null)
				newBudget.setText("$ "+Setting.nf.format(movie.getBudget()));
			else
				newBudget.setText("--");

			if(movie.getPoster_path() != null)
				newPoster.setImage(new Image("file:"+Setting.POSTER_PATH+Sys.file_separator+movie.poster_path().substring(1)));
			else
				newPoster.setImage(new Image(Setting.POSTER_DEFAULT));
		} else {
			newTitle.setText("--");
			newOriginalTitle.setText("--");
			newOverview.setText("--");
			newVote.setText("--");
			newVoteCount.setText("--");
			newDate.setText("--");
			newTagline.setText("--");
			newRevenue.setText("--");
			newBudget.setText("--");
			genresText.setText("--");
			newPoster.setImage(new Image(Setting.POSTER_DEFAULT));
		}
	}

	public void setOldMovie(Movie movie, String abs_path) {
		oldMovie=movie;
		if (movie!= null) {
			if(movie.getTitle() != null)
				oldTitle.setText(movie.getTitle());
			else
				oldTitle.setText("--");

			if(movie.getOriginal_title() != null)
				oldOriginalTitle.setText(movie.getOriginal_title());
			else
				oldOriginalTitle.setText("--");

			if(movie.getOverview() != null)
				oldOverview.setText(movie.getOverview());
			else
				oldOverview.setText("\n\n\n\n--");

			if(movie.getPoster_path() != null)
				oldPoster.setImage(new Image("file:"+Setting.POSTER_PATH+Sys.file_separator+movie.poster_path().substring(1)));
			else
				oldPoster.setImage(new Image(Setting.POSTER_DEFAULT));
		} else {

			oldTitle.setText("--");
			oldOriginalTitle.setText("--");
			oldOverview.setText("\n\n\n\n--");
			oldPoster.setImage(new Image(Setting.POSTER_DEFAULT));
		}
		if(abs_path != null)
			oldFilename.setText(abs_path.substring(abs_path.lastIndexOf(Sys.file_separator)+1));
		else
			oldFilename.setText("--");
	}

	@FXML
	private void handleSearch(){
		String title = textTitle.getText();
		String string_data = textData.getText().replaceAll("[^0-9]", "");;
		Integer data = null;

		if(string_data.length() > 0 && Integer.parseInt(string_data) >= 1895)
			data = Integer.parseInt(string_data);

		hBox.getChildren().clear();


		 new Thread(new DownloadModify(hBox,title,data)).start();
	}


	@FXML
	private void handleAnnul(){
		dialogStage.close();
	}

	@FXML
	private void handleApply(){
		Platform.runLater(()->{
			for(Node n:MovieViewController.myMasonryPane.getChildren()){
				MovieButton tmp1=(MovieButton) n;
				newMovie.setMovie_path(tmp1.getAbs_path());
				if(tmp1==actualMButton){
					newMovie.addInfo(oldMovie);
					tmp1.setMovie(newMovie);
					break;
				}
			}
			MainApp.mvController.showMovieDetails(newMovie, newMovie.getMovie_path());
		});
		
		dialogStage.close();
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
	 * Funzione che permette di modificare la label che indica il numero di risultati trovati
	 * @param mainApp
	 */
	public void setResultLabel(Integer i){
		Platform.runLater(()->{
			if(i==0)
				result.setText("Nessun risultato trovato.");
			if(i==1)
				result.setText("Trovato un solo risultato:");
			if(i>1)
				result.setText("Trovati "+i+" risultati:");
		});
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
}
