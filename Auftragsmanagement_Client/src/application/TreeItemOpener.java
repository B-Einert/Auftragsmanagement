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

		// column.setCellFactory(
		// new Callback<TreeTableColumn<ArchiveEntry, String>,
		// TreeTableCell<ArchiveEntry, String>>() {
		// @Override
		// public TreeTableCell<ArchiveEntry, String>
		// call(TreeTableColumn<ArchiveEntry, String> p) {
		// TreeTableCell<ArchiveEntry, String> cell = new
		// TreeTableCell<ArchiveEntry, String>(){
		// @Override
		// public void updateItem(String item, boolean empty) {
		// super.updateItem(item, empty);
		// setText(empty ? null : getString());
		// setGraphic(null);
		// }
		// private String getString() {
		// return getItem() == null ? "" : getItem().toString();
		// }
		// };
		// cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new
		// EventHandler<MouseEvent>() {
		// @Override
		// public void handle(MouseEvent event) {
		// TreeTableCell<ArchiveEntry, String> c = (TreeTableCell<ArchiveEntry,
		// String>) event.getSource();
		// System.out.println(c.getTreeTableRow());
		// System.out.println(c.getTreeTableRow().getTreeItem());
		// System.out.println(c.getTreeTableRow().getTreeItem().getValue());
		// System.out.println(c.getTreeTableRow().getTreeItem().getValue().getName());
		//// if (cell.getTreeTableRow().getItem().isRoot()) {
		//// ClientGUI.sender.sendString("archiveProjects");
		//// ClientGUI.sender.sendString(
		//// ((TreeTableCell<ArchiveEntry, String>)
		// event.getSource()).getText());
		//// }
		// }
		// });
		// return cell;
		// }
		// });

	}
}
