package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ChoiceBox {

    //Create variable
    static String answer;

    public static String display(double x, double y, String choiceA, String choiceB) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Weiterführen");
        window.setX(x + 10);
        window.setY(y - 10);
        window.setMinWidth(250);
        window.setHeight(120);
        window.setOnCloseRequest(e -> {
        	//Fenster vom schließen hindern
        	try
        	{
        		answer = "";
        	}
        	catch(Exception a)
        	{
        		System.out.println(a);
    			a.printStackTrace();
        	}
        });

        //Create two buttons
        Button buttonA = new Button(choiceA);
        Button buttonB = new Button(choiceB);

        //Clicking will set answer and close window
        buttonA.setOnAction(e -> {
            answer = choiceA;
            window.close();
        });
        buttonB.setOnAction(e -> {
            answer = choiceB;
            window.close();
        });

        VBox layout = new VBox(10);

        //Add buttons
        layout.getChildren().addAll(buttonA, buttonB);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

}