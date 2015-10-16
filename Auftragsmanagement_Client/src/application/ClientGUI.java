package application;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
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
    public static Socket SOCK;

    public static void main(String[] args) {
    	final int PORT = 444;
        final String HOST = "localhost";
        try {
			SOCK = new Socket(HOST, PORT);
		} catch (UnknownHostException e) {
			System.out.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
        System.out.println("You connected to: "+ HOST);
        sender = new ClientSender();
    	client = new ClientReceiver();
    	
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setWidth(837);
        window.setTitle("Auftragsmanagement");
        window.setOnCloseRequest(e -> {
        	//Fenster vom schließen hindern
        	e.consume(); 
        	try
        	{
        		client.DISCONNECT();
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
        TableColumn<Entry, String> linkColumn = new TableColumn<>("Link");
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
        
        //contact column
        TableColumn<Entry, String> contactColumn = new TableColumn<>("Kontakt");
        contactColumn.setMinWidth(120);
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactColumn.setCellFactory(column -> {
        	return new TableCell<Entry, String>(){
        		@Override
        		protected void updateItem(String item, boolean empty) {
        			super.updateItem(item, empty);
        			System.out.println(item);
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
        TableColumn<Entry, String> pursueColumn = new TableColumn<>("Weiterführen");
        pursueColumn.setMinWidth(80);
        pursueColumn.setCellValueFactory(new PropertyValueFactory<>("pursue"));
        pursueColumn.getStyleClass().add("center");
        
        //detail column
        TableColumn<Entry, String> detailColumn = new TableColumn<>("Detail");
        detailColumn.setMinWidth(60);
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));
        detailColumn.getStyleClass().add("center");

        //Button
        Button newEntryButton = new Button("neuer Vorgang");
        newEntryButton.setOnAction(e -> newEntryButtonClicked());
        Button disconnectButton = new Button("Disconnect");
        disconnectButton.setOnAction(e -> disconnectButtonClicked());

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.getChildren().addAll(newEntryButton, disconnectButton);
        
        table = new TableView<>();
        table.setItems(getEntry());
        table.getColumns().addAll(dateColumn, linkColumn, customerColumn, itemColumn, 
        		contactColumn, pursueColumn, detailColumn);
        table.setEditable(true);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        window.show();
        
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

    //Delete button clicked
    public void disconnectButtonClicked(){
    	try
     	{
    		client.DISCONNECT();
     	}
    	catch(Exception e)
    	{
    		System.out.println(e);
			e.printStackTrace();
    	}
    }
        

    //Get all of the products
    public ObservableList<Entry> getEntry(){
        return entries;
    }
    
    public void setEntry(ObservableList<Entry> entries){
    	ClientGUI.entries = entries;
    }
}
