package spotlight.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import com.jfoenix.controls.JFXMasonryPane;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import spotlight.exceptions.ExceptionDialog;
import spotlight.main.MainApp;
import spotlight.util.Analyze;
import spotlight.util.BarManager;
import spotlight.util.Check;
import spotlight.util.MovieButton;
import spotlight.util.MovieButtonWrapper;
import spotlight.util.SearchMovie;
import spotlight.util.Setting;
import spotlight.util.Sys;
import spotlight.view.MovieViewController;

public class MovieLoader implements Runnable {
	private JFXMasonryPane myMasonryPane;
	private ObservableList<File> moviePath;
	private MovieViewController mvController;
	private String newDirectory = null;

	public MovieLoader() {
		super();
		this.mvController = MainApp.mvController;
		this.myMasonryPane = MovieViewController.myMasonryPane;
		this.moviePath = MainApp.moviePath;
	}

	public MovieLoader(String newDirectory) {
		super();
		this.mvController = MainApp.mvController;
		this.myMasonryPane = MovieViewController.myMasonryPane;
		this.moviePath = MainApp.moviePath;
		this.newDirectory = newDirectory;
	}

	@Override
	public void run() {
		NameFileFinder nff = new NameFileFinder();
		ArrayList<MovieButton> buttonList = new ArrayList<MovieButton>();
		Boolean flag = null;
		Boolean restart = false;
		Movie tempMovie = null;
		ArrayList<Movie> mArray = LocalData.load();
		ObservableList<Node> obList = myMasonryPane.getChildrenUnmodifiable();
		ArrayList<String> wrap = MovieButtonWrapper.wrap(obList);
		ArrayList<String> wrapNull = MovieButtonWrapper.wrapNull(obList);
		ArrayList<String> wrapLoad = MovieButtonWrapper.wrapLoad(mArray);
		MovieButton movieButton;
		Integer counter = 0;

		ClearNotNeededButton();

		if (Setting.TEMP_ARRAY.size() == 0) {

			// aggiungo tutti i file di tutti i path
			if (newDirectory == null) {
				for (File f : moviePath)
					nff.listDirectory(f, Setting.RECURSIVE);
			} else {
				nff.listDirectory(new File(newDirectory), Setting.RECURSIVE);
			}

			// aggiungo per ogni file trovato un bottone
			for (String s : nff.getFilmList()) {

				// se sto aggiungendo un bottone che si riferisce ad un nuovo file creo il
				// bottone altrimenti metto il bottone già creato
				if (wrap.contains(s))
					movieButton = (MovieButton) obList.get(wrap.indexOf(s));
				else
					movieButton = new MovieButton(s, mvController);

				if (wrapLoad.contains(s)) {
					// controllo se è stato salvato il film che sto cercando
					if (mArray.get(wrapLoad.indexOf(s)).getMovie_path().equals(s))
						movieButton.setMovie(mArray.get(wrapLoad.indexOf(s)));

				} else if (wrapNull.contains(s) || !wrap.contains(s)) {
					// controllo se esiste un bottone con lo stesso path
					buttonList.add(movieButton);
				}
				addButton(movieButton);
			}
		} else {
			for (MovieButton mb : Setting.TEMP_ARRAY) {
				if (mb != null && mb.getMovie() == null) {
					buttonList.add(mb);
				}
			}
			Setting.TEMP_ARRAY.clear();
		}

		BarManager.sumTotal(buttonList.size());
		if (buttonList.size() != 0) {
			if (newDirectory != null)
				BarManager.justMsg("inizio caricamento dei fin in: " + "\"" + newDirectory + "\"");
			else
				BarManager.justMsg("Inizio caricamento dei film");
		}

		for (MovieButton mButton : buttonList) {
			this.moviePath = MainApp.moviePath;
			System.out.println("è cosi\t" + newDirectory);
			System.out.println(moviePath);
			if (newDirectory == null) {
				flag = false;
				for (File path : moviePath) {
					if (mButton.getAbs_path().contains(path.getAbsolutePath() + Sys.file_separator)) {
						flag = true;
						break;
					}
				}
			} else {
				if (moviePath.contains(new File(newDirectory))) {
					System.out.println("è contenuto\t" + newDirectory);
					flag = true;
				} else {
					System.out
							.println("non è contenuto\t" + newDirectory + "\n\n" + buttonList.size() + "\n" + counter);
					BarManager.addNull(buttonList.size() - counter + 1);
					break;
				}
			}

			if (flag) { // se il film è contenuto tra i path cercati

				while (!Check.Connection()) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						ExceptionDialog.show(e);
						e.printStackTrace();
					}
				}

				System.out.println("Inizio ricerca del Film: "
						+ mButton.getAbs_path().substring(mButton.getAbs_path().lastIndexOf(Sys.file_separator) + 1));
				if (Check.Connection())
					tempMovie = SearchMovie.TMDb(Analyze.Filename(mButton.getAbs_path()));
				else {
					restart = true;
					while (!Check.Connection()) {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							ExceptionDialog.show(e);
							e.printStackTrace();
						}
					}
					tempMovie = SearchMovie.TMDb(Analyze.Filename(mButton.getAbs_path()));
				}

				if (tempMovie != null) {
					MainApp.needSave = true;
					counter++;
					new Thread(new DownloadAndUpdate(mButton, tempMovie)).start();
				} else {
					counter++;
					BarManager.addMessage("Non trovato: " + mButton.getAbs_path()
							.substring(mButton.getAbs_path().lastIndexOf(Sys.file_separator) + 1));
				}
			} else {
				counter++;
				BarManager.addNull();
			}
		}

		// BarManager.subTotal(buttonList.size());

		if (restart) {
			while (!Check.Connection()) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					ExceptionDialog.show(e);
					e.printStackTrace();
				}
			}
			if (newDirectory != null)
				new Thread(new MovieLoader(newDirectory)).start();
			else
				new Thread(new MovieLoader()).start();
		}
	}

	/**
	 * Elimina tutti i bottoni per cui non esiste un file associato
	 */
	private void ClearNotNeededButton() {
		MovieButton tmp = null;
		Iterator<Node> it = MovieViewController.myMasonryPane.getChildren().iterator();
		while (it.hasNext()) {
			tmp = (MovieButton) it.next();
			if (tmp != null && tmp.getMovie() != null && tmp.getMovie().getMovie_path() != null) {
				if (!(new File(tmp.getMovie().getMovie_path()).exists())) {
					RemoveLater(tmp);
				}
			}
		}
	}

	private void RemoveLater(Node n) {
		Platform.runLater(() -> {
			MovieViewController.myMasonryPane.getChildren().remove(n);
		});
	}

	private void addButton(MovieButton movieButton) {
		// "Platform.runLater.." è necessario per avviare un thread che modifica la UI
		// del programma
		// Bisogna usare il metodo "getChildrenUnmodifiable()" per evitare un eccezione
		// dovuta al fatto che il programma crede di

		// Tutto quello che avviene dentro il "Platform.runLater" viene eseguito in un
		// altro momento
		Platform.runLater(() -> {
			if (!MovieButtonWrapper.wrap(myMasonryPane.getChildrenUnmodifiable()).contains(movieButton.getAbs_path())) {
				myMasonryPane.getChildren().add(movieButton);
			}
		});
	}

}
