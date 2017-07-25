package spotlight.model;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import spotlight.util.MovieButtonDuplicate;

public class CheckDuplicate implements Runnable {
	private HBox hBox;

	/**
	 * Funzione che rimuove tutti gli elementi che non hanno duplicati
	 * @param hBox di cui deve controllare i duplicati
	 */
	public CheckDuplicate(HBox hBox) {
		this.hBox = hBox;
	}

	@Override
	public void run() {
		Platform.runLater(() -> {
			int count;
			MovieButtonDuplicate tmpBut1, tmpBut2;
			for (Node n : hBox.getChildren()) {
				count = 0;
				tmpBut1 = (MovieButtonDuplicate) n;
				for (Node k : hBox.getChildren()) {
					tmpBut2 = (MovieButtonDuplicate) k;
					if (tmpBut1.getMovie().getTitle().equals(tmpBut2.getMovie().getTitle()))
						count++;
				}
				if (count == 1) {
					hBox.getChildren().remove(tmpBut1);
					break;
				}
			}
		});

	}

}
