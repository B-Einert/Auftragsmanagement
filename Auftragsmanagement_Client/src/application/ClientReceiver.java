package application;

import java.net.*;
import java.time.LocalDate;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
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
    		received = null;
    		while(received == null)
    		{
    			received = objIn.readObject();
    		}
    		for(String customer : (ArrayList<String>) received){
    			ClientGUI.customers.add(customer);
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
		    			if(message.contentEquals(e.getLinkString())){
		    				e.setContact(INPUT.nextLine());
		    				e.setState(Integer.parseInt(INPUT.nextLine()));
		    				System.out.println(e.getContact());
		    				Entry entry = new Entry(e.getLinkString(), e.getDate(), e.getCustomer(), e.getItem(), e.getContact(), e.getState());
		    				ClientGUI.entries.remove(e);
		    				ClientGUI.entries.add(entry);
		    				break;
		    			}
		    		}
		    	}
		    	else if(message.contains("detail")){
		    		System.out.println("in the details");
		    		LinkedList<String> details = new LinkedList<String>();
		    		try {
//		    			details = (String[]) objIn.readObject();
		    			String next;
		    			while(INPUT.hasNext()){
		    				if(!(next=INPUT.nextLine()).contentEquals("!?#end"))details.add(next);
		    				else break;
		    			}
		    			DetailBox.setDetails(details);
		    			DetailBox.setReady(true);
		    			System.out.println("details done");
		    		} catch (Exception e) {
		    			e.printStackTrace();
		    			JOptionPane.showMessageDialog(null, "receiving fehlgeschlagen. bitte neu starten!");
		    		}
		    	}
		    	else if(message.contains("delete")){
		    		message=INPUT.nextLine();
		    		for(Entry e : ClientGUI.entries){
		    			if(message.contentEquals(e.getLinkString())){
		    				ClientGUI.entries.remove(e);
		    				break;
		    			}
		    		}
		    	}
		    	else if(message.contentEquals("cust")){
		    		message=INPUT.nextLine();
		    		ClientGUI.customers.add(message);
		    	}
		    	else if(message.contentEquals("custInf")){
		    		Map<String,String> answer = new HashMap<String,String>();
		    		try {
		    			String next;
		    			while(INPUT.hasNext()){
		    				if(!(next=INPUT.nextLine()).contentEquals("!?#end"))answer.put(next, INPUT.nextLine());
		    				else break;
		    			}
		    			CreateBox.setPartnerInfo(answer);
		    			System.out.println("partners done");
		    		} catch (Exception e) {
		    			e.printStackTrace();
		    			JOptionPane.showMessageDialog(null, "receiving fehlgeschlagen. bitte neu starten!");
		    		}
		    		CreateBox.setReady(true);
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

