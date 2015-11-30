package application;

import java.util.LinkedList;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.*;

public class AuftragsBox {
	private static String[] newEntry;
	private static LinkedList<Label> labels, labelx;
	private static LinkedList<Node> fields;
	private static Stage window;
	private static boolean ready = false;
	private static LinkedList<String> details;

	public static String[] display() {
		window = new Stage();
		labels = new LinkedList<Label>();
		labelx = new LinkedList<Label>();
		fields = new LinkedList<Node>();

		labels.add(new Label("Kunde"));
		labels.add(new Label("Gegenstand"));
		labels.add(new Label("Ansprechpartner"));
		labels.add(new Label("Telefonnummer"));
		labels.add(new Label("Bearbeiter"));
		labels.add(new Label("Artikelnummer"));

		labelx.add(new Label("x"));
		labelx.add(new Label("x"));
		labelx.add(new Label("x"));
		labelx.add(new Label("x"));
		labelx.add(new Label("x"));
		labelx.add(new Label("x"));

		fields.add(new Label(details.get(0)));
		fields.add(new TextField(details.get(1)));
		fields.add(new TextField(details.get(2)));
		fields.add(new TextField(details.get(3)));
		fields.add(new TextField(details.get(4)));
		fields.add(new TextField(details.get(5)));

		Button submitButton = new Button("Best‰tigen");

		GridPane grid = new GridPane();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Neuer Vorgang");
		window.setMinWidth(250);

		window.setOnCloseRequest(e -> {
			// Fenster vom schlieﬂen hindern
			try {
				String[] entries = { "" };
				newEntry = entries;
			} catch (Exception a) {
				System.out.println(a);
				a.printStackTrace();
			}
		});

		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		for (int i = 0; i <= 5; i++) {
			(labelx.get(i)).setTextFill(Color.RED);
			(labelx.get(i)).setVisible(false);
			GridPane.setConstraints(labels.get(i), 0, i);
			GridPane.setConstraints(fields.get(i), 1, i);
			GridPane.setConstraints(labelx.get(i), 2, i);
			grid.getChildren().addAll(labels.get(i), fields.get(i), labelx.get(i));
		}

		GridPane.setConstraints(submitButton, 1, 6);

		grid.getChildren().add(submitButton);

		submitButton.setOnAction(e -> submit());
		submitButton.setOnKeyReleased(e -> {
			if (e.getCode() == KeyCode.ENTER) {
				submit();
			}
		});

		Scene scene = new Scene(grid, 350, 260);
		window.setScene(scene);
		window.showAndWait();
		return newEntry;
	}

	private static void submit() {
		{
			boolean missing = false;

			for (int i = 1; i <= 5; i++) {

				if (((TextField) fields.get(i)).getText().isEmpty()) {
					(labelx.get(i)).setVisible(true);
					missing = true;
				} else
					(labelx.get(1)).setVisible(false);
			}

			if (missing == false) {
				String[] entries = { ((TextField) fields.get(1)).getText(), ((TextField) fields.get(2)).getText(),
						((TextField) fields.get(3)).getText(), ((TextField) fields.get(4)).getText(),
						((TextField) fields.get(5)).getText() };
				newEntry = entries;
				window.close();
			}
		}
	}
	
	public static void setDetails(LinkedList<String> details){
		AuftragsBox.details=details;
	}

	public static boolean getReady() {
		return ready;
	}

	public static void setReady(boolean ready) {
		AuftragsBox.ready = ready;
	}
}
