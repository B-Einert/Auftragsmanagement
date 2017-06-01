package application;

import java.net.*;
import java.time.format.DateTimeFormatter;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import javafx.application.Platform;
import javafx.scene.control.TreeItem;

public class ClientReceiver implements Runnable {

	private static Socket SOCK;
	private ObjectInputStream objIn;
	private static Scanner INPUT;
	private String message;
	private DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	// private DateTimeFormatter dtf =
	// DateTimeFormatter.ofPattern("yyyy-MMM-dd");

	public ClientReceiver() {
		this.SOCK = ClientGUI.SOCK;
		try {
			try {
				Thread X = new Thread(this);
				X.start();
			}

			catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				ExitBox.display("Verbindung zum Server konnte nicht hergestellt werden.");
			}
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			ExitBox.display("Verbindung zum Server konnte nicht hergestellt werden.");
		}
	}

	public void run() {
		try {
			try {
				initialize();
				CheckStream();
			} finally {
				SOCK.close();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void initialize() {
		try {
			objIn = new ObjectInputStream(new BufferedInputStream(SOCK.getInputStream()));
			System.out.println("objin init");

			ClientGUI.sender.init();

			INPUT = new Scanner(SOCK.getInputStream());
			System.out.println("Input init");

			Object received = null;
			while (received == null) {
				received = objIn.readObject();
			}
			HashSet<String[]> initList = (HashSet<String[]>) received;

			for (String[] entry : initList) {
				Entry e = new Entry(entry[0], entry[1], entry[2], entry[3], entry[4], Integer.parseInt(entry[5]));
				ClientGUI.entries.add(e);
			}
			received = null;
			while (received == null) {
				received = objIn.readObject();
			}
			for (String customer : (HashSet<String>) received) {
				ClientGUI.customers.add(customer);
			}
			received = null;
			while (received == null) {
				received = objIn.readObject();
			}
			for (String customer : (HashSet<String>) received) {
				ClientGUI.archived.add(new TreeItem<ArchiveEntry>(new ArchiveEntry(customer)));
			}
			received=null;
			while (received == null) {
				received = objIn.readObject();
			}
			ClientGUI.datenbank=(String)received;
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			ExitBox.display("Beim Erstellen der Tabelle ist ein Fehler aufgetreten.");
		}
	}

	public static void DISCONNECT() throws IOException {

		try {
			ClientGUI.sender.sendString("disconnect");
		} catch (Exception e) {

		}
		INPUT.close();
		SOCK.close();
		System.exit(0);
	}

	// LATER
	public void CheckStream() {
		while (true) {
			RECEIVE();
		}
	}

	public void RECEIVE() {
		if (INPUT.hasNext()) {
			try {
				message = INPUT.nextLine();
				System.out.println("received: " + message);
				if (message.contains("new")) {
					receiveNewEntry();
				} else if (message.contains("disconnect")) {
					INPUT.close();
					SOCK.close();
					AlertBox.display("Das Serverprogramm wurde beendet");
				} else if (message.contentEquals("alert")) {
					AlertBox3.display(INPUT.nextLine());
				} else if (message.contains("edit")) {
					message = INPUT.nextLine();
					for (Entry e : ClientGUI.entries) {
						if (message.contentEquals(e.getLinkString())) {
							e.setContact(INPUT.nextLine());
							e.setState(Integer.parseInt(INPUT.nextLine()));
							System.out.println(e.getContact());
							Entry entry = new Entry(e.getLinkString(), e.getDate(), e.getCustomer(), e.getItem(),
									e.getContact(), e.getState());
							Platform.runLater(() -> {
								ClientGUI.entries.remove(e);
								ClientGUI.entries.add(entry);
							});
							break;
						}
					}
				} else if (message.contains("archivedProjects")) {
					String customer = INPUT.nextLine();
					LinkedList<TreeItem<ArchiveEntry>> projects = new LinkedList<TreeItem<ArchiveEntry>>();
					while (true) {
						String test = INPUT.nextLine();
						if (test.contentEquals("!?#end"))
							break;
						else {
							projects.add(new TreeItem<ArchiveEntry>(new ArchiveEntry(test, INPUT.nextLine(),
									INPUT.nextLine(), INPUT.nextLine(), INPUT.nextLine())));

						}
					}
					for (TreeItem<ArchiveEntry> t : ClientGUI.archived) {
						if (t.getValue().getName().contentEquals(customer)) {
							t.getChildren().setAll(projects);
							break;
						}
					}
				} else if (message.contains("miniDetail")) {
					LinkedList<String> details = new LinkedList<String>();
					try {
						String next;
						while (INPUT.hasNext()) {
							if (!(next = INPUT.nextLine()).contentEquals("!?#end"))
								details.add(next);
							else
								break;
						}
						AuftragsBox.setDetails(details);
						AuftragsBox.setReady(true);
						System.out.println("details done");
					} catch (Exception e) {
						e.printStackTrace();
						ExitBox.display("Details konnten nicht korrekt geladen werden.");
					}
				} else if (message.contains("detail")) {
					System.out.println("in the details");
					LinkedList<String> details = new LinkedList<String>();
					try {
						String next;
						while (INPUT.hasNext()) {
							if (!(next = INPUT.nextLine()).contentEquals("!?#end"))
								details.add(next);
							else
								break;
						}
						DetailBox.setDetails(details);
						DetailBox.setReady(true);
						System.out.println("details done");
					} catch (Exception e) {
						e.printStackTrace();
						ExitBox.display("Details konnten nicht korrekt geladen werden.");
					}
				} else if (message.contains("delete")) {
					message = INPUT.nextLine();
					System.out.println(message);
					for (Entry e : ClientGUI.entries) {
						if (message.contentEquals(e.getLinkString())) {
							Platform.runLater(() -> {
								ClientGUI.entries.remove(e);
							});
							boolean found = false;
							for (TreeItem<ArchiveEntry> ti : ClientGUI.archived) {
								if (e.getCustomer().contentEquals(ti.getValue().getName())) {
									found = true;
									break;
								}
							}
							if (!found)
								ClientGUI.archived.add(new TreeItem<ArchiveEntry>(new ArchiveEntry(e.getCustomer())));
							break;
						}
					}

				} else if (message.contains("changeLink")) {
					String link = INPUT.nextLine();
					String newLink = INPUT.nextLine();
					for (Entry e : ClientGUI.entries) {
						if (link.contentEquals(e.getLinkString())) {
							e.setLinkString(newLink);
							e.setItem(new File(newLink).getName().substring(7));
							Entry entry = new Entry(e.getLinkString(), e.getDate(), e.getCustomer(), e.getItem(),
									e.getContact(), e.getContactDate(), e.getState());
							Platform.runLater(() -> {
								ClientGUI.entries.remove(e);
								ClientGUI.entries.add(entry);
							});
							break;
						}
					}
				} else if (message.contentEquals("cust")) {
					message = INPUT.nextLine();
					ClientGUI.customers.add(message);
				} else if (message.contentEquals("custInf")) {
					Map<String, String> answer = new HashMap<String, String>();
					try {
						String next;
						while (INPUT.hasNext()) {
							if (!(next = INPUT.nextLine()).contentEquals("!?#end"))
								answer.put(next, INPUT.nextLine());
							else
								break;
						}
						CreateBox.setPartnerInfo(answer);
						System.out.println("partners done");
					} catch (Exception e) {
						e.printStackTrace();
						ExitBox.display("Datenübertragung fehlgeschlagen");
					}
					CreateBox.setReady(true);
				} else if (message.contains("archive")) {
					String message = INPUT.nextLine();
					for (TreeItem<ArchiveEntry> e : ClientGUI.archived) {
						if (message.contentEquals(e.getValue().getName())) {
							return;
						}
					}
					ClientGUI.archived.add(new TreeItem<ArchiveEntry>(new ArchiveEntry(message)));
				} else if (message.contains("midnight")) {
					ClientGUI.entries.clear();
					HashSet<String[]> initList = new HashSet<String[]>();
					String next;
					while (INPUT.hasNext()) {
						if (!(next = INPUT.nextLine()).contentEquals("!?#end")) {
							Entry e = new Entry(next, INPUT.nextLine(), INPUT.nextLine(), INPUT.nextLine(),
									INPUT.nextLine(), Integer.parseInt(INPUT.nextLine()));
							Platform.runLater(() -> {
								ClientGUI.entries.add(e);
							});
						} else
							break;
					}
					Platform.runLater(() -> {
						ClientGUI.table.sort();
					});
				}
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
				ExitBox.display("Datenübertragung fehlgeschlagen");
			}

		}
	}

	public void receiveNewEntry() {
		LinkedList<String> entry = new LinkedList<String>();
		String next;
		while (INPUT.hasNext()) {
			if (!(next = INPUT.nextLine()).contentEquals("!?#end"))
				entry.add(next);
			else
				break;
		}
		Entry e = new Entry(entry.get(0), entry.get(1), entry.get(2), entry.get(3), entry.get(4),
				Integer.parseInt(entry.get(5)));
		Platform.runLater(() -> {
			ClientGUI.entries.add(e);
		});
	}
}
