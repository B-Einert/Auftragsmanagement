package application;

import java.io.*;
import java.net.*;

public class ServerReturn implements Runnable
{
    
    Socket SOCK;
    private ObjectInputStream INPUT;
    private ObjectOutputStream OUT;
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
                OUT = new ObjectOutputStream(SOCK.getOutputStream());
                OUT.flush();
                INPUT = new ObjectInputStream(SOCK.getInputStream());
                System.out.println(INPUT.readUTF());
                initialize();
                
                
                while(true)
                {
                    CheckConnection();
                    message = INPUT.readUTF();
                    System.out.println(message);
                    for(int i = 1; i <= Server.ConnectionArray.size(); i++)
                    {
                    Socket TEMP_SOCK = (Socket) Server.ConnectionArray.get(i-1);
                    AppendingObjectOutputStream TEMP_OUT = new AppendingObjectOutputStream(TEMP_SOCK.getOutputStream());
                    TEMP_OUT.writeUTF(message);
                    TEMP_OUT.flush();
                    System.out.println("Sent to: " + TEMP_SOCK.getLocalAddress());
                    }
                    
//                    if(INPUT.available() > 0)
//                    MESSAGE = (String) INPUT.readObject();
//                    if(MESSAGE == "")
//                    {
//                        return;
//                    }
//                    System.out.println(SOCK.getLocalAddress() + " said: " + MESSAGE);
                    
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


