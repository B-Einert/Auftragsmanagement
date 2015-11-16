package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.geometry.*;

public class ChoiceBox {

    //Create variable
    static int answer;
    private static Stage window;

    public static int display(String choiceA, String choiceB) {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Programm schließen");
        window.setMinWidth(250);
        window.setHeight(120);
        window.setOnCloseRequest(e -> {
        	//Fenster vom schließen hindern
        	try
        	{
        		answer = 1;
        	}
        	catch(Exception a)
        	{
        		System.out.println(a);
    			a.printStackTrace();
        	}
        });

        //Create two buttons
        Label label = new Label("Möchten Sie das Programm wirklich beenden?");
        Button buttonA = new Button(choiceA);
        Button buttonB = new Button(choiceB);

        //Clicking will set answer and close window
        buttonA.setOnAction(e -> ba());
        buttonA.setOnKeyReleased(e -> {
        	if(e.getCode()==KeyCode.ENTER){
        		ba();
        	}
        });
        buttonB.setOnAction(e -> bb());
        buttonB.setOnKeyReleased(e -> {
        	if(e.getCode()==KeyCode.ENTER){
        		bb();
        	}
        });

        VBox layout = new VBox(10);
        HBox hbox = new HBox(10);
        
        hbox.getChildren().addAll(buttonA, buttonB);
        layout.getChildren().add(label);
        layout.getChildren().add(hbox);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }
    
    private static void ba(){
    	answer = 0;
        window.close();
	}
    
    private static void bb(){
    	answer = 1;
        window.close();
	}

}