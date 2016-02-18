package application;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TreeItem;

public class TreeItemOpener {

	private TreeItem<ArchiveEntry> treeItem;

	public TreeItemOpener(TreeItem<ArchiveEntry> item) {
		this.treeItem = item;

		treeItem.expandedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				BooleanProperty bb = (BooleanProperty) observable;
				TreeItem<ArchiveEntry> t = (TreeItem<ArchiveEntry>) bb.getBean();
				if (newValue == false) {
					t.getChildren().remove(0, t.getChildren().size());
					t.getChildren().add(new TreeItem<ArchiveEntry>(new ArchiveEntry("")));
				} else {
					ClientGUI.sender.sendString("archivedProjects");
					ClientGUI.sender.sendString(t.getValue().getName());
				}

			}
		});
	}
}
