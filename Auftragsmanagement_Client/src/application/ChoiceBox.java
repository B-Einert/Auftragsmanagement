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

    public static int display(double x, double y, String choiceA, String choiceB, String choiceC) {
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Weiterführen");
        window.setX(x + 10);
        window.setY(y - 10);
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
        Button buttonB = new Button(choiceB);
        Button buttonC = new Button(choiceC);

        //Clicking will set answer and close window
        buttonB.setOnAction(e -> bb());
        buttonB.setOnKeyReleased(e -> {
        	if(e.getCode()==KeyCode.ENTER){
        		bb();
        	}
        });
        buttonC.setOnAction(e -> bc());
        buttonC.setOnKeyReleased(e -> {
        	if(e.getCode()==KeyCode.ENTER){
        		bc();
        	}
        });

        VBox layout = new VBox(10);

        //Add buttons
        if (choiceA!=null){
        	window.setHeight(180);
        	Button buttonA = new Button(choiceA);
        	buttonA.setOnAction(e -> ba());
            buttonA.setOnKeyReleased(e -> {
            	if(e.getCode()==KeyCode.ENTER){
            		ba();
            	}
            });
            layout.getChildren().add(buttonA);
        }
        layout.getChildren().add(buttonB);
        layout.getChildren().add(buttonC);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
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
    
    private static void bc(){
    	answer = 2;
        window.close();
	}

}