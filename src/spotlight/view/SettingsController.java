package spotlight.view;

import java.io.File;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXSlider;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import spotlight.main.MainApp;
import spotlight.model.LocalData;
import spotlight.model.MovieLoader;
import spotlight.util.DelDir;
import spotlight.util.MovieButton;
import spotlight.util.Setting;

public class SettingsController {
	private MainApp mainApp;
	@FXML
	private ChoiceBox<String> langChoice;
	@FXML
	private TextField deepText;
	@FXML
	private JFXCheckBox recursiveBox;
	@FXML
	private JFXSlider dimMovieButton;

	private Stage dialogStage;

	public SettingsController() {
	}

	@FXML
	private void initialize() {
		langChoice.setItems(FXCollections.observableArrayList(
				"Italiano (it-IT)", "English (en-US) ", "Spanish (es-ES)", "German (de-DE)"
				));
		for(String s:langChoice.getItems()){
			if(s.contains(Setting.LANGUAGE)){
				langChoice.setValue(s);
				break;
			}	
		}
		deepText.setText(Setting.DEEP.toString());
		recursiveBox.setSelected(Setting.RECURSIVE);
		dimMovieButton.setMin(125.0);
		dimMovieButton.setMax(325.0);
		dimMovieButton.setValue(Setting.BUTTON_HIGH);
	}

	@FXML
	private void handleApply(){
		Platform.runLater(() ->{
			Boolean lenguageRefresh = false;
			Boolean buttonRefresh = false;
			Boolean recursiveRefresh = false;

			String lang = null;
			String deepString = null;
			Integer deep = null;

			if(langChoice.getValue() != null && langChoice.getValue().contains("(") && langChoice.getValue().contains(")") && (langChoice.getValue().indexOf('(')+1 < langChoice.getValue().indexOf(')')))
				lang = langChoice.getValue().substring(langChoice.getValue().indexOf('(')+1, langChoice.getValue().indexOf(')'));

			deepString = deepText.getText().replaceAll("[^0-9]", "");
			if(deepString.length() > 0 && Integer.parseInt(deepString) >= 0)
				deep = Integer.parseInt(deepString);

			if(lang != null && !lang.equals(Setting.LANGUAGE)){
				Setting.LANGUAGE = lang;
				lenguageRefresh = true;
			}

			if(deep != Setting.DEEP && deep >= 0){
				Setting.DEEP = deep;
			}

			if(Setting.RECURSIVE != recursiveBox.isSelected()){
				Setting.RECURSIVE = recursiveBox.isSelected();
				recursiveRefresh = true;
			}

			if(Setting.BUTTON_HIGH != dimMovieButton.getValue()){
				Setting.BUTTON_HIGH = dimMovieButton.getValue();
				buttonRefresh = true;
			}

			if(buttonRefresh || lenguageRefresh){
				if(!lenguageRefresh){
					LocalData.save();
					ObservableList<Node> tList = MovieViewController.myMasonryPane.getChildren();
					MovieViewController.myMasonryPane.getChildren().clear();
					MovieViewController.myMasonryPane.setCellHeight(Setting.BUTTON_HIGH);
					MovieViewController.myMasonryPane.setCellWidth(MovieViewController.myMasonryPane.getCellHeight()*0.66);
					MovieViewController.myMasonryPane.getChildren().addAll(tList);
				} else {
					MovieViewController.myMasonryPane.getChildren().clear();
					MainApp.mvController.showMovieDetails(null, null);
					DelDir.delDirRec(new File(Setting.POSTER_PATH));
					new File(Setting.LOCALDATA_FILE_PATH).delete();
					Setting.settingDir();
				}
			}

			if(!recursiveRefresh){
				Setting.TEMP_ARRAY.clear();
				for(Node n: MovieViewController.myMasonryPane.getChildren())
					Setting.TEMP_ARRAY.add((MovieButton) n);
			}
			new Thread(new MovieLoader()).start();

			LocalData.saveSettings();
		});
		dialogStage.close();
	}

	@FXML
	private void handleCancel(){
		dialogStage.close();
	}

	@FXML
	private void handleChangeDirectory(){

	}
	
	@FXML
	private void handleResetDefault(){
		langChoice.setValue(langChoice.getItems().get(0));
		deepText.setText("3");
		recursiveBox.setSelected(false);
		dimMovieButton.setValue(225.0);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;	
		this.dialogStage.getIcons().add(new Image("file:resources/defaultImages/Sym.png"));
	}
}
