package spotlight.util;

import javafx.application.Platform;
import javafx.scene.Node;
import spotlight.view.MovieViewController;

public class BarManager {
	private static MovieViewController mvc = null;
	private static Double counter = 0.;
	private static Double total = 0.;

	public BarManager(MovieViewController mvc){
		this.mvc = mvc;
	}

	public static void reset(){
		counter = 0.;
		total = 0.;
	}

	public synchronized static void sumTotal(Integer toSum){
		total += new Double(toSum);
		if(counter >= total && total != 0.){
			Boolean c = Check.Connection();

			if(mvc != null){
				Platform.runLater(()->{
					mvc.setStatusBar(null, c, 1.0);
				});
			}

			reset();
		}
	}

//	public static void subTotal(Integer toSum){
//		counter -= new Double(toSum);
//		total -= new Double(toSum);
//		if(counter >= total){
//			Boolean c = Check.Connection();
//
//			if(mvc != null){
//				Platform.runLater(()->{
//					mvc.setStatusBar("Completato", c, 1.0);
//				});
//			}
//
//			reset();
//		}
//	}

	public synchronized static void addMessage(String msg){
		Boolean c = Check.Connection();
		counter++;
		if(counter > total)
			counter = total;
		if(mvc != null){
			Platform.runLater(()->{
				mvc.setStatusBar(msg, c, counter/total);
				if(counter >= total){
					reset();
				}
			});
		}
	}
	
	public synchronized static void justMsg(String msg){
		Boolean c = Check.Connection();
		if(counter > total)
			counter = total;
		Platform.runLater(()->{
			mvc.setStatusBar(msg, c, counter/total);
		});
	}

	public synchronized static void addNull(){
		Boolean c = Check.Connection();
		counter++;
		if(counter > total)
			counter = total;
		Platform.runLater(()->{
			mvc.setStatusBar(null, c, counter/total);
		});
	}
	
	public synchronized static void addNull(Integer i){
		Boolean c = Check.Connection();
		counter += i;
		if(counter > total)
			counter = total;
		Platform.runLater(()->{
			mvc.setStatusBar(null, c, counter/total);
		});
	}

	public static void updateConnection(Boolean status){
		if(mvc != null){
			if(status){
				Platform.runLater(()->{
					mvc.setStatusBar(null, status, null);
				});
			}
			else{
				Platform.runLater(()->{
					mvc.setStatusBar(null, status, null);
				});
			}
		}

	}

	public synchronized static void settingCount(){
		int count=0;
		for(Node n:MovieViewController.myMasonryPane.getChildrenUnmodifiable()){
			MovieButton tmp=(MovieButton) n;
			if(tmp.getMovie() != null && tmp.getMovie().getId() == null)	//è stato modificato
				count++;
		}
		if(count==0)
			mvc.setStatusBar("Film visualizzati: "+MovieViewController.myMasonryPane.getChildrenUnmodifiable().size(), Check.Connection(), null);
		else
			mvc.setStatusBar("Film visualizzati: "+MovieViewController.myMasonryPane.getChildrenUnmodifiable().size()+" - Non trovati: "+count, Check.Connection(), null);

	}
}
