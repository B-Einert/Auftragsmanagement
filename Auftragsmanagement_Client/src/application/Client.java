package application;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class Client implements Runnable{
    
    private Socket SOCK;
    private Scanner INPUT;
    private PrintWriter OUT;

    public Client() 
    {
    	try
        {
            final int PORT = 444;
            final String HOST = "192.168.178.35";
            this.SOCK = new Socket(HOST, PORT);
            System.out.println("You connected to: "+ HOST);
            
            OUT = new PrintWriter(SOCK.getOutputStream());
            OUT.println("connected");
            OUT.flush();
            
            Thread X = new Thread(this);
            X.start();
            
        }
        catch(Exception e)
        {
            System.out.print(e);
            JOptionPane.showMessageDialog(null, "Server reagiert nicht.");
            System.exit(0);
        }
    }
    
    public void run()
    {
        try
        {
            try
            {
                INPUT = new Scanner(SOCK.getInputStream());
                OUT = new PrintWriter(SOCK.getOutputStream());
                OUT.flush();
                CheckStream();
            }
            finally
            {
                SOCK.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    public void DISCONNECT() throws IOException
    {
    	
        OUT.println("disconnect");
        OUT.flush();
        SOCK.close();
        JOptionPane.showMessageDialog(null, "You disconnected");
        System.exit(0);
    }

// LATER    
    public void CheckStream()
    {
        while(true)
        {
            RECEIVE();
        }
    }
//    
    
    public void RECEIVE()
    {
        if(INPUT.hasNext())
        {
            String MESSAGE = INPUT.nextLine();
            System.out.println(MESSAGE);
            
        }
    }
    
    public void SEND(String x)
    {
        OUT.println(x);
        OUT.flush();
    }
    
    
    
}

