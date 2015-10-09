package application;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerReturn implements Runnable
{
    
    Socket SOCK;
    private ObjectInputStream objIn;
    private ObjectOutputStream objOut;
    private Scanner INPUT;
    private PrintWriter OUT;
    String message;
    
    public ServerReturn(Socket X)
    {
        this.SOCK = X;
    }
    
    public void CheckConnection() throws IOException
    {
        if(!SOCK.isConnected())
        {
        	disconnect();
        }
    }
    
    public void run()
    {
        try
        {
            try
            {
                objOut = new ObjectOutputStream(SOCK.getOutputStream());
                System.out.println("objOut init");
                objIn = new ObjectInputStream(SOCK.getInputStream());
                System.out.println(objIn.readUTF());
                System.out.println("objInput init");
                
                
                INPUT = new Scanner(SOCK.getInputStream());
                System.out.println("Input init");
                System.out.println(INPUT.nextLine());
                OUT = new PrintWriter(SOCK.getOutputStream());
                System.out.println("Output init");
                initialize();
                
                
                while(true)
                {
                    //CheckConnection();
                    if(!INPUT.hasNext())
                    {
                        return;
                    }
                    message = INPUT.nextLine();
                    System.out.println(message);
                    
                    if(message.contains("disconnect")){
                    	disconnect();
                    }
                    else if(message.contains("new")){
                    	String[] entry = (String[]) objIn.readObject();
                    	entry = Server.dbManager.manageEntry(entry);
                    	sendString("new");
                    	sendStrings(entry);
                    }
                    else{
                    	sendString(message);
                    }
                }
            }
            finally
            {
                SOCK.close();
            }
        }
        catch(Exception e)
        {
        	System.out.println(e);
			e.printStackTrace();
        }
    }
    
    public void initialize() throws IOException
    {
    	try
    	{
    		
    		objOut.writeObject(Server.dbManager.getInitList());
    		objOut.flush();
    	}
    	catch(Exception e)
    	{
    		System.out.print(e);
    		System.out.println(e.getLocalizedMessage());
    		System.out.println(e.getStackTrace());
    	}
    }
    
    public void disconnect() throws IOException{
    	for(int i = 0; i <= Server.ConnectionArray.size(); i++)
        {
            if(Server.ConnectionArray.get(i)==SOCK)
            {
            	System.out.println("disconnected");
            	SOCK.close();
                Server.ConnectionArray.remove(i);
                Server.Threads.remove(i);
            }
        }
    }
    
    public void sendString(String message){
    	try{
            for(int i = 1; i <= Server.ConnectionArray.size(); i++)
            {
                Socket TEMP_SOCK = (Socket) Server.ConnectionArray.get(i-1);
                PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
                TEMP_OUT.println(message);
                TEMP_OUT.flush();
                System.out.println("Sent to: " + TEMP_SOCK.getLocalAddress());
            }
    	}
    	catch(Exception e){
    		System.out.println(e);
    		e.printStackTrace();
    
    	}
    }
    
    public void sendStrings(String[] message){
    	try{
            for(int i = 1; i <= Server.ConnectionArray.size(); i++)
            {
                Socket TEMP_SOCK = (Socket) Server.ConnectionArray.get(i-1);
                AppendingObjectOutputStream TEMP_OUT = new AppendingObjectOutputStream(TEMP_SOCK.getOutputStream());
                TEMP_OUT.writeObject(message);
                TEMP_OUT.flush();
                System.out.println("Sent to: " + TEMP_SOCK.getLocalAddress());
            }
    	}
    	catch(Exception e){
    		System.out.println(e);
    		e.printStackTrace();
    
    	}
    }
}


