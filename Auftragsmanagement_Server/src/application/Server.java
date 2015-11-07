package application;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalTime;
import java.util.LinkedList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Server implements Runnable
{
    public static LinkedList<Socket> ConnectionArray = new LinkedList<Socket>();
    public static LinkedList<Thread> Threads = new LinkedList<Thread>();
    public static DatabaseManager dbManager;

	public static void DISCONNECT(){
		for(Socket s : ConnectionArray){
			try{
            	PrintWriter TEMP_OUT = new PrintWriter(s.getOutputStream());
            	TEMP_OUT.println("disconnect");
            	TEMP_OUT.flush();
            }
            catch(SocketException e){
            	e.printStackTrace();
            }
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		System.exit(0);
	}

	@Override
	public void run() {
		Server.dbManager = new DatabaseManager();
		
		try
        {
            final int PORT = 444;
            ServerSocket SERVER = new ServerSocket(PORT);
            System.out.println("Waiting for clients...");
            
            while(true)
            {
                Socket SOCK = SERVER.accept();
                ConnectionArray.add(SOCK);
                
                System.out.println("Client connected from: " + SOCK.getLocalAddress());                
                ServerReturn connection = new ServerReturn(SOCK);
                Thread X = new Thread(connection);
                X.start();
                Threads.add(X);
                ServerGUI.tableEntries.add(new TableEntry("Client " + SOCK.getLocalAddress() + " hinzugefügt"));
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
	}
    
}


