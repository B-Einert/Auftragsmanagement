package applicationServer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Server
{
    public static LinkedList<Socket> ConnectionArray = new LinkedList<Socket>();
    public static LinkedList<Thread> Threads= new LinkedList<Thread>();
    public static DatabaseManager dbManager = new DatabaseManager();
    
    public static void main(String[] args) throws IOException
    {
        
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
                
                //AddUserName(SOCK);
                
                ServerReturn connection = new ServerReturn(SOCK);
                Thread X = new Thread(connection);
                X.start();
                Threads.add(X);
                
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }
    
}


