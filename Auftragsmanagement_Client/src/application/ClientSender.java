package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientSender {
	private Socket SOCK;
    private PrintWriter OUT;
    
    
    public ClientSender(){
    	
    }
    
    public void init(){
    	try
    	{
    	SOCK = ClientGUI.SOCK;
    	OUT = new PrintWriter(SOCK.getOutputStream());
    	System.out.println("Output init");
		OUT.println("hi");
		OUT.flush();
    	}
    	catch(Exception e)
        {
    		System.out.println(e);
			e.printStackTrace();
        }
    }
    
    public void sendString(String x)
    {
        OUT.println(x);
		OUT.flush();
        
    }
    public void sendEntry(String[] entry){
//    	try{
//    		OUT.writeObject(entry);
//    		OUT.flush();
//    	} 
//    	catch (IOException e) {
//			System.out.println(e);
//			e.printStackTrace();
//		}
    }
}
