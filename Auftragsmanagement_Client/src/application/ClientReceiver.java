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
    private ObjectInputStream objIn;
    private Scanner INPUT;
    String message;

    public ClientReceiver() 
    {
    	this.SOCK = ClientGUI.SOCK;
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
    		objIn = new ObjectInputStream(new BufferedInputStream(SOCK.getInputStream()));
    		System.out.println("objin init");
    		
    		ClientGUI.sender.init();
    		
    		INPUT= new Scanner(SOCK.getInputStream());
    		System.out.println("Input init");
    	
    		Object received = null;
    		while(received == null)
    		{
    			received = objIn.readObject();
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
    	
    	ClientGUI.sender.sendString("disconnect");
        INPUT.close();
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
    	if(INPUT.hasNext())
        {
	    	try{
	    	message = INPUT.nextLine();
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
}

