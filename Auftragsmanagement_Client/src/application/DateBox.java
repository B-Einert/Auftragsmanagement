package application;

import javafx.stage.*;
import javafx.util.Callback;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.control.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.*;

public class DateBox {

	// Create variable
	static String answer;
	private static DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy");

	public static String display(double x, double y, boolean boo, String los) {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Versanddatum");
		window.setX(x + 10);
		window.setY(y - 10);
		window.setMinWidth(250);
		window.setMinHeight(225);
		window.setOnCloseRequest(e -> {
			// Fenster vom schließen hindern
			try {
				answer = "";
			} catch (Exception a) {
				System.out.println(a);
				a.printStackTrace();
			}
		});

		Label label1 = new Label("ABN eingeben!");
		TextField tf = new TextField();
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (newValue.length() > 6) {
					tf.setText(oldValue);
				} else {
					try {
						if(!newValue.contentEquals("")){
							int num = Integer.parseInt(newValue);
						}
					} catch (NumberFormatException e) {
						tf.setText(oldValue);
					}
				}
			}
		});
		Label label3 = new Label("ABN fehlerhaft");
		label3.setTextFill(Color.RED);

		Label label2 = new Label("Versanddatum für " + los + "  auswählen!");

		DatePicker datePicker = new DatePicker();
		datePicker.setValue(LocalDate.now().plusDays(14));

		Callback<DatePicker, DateCell> dayCellFactory = dp -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);

				if (item.isBefore(LocalDate.now()) || item.getDayOfWeek() == DayOfWeek.SATURDAY
						|| item.getDayOfWeek() == DayOfWeek.SUNDAY) {
					getStyleClass().add("disabled");
					setDisable(true);
				}
			}
		};

		datePicker.setDayCellFactory(dayCellFactory);
		VBox layout = new VBox(10);

		// Create submit buttons
		Button submit = new Button("Bestätigen");

		// Clicking will set answer and close window
		submit.setOnAction(e -> {
			if (boo) {
				if (tf.getText().length()==6) {
					answer = "Versanddatum " + los + " " + date.format(datePicker.getValue()) + " " + tf.getText();
					window.close();
				} else {
					if (!layout.getChildren().contains(label3))
						layout.getChildren().add(2, label3);
				}
			} else {
				answer = "Versanddatum " + los + " " + date.format(datePicker.getValue());
				window.close();
			}
		});

		if (boo) {
			layout.getChildren().addAll(label1, tf);
		}
		layout.getChildren().addAll(label2, datePicker, submit);
		layout.setAlignment(Pos.CENTER);
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();

		// Make sure to return answer
		return answer;
	}

}