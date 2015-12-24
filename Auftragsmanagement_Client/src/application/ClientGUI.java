package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ClientGUI extends Application {

	public static ClientReceiver client;
	public static ClientSender sender;
    private static Stage window;
    private static Scene scene;
    private Scene scene2;
    private TableView<Entry> table;
    private TreeTableView<ArchiveEntry> table2;
    private TreeItem<ArchiveEntry> root = new TreeItem<ArchiveEntry>(new ArchiveEntry("root"));;
    public static ObservableList<Entry> entries = FXCollections.observableArrayList();
    public static ObservableList<String> customers = FXCollections.observableArrayList();
    public static HashSet<TreeItem<ArchiveEntry>> archived = new HashSet<TreeItem<ArchiveEntry>>();
    public static Socket SOCK;
    private static Image dupl;
    private static Image folder;
    private static Image pursue;
    private static Image detail;
    
    //TODO set db
    public static String datenbank = "D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/";

    public static void main(String[] args) {	
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	final int PORT = 444;
        //final String HOST = "192.168.178.49";
    	final String HOST = "192.168.178.35";
        try {
			SOCK = new Socket(HOST, PORT);
		} catch (UnknownHostException e) {
			AlertBox2.display("Verbindung zum Server konnte nicht hergestellt werden.");
			System.exit(0);
			System.out.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			AlertBox2.display("Verbindung zum Server konnte nicht hergestellt werden.");
			System.exit(0);
			System.out.println(e);
			e.printStackTrace();
		}
        sender = new ClientSender();
    	client = new ClientReceiver();
    	
        window = primaryStage;
        window.setWidth(947);
        window.setTitle("Auftragsmanagement");
        window.getIcons().add(new Image("application/images/LSK.jpg"));
        window.setOnCloseRequest(e -> {
        	//Fenster vom schließen hindern
        	e.consume(); 
        	try
        	{
        		ClientReceiver.DISCONNECT();
        	}
        	catch(Exception a)
        	{
        		System.out.println(a);
    			a.printStackTrace();
        	}
        });
        
        dupl = new Image(getClass().getResourceAsStream("images/duplicate.png"));
        folder = new Image(getClass().getResourceAsStream("images/folderbtn.png"));
        pursue = new Image(getClass().getResourceAsStream("images/pursuebtn.png"));
        detail = new Image(getClass().getResourceAsStream("images/detailbtn.png"));

        //date column
        TableColumn<Entry, String> dateColumn = new TableColumn<>("Anfrage");
        dateColumn.setMinWidth(80);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateColumn.setComparator(new Comparator<String>(){
        	@Override
			public int compare(String s1, String s2) {
				LocalDate l1 = LocalDate.parse(s1.substring(6) + "-" + s1.substring(3, 5) + "-" + s1.substring(0, 2));
				LocalDate l2 = LocalDate.parse(s2.substring(6) + "-" + s2.substring(3, 5) + "-" + s2.substring(0, 2));
				return l1.compareTo(l2);
			}
        });
        
        //link column
        TableColumn<Entry, Button> linkColumn = new TableColumn<>("Link");
        linkColumn.setSortable(false);
        linkColumn.setMinWidth(50);
        linkColumn.setCellValueFactory(new PropertyValueFactory<>("link"));
        linkColumn.getStyleClass().add("center");
        
        //customer column
        TableColumn<Entry, String> customerColumn = new TableColumn<>("Kunde");
        customerColumn.setMinWidth(200);
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        
     	//item column
        TableColumn<Entry, String> itemColumn = new TableColumn<>("Gegenstand");
        itemColumn.setMinWidth(200);
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));
        
        //contact date column
        TableColumn<Entry, String> contactDateColumn = new TableColumn<>("Kontaktdatum");
        contactDateColumn.setMinWidth(80);
        contactDateColumn.setCellValueFactory(new PropertyValueFactory<>("contactDate"));
        contactDateColumn.setComparator(new Comparator<String>(){
        	@Override
			public int compare(String s1, String s2) {
				LocalDate l1 = LocalDate.parse(s1.substring(6) + "-" + s1.substring(3, 5) + "-" + s1.substring(0, 2));
				LocalDate l2 = LocalDate.parse(s2.substring(6) + "-" + s2.substring(3, 5) + "-" + s2.substring(0, 2));
				return l1.compareTo(l2);
			}
        });
        
        //last contact column
        TableColumn<Entry, String> contactColumn = new TableColumn<>("Aktion");
        contactColumn.setMinWidth(130);
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactColumn.setCellFactory(column -> {
        	return new TableCell<Entry, String>(){
        		@Override
        		protected void updateItem(String item, boolean empty) {
        			super.updateItem(item, empty);
        			if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                    	setText(item);
                    	if(item.contains("Archiviert")){
                    		((Node)this).getStyleClass().add("archived");
                    	}
                    	else{
                    		((Node)this).getStyleClass().remove("archived");
                    	}
                    	if(item.startsWith("old ")){
                    		setText(item.substring(4));
            				((Node)this).getStyleClass().add("highlight");
            			}
            			else{
            				((Node)this).getStyleClass().remove("highlight");
            			}
                    	this.setWrapText(true);
                    }
//        			
        		}
        	};
        });
        
        //pursue column
        TableColumn<Entry, Button> pursueColumn = new TableColumn<>("Weiterführen");
        pursueColumn.setSortable(false);
        pursueColumn.setMinWidth(80);
        pursueColumn.setCellValueFactory(new PropertyValueFactory<>("pursue"));
        pursueColumn.getStyleClass().add("center");
        
        //detail column
        TableColumn<Entry, Button> detailColumn = new TableColumn<>("Detail");
        detailColumn.setSortable(false);
        detailColumn.setMinWidth(60);
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailColumn.getStyleClass().add("center");

        //Button neuer Vorgang
        Button newEntryButton = new Button("Neuer Vorgang");
        newEntryButton.setOnAction(e -> newEntryButtonClicked());
        
        ////Button Archiv
        Button archiveButton = new Button("Archiv");
        archiveButton.setOnAction(e -> archiveButtonClicked());

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(newEntryButton, archiveButton);
        table = new TableView<Entry>();
        table.setItems(getEntry());
        table.getColumns().addAll(dateColumn, linkColumn, customerColumn, itemColumn, 
        		contactDateColumn, contactColumn, pursueColumn, detailColumn);
        table.setEditable(true);
        table.getSortOrder().add(dateColumn);
        table.scrollTo(table.getItems().size() - 1);
        table.getItems().addListener((ListChangeListener<Entry>) (c -> {
            c.next();
            final int size = table.getItems().size();
            if (size > 0) {
                table.scrollTo(size - 1);
            }
        }));
        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);
        scene = new Scene(vBox);
        
        //Scene2
        TreeTableColumn<ArchiveEntry, String> c1 = new TreeTableColumn<>("Kunde/Projekt");
        c1.setMinWidth(200);
        c1.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ArchiveEntry, String> param) -> 
                new ReadOnlyStringWrapper(param.getValue().getValue().getName())
            );
        
        TreeTableColumn<ArchiveEntry, String> c2 = new TreeTableColumn<>("Datum");
        c2.setMinWidth(60);
        c2.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ArchiveEntry, String> param) -> 
                new ReadOnlyStringWrapper(param.getValue().getValue().getDate())
            );
        c2.setComparator(new Comparator<String>(){
        	@Override
			public int compare(String s1, String s2) {
        		if(s1.contentEquals("")||s2.contentEquals(""))return 0;
				LocalDate l1 = LocalDate.parse(s1.substring(6) + "-" + s1.substring(3, 5) + "-" + s1.substring(0, 2));
				LocalDate l2 = LocalDate.parse(s2.substring(6) + "-" + s2.substring(3, 5) + "-" + s2.substring(0, 2));
				return l1.compareTo(l2);
			}
        });
        
        TreeTableColumn<ArchiveEntry, String> c3 = new TreeTableColumn<>("Artikelnummer");
        c3.setMinWidth(60);
        c3.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ArchiveEntry, String> param) -> 
                new ReadOnlyStringWrapper(param.getValue().getValue().getAnum())
            );
        
        TreeTableColumn<ArchiveEntry, String> c4 = new TreeTableColumn<>("ABN");
        c4.setMinWidth(60);
        c4.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ArchiveEntry, String> param) -> 
                new ReadOnlyStringWrapper(param.getValue().getValue().getAbn())
            );
        
        TreeTableColumn<ArchiveEntry, Button> c5 = new TreeTableColumn<>("Duplizieren");
        c5.setMinWidth(60);
        c5.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ArchiveEntry, Button> param) -> 
                new ReadOnlyObjectWrapper<Button>(param.getValue().getValue().getDuplicate())
            );
       
        TreeTableColumn<ArchiveEntry, Button> c6 = new TreeTableColumn<>("Detail");
        c6.setSortable(false);
        c6.setMinWidth(60);
        c6.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ArchiveEntry, Button> param) -> 
                new ReadOnlyObjectWrapper<Button>(param.getValue().getValue().getDetails())
            );
        
      //link column
        TreeTableColumn<ArchiveEntry, Button> c7 = new TreeTableColumn<>("Link");
        c7.setSortable(false);
        c7.setMinWidth(50);
        c7.setCellValueFactory(
                (TreeTableColumn.CellDataFeatures<ArchiveEntry, Button> param) -> 
                new ReadOnlyObjectWrapper<Button>(param.getValue().getValue().getLinkBtn())
            );
        
        
        table2 = new TreeTableView<>(root);
        table2.getColumns().addAll(c1, c2, c3, c4, c7, c5, c6);
        table2.setShowRoot(false);
        table2.getSortOrder().addAll(c1, c2);
        
        
        Button back = new Button("Zurück");
        back.setOnAction(e -> back());
        
        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(table2, back);
        scene2 = new Scene(vBox2);
        
        window.setScene(scene);
        window.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        window.show();
        
    }

    private void setArchiveTree() {
		root.getChildren().setAll(archived);
		for(TreeItem<ArchiveEntry> item : root.getChildren()){
			item.getChildren().remove(0, item.getChildren().size());
			item.getChildren().add(new TreeItem<ArchiveEntry>(new ArchiveEntry("")));
			item.setExpanded(false);
			new TreeItemOpener(item);
		}
	}

	public void archiveButtonClicked() {
		setArchiveTree();
    	window.setScene(this.scene2);
	}
    
    public static void back(){
    	window.setScene(scene);
    }

	//Add button clicked
    public void newEntryButtonClicked(){
    	{	
    		String[] newEntry=CreateBox.display();
    		if (newEntry[0]=="");
    		else {
    			sender.sendString("new");
    			sender.sendEntry(newEntry);
    		}
    		
    		//new CreateBox();
    	}
    }   

    //Get all of the products
    public ObservableList<Entry> getEntry(){
        return entries;
    }
    
    public void setEntry(ObservableList<Entry> entries){
    	ClientGUI.entries = entries;
    }

	public static void alert(String alert) {
        AlertBox.display(alert);
	}
	
	public static Image getGraphic(String name){
		switch (name){
			case "duplicate":
				return dupl;
			case "detail":
				return detail;
			case "folder":
				return folder;
			case "pursue":
				return pursue;
			default: 
				return null;
		}
				
	}
}
