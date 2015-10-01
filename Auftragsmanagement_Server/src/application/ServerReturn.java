package application;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ServerReturn implements Runnable
{
    
    Socket SOCK;
    //private Scanner INPUT;
    //private PrintWriter OUT;
    private ObjectInputStream INPUT;
    private ObjectOutputStream OUT;
    String MESSAGE = "";
    int folderCount = 0;
    
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
                //INPUT = new ObjectInputStream(SOCK.getInputStream());
                OUT = new ObjectOutputStream(SOCK.getOutputStream());
                
                initialize();
                
                while(true)
                {
                    CheckConnection();
//                    
//                    if(!INPUT.hasNext())
//                    {
//                        return;
//                    }
//                    
//                    MESSAGE = INPUT.nextLine();
//                    
//                    System.out.println(SOCK.getLocalAddress() + " said: " + MESSAGE);
//                    
//                    if(MESSAGE.startsWith("++")){
//                    	Server.dbManager.createNewProject(MESSAGE.substring(2));
//                    }
//                    //if(MESSAGE.startsWith("disconnect")){
//                    //	disconnect();
//                    //}
//                    
//                    for(int i = 1; i <= Server.ConnectionArray.size(); i++)
//                    {
//                        Socket TEMP_SOCK = (Socket) Server.ConnectionArray.get(i-1);
//                        PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
//                        TEMP_OUT.println(MESSAGE);
//                        TEMP_OUT.flush();
//                        System.out.println("Sent to: " + TEMP_SOCK.getLocalAddress());
//                    }
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
        }
    }
    
    public void initialize() throws IOException
    {
    	try
    	{
    		
    		OUT.writeObject(Server.dbManager.getInitList());
    		OUT.flush();
    	}
    	catch(Exception e)
    	{
    		System.out.print(e);
    		System.out.println(e.getLocalizedMessage());
    		System.out.println(e.getStackTrace());
    	}
    }
    
    public void disconnect() throws IOException{
    	for(int i = 1; i <= Server.ConnectionArray.size(); i++)
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
}


