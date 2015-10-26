package application;

import java.util.LinkedList;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DetailBox {
	private static LinkedList<String> details;
	private static boolean ready;
	
	public static void display()
    {
    	Stage window = new Stage();
    	window.setTitle("Details " + details.get(0) + "_" + details.get(1));
        window.setWidth(400); 
        window.setHeight(300);
        
    	LinkedList<Label> labels= new LinkedList<Label>();
    	LinkedList<Label> entries= new LinkedList<Label>();    	
    	labels.add(new Label("Kunde"));
    	labels.add(new Label("Gegenstand"));
    	labels.add(new Label("Link"));
    	Label partner = new Label("Ansprechpartner");
    	partner.minWidthProperty();
    	labels.add(partner);
    	labels.add(new Label("Telefonnummer"));
    	labels.add(new Label("Bearbeiter"));
        
    	entries.add(new Label(details.get(0))); 
    	entries.add(new Label(details.get(1)));
    	Label link=new Label(details.get(2));
    	entries.add(link);
    	entries.add(new Label(details.get(3)));
    	entries.add(new Label(details.get(4)));
    	entries.add(new Label(details.get(5)));
    	                
        VBox left= new VBox();
        left.setMinWidth(130);
        left.setPadding(new Insets(10, 10, 10, 10));
        left.setSpacing(5);
        VBox right=new VBox();
        right.setPadding(new Insets(10, 10, 10, 10));
        right.setSpacing(5);
        HBox up = new HBox();
        
       left.getChildren().addAll(labels);
       right.getChildren().addAll(entries);
       up.getChildren().addAll(left, right);
        
        ListView<String> list = new ListView<String>();
        for(int i=6; i<details.size(); i++){
        	if(details.get(i)!=null)list.getItems().add(details.get(i));
        	else break;
        }
        
        VBox vbox=new VBox();
        vbox.getChildren().addAll(up, list);

        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.show();
    }
	
	public static void setDetails(LinkedList<String> details){
		DetailBox.details=details;
	}
	
	public static boolean getReady(){
		return ready;
	}
	public static void setReady(boolean ready){
		DetailBox.ready = ready;
	}
}
