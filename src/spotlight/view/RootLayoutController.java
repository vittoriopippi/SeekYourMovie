package spotlight.view;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import spotlight.main.MainApp;
import spotlight.model.CleanImages;
import spotlight.model.LocalData;
import spotlight.model.MovieLoader;
import spotlight.util.BarManager;
import spotlight.util.DelDir;
import spotlight.util.MovieButton;
import spotlight.util.OnClosing;
import spotlight.util.Rename;
import spotlight.util.Setting;
import spotlight.util.Sys;


/**
 * The controller for the root layout. The root layout provides the basic
 * application layout containing a menu bar and space where other JavaFX
 * elements can be placed.
 * 
 * @author Spotlight
 */
public class RootLayoutController {

	//variabile booleana che stabilisce se l'ordinamento deve essere crescente o decrescente
	Boolean order;
	Boolean isFull;

	@FXML
	CheckMenuItem anno;
	@FXML
	CheckMenuItem bit_rate;
	@FXML
	CheckMenuItem budget;
	@FXML
	CheckMenuItem dimensione;
	@FXML
	CheckMenuItem durata;
	@FXML
	CheckMenuItem filename;
	@FXML
	CheckMenuItem incassi;
	@FXML
	CheckMenuItem risoluzione;
	@FXML
	CheckMenuItem titolo;
	@FXML
	CheckMenuItem titolo_originale;
	@FXML
	CheckMenuItem voto;
	@FXML
	CheckMenuItem cres;
	@FXML
	CheckMenuItem decr;
	@FXML
	CheckMenuItem fps;

	Thread ref;

	public static ArrayList<CheckMenuItem> arrMenu;


	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize(){
		arrMenu=new ArrayList<CheckMenuItem>();
		isFull=false;
		arrMenu.add(anno);
		arrMenu.add(bit_rate);
		arrMenu.add(budget);
		arrMenu.add(dimensione);
		arrMenu.add(durata);
		arrMenu.add(filename);
		arrMenu.add(fps);
		arrMenu.add(incassi);
		arrMenu.add(risoluzione);
		arrMenu.add(titolo);
		arrMenu.add(titolo_originale);
		arrMenu.add(voto);
		cres.setSelected(true);
		order=true;
	}


	//Riferimento alla MainApp
	private MainApp mainApp;

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Richiama la funzione del main che permette di mostrare un dialogo tramite il quale 
	 * l'utente di inserire o eliminare i paths di ricerca dei film
	 */
	@FXML
	private void handleAddPath() {
		mainApp.showMoviePathEditor();
	}

	/**
	 * Richiama la funzione del main che permette di mostrare un dialogo tramite il quale 
	 * l'utente può prendere visione dei film duplicati
	 */
	@FXML
	private void handleShowDuplicate() {
		mainApp.showDuplicate();
	}

	@FXML
	private void handleOrderCres(){
		order=true;
		decr.setSelected(false);
		if(cres.isSelected()){
			selectOrder();
		}
		cres.setSelected(true);
	}

	@FXML
	private void handleOrderDecr(){
		order=false;
		cres.setSelected(false);
		if(decr.isSelected()){
			selectOrder();
		}
		decr.setSelected(true);
	}

	@FXML
	private void handleSortByTitle() {
		//if(titolo.isSelected()){ //se metto la condizione, non effettua nessun ordinamento nel caso in cui il parametro in base a cui ordinare è già selezionato
		sortByTitle(order);
		checkSelection(titolo);
		//}
		titolo.setSelected(true);
	}

	@FXML
	private void handleSortByOriginalTitle() {
		//if(titolo_originale.isSelected()){
		sortByOriginalTitle(order);
		checkSelection(titolo_originale);
		//}
		titolo_originale.setSelected(true);
	}

	@FXML
	private void handleSortByFilename() {
		//if(filename.isSelected()){
		sortByFilename(order);
		checkSelection(filename);
		//}
		filename.setSelected(true);
	}

	@FXML
	private void handleSortByBudget() {
		//if(budget.isSelected()){
		sortByBudget(order);
		checkSelection(budget);
		//}
		budget.setSelected(true);
	}


	@FXML
	private void handleSortByData() {
		if(anno.isSelected()){
			sortByData(order);
			checkSelection(anno);
		}
		anno.setSelected(true);
	}

	@FXML
	private void handleSortByRating() {
		//if(voto.isSelected()){
		sortByRating(order);
		checkSelection(voto);
		//}
		voto.setSelected(true);
	}


	@FXML
	private void handleSortByRuntime() {
		//if(durata.isSelected()){
		sortByRuntime(order);
		checkSelection(durata);
		//}
		durata.setSelected(true);
	}

