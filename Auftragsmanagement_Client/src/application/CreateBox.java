package application;

import java.util.LinkedList;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class CreateBox {
    private static String[] newEntry;
    private static ComboBox<String> customerBox, partnerBox;
    private static ObservableList<String> partnerInfo;
    private static Map<String, String> infoList;
    private static LinkedList<Label> labels, labelx;
    private static LinkedList<Node> fields;
    private static Stage window;
    private static boolean ready;

    public static String[] display()
    {
    	partnerInfo = FXCollections.observableArrayList();
    	window = new Stage();
    	labels= new LinkedList<Label>();
    	labelx= new LinkedList<Label>();
    	fields= new LinkedList<Node>();
    	
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
        
    	customerBox = new ComboBox<String>();
    	customerBox.setItems(ClientGUI.customers);
    	
    	fields.add(customerBox);
    	
    	TextField itemField = new TextField();
    	fields.add(itemField);
    	
    	partnerBox = new ComboBox<String>();
    	partnerBox.setItems(partnerInfo);
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
    	
    	partnerBox.setOnAction(e ->{
    		if(infoList.containsKey(partnerBox.getValue())){
    			((TextField)fields.get(3)).setText(infoList.get(partnerBox.getValue()));
    		}
    	});
    	

    	new AutoCompleteComboBoxListener(customerBox, partnerBox, (TextField)fields.get(3));
    	
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
        
        createButton.setOnAction(e -> submit());
        createButton.setOnKeyReleased(e -> {
        	if(e.getCode()==KeyCode.ENTER){
        		submit();
        	}
        });

        Scene scene = new Scene(grid, 350, 260);
        window.setScene(scene);
        window.showAndWait();
        return newEntry;
    }
    
    private static void submit(){
    	{
        	boolean missing = false;
        	if(customerBox.getValue()==null||customerBox.getValue().isEmpty()){
        		(labelx.get(0)).setVisible(true);
    			missing=true;
        	}
        	else (labelx.get(0)).setVisible(false);
        	
        	if(((TextField)fields.get(1)).getText().isEmpty()){
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
        }
    }
    
    public static boolean getReady(){
    	return ready;
    }
    
    public static void setReady(boolean ready){
    	CreateBox.ready = ready;
    }
	
	public static void setPartnerInfo(Map<String, String> infoList){
		//int i=0;
		while(!partnerInfo.isEmpty()){
			partnerInfo.remove(0);
		}
		CreateBox.infoList = infoList;
		for(String info: infoList.keySet())
		{		
			partnerInfo.add(info);
		}
		if(!partnerInfo.isEmpty())CreateBox.partnerBox.setValue(partnerInfo.get(0));
	}
}
