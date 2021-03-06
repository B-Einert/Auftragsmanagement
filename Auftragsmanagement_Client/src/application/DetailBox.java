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
	private static boolean ready = false;
	
	public static void display()
    {
    	Stage window = new Stage();
    	window.setTitle("Details " + details.get(0) + "_" + details.get(1));
//        window.setWidth(400); 
//        window.setHeight(300);
        
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
    	Label link=new Label(ClientGUI.datenbank + details.get(2));
    	entries.add(link);
    	entries.add(new Label(details.get(3)));
    	entries.add(new Label(details.get(4)));
    	entries.add(new Label(details.get(5)));
    	
    	if(!details.get(6).contentEquals("")){
      	   labels.add(new Label("Artikelnummer"));
      	   entries.add(new Label(details.get(6)));
         }
    	if(!details.get(7).contentEquals("")){
     	   labels.add(new Label("ABN"));
     	   entries.add(new Label(details.get(7)));
        }
    	                
        VBox left= new VBox();
        left.setMinWidth(150);
        left.setPadding(new Insets(0, 10, 10, 10));
        left.setSpacing(5);
        VBox right=new VBox();
        right.setPadding(new Insets(0, 10, 10, 10));
        right.setSpacing(5);
        HBox up = new HBox();
        
       left.getChildren().addAll(labels);
       right.getChildren().addAll(entries);
       up.getChildren().addAll(left, right);
        
        ListView<String> list = new ListView<String>();
        list.setPrefHeight(24*5);
        for(int i=8; i<details.size(); i++){
        	if(details.get(i)!=null)list.getItems().add(details.get(i));
        	else break;
        }
        try{
        	list.scrollTo(list.getItems().size() - 1);
        }
        catch (IllegalStateException oi){
        	System.out.println(oi);
        }
        
        VBox vbox=new VBox();
        vbox.getChildren().addAll(up, list);
        vbox.setPadding(new Insets(25));
        vbox.setPrefWidth(500);

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
