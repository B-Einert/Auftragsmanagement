package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ServerGUI extends Application
{
	private Stage window;
    private TableView<TableEntry> table;
    public static ObservableList<TableEntry> tableEntries = FXCollections.observableArrayList();
    public static Server server;
    
    public static void main(String[] args) throws IOException
    {
    	server = new Server();
        Thread X = new Thread(server);
        X.start();
    	launch(args);
	}
    
    @Override
	public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setWidth(937);
        window.setTitle("Auftragsmanagement Server");
        window.setOnCloseRequest(e -> {
        	//Fenster vom schlieﬂen hindern
        	e.consume(); 
        	try
        	{
        		int choice = ChoiceBox.display("Ja", "Nein");
            	if(choice==0)Server.DISCONNECT();
        	}
        	catch(Exception a)
        	{
        		System.out.println(a);
    			a.printStackTrace();
        	}
        });

        
        //time column
        TableColumn<TableEntry, LocalTime> timeColumn = new TableColumn<>("Zeit");
        timeColumn.setMinWidth(80);
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        
        //entry column
        TableColumn<TableEntry, String> entryColumn = new TableColumn<>("Eintrag");
        entryColumn.setMinWidth(600);
        entryColumn.setCellValueFactory(new PropertyValueFactory<>("entry"));

        //Button schlieﬂen
        Button close = new Button("schlieﬂen");
        close.setOnMouseClicked(e -> {
        	int choice = ChoiceBox.display("Ja", "Nein");
        	if(choice==0)Server.DISCONNECT();
        });

        
        table = new TableView<>();
        table.setItems(tableEntries);
        table.getColumns().addAll(timeColumn, entryColumn);
        table.setEditable(true);
        table.getItems().addListener((ListChangeListener<TableEntry>) (c -> {
            c.next();
            final int size = table.getItems().size();
            if (size > 0) {
                try{
                	table.scrollTo(size - 1);
                }
                catch (IllegalStateException oi){
                	System.out.println(oi);
                }
            }
        }));

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, close);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
        
    }
}