	@FXML
	private void handleSortBySize() {
		//if(dimensione.isSelected()){
		sortBySize(order);
		checkSelection(dimensione);
		//}
		dimensione.setSelected(true);
	}


	@FXML
	private void handleSortByQuality() {
		//if(risoluzione.isSelected()){
		sortByQuality(order);
		checkSelection(risoluzione);
		//}
		risoluzione.setSelected(true);
	}


	@FXML
	private void handleSortByRevenue() {
		//if(incassi.isSelected()){
		sortByRevenue(order);
		checkSelection(incassi);
		//}
		incassi.setSelected(true);
	}

	@FXML
	private void handleSortByBitRate() {
		//if(bit_rate.isSelected()){
		sortByBitRate(order);
		checkSelection(bit_rate);
		//}
		bit_rate.setSelected(true);
	}

	@FXML
	private void handleSortByFps() {
		sortByFps(order);
		checkSelection(fps);

		fps.setSelected(true);
	}

	private void checkSelection(CheckMenuItem cmi){
		for(CheckMenuItem ck:arrMenu){
			if(ck!=cmi)
				ck.setSelected(false);
		}
	}

	private void selectOrder(){
		if(anno.isSelected())
			sortByData(order);
		else if(bit_rate.isSelected())
			sortByBitRate(order);
		else if(budget.isSelected())
			sortByBudget(order);
		else if(dimensione.isSelected())
			sortBySize(order);
		else if(durata.isSelected())
			sortByRuntime(order);
		else if(filename.isSelected())
			sortByFilename(order);
		else if(incassi.isSelected())
			sortByRevenue(order);
		else if(risoluzione.isSelected())
			sortByQuality(order);
		else if(titolo.isSelected())
			sortByTitle(order);
		else if(titolo_originale.isSelected())
			sortByOriginalTitle(order);
		else if(voto.isSelected())
			sortByRating(order);
		else if(fps.isSelected())
			sortByFps(order);
	}


	private void sortByTitle(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;
					
					if(s0.getMovie().getTitle() == null && s1.getMovie().getTitle() == null)
						return 0;
					if(s0.getMovie().getTitle() == null)
						return +1;
					if(s1.getMovie().getTitle() == null)
						return -1;

					String d0 = s0.getMovie().getTitle();
					String d1 = s1.getMovie().getTitle();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}



	private void sortByOriginalTitle(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;
					
					if(s0.getMovie().getOriginal_title() == null && s1.getMovie().getOriginal_title() == null)
						return 0;
					if(s0.getMovie().getOriginal_title() == null)
						return +1;
					if(s1.getMovie().getOriginal_title() == null)
						return -1;

					String d0 = s0.getMovie().getOriginal_title();
					String d1 = s1.getMovie().getOriginal_title();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}



	private void sortByFilename(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;
					
					if(s0.getAbs_path() == null && s1.getAbs_path() == null)
						return 0;
					if(s0.getAbs_path() == null)
						return +1;
					if(s1.getAbs_path() == null)
						return -1;

					String d0 = s0.getAbs_path().substring(s0.getAbs_path().lastIndexOf(Sys.file_separator)+1);
					String d1 = s1.getAbs_path().substring(s1.getAbs_path().lastIndexOf(Sys.file_separator)+1);
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}

	private void sortByBudget(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;
					
					if(s0.getMovie().getBudget() == null && s1.getMovie().getBudget() == null)
						return 0;
					if(s0.getMovie().getBudget() == null)
						return +1;
					if(s1.getMovie().getBudget() == null)
						return -1;

					Integer d0 = s0.getMovie().getBudget();
					Integer d1 = s1.getMovie().getBudget();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}



	private void sortByData(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;
					
					if(s0.getMovie().getRelease_date() == null && s1.getMovie().getRelease_date() == null)
						return 0;
					if(s0.getMovie().getRelease_date() == null)
						return +1;
					if(s1.getMovie().getRelease_date() == null)
						return -1;

					String d0 = s0.getMovie().getRelease_date();
					String d1 = s1.getMovie().getRelease_date();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}



	private void sortByRating(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;
					
					if(s0.getMovie().getVote_average() == null && s1.getMovie().getVote_average() == null)
						return 0;
					if(s0.getMovie().getVote_average() == null)
						return +1;
					if(s1.getMovie().getVote_average() == null)
						return -1;

					Double d0 = (Double) s0.getMovie().getVote_average();
					Double d1 = (Double) s1.getMovie().getVote_average();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}


