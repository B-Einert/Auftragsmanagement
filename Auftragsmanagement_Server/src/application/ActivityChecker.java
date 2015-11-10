package application;

import java.net.Socket;
import java.io.IOException;
import java.net.InetAddress;

public class ActivityChecker implements Runnable{

	@Override
	public void run() {
		while(true){
			for(Socket s: Server.ConnectionArray){
				try {
					if(!s.getInetAddress().isReachable(10)){
						Server.ConnectionArray.remove(s);
						System.out.println("asshole disconnected");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
