package application;

import java.util.LinkedList;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class CreateBox {
    private static String[] newEntry;

    public static String[] display()
    {
    	Stage window = new Stage();
    	LinkedList<Label> labels= new LinkedList<Label>();
    	LinkedList<Label> labelx= new LinkedList<Label>();
    	LinkedList<TextField> fields= new LinkedList<TextField>();
    	
    	labels.add(new Label("Kunde"));
    	labels.add(new Label("Gegenstand"));
    	labels.add(new Label("Ansprechpartner"));
    	labels.add(new Label("Telefonnummer"));
    	labels.add(new Label("Bearbeiter"));
        
    	labelx.add(new Label("x")); 
    	labelx.add(new Label("x"));
    	labelx.add(new Label("x"));
    	labelx.add(new Label("x"));
    	labelx.add(new Label("x"));
        
    	fields.add(new TextField());
    	fields.add(new TextField());
    	fields.add(new TextField());
    	fields.add(new TextField());
    	fields.add(new TextField());
        Button createButton = new Button("Vorgang erstellen");
                
        GridPane grid = new GridPane();
    	
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("neuer Vorgang");
        window.setMinWidth(250);       
        
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        for(int i=0; i<=4; i++){
        	(labelx.get(i)).setTextFill(Color.RED);
        	(labelx.get(i)).setVisible(false);
        	GridPane.setConstraints(labels.get(i), 0, i);
        	GridPane.setConstraints(fields.get(i), 1, i);
        	GridPane.setConstraints(labelx.get(i), 2, i);
        	grid.getChildren().addAll(labels.get(i), fields.get(i), labelx.get(i));
        } 

        GridPane.setConstraints(createButton, 1, 5);
        
        grid.getChildren().add(createButton);
        
        createButton.setOnAction(e -> 
        {
        	boolean missing = false;

        	for(int i=0; i<=4; i++){
        		if((fields.get(i)).getText().isEmpty()){
        			(labelx.get(i)).setVisible(true);
        			missing=true;
        		}
        		else (labelx.get(i)).setVisible(false);
        	}
        	
        	if (missing == false)
        	{
        		String[] entries = {fields.get(0).getText(), fields.get(1).getText(), fields.get(2).getText(), fields.get(3).getText(), fields.get(4).getText()};
        		newEntry = entries;
        		window.close();
        	}
        }		);

        Scene scene = new Scene(grid, 350, 260);
        window.setScene(scene);
        window.showAndWait();
        return newEntry;
    }
}
