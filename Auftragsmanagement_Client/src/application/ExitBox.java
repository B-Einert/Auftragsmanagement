package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ExitBox {
	
	//Create variable
    static int answer;
    private static Stage window;

    public static int display(String string) {
    	window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Meldung");
        window.setMinWidth(250);
        window.setHeight(120);
        window.setOnCloseRequest(e -> {
        	//Fenster vom schließen hindern
        	try
        	{
        		ClientReceiver.DISCONNECT();
        		answer = -1;
        		window.close();
        	}
        	catch(Exception a)
        	{
        		System.out.println(a);
    			a.printStackTrace();
        	}
        });

        //Create two buttons
        Button buttonA = new Button("OK");

        //Clicking will set answer and close window
        buttonA.setOnAction(e -> ba());
        buttonA.setOnKeyReleased(e -> {
        	if(e.getCode()==KeyCode.ENTER){
        		ba();
        	}
        });
        	

        VBox layout = new VBox(10);

        layout.getChildren().addAll(new Label(string), new Label("Bitte starten Sie das Programm neu!"), buttonA);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

	private static void ba(){
		try {
			ClientReceiver.DISCONNECT();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        answer = 1;
        window.close();
	}
	
	

}