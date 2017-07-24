package spotlight.view;


import java.io.IOException;
import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import spotlight.main.MainApp;
import spotlight.util.Sys;

public class CheckFFmpegController {
	private MainApp mainApp;
	@FXML
	private JFXProgressBar progress;
	@FXML
	private Label label;
	@FXML
	private TextField command;
	@FXML
	private AnchorPane mainAnchor;

	private Stage dialogStage;

	public CheckFFmpegController() {
	}

	@FXML
	private void initialize() {
		try {
			if(Sys.os_name.toLowerCase().contains("mac"))
				Runtime.getRuntime().exec("/usr/local/Cellar/ffmpeg/3.3.2/bin/ffprobe -version");
			else
				new ProcessBuilder("ffprobe","-version").start();
			label.setText("FFmpeg è correttamente installato su questo PC!");
			label.setStyle("-fx-font-size: 13pt; -fx-font-family: \"Segoe UI Semibold\"; -fx-text-fill: white;  -fx-opacity: 0.9;");
			command.setVisible(false);
			mainAnchor.setPrefHeight(100.);
			mainAnchor.setBottomAnchor(label, 30.);
			
		} catch (IOException e) {
			
			if(Sys.os_name.toLowerCase().contains("win")){
				label.setText("FFmpeg non è attualmente installato su questo PC,\nper installarlo seguire questa guda:");
				command.setText("http://adaptivesamples.com/how-to-install-ffmpeg-on-windows/");
				mainAnchor.setPrefHeight(150.);
			}
			else if(Sys.os_name.toLowerCase().contains("mac")){
				label.setText("FFmpeg non è attualmente installato su questo PC,\nper installarlo incolla questo comando sul terminale");
				command.setText("/usr/bin/ruby -e \"$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)\"; brew install ffmpeg");
			}
			else {
				label.setText("FFmpeg non è attualmente installato su questo PC,\nper installarlo incolla questo comando sul terminale");
				command.setText("sudo apt install ffmpeg");
			}
		}
	}

	@FXML
	private void handleOk(){
		dialogStage.close();
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;	
		this.dialogStage.getIcons().add(new Image("file:resources/defaultImages/Sym.png"));
	}
}
