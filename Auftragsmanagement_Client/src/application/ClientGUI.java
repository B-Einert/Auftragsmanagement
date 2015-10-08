package application;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    public static Socket SOCK;

    public static void main(String[] args) {
    	final int PORT = 444;
        final String HOST = "192.168.178.35";
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
        window.setTitle("Auftragsmanagement");
        window.setOnCloseRequest(e -> {
        	//Fenster vom schließen hindern
        	// e.consume(); 
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
        TableColumn<Entry, String> dateColumn = new TableColumn<>("Anfrage");
        dateColumn.setMinWidth(200);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        //link column
        TableColumn<Entry, String> linkColumn = new TableColumn<>("Link");
        linkColumn.setMinWidth(200);
        linkColumn.setCellValueFactory(new PropertyValueFactory<>("link"));
        
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
        contactColumn.setMinWidth(200);
        contactColumn.setCellValueFactory(new PropertyValueFactory<>("contact"));
        
        //pursue column
        TableColumn<Entry, String> pursueColumn = new TableColumn<>("Weiterführen");
        pursueColumn.setMinWidth(200);
        pursueColumn.setCellValueFactory(new PropertyValueFactory<>("pursue"));
        
        //detail column
        TableColumn<Entry, String> detailColumn = new TableColumn<>("Detail");
        detailColumn.setMinWidth(200);
        detailColumn.setCellValueFactory(new PropertyValueFactory<>("detail"));

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

        VBox vBox = new VBox();
        vBox.getChildren().addAll(table, hBox);

        Scene scene = new Scene(vBox);
        window.setScene(scene);
        window.show();
    }

    //Add button clicked
    public void newEntryButtonClicked(){
    	{	
    		String[] newEntry=CreateBox.display();
    		sender.sendString("xyz");
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
    	this.entries = entries;
    }


}

//package application;
//
//import javax.swing.*;
//import java.awt.event.*;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.net.*;
//import java.util.ArrayList;
//
//public class ClientGUI {
//    
//    private static Client client;
//    
//    public static JFrame MainWindow = new JFrame();
//    public static String UserName = "Anonymous";
//    private static JButton B_DISCONNECT = new JButton();
//    private static JButton B_SEND = new JButton();    
//   
//    public static void main(String[] args) 
//    {
//    	client = new Client();
//        BuildMainWindow();
//    }
//    
//    public static void BuildMainWindow()
//    {
//        MainWindow.setTitle("Hi Box");
//        MainWindow.setSize(450, 500);
//        MainWindow.setLocation(220, 180);
//        MainWindow.setResizable(false);
//        ConfigureMainWindow();
//        MainWindow_Action();
//        MainWindow.setVisible(true);
//    }
//    
//    public static void ConfigureMainWindow()
//    {
//        MainWindow.setBackground(new java.awt.Color(255, 255, 255));
//        MainWindow.setSize(500, 320);
//        MainWindow.getContentPane().setLayout(null);
//        
//        B_SEND.setBackground(new java.awt.Color(0, 0, 255));
//        B_SEND.setForeground(new java.awt.Color(255, 255, 255));
//        B_SEND.setText("SEND");
//        MainWindow.getContentPane().add(B_SEND);
//        B_SEND.setBounds(250, 40, 81, 25);
//        
//        B_DISCONNECT.setBackground(new java.awt.Color(0, 0, 255));
//        B_DISCONNECT.setForeground(new java.awt.Color(255, 255, 255));
//        B_DISCONNECT.setText("DISCONNECT");
//        MainWindow.getContentPane().add(B_DISCONNECT);
//        B_DISCONNECT.setBounds(10, 40, 110, 25);
//        
//    }
//    
//    public static void MainWindow_Action()
//    {
//        B_SEND.addActionListener(
//                new java.awt.event.ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ACTION_B_SEND();
//            }
//        }
//        );
//        
//        B_DISCONNECT.addActionListener(
//                new java.awt.event.ActionListener() {
//
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                ACTION_B_DISCONNECT();
//            }
//        }
//        );
//    }
//    
//    public static void ACTION_B_SEND()
//    {
//        client.SEND("++xyz");
//    }
//    
//    public static void ACTION_B_DISCONNECT()
//    {
//        try
//        {
//            client.DISCONNECT();
//        }
//        catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//}