	private void sortByRuntime(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;

					if(s0.getMovie().getRuntime() == null && s1.getMovie().getRuntime() == null)
						return 0;
					if(s0.getMovie().getRuntime() == null)
						return +1;
					if(s1.getMovie().getRuntime() == null)
						return -1;

					Integer d0 = s0.getMovie().getRuntime();
					Integer d1 = s1.getMovie().getRuntime();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}


	private void sortBySize(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;

					if(s0.getMovie().getSize() == null && s1.getMovie().getSize() == null)
						return 0;
					if(s0.getMovie().getSize() == null)
						return +1;
					if(s1.getMovie().getSize() == null)
						return -1;

					Long d0 = s0.getMovie().getSize();
					Long d1 = s1.getMovie().getSize();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}


	private void sortByQuality(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;

					if((s0.getMovie().getWidth() == null || s0.getMovie().getHeight() == null) && (s1.getMovie().getWidth() == null || s1.getMovie().getHeight() == null))
						return 0;
					if(s0.getMovie().getWidth() == null || s0.getMovie().getHeight() == null)
						return +1;
					if(s1.getMovie().getWidth() == null || s1.getMovie().getHeight() == null)
						return -1;

					Long d0 = s0.getMovie().getWidth()*s0.getMovie().getHeight();
					Long d1 = s1.getMovie().getWidth()*s1.getMovie().getHeight();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}


	private void sortByRevenue(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;
					
					if(s0.getMovie().getRevenue() == null && s1.getMovie().getRevenue() == null)
						return 0;
					if(s0.getMovie().getRevenue() == null)
						return +1;
					if(s1.getMovie().getRevenue() == null)
						return -1;

					Long d0 = s0.getMovie().getRevenue();
					Long d1 = s1.getMovie().getRevenue();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}


	private void sortByBitRate(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;

					if(s0.getMovie().getBit_rate() == null && s1.getMovie().getBit_rate() == null)
						return 0;
					if(s0.getMovie().getBit_rate() == null)
						return +1;
					if(s1.getMovie().getBit_rate() == null)
						return -1;

					Integer d0 = s0.getMovie().getBit_rate();
					Integer d1 = s1.getMovie().getBit_rate();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}

	private void sortByFps(Boolean isInc){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if(s0 == null && s1 == null)
						return 0;
					if(s0 == null)
						return +1;
					if(s1 == null)
						return -1;

					if(s0.getMovie() == null && s1.getMovie() == null)
						return 0;
					if(s0.getMovie() == null)
						return +1;
					if(s1.getMovie() == null)
						return -1;

					if(s0.getMovie().getAvg_frame_rate() == null && s1.getMovie().getAvg_frame_rate() == null)
						return 0;
					if(s0.getMovie().getAvg_frame_rate() == null)
						return +1;
					if(s1.getMovie().getAvg_frame_rate() == null)
						return -1;

					String d0 = s0.getMovie().getAvg_frame_rate();
					String d1 = s1.getMovie().getAvg_frame_rate();
					if(isInc)
						return d0.compareTo(d1);
					else
						return d0.compareTo(d1)*-1;
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}

	@FXML
	private void handleShowNotFound(){
		sortByNotFound();
	}

	private void sortByNotFound(){
		Platform.runLater(() ->{
			ArrayList<MovieButton> tList = new ArrayList<MovieButton>();
			for(Node n: MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
				tList.add((MovieButton) n);
			}
			Collections.sort(tList, new Comparator<MovieButton>() {
				@Override
				public int compare(MovieButton s0, MovieButton s1) {

					if((s0 == null && s1 == null) || (s0.getMovie() == null && s1.getMovie() == null))
						return 0;

					if(s0.getMovie() != null && s0.getMovie().getId() != null)
						return +1;
					if(s1.getMovie() != null && s1.getMovie().getId() != null)
						return -1;

					if(s0.getMovie() != null)
						return +1;
					if(s1.getMovie() != null)
						return -1;
					return 0; //messo per evitare warnings
				}	
			});
			MovieViewController.myMasonryPane.getChildren().clear();
			MovieViewController.myMasonryPane.getChildren().addAll(tList);
		});
	}

	/**
	 * Richiama la funzione statica della classe LocalData che permette di salvare
	 *  tutte le info di tutti i film mostarti correttamente
	 */
	@FXML
	private void handleSave() {
		LocalData.save();
		//Elimino locandine che non sono riferite ai film salvati
		CleanImages.clean();
	}

	/**
	 * Richiama la funzione MovieLoader per un Update dei film mostrati a video
	 */
	@FXML
	private void handleRefresh() {
		System.out.println(ref);
		if(ref==null || !ref.isAlive()){
			System.out.println("Gatooo");
			ref=new Thread(new MovieLoader());
			ref.start();
		}
	}

