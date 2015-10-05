package application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class CreateBox {
	
	private Stage window = new Stage();
	
	private Label customer = new Label("Kunde");
    private Label item = new Label("Gegenstand");
    private Label contactPerson = new Label("Ansprechpartner");
    private Label phone = new Label("Telefonnummer");
    private Label agent = new Label("Bearbeiter");
    
    private Label custx = new Label("x"); 
    private Label itemx =new Label("x");
    private Label contx =new Label("x");
    private Label phonx =new Label("x");
    private Label agentx =new Label("x");
    
    private TextField customerIn = new TextField();
    private TextField itemIn = new TextField();
    private TextField contactIn = new TextField();
    private TextField phoneIn = new TextField();
    private TextField agentIn = new TextField();
    private Button createButton = new Button("Vorgang erstellen");
    
    private GridPane grid = new GridPane();

    
    public CreateBox()
    {
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("neuer Vorgang");
        window.setMinWidth(250);
        
        custx.setTextFill(Color.RED);
        itemx.setTextFill(Color.RED);
        contx.setTextFill(Color.RED);
        phonx.setTextFill(Color.RED);
        agentx.setTextFill(Color.RED);
        custx.setVisible(false);
        itemx.setVisible(false);
        contx.setVisible(false);
        phonx.setVisible(false);
        agentx.setVisible(false);
        
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(8);
        grid.setHgap(10);
        
        GridPane.setConstraints(customer, 0, 0);
        GridPane.setConstraints(item, 0, 1);
        GridPane.setConstraints(contactPerson, 0, 2);
        GridPane.setConstraints(phone, 0, 3);
        GridPane.setConstraints(agent, 0, 4);
        
        GridPane.setConstraints(customerIn, 1, 0);
        GridPane.setConstraints(itemIn, 1, 1);
        GridPane.setConstraints(contactIn, 1, 2);
        GridPane.setConstraints(phoneIn, 1, 3);
        GridPane.setConstraints(agentIn, 1, 4);
        
        GridPane.setConstraints(custx, 2, 0);
        GridPane.setConstraints(itemx, 2, 1);
        GridPane.setConstraints(contx, 2, 2);
        GridPane.setConstraints(phonx, 2, 3);
        GridPane.setConstraints(agentx, 2, 4);

        
        GridPane.setConstraints(createButton, 1, 5);
        grid.getChildren().addAll(customer, customerIn, item, itemIn, contactPerson, contactIn, phone, phoneIn, agent, agentIn, createButton, custx, itemx, contx, phonx, agentx);
        
        createButton.setOnAction(e -> createButtonClicked());

        Scene scene = new Scene(grid, 350, 260);
        window.setScene(scene);
        window.showAndWait();
    }
    
    public void createButtonClicked(){
    	boolean missing = false;
    	
    	if (customerIn.getText().isEmpty()){
    		custx.setVisible(true);
    		missing = true;
    	}
    	else custx.setVisible(false);
    	
    	if (itemIn.getText().isEmpty()){
    		itemx.setVisible(true);    		
    		missing = true;
    	}
    	else itemx.setVisible(false);
    	
    	if (contactIn.getText().isEmpty()){
    		contx.setVisible(true);
    		missing = true;
    	}
    	else contx.setVisible(false);
    	
    	if (phoneIn.getText().isEmpty()){
    		phonx.setVisible(true);
    		missing = true;
    	}
    	else phonx.setVisible(false);
    	
    	if (agentIn.getText().isEmpty()){
    		agentx.setVisible(true);
    		missing = true;
    	}
    	else agentx.setVisible(false);
    	
    	if (missing == false)
    	{
    		ClientGUI.sender.sendString("xyz");
    		window.close();
    	}
    }
}
