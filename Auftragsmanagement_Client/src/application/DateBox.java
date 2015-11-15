package application;

import javafx.stage.*;
import javafx.util.Callback;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import javafx.geometry.*;

public class DateBox {

    //Create variable
    static String answer;

    public static String display(double x, double y, boolean boo) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Versanddatum");
        window.setX(x + 10);
        window.setY(y - 10);
        window.setMinWidth(250);
        window.setMinHeight(225);
        window.setOnCloseRequest(e -> {
        	//Fenster vom schlieﬂen hindern
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
        
        Label label1 = new Label("bitte Best‰tigungsnummer eingeben!");
        TextField tf= new TextField();
        tf.setPrefWidth(100);
        Label label3 = new Label("Die Best‰tigungsnummer fehlt.");
        label3.setTextFill(Color.RED);
        
        Label label2 = new Label("bitte Versanddatum ausw‰hlen!");
        
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now().plusDays(14));
        
        
        Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell()
        {
            @Override
            public void updateItem(LocalDate item, boolean empty)
            {
                super.updateItem(item, empty);

                if(item.isBefore(LocalDate.now())||item.getDayOfWeek()==DayOfWeek.SATURDAY||item.getDayOfWeek()==DayOfWeek.SUNDAY)
                {
                    getStyleClass().add("disabled");
                    setDisable(true);
                }
            }
        };

        datePicker.setDayCellFactory(dayCellFactory);
        VBox layout = new VBox(10);
        
        //Create submit buttons
        Button submit = new Button("Ausw‰hlen");

        //Clicking will set answer and close window
        submit.setOnAction(e -> {
            if(boo){
            	if(!tf.getText().isEmpty()){
            		answer = datePicker.getValue().toString() + " " + tf.getText();
            		window.close();
            	}
            	else{
            		if(!layout.getChildren().contains(label3))layout.getChildren().add(2, label3);
            	}
            }
            else {
            	answer = datePicker.getValue().toString();
            	window.close();
            }
        });

        

        if(boo){
        	layout.getChildren().addAll(label1, tf);
        }
        layout.getChildren().addAll(label2, datePicker, submit);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

}