	/**
	 * Richiama la funzione MovieLoader per un Update dei film mostrati a video
	 */
	@FXML
	private void handleRename() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Rinominare tutti i file");
		alert.setHeaderText("Attenzione, così facendo verranno rinominati"
				+ "\ntutti i Film trovati come in esempio"
				+ "\nes. \"titolofilm anno.estensione\"");
		alert.setContentText("Sicuro di voler rinominare tutti i file?");

		alert.getDialogPane().getStylesheets().add(Setting.CSS_DEFAULT);
		alert.getDialogPane().getStyleClass().add("custom-dialog");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			new Thread(new Rename(false)).start();
		} else {
			// ... user chose CANCEL or closed the dialog
		}
	}

	@FXML
	private void handleRecoverFilename() {
		File f = new File(Setting.BACKUP_FILE_PATH);

		if(f.exists()){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Ripristinare i nomi salvati da backup di tutti i film");
			alert.setHeaderText("Attenzione, così facendo verranno ripristinati\ntutti i nomi dei file che avevano in precedenza");
			alert.setContentText("Sicuro di voler rinominare tutti i file?");

			alert.getDialogPane().getStylesheets().add(Setting.CSS_DEFAULT);
			alert.getDialogPane().getStyleClass().add("custom-dialog");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				new Thread(new Rename(true)).start();
			} else {
				// ... user chose CANCEL or closed the dialog
			}
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Backup di recupero non trovato");
			alert.setHeaderText(null);
			alert.setContentText("Non esiste un backup dei titoli trovati");

			alert.getDialogPane().getStylesheets().add(Setting.CSS_DEFAULT);
			alert.getDialogPane().getStyleClass().add("custom-dialog");

			alert.showAndWait();
		}
	}

	@FXML
	private void handleResearch(){
		mainApp.mvController.setBar(true);
	}

	/**
	 * Opens a settings dialog.
	 */
	@FXML
	private void handleSettings(){
		mainApp.showSettings();
	}

	/**
	 * Opens an info dialog.
	 */
	@FXML
	private void handleAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Spotlight");
		alert.setHeaderText("Informazioni");
		alert.setContentText("Autori: SeekYourMovie Inc.");
		

		alert.getDialogPane().getStylesheets().add(Setting.CSS_DEFAULT);
		alert.getDialogPane().getStyleClass().add("custom-dialog");

		alert.showAndWait();
	}

	@FXML
	private void handleFullScreen(){
		isFull=!isFull;
		mainApp.getPrimaryStage().setFullScreen(isFull);
	}

	@FXML
	private void handleReset(){
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Conferma Reset");
		alert.setHeaderText("Attenzione, premendo \"Si\" verrano ELIMINATI definitivamente\n"
				+"tutti i salvataggi riguardanti film, percorsi aggiunti e preferenze impostate.");
		alert.setContentText("Sei sicuro di procedere?");

		ButtonType buttonTypeOne = new ButtonType("Si");
		ButtonType buttonTypeTwo = new ButtonType("No");

		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);

		alert.getDialogPane().getStylesheets().add(Setting.CSS_DEFAULT);
		alert.getDialogPane().getStyleClass().add("custom-dialog");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOne){
			MovieViewController.myMasonryPane.getChildren().clear();
			MainApp.mvController.showMovieDetails(null, null);
			MainApp.moviePath.clear();
			BarManager.settingCount();
			Setting.LANGUAGE="it-IT";
			Setting.DEEP = 3;
			Setting.RECURSIVE = false;
			Setting.BUTTON_HIGH = 224.0;
			MovieViewController.myMasonryPane.setCellHeight(Setting.BUTTON_HIGH);
			MovieViewController.myMasonryPane.setCellWidth(MovieViewController.myMasonryPane.getCellHeight()*0.66);
			DelDir.delDir(Setting.HOME);
			Setting.settingDir();
			BarManager.reset();  //<----
		}
	}

	@FXML
	private void handleRemoveNotFound(){
		ArrayList<MovieButton> tmpArr=new ArrayList<MovieButton>();

		for(Node n:MovieViewController.myMasonryPane.getChildren()){
			MovieButton tmp1=(MovieButton) n;
			if(tmp1.getMovie()==null)
				tmpArr.add(tmp1);
			else if(tmp1.getMovie().getId()==null)
				tmpArr.add(tmp1);
		}
		Platform.runLater(()->{
			MovieViewController.myMasonryPane.getChildren().removeAll(tmpArr);
			BarManager.settingCount();
		});
	}

	@FXML
	private void handleThereIsFFmpeg(){
		mainApp.showCheckFFmpeg();
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		OnClosing.close();
	}

}
