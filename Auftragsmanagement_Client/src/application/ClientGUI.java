package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ClientGUI extends Application {

	public static ClientReceiver client;
	public static ClientSender sender;
    private Stage window;
    private TableView<Entry> table;
    public static ObservableList<Entry> entries = FXCollections.observableArrayList();
    public static ObservableList<String> customers = FXCollections.observableArrayList();
    public static Socket SOCK;
    //TODO
    private File archive = new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/abgeschlossene_Vorgaenge");

    public static void main(String[] args) {	
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	final int PORT = 444;
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
        window.setWidth(937);
        window.setTitle("Auftragsmanagement");
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

        //date column
        TableColumn<Entry, LocalDate> dateColumn = new TableColumn<>("Anfrage");
        dateColumn.setMinWidth(80);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        
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
        TableColumn<Entry, LocalDate> contactDateColumn = new TableColumn<>("Kontaktdatum");
        contactDateColumn.setMinWidth(80);
        contactDateColumn.setCellValueFactory(new PropertyValueFactory<>("contactDate"));
        
        //last contact column
        TableColumn<Entry, String> contactColumn = new TableColumn<>("Aktion");
        contactColumn.setMinWidth(120);
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
        
        
        table = new TableView<>();
        table.setItems(getEntry());
        table.getColumns().addAll(dateColumn, linkColumn, customerColumn, itemColumn, 
        		contactDateColumn, contactColumn, pursueColumn, detailColumn);
        table.setEditable(true);
        table.getSortOrder().add(dateColumn);
        table.getSortOrder().add(customerColumn);
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

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        window.show();
        
    }

    public void archiveButtonClicked() {
    	try {
			Desktop dt = Desktop.getDesktop();
			dt.open(archive);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
}
