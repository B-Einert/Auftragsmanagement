package application;

import javafx.application.Platform;

public class TableAdder {

	public static void add(String string) {

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ServerGUI.tableEntries.add(new TableEntry(string));
			}

		});
	}

}
