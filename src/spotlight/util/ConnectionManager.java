package spotlight.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ConnectionManager implements Runnable {
	boolean showed=false;

	//	@Override
	//	public void run() {
	//		if(!Check.Connection()){
	//			Platform.runLater(()->{
	//				// Create the dialog Stage.
	//				Alert alert = new Alert(AlertType.WARNING);
	//				alert.setTitle("Nessuna connessione");
	//				alert.setHeaderText("Non connesso a Internet");
	//				alert.setContentText("Verificare la connessione Internet");
	//				alert.showAndWait();
	//				
	//			});
	//		}
	//
	//		try {
	//			Thread.sleep(5000);
	//			run();
	//		} catch (InterruptedException e) {
	//			e.printStackTrace();
	//		}
	//
	//
	//	}
	@Override
	public void run() {
		while(true){
			if(Check.Connection())
				BarManager.updateConnection(true);
			else
				BarManager.updateConnection(false);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
