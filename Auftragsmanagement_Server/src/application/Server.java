package application;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

public class Server implements Runnable
{
    public static LinkedList<Socket> ConnectionArray = new LinkedList<Socket>();
    public static LinkedList<Thread> Threads = new LinkedList<Thread>();
    public static DatabaseManager dbManager;
    //public static ActivityChecker checker;

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
		//checker = new ActivityChecker();
		//Thread check = new Thread(checker);
        //check.start();
		
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
                ServerGUI.tableEntries.add(new TableEntry("Client " + SOCK.getLocalSocketAddress() + " hinzugefügt"));
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
	}
    
}


