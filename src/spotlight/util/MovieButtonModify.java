package spotlight.util;


import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import spotlight.main.MainApp;
import spotlight.model.Movie;
import spotlight.view.ModifyMovieController;

public class MovieButtonModify extends MovieButton{
	ModifyMovieController mmController;
	
	public MovieButtonModify(Movie movie, ModifyMovieController mmController) {
		super(null, MainApp.mvController);
		this.mmController=mmController;
		this.setMinWidth(125);
		this.setPrefSize(125, 200);
		setOnClick();
	}
	
	private void setOnClick(){
		this.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
		    public void handle(MouseEvent mouseEvent) {
		        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
		            if(mouseEvent.getClickCount() == 1){
		            	mmController.setNewMovie(movie, null);
		            }
		        }
		    }
		});
	}

	@Override
	protected void setContMenu() {
	}

	

}
