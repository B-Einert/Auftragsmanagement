package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSender {
	private Socket SOCK;
    private ObjectOutputStream OUT;
    
    public ClientSender(){
    	try
    	{
    	SOCK = ClientGUI.SOCK;
    	OUT = new ObjectOutputStream(SOCK.getOutputStream());
		OUT.writeUTF("hi");
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
        try {
			OUT.writeUTF(x);
			OUT.flush();
		} 
        catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
        
    }
    public void sendEntry(String[] entry){
    	try{
    		OUT.writeObject(entry);
    		OUT.flush();
    	} 
    	catch (IOException e) {
			System.out.println(e);
			e.printStackTrace();
		}
    }
}
