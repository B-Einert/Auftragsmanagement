package application;

import javafx.stage.*;
import javafx.util.Callback;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.time.DayOfWeek;
import java.time.LocalDate;

import javafx.geometry.*;

public class DateBox {

    //Create variable
    static String answer;

    public static String display(double x, double y) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Versanddatum");
        window.setX(x + 10);
        window.setY(y - 10);
        window.setMinWidth(250);
        window.setHeight(150);
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
        
        Label label = new Label("bitte Versanddatum ausw‰hlen!");
        
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        
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
        
        //Create submit buttons
        Button submit = new Button("Ausw‰hlen");

        //Clicking will set answer and close window
        submit.setOnAction(e -> {
            answer = datePicker.getValue().toString();
            window.close();
        });

        VBox layout = new VBox(10);

        
        layout.getChildren().addAll(label, datePicker, submit);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();

        //Make sure to return answer
        return answer;
    }

}