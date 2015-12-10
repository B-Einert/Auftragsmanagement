package application;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

public class ServerReturn implements Runnable {

	Socket SOCK;
	private ObjectInputStream objIn;
	private ObjectOutputStream objOut;
	private Scanner INPUT;
	private PrintWriter OUT;
	String message;

	public ServerReturn(Socket X) {
		this.SOCK = X;
	}

	public void CheckConnection() throws IOException {
		if (!SOCK.isConnected()) {
			disconnect(SOCK);
		}
	}

	public void run() {
		try {
			try {
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

				while (true) {
					// CheckConnection();
					if (!INPUT.hasNext()) {
						return;
					}
					message = INPUT.nextLine();
					System.out.println(message);

					if (message.contains("disconnect")) {
						disconnect(SOCK);
						ServerGUI.tableEntries
								.add(new TableEntry("Client " + SOCK.getInetAddress() + " hat das Programm beendet."));
					} else if (message.contains("new")) {
						String[] entry = (String[]) objIn.readObject();
						entry = Server.dbManager.manageEntry(entry);
						if (!entry[0].equals("-1")) {
							sendString("new");
							sendStrings(entry);
							if (Server.dbManager.checkCustomer(entry[2])) {
								sendString("cust");
								sendString(entry[2]);
							}
						} else {
							OUT.println("fail");
							OUT.flush();
						}
					} else if (message.contains("editDetails")) {
						String link = INPUT.nextLine();
						Entry e = Server.dbManager.findEntry(link);
						LinkedList<String> edits = new LinkedList<String>();
						for (int i = 0; i <= 4; i++) {
							edits.add(INPUT.nextLine());
						}
						if (Server.dbManager.editWholeProject(e, edits) == "alert") {
							OUT.println("alert");
							OUT.flush();
							OUT.println(
									"Ein Fehler is aufgetreten. Schließen Sie alle Programme die in dem Projektordner liegen und wiederholen Sie den Vorgang!");
							OUT.flush();
						} else if (!link.contentEquals(e.getLink())) {
							sendString("changeLink");
							sendString(link);
							sendString(e.getLink());
						}
					} 
					else if (message.contentEquals("archivedProjects")) {
						String customer=INPUT.nextLine();
						String[] projects = Server.dbManager.getArchivedProjects(customer);
						if(!(projects == null)){
							OUT.println("archivedProjects");
							OUT.flush();
							OUT.println(customer);
							OUT.flush();
							sendStrings(projects, this);
						}
					} else if (message.contains("edit")) {
						Entry e = Server.dbManager.findEntry(INPUT.nextLine());
						String state = (INPUT.nextLine());
						String edit = INPUT.nextLine();
						if (edit.contains("Auftrag bestätigt")) {
							Server.dbManager.editEntry(e, state, edit);
							Server.dbManager.editEntry(e, state, INPUT.nextLine());

						} else if (edit.contains("Projekt abgeschlossen")) {
							String link=e.getLink();
							Server.dbManager.editEntry(e, state, edit);
							sendString("delete");
							sendString(link);
						} else if (edit.contains("Los")) {
							Server.dbManager.editEntry(e, state, edit);
							Server.dbManager.editEntry(e, state, INPUT.nextLine());
						} else
							Server.dbManager.editEntry(e, state, edit);
						sendString("edit");
						sendString(e.getLink());
						sendString(e.getLastContact());
						sendString(state.substring(1));
					} else if (message.contains("miniDetail")) {
						String link = INPUT.nextLine();
						String[] details = Server.dbManager.getAuftragsDetails(link);
						OUT.println("miniDetail");
						OUT.flush();
						sendStrings(details);
					} else if (message.contains("detail")) {
						String link = INPUT.nextLine();
						String[] details = Server.dbManager.getDetails(link);
						OUT.println("detail");
						OUT.flush();
						sendStrings(details);
						System.out.println("sent details");
					}
					else if (message.contains("duplicate")) {
						Entry e = Server.dbManager.createDouble(INPUT.nextLine(), INPUT.nextLine(), INPUT.nextLine(), INPUT.nextLine(), INPUT.nextLine(), INPUT.nextLine(), INPUT.nextLine());
						if(e!=null){
							Server.dbManager.editEntry(e, "#3", "Auftrag erhalten");
							System.out.println(e.getState());
							sendString("new");
							sendStrings(e.getFirstStatus());
						}
					}
					else if (message.contains("archive")) {
						String link = INPUT.nextLine();
						if (Server.dbManager.archive(link)) {
							sendString("delete");
							sendString(link);
						}
					} else if (message.contentEquals("custInf")) {
						String customer = INPUT.nextLine();
						Map<String, String> answer = Server.dbManager.getCustInf(customer);
						OUT.println("custInf");
						OUT.flush();
						for (String a : answer.keySet()) {
							OUT.println(a);
							OUT.flush();
							OUT.println(answer.get(a));
							OUT.flush();
						}
						OUT.println("!?#end");
						OUT.flush();
					}
				}
			} finally {
				SOCK.close();
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	public void initialize() throws IOException {
		try {

			objOut.writeObject(Server.dbManager.getInitList());
			objOut.flush();
			objOut.writeObject(Server.dbManager.getCustomerList());
			objOut.flush();
			objOut.writeObject(Server.dbManager.getArchivedList());
			objOut.flush();
		} catch (Exception e) {
			System.out.print(e);
			System.out.println(e.getLocalizedMessage());
			System.out.println(e.getStackTrace());
		}
	}

	public void disconnect(Socket s) throws IOException {
		for (int i = 0; i <= Server.ConnectionArray.size(); i++) {
			if (Server.ConnectionArray.get(i) == s) {
				System.out.println("disconnected");
				Server.ConnectionArray.remove(i);
				Server.Threads.remove(i);

				s.close();
				return;
			}
		}
	}

	public void sendString(String message) {
		try {
			for (int i = 1; i <= Server.ConnectionArray.size(); i++) {
				Socket TEMP_SOCK = (Socket) Server.ConnectionArray.get(i - 1);
				try {
					System.out.println("send " + message + " to " + TEMP_SOCK.getInetAddress());
					PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
					TEMP_OUT.println(message);
					TEMP_OUT.flush();
				} catch (SocketException e) {
					e.printStackTrace();
					ServerGUI.tableEntries
							.add(new TableEntry("Das Programm von " + TEMP_SOCK.getInetAddress() + " ist abgestürzt."));
					disconnect(TEMP_SOCK);
					i--;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();

		}
	}

	public void sendStrings(String[] message) {
		try {
			for (int i = 1; i <= Server.ConnectionArray.size(); i++) {
				Socket TEMP_SOCK = (Socket) Server.ConnectionArray.get(i - 1);
				try {
					System.out.println("send " + message + " to " + TEMP_SOCK.getInetAddress());
					PrintWriter TEMP_OUT = new PrintWriter(TEMP_SOCK.getOutputStream());
					for (String s : message) {
						TEMP_OUT.println(s);
						TEMP_OUT.flush();
					}
					TEMP_OUT.println("!?#end");
					TEMP_OUT.flush();
				} catch (SocketException e) {
					e.printStackTrace();
					ServerGUI.tableEntries
							.add(new TableEntry("Das Programm von " + TEMP_SOCK.getInetAddress() + " ist abgestürzt."));
					disconnect(TEMP_SOCK);
					i--;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();

		}
	}

	private void sendStrings(String[] message, ServerReturn serverReturn) {
		try {
			for (String s : message) {
				OUT.println(s);
				OUT.flush();
			}
			OUT.println("!?#end");
			OUT.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("couldnt send message");
		}
	}
}
