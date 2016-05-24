package application;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;

public class Server implements Runnable {
	public static LinkedList<Socket> ConnectionArray = new LinkedList<Socket>();
	public static LinkedList<Thread> Threads = new LinkedList<Thread>();
	public static DatabaseManager dbManager;

	public static void DISCONNECT() {
		for (Socket s : ConnectionArray) {
			try {
				PrintWriter TEMP_OUT = new PrintWriter(s.getOutputStream());
				TEMP_OUT.println("disconnect");
				TEMP_OUT.flush();
				TEMP_OUT.close();
			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		System.exit(0);
	}

	public static void midnight() {
		dbManager.midnight();
		for (Socket s : ConnectionArray) {
			try {
				PrintWriter TEMP_OUT = new PrintWriter(s.getOutputStream());
				TEMP_OUT.println("midnight");
				TEMP_OUT.flush();
				for (String[] entry : Server.dbManager.getInitList()) {
					for (String str : entry) {
						TEMP_OUT.println(str);
						TEMP_OUT.flush();
					}
				}
				TEMP_OUT.println("!?#end");
				TEMP_OUT.flush();

			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		Server.dbManager = new DatabaseManager();
		DaylyTask.startTask();
		// checker = new ActivityChecker();
		// Thread check = new Thread(checker);
		// check.start();

		try {
			final int PORT = 444;
			ServerSocket SERVER = new ServerSocket(PORT);
			System.out.println("Waiting for clients...");

			while (true) {
				Socket SOCK = SERVER.accept();
				ConnectionArray.add(SOCK);

				System.out.println("Client connected from: " + SOCK.getLocalAddress());
				ServerReturn connection = new ServerReturn(SOCK);
				Thread X = new Thread(connection);
				X.start();
				Threads.add(X);
				ServerGUI.tableEntries.add(new TableEntry("Client " + SOCK.getInetAddress() + " hinzugefügt"));
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}