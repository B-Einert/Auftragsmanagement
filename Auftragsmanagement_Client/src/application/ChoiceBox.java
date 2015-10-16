package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class ChoiceBox {

    //Create variable
    static int answer;

    public static int display(double x, double y, String choiceA, String choiceB, String choiceC) {
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
        		answer = -1;
        	}
        	catch(Exception a)
        	{
        		System.out.println(a);
    			a.printStackTrace();
        	}
        });

        //Create two buttons
        Button buttonA = new Button(choiceA);
        Button buttonC = new Button(choiceC);

        //Clicking will set answer and close window
        buttonA.setOnAction(e -> {
            answer = 0;
            window.close();
        });
        buttonC.setOnAction(e -> {
            answer = 2;
            window.close();
        });

        VBox layout = new VBox(10);

        //Add buttons
        layout.getChildren().add(buttonA);
        if (choiceB!=null){
        	window.setHeight(180);
        	Button buttonB = new Button(choiceB);
        	buttonB.setOnAction(e -> {
                answer = 1;
                window.close();
            });
            layout.getChildren().add(buttonB);
        }
        layout.getChildren().add(buttonC);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

}