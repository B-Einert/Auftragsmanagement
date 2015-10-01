package application;

import java.net.*;
import java.time.LocalDate;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

import javafx.collections.ObservableList;

public class Client implements Runnable{
    
    private Socket SOCK;
    private ObjectInputStream INPUT;
    private ObjectOutputStream OUT;

    public Client() 
    {
    	try
        {
            final int PORT = 444;
            final String HOST = "192.168.178.35";
            this.SOCK = new Socket(HOST, PORT);
            System.out.println("You connected to: "+ HOST);
            
//            OUT = new ObjectOutputStream(SOCK.getOutputStream());
//            OUT.println("connected");
//            OUT.flush();
            try
            {
            	INPUT = new ObjectInputStream(SOCK.getInputStream());
            	OUT = new ObjectOutputStream(SOCK.getOutputStream());
            	Object received = null;
            	while(received == null)
            	{
            		received = INPUT.readObject();
            	}
            	ArrayList<String[]> initList = (ArrayList<String[]>) received;
            	
            	for(String[] entry: initList)
            	{
            		Entry e = new Entry(LocalDate.now(), entry[0], entry[1], entry[2]);
            		ClientGUI.entries.add(e);
            	}
            	Thread X = new Thread(this);
                X.start();
            }
            catch(Exception e)
            {
            	System.out.print(e);
            	System.out.println(e.getLocalizedMessage());
        		System.out.println(e.getStackTrace());
            	JOptionPane.showMessageDialog(null, "Initialisierung Fehlgeschlagen");
            	System.exit(0);
            }                     
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
                INPUT = new ObjectInputStream(SOCK.getInputStream());
                OUT = new ObjectOutputStream(SOCK.getOutputStream());
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
    	
        OUT.writeChars("disconnect");
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
    
    public void RECEIVE()
    {
//        if(INPUT.hasNext())
//        {
//            String MESSAGE = INPUT.nextLine();
//            System.out.println(MESSAGE);
//            
//        }
    }
    
    public void SEND(String x)
    {
//        OUT.println(x);
//        OUT.flush();
    }
    
    
    
}

