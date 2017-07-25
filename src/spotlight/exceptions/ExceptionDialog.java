package spotlight.exceptions;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**Classe che contiene il metodo per generare un dialogo di errore
 * @author SYM
 */
public class ExceptionDialog {

	/**Metodo statico che permette di creare un dialog espandibile per visualizzare a schermo l'eccezione prodotta
	 * @param ex eccezione generata
	 */
	public static void show(Exception ex){
		show(ex,null);
	}	

	/**Metodo statico che permette di creare un dialog espandibile per visualizzare a schermo l'eccezione prodotta.
	 * Aggiunta una stringa personalizzabile
	 * @param ex eccezione generata
	 * @param motive stringa personalizzata
	 */
	public static void show(Exception ex,String motive){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setHeaderText("Exception created");
		if(motive == null)
			alert.setContentText("");
		else
			alert.setContentText(motive);

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
}
