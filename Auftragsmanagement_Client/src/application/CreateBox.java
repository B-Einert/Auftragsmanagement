package application;

import java.util.LinkedList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
    	LinkedList<Node> fields= new LinkedList<Node>();
    	
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
        
    	ComboBox<String> customerBox = new ComboBox<String>();
    	customerBox.setItems(ClientGUI.customers);
    	new AutoCompleteComboBoxListener(customerBox);
    	
    	fields.add(customerBox);
    	
    	TextField itemField = new TextField();
    	fields.add(itemField);
    	
    	ComboBox<String> partnerBox = new ComboBox<String>();
    	partnerBox.setEditable(true);
    	partnerBox.setOnKeyPressed(e->{
    		partnerBox.hide();
    	});
    	partnerBox.setOnKeyReleased(e -> {
    		partnerBox.setValue(partnerBox.getEditor().getText());
    	});
    	fields.add(partnerBox);
    	fields.add(new TextField());
    	fields.add(new TextField());
        Button createButton = new Button("Vorgang erstellen");
                
        GridPane grid = new GridPane();
    	
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("neuer Vorgang");
        window.setMinWidth(250);     
        
        window.setOnCloseRequest(e -> {
        	//Fenster vom schlieﬂen hindern
        	try
        	{
        		String[] entries={""};
        		newEntry = entries;
        	}
        	catch(Exception a)
        	{
        		System.out.println(a);
    			a.printStackTrace();
        	}
        });
        
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
        	if(customerBox.getValue()==null||customerBox.getValue().isEmpty()){
        		(labelx.get(0)).setVisible(true);
    			missing=true;
        	}
        	else {
        		(labelx.get(0)).setVisible(false);
        	}
        	
        	if(itemField.getText().isEmpty()){
        		(labelx.get(1)).setVisible(true);
    			missing=true;
        	}
        	else (labelx.get(1)).setVisible(false);
        	
        	if(partnerBox.getValue()==null||partnerBox.getValue().isEmpty()){
        		(labelx.get(2)).setVisible(true);
    			missing=true;
        	}
        	else (labelx.get(2)).setVisible(false);
        	
        	for(int i=3; i<=4; i++){
        		if(((TextField)fields.get(i)).getText().isEmpty()){
        			(labelx.get(i)).setVisible(true);
        			missing=true;
        		}
        		else (labelx.get(i)).setVisible(false);
        	}
        	
        	if (missing == false)
        	{
        		String[] entries = {(String)customerBox.getValue(), ((TextField)fields.get(1)).getText(), (String)partnerBox.getValue() ,((TextField)fields.get(3)).getText(), ((TextField)fields.get(4)).getText()};
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
