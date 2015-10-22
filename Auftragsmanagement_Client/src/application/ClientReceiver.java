package application;

import java.net.*;
import java.time.LocalDate;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class ClientReceiver implements Runnable{
    
    private Socket SOCK;
    private ObjectInputStream objIn;
    private Scanner INPUT;
    private String message;
   // private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MMM-dd");

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
    			addEntry(entry);
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
    
    public void addEntry(String[] entry){
    	Entry e = new Entry(entry[0], LocalDate.parse(entry[1]), entry[2], entry[3], entry[4], Integer.parseInt(entry[5]));
		ClientGUI.entries.add(e);
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
	    	try
	    	{	
		    	message = INPUT.nextLine();
		    	System.out.println(message);
		    	if(message.contains("new")){
		    		receiveNewEntry();
		    	}
		    	else if(message.contains("disconnect")){
		    		INPUT.close();
		            SOCK.close();
		            JOptionPane.showMessageDialog(null, "Server reagiert nicht. Sorry");
		            System.exit(0);
		    	}
		    	else if(message.contains("edit")){
		    		message=INPUT.nextLine();
		    		for(Entry e : ClientGUI.entries){
		    			if(message.contains(e.getLinkString())){
		    				e.setContact(INPUT.nextLine());
		    				e.setState(Integer.parseInt(INPUT.nextLine()));
		    				System.out.println(e.getContact());
		    				Entry entry = new Entry(e.getLinkString(), e.getDate(), e.getCustomer(), e.getItem(), e.getContact(), e.getState());
		    				ClientGUI.entries.remove(e);
		    				ClientGUI.entries.add(entry);
		    				//ClientGUI.updateTable();
		    				break;
		    			}
		    		}
		    	}
		    	else if(message.contains("detail")){
		    		String[] details;
		    		try {
		    			details = (String[]) objIn.readObject();
		    			DetailBox.setDetails(details);
		    		} catch (Exception e) {
		    			e.printStackTrace();
		    			JOptionPane.showMessageDialog(null, "receiving fehlgeschlagen. bitte neu starten!");
		    		}
		    	}
		    	else if(message.contains("delete")){
		    		message=INPUT.nextLine();
		    		for(Entry e : ClientGUI.entries){
		    			if(message.contains(e.getLinkString())){
		    				ClientGUI.entries.remove(e);
		    				break;
		    			}
		    		}
		    	}
	    	}
	    	catch(Exception e)
	        {
	    		System.out.println(e);
				e.printStackTrace();
	        	JOptionPane.showMessageDialog(null, "receiving fehlgeschlagen");
	        }
    
        }
    }
    
    public void receiveNewEntry(){
    	String[] entry;
		try {
			entry = (String[]) objIn.readObject();
			addEntry(entry);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "receiving fehlgeschlagen. bitte neu starten!");
		}
		
    }
}

