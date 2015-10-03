package application;

import java.net.*;
import java.time.LocalDate;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

import javafx.collections.ObservableList;

public class ClientReceiver implements Runnable{
    
    private Socket SOCK;
    private ObjectInputStream INPUT;
    private ClientSender sender;
    String message;

    public ClientReceiver() 
    {
    	this.SOCK = ClientGUI.SOCK;
    	this.sender = ClientGUI.sender;
    	try
        {
            try
            {
            	Thread X = new Thread(this);
                X.start();	
            }
            
            catch(Exception e)
            {
            	System.out.println(e);
    			e.printStackTrace();
            	JOptionPane.showMessageDialog(null, "Thread not startable");
            	System.exit(0);
            }                     
        }
        catch(Exception e)
        {
        	System.out.println(e);
			e.printStackTrace();
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
            	initialize();
//                INPUT = new ObjectInputStream(SOCK.getInputStream());
//                OUT = new ObjectOutputStream(SOCK.getOutputStream());
//                OUT.flush();
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
    
    public void initialize(){
    	try
    	{
    		INPUT = new ObjectInputStream(SOCK.getInputStream());
    		
    	
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
    	}
    	catch(Exception e)
        {
    		System.out.println(e);
			e.printStackTrace();
        	JOptionPane.showMessageDialog(null, "Initialisierung Fehlgeschlagen");
        	System.exit(0);
        }       
    }
    
    public void DISCONNECT() throws IOException
    {
    	
        sender.sendString("disconnect");
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
    	try{
    	message = INPUT.readUTF();
    	System.out.println(message);
    	}
    	catch(Exception e)
        {
    		System.out.println(e);
			e.printStackTrace();
        	JOptionPane.showMessageDialog(null, "receiving fehlgeschlagen");
        }
    }   
}

