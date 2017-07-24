package spotlight.view;



import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXTabPane;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import spotlight.main.MainApp;
import spotlight.model.Movie;
import spotlight.model.Research;
import spotlight.util.BarManager;
import spotlight.util.Check;
import spotlight.util.MovieButton;
import spotlight.util.Setting;
import spotlight.util.Sys;

public class MovieViewController {

	public static JFXMasonryPane myMasonryPane;
	@FXML
	private ScrollPane myMovieScroll;
	@FXML
	private ScrollPane showDetailsScroll;
	@FXML
	private  ImageView myMovieImage;

	@FXML
	private  Text moviePathText;
	@FXML
	private  Text titleText;
	@FXML
	private  Text originalTitleText;
	@FXML
	private  Text originalLangText;
	@FXML
	private  Text runtimeText;
	@FXML
	private  Text genresText;
	@FXML
	private  Text overviewText;
	@FXML
	private Text vote_averageText;
	@FXML
	private Text vote_countText;
	@FXML
	private Text releaseDateText;
	@FXML
	private Text taglineText;
	@FXML
	private Text revenueText;
	@FXML
	private Text budjetText;
	@FXML
	private JFXProgressBar progress;
	@FXML
	private Label status;
	@FXML
	private Text connection;
	@FXML
	private JFXTabPane tabPane;

	//Secondo tab
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

	//barra di ricerca
	@FXML
	private AnchorPane searchBar;
	@FXML
	private AnchorPane mainAnchor;
	@FXML
	private TextField searchField;
	@FXML
	private JFXButton searchClose;

	//Riferimento alla MainApp
	public MainApp mainApp;
	
	Research res;

	public MovieViewController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		//imposto valori ed immagine di default
		myMasonryPane = new JFXMasonryPane();
		myMovieScroll.setContent(myMasonryPane);
		myMovieScroll.setFitToWidth(true);

		myMasonryPane.setCellHeight(Setting.BUTTON_HIGH);	//decidi la dimensione che vuoi
		myMasonryPane.setCellWidth(myMasonryPane.getCellHeight()*0.66);

		myMasonryPane.setHSpacing(2.5);
		myMasonryPane.setVSpacing(2.5);

		myMovieScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);		//Barra orizzontale
		myMovieScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);	//Barra verticale

		res = new Research(this);
		setBar(false);

		// Listen for changes in the text
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.equals(""))
				res.showAll();
			else
				res.find(newValue);
		});


		new BarManager(this);
		setStatusBar("",Check.Connection(),0.0);

		showMovieDetails(null,null);
	}


	/**
	 * Fills all text fields to show details about the movie.
	 * If the specified movie is null, all text fields are cleared and image sets on default..
	 * @param abs_path 
	 * 
	 * @param person the person or null
	 */
	public void showMovieDetails(Movie movie, String abs_path) {
		showDetailsScroll.setVvalue(0.);
		Double size;
		Double Mbps;
		Integer durata;
		System.out.println("\n\n"+movie+"\n\n");
		if (movie!= null) {
			// Fill the labels with info from the person object.
			if(movie.getTitle() != null)
				titleText.setText(movie.getTitle());
			else
				titleText.setText("--");
			//
			if(movie.getOriginal_title() != null)
				originalTitleText.setText(movie.getOriginal_title());
			else
				originalTitleText.setText("--");
			//
			if(movie.getOriginal_language() != null)
				originalLangText.setText(movie.getOriginal_language());
			else
				originalLangText.setText("--");
			//
			if(movie.getRuntime() != null)
				runtimeText.setText(movie.getRuntime().toString());
			else
				runtimeText.setText("--");
			//
			if(movie.getOverview() != null)
				overviewText.setText(movie.getOverview());
			else
				overviewText.setText("--");
			//
			if(movie.getVote_average() != null)
				vote_averageText.setText(movie.getVote_average().toString());
			else
				vote_averageText.setText("--");
			
			if(movie.getGenres() != null)
				genresText.setText(movie.getGenres());
			else
				genresText.setText("--");
			//
			if(movie.getVote_count() != null)
				vote_countText.setText(movie.getVote_count().toString());
			else
				vote_countText.setText("--");
			//
			if(movie.getRelease_date() != null)
				releaseDateText.setText(movie.getRelease_date());
			else
				releaseDateText.setText("--");
			//
			if(movie.getTagline()!= null)
				taglineText.setText(movie.getTagline());
			else
				taglineText.setText("--");
			//
			if(movie.getRevenue()!= null)
				revenueText.setText("$ "+Setting.nf.format(movie.getRevenue()));
			else
				revenueText.setText("--");
			//
			if(movie.getBudget()!= null)
				budjetText.setText("$ "+Setting.nf.format(movie.getBudget()));
			else
				budjetText.setText("--");
			//
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
				myMovieImage.setImage(new Image("file:"+Setting.POSTER_PATH+Sys.file_separator+movie.poster_path().substring(1)));
			else
				myMovieImage.setImage(new Image(Setting.POSTER_DEFAULT));
		} else {
			titleText.setText("--");
			originalTitleText.setText("--");
			releaseDateText.setText("--");
			runtimeText.setText("--");
			taglineText.setText("--");
			originalLangText.setText("--");
			overviewText.setText("--");
			vote_averageText.setText("--");
			budjetText.setText("--");
			vote_countText.setText("--");
			revenueText.setText("--");
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
			genresText.setText("--");
			myMovieImage.setImage(new Image(Setting.POSTER_DEFAULT));
		}
		if(abs_path!=null){
			moviePathText.setText(abs_path.substring(abs_path.lastIndexOf(Sys.file_separator)+1));
			filename.setText(abs_path);
			size=new Double(new File(abs_path).length());
			if(size/Math.pow(1024., 2)>=800){
				size=size/Math.pow(1024., 3);
				fileSize.setText(Setting.df.format(size)+" GB");
			}else{
				size=size/Math.pow(1024., 2);
				fileSize.setText(Setting.df.format(size)+" MB");
			}
		}else{
			moviePathText.setText("--");
			filename.setText("--");
			fileSize.setText("--");
		}
	}

	public void setStatusBar(String statusText, Boolean isConnected,Double value){
		//System.out.println("\n\n"+isConnected+"\n\n");
		if(statusText != null){
			status.setText(statusText);
		}
		//
		if(isConnected){
			connection.setText("Connesso");
			connection.setFill(Color.GREEN);
			progress.setStyle("-fx-accent: Green;");
		} else {
			connection.setText("Disconnesso");
			connection.setFill(Color.RED);
			progress.setStyle("-fx-accent: RED;");
		}
		//
		if(value != null && value >= 0.0 && value <= 1.0){
			progress.setProgress(value);
			if(value == 1.0){
				int count=0;
				for(Node n:MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
					MovieButton tmp=(MovieButton) n;
					if(tmp.getMovie()!=null && tmp.getMovie().getId()==null)
						count++;
				}			
				status.setText("Trovati "+(MovieViewController.myMasonryPane.getChildrenUnmodifiable().size() -count)+" film su "+MovieViewController.myMasonryPane.getChildrenUnmodifiable().size());
			}
		}

	}


	public void setBar(Boolean show){
		if(show){
			mainAnchor.setTopAnchor(myMovieScroll, 50.);
			searchField.setVisible(true);
			searchBar.setVisible(true);
			searchClose.setVisible(true);
			searchField.requestFocus();
		}
		else{
			res.showAll();
			mainAnchor.setTopAnchor(myMovieScroll, 0.);
			searchField.setVisible(false);
			searchBar.setVisible(false);
			searchClose.setVisible(true);
		}
	}

	@FXML
	private void handlerCloseResearch(){
		setBar(false);
	}

	public ScrollPane getMyMovieScroll() {
		return myMovieScroll;
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
