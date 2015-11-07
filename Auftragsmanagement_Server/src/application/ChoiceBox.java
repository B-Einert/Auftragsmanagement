package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ChoiceBox {

    //Create variable
    static int answer;

    public static int display(String choiceA, String choiceB) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Programm schlie�en");
        window.setMinWidth(250);
        window.setHeight(120);
        window.setOnCloseRequest(e -> {
        	//Fenster vom schlie�en hindern
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
        Label label = new Label("M�chten Sie das Programm wirklich beenden?");
        Button buttonA = new Button(choiceA);
        Button buttonB = new Button(choiceB);

        //Clicking will set answer and close window
        buttonA.setOnAction(e -> {
            answer = 0;
            window.close();
        });
        buttonB.setOnAction(e -> {
            answer = 1;
            window.close();
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

}