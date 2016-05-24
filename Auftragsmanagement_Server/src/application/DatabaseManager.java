package application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class DatabaseManager {

	private String customerModel;
	private String projectModel;
	public String db;
	private HashSet<String[]> initList;
	private HashSet<Customer> customers;
	private HashSet<String> customerList;
	private HashSet<String> archivedList;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
	private DateFormat date = new DateFormat();
	private int daysAfterAnfrage;
	private int daysAfterContact;
	private int daysAfterDate;

	public DatabaseManager() {
		config();
		loadData();
		buildInitList();
	}

	public void config() {
		File config = new File("config.txt");
		try {
			BufferedReader in = new BufferedReader(new FileReader(config));
			try {
				in.readLine();
				db = in.readLine();
				in.readLine();
				in.readLine();
				customerModel = in.readLine();
				in.readLine();
				in.readLine();
				projectModel = in.readLine();
				in.readLine();
				in.readLine();
				daysAfterAnfrage = Integer.parseInt(in.readLine());
				in.readLine();
				in.readLine();
				daysAfterContact = Integer.parseInt(in.readLine());
				in.readLine();
				in.readLine();
				daysAfterDate = Integer.parseInt(in.readLine());
				in.close();
				ServerGUI.tableEntries.add(new TableEntry("Konfigurationen geladen"));
			} catch (Exception e) {
				System.out.println("couldnt read config");
				e.printStackTrace();
				ExitBox.display("Konfigurationen(Config-Datei) Konnten nicht geladen werden.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("config not found");
			e.printStackTrace();
			ExitBox.display("Konfigurationen(Config-Datei) wurden nicht gefunden.");
		}
	}

	public void loadData() {
		ServerGUI.tableEntries.add(new TableEntry("Daten werden geladen"));
		customerList = new HashSet<String>();
		archivedList = new HashSet<String>();
		File custs = new File(db + "/abgeschlossene_Vorgaenge");
		System.out.println(custs.exists());
		if (custs.listFiles() != null) {
			for (File f : custs.listFiles()) {
				if (f.isDirectory())
					customerList.add(f.getName());
				archivedList.add(f.getName());
			}
		}

		customers = new HashSet<Customer>();
		File files = new File(db + "/laufende_Vorgaenge");
		BufferedReader in;
		String date = null;
		String link = null;
		String customer = null;
		String item = null;
		String lastContact = null;
		String state = null;
		if(files.listFiles()!=null){
		for (File f : files.listFiles()) {
			if (f.isDirectory()) {
				customerList.add(f.getName());
				Customer cust = new Customer(f.getName());
				customers.add(cust);
				for (File project : f.listFiles()) {
					if (isProject(project.getName())) {
						File temp = new File(project.getPath() + "/Protokoll.txt");
						if (temp.exists()) {
							try {
								in = new BufferedReader(new FileReader(temp));
								try {
									in.readLine();
									date = in.readLine();
									in.readLine();
									in.readLine();
									lastContact = in.readLine();
									in.readLine();
									in.readLine();
									customer = in.readLine();
									in.readLine();
									in.readLine();
									item = in.readLine();
									in.readLine();
									in.readLine();
									link = in.readLine();
									state = getState(lastContact);
									if(state==""){
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										in.readLine();
										String weirdContact="";
										String line;
										while (true) {
											if ((line = in.readLine()) == null)
												break;
											if(line.contains("Kontakt aufgenommen")){
												weirdContact=line.substring(0, 10)+weirdContact.substring(10);
											}
											else{
												weirdContact=line;
											}
										}
										lastContact=weirdContact;
										state=getState(lastContact);
									}
									Entry e = new Entry(date, link, customer, item, lastContact, state);
									cust.addEntry(e);
									in.close();
									checkOld(e);
								} catch (Exception e) {
									System.out.println("couldnt read protocoll");
									e.printStackTrace();
									ServerGUI.tableEntries.add(new TableEntry("Protokoll von " + customer + "/"
											+ project.getName() + " konnte nicht geladen werden."));
								}
							} catch (FileNotFoundException e) {
								System.out.println("protokoll not found");
								e.printStackTrace();
								AlertBox.display("Protokoll von " + customer + "/" + project.getName()
										+ " wurde nicht gefunden.");
							}
						}
					}
				}
			}
		}
		}
		ServerGUI.tableEntries.add(new TableEntry("Daten geladen"));
	}

	public boolean checkOld(Entry e) {
		try {
			System.out.println(e.getState());
			switch (Integer.parseInt(e.getState())) {
			case 1:
				LocalDate ref1 = date.toDate(e.getLastContact().substring(0, 10));
				System.out.println(ref1);
				if (LocalDate.now().isAfter(ref1.plusDays(addWeekend(LocalDate.now(), this.daysAfterAnfrage)))) {
					e.setLastContact("old " + e.getLastContact());
					return true;
				}
				return false;
			case 4:
				try {
					System.out.println(e.getLastContact().substring(30));
					LocalDate reference = date.toDate(e.getLastContact().substring(30));
					System.out.println(reference);
					if (LocalDate.now().isAfter(reference.plusDays(addWeekend(reference, daysAfterDate)))) {
						e.setLastContact("old " + e.getLastContact());
						return true;
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					break;
				}
				return false;
			default:
				LocalDate ref = date.toDate(e.getLastContact().substring(0, 10));
				System.out.println(ref);
				if (LocalDate.now().isAfter(ref.plusDays(addWeekend(LocalDate.now(), daysAfterContact)))) {
					e.setLastContact("old " + e.getLastContact());
					return true;
				}
				return false;
			}
		} catch (DateTimeParseException dtpe) {
			ServerGUI.tableEntries.add(new TableEntry("Letzter Kontakt vom Protokoll von " + e.getCustomer() + "_"
					+ e.getItem() + " konnte nicht geladen werden."));
			return false;
		}
		return false;
	}

	public String getState(String action) {
		String answer = "";
		if (action.contains("Anfrage erstellt"))
			answer = "1";
		else if (action.contains("Angebot erstellt"))
			answer = "2";
		else if (action.contains("Auftrag erhalten"))
			answer = "3";
		else if (action.contains("Projekt abgeschlossen") || action.contains("Archiviert"))
			answer = "6";
		else if (action.contains("Auftrag bestätigt") || action.contains("Los"))
			answer = "4";
		if (answer==""){
			
		}
		System.out.println(action);
		System.out.println(answer);
		return answer;
	}

	public int addWeekend(LocalDate start, int days) {
		for (int i = 0; i <= days; i++) {
			if (start.plusDays(i).getDayOfWeek() == DayOfWeek.SATURDAY
					|| start.plusDays(i).getDayOfWeek() == DayOfWeek.SUNDAY) {
				days++;
			}
		}
		return days;
	}

	public boolean isProject(String name) {
		try {
			Integer.parseInt(name.substring(0, 5));
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public void buildInitList() {
		this.initList = new HashSet<String[]>();
		for (Customer c : customers) {
			for (Entry e : c.getEntries()) {
				initList.add(e.getFirstStatus());
			}
		}

	}

	public boolean createNewProject(Entry entry, String contact, String phone, String agent, String artnum,
			String copyDir) throws IOException {
		File projectCustomer = new File(db + "/laufende_Vorgaenge/" + entry.getCustomer());
		if (!projectCustomer.exists()) {
			if (projectCustomer.mkdir())
				ServerGUI.tableEntries.add(new TableEntry(projectCustomer.getAbsolutePath() + " angelegt"));
			else {
				System.out.println("Failed to create customer directory in laufende Vorgänge!");
				return false;
			}
		}
		String newLink = "/laufende_Vorgaenge/" + entry.getCustomer() + "/" + LocalDate.now().format(formatter) + "_"
				+ entry.getItem();
		File project = new File(db + newLink);
		if (project.mkdir()) {
			if (copyDir.contentEquals(""))
				copy(new File(projectModel), project);
			else {
				copy(new File(db + copyDir), project);
				new File(project + "/Protokoll.txt").delete();
			}
			entry.setLink(newLink);
			try {
				File txtfile = new File(project + "/Protokoll.txt");
				PrintWriter writer = new PrintWriter(new FileWriter(txtfile));
				writer.println("//erster Kontakt");
				writer.println(entry.getLastContact().subSequence(0, 10));
				writer.println("");
				writer.println("//letzter Kontakt");
				writer.println(entry.getLastContact());
				writer.println("");
				writer.println("//Kunde");
				writer.println(entry.getCustomer());
				writer.println("");
				writer.println("//Gegenstand");
				writer.println(entry.getItem());
				writer.println("");
				writer.println("//Link");
				writer.println(entry.getLink());
				writer.println("");
				writer.println("//Ansprechpartner");
				writer.println(contact);
				writer.println("");
				writer.println("//Kontakt");
				writer.println(phone);
				writer.println("");
				writer.println("//Bearbeiter");
				writer.println(agent);
				writer.println("");
				writer.println("//Artikelnummer");
				writer.println(artnum);
				writer.println("");
				writer.println("//AB");
				writer.println("");
				writer.println("");
				writer.println("");
				writer.println(entry.getLastContact());
				writer.close();
				ServerGUI.tableEntries.add(new TableEntry(project.getAbsolutePath() + " und Untrordner angelegt"));
			} catch (Exception e1) {
				System.out.println("Failed to write protocol");
				e1.printStackTrace();
				return false;
			}
		} else {
			System.out.println("Failed to create Project!");
			return false;
		}
		File customer = new File(db + "/Kundenverzeichnis/" + entry.getCustomer());
		if (!customer.exists()) {
			if (customer.mkdir()) {
				for (File f : new File(customerModel).listFiles()) {
					File temp = new File(customer.getPath() + "/" + f.getName());
					copy(f, temp);
				}
				ServerGUI.tableEntries.add(new TableEntry(customer.getAbsolutePath() + " und Untrordner angelegt"));
			} else {
				System.out.println("Failed to create customer directory in Kundenverzeichnis!");
				return false;
			}
		}
		return true;

	}

	public void copy(File sourceLocation, File targetLocation) throws IOException {
		if (sourceLocation.isDirectory()) {
			copyDirectory(sourceLocation, targetLocation);
		} else {
			copyFile(sourceLocation, targetLocation);
		}
	}

	private void copyDirectory(File source, File target) throws IOException {
		if (!target.exists()) {
			target.mkdir();
		}

		for (String f : source.list()) {
			copy(new File(source, f), new File(target, f));
		}
	}

	private void copyFile(File source, File target) throws IOException {
		try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(target)) {
			byte[] buf = new byte[1024];
			int length;
			while ((length = in.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
		}
	}

	public HashSet<String[]> getInitList() {
		return initList;
	}

	public HashSet<String> getArchivedList() {
		return archivedList;
	}

	public String[] manageEntry(String[] entry) {
		Entry e = new Entry(this.date.toString(LocalDate.now()), entry[0], entry[1]);
		try {
			if (createNewProject(e, entry[2], entry[3], entry[4], "", "")) {
				addToCustomer(e);
				initList.add(e.getFirstStatus());
				return e.getFirstStatus();
			} else
				System.out.println("creation failed");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String[] newEntry = { "-1" };
		return newEntry;
	}

	public Entry createDouble(String link, String customer, String item, String contact, String tel, String agent,
			String artnum) {
		Entry e = new Entry(this.date.toString(LocalDate.now()), "", customer, item,
				this.date.toString(LocalDate.now()) + " Dupliziert", "3");
		try {
			if (createNewProject(e, contact, tel, agent, artnum, link)) {
				addToCustomer(e);
				initList.add(e.getFirstStatus());
				return e;
			} else
				System.out.println("creation failed");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	public void addToCustomer(Entry entry) {
		for (Customer c : customers) {
			if (c.getName() == entry.getCustomer()) {
				c.addEntry(entry);
				return;
			}
		}
		customers.add(new Customer(entry));
	}

	public Entry findEntry(String link) {
		for (Customer c : customers) {
			for (Entry e : c.getEntries()) {
				if (link.contentEquals(e.getLink())) {
					return e;
				}
			}
		}
		System.out.println("entry not found");
		return null;
	}

	public boolean editEntry(Entry entry, String state, String edit) {
		entry.setState(state.substring(1));
		String newContact = this.date.toString(LocalDate.now()) + " " + edit;
		String weirdContact = "";
		if (newContact.contains("Kontakt")){
			if(entry.getLastContact().contains("old")) weirdContact = this.date.toString(LocalDate.now()) + entry.getLastContact().substring(14);
			else weirdContact = this.date.toString(LocalDate.now()) + entry.getLastContact().substring(10);
		}
		File txtfile = new File(db + entry.getLink() + "/Protokoll.txt");
		File tmp = new File(db + entry.getLink() + "/tmp.txt");
		try {
			BufferedReader file = new BufferedReader(new FileReader(txtfile));
			String line;
			PrintWriter writer = new PrintWriter(new FileWriter(tmp));
			int i = 0;
			while ((line = file.readLine()) != null) {
				if (i == 4) {
					if (newContact.contains("Kontakt"))
						writer.println(weirdContact);
					else
						writer.println(newContact);
				} else if (i == 13 && edit.contentEquals("Archiviert")) {
					writer.println("/abgeschlossene_Vorgaenge/" + entry.getCustomer() + "/"
							+ new File(entry.getLink()).getName());
				} else if (i == 28) {
					if (edit.contains("Auftrag bestätigt")) {
						System.out.println(edit);
						writer.println(edit.substring(18));
					} else
						writer.println(line);
				} else
					writer.println(line);
				i++;
			}
			writer.println(newContact);
			file.close();
			writer.close();
			txtfile.delete();
			tmp.renameTo(new File(db + entry.getLink() + "/Protokoll.txt"));
			ServerGUI.tableEntries
					.add(new TableEntry(entry.getCustomer() + "_" + entry.getItem() + " wurde bearbeitet"));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		if (newContact.contains("Kontakt"))
			entry.setLastContact(weirdContact);
		else
			entry.setLastContact(newContact);
		for (String[] s : initList) {
			if (entry.getLink().contentEquals(s[0])) {
				initList.remove(s);
				initList.add(entry.getFirstStatus());
				break;
			}
		}
		if (edit.contains("Projekt abgeschlossen")) {
			archive(entry.getLink());
		}
		return true;
	}

	public String editWholeProject(Entry entry, LinkedList<String> edits) {
		if (!entry.getItem().contentEquals(edits.get(0))) {

			String newLink = "/" + LocalDate.now().format(formatter) + "_" + edits.get(0);
			File project = new File(new File(db + entry.getLink()).getParentFile().getPath() + newLink);
			try {
				Files.move(new File(db + entry.getLink()).toPath(), project.toPath(), StandardCopyOption.ATOMIC_MOVE);
				for (String[] s : initList) {
					if (entry.getLink().contentEquals(s[0])) {
						initList.remove(s);
						entry.setItem(edits.get(0));
						entry.setLink(newLink);
						initList.add(entry.getFirstStatus());
						break;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "alert";
			}
		}
		File txtfile = new File(db + entry.getLink() + "/Protokoll.txt");
		File tmp = new File(db + entry.getLink() + "/tmp.txt");
		try {
			BufferedReader file = new BufferedReader(new FileReader(txtfile));
			String line;
			PrintWriter writer = new PrintWriter(new FileWriter(tmp));
			int i = 0;
			while ((line = file.readLine()) != null) {
				if (i == 10) {
					writer.println(edits.get(0));
				} else if (i == 13) {
					writer.println(entry.getLink());
				} else if (i == 16) {
					writer.println(edits.get(1));
				} else if (i == 19) {
					writer.println(edits.get(2));
				} else if (i == 22) {
					writer.println(edits.get(3));
				} else if (i == 25) {
					writer.println(edits.get(4));
				} else
					writer.println(line);
				i++;
			}
			file.close();
			writer.close();
			txtfile.delete();
			tmp.renameTo(new File(db + entry.getLink() + "/Protokoll.txt"));
			ServerGUI.tableEntries
					.add(new TableEntry(entry.getCustomer() + "_" + entry.getItem() + " wurde bearbeitet"));
		} catch (IOException e) {
			e.printStackTrace();
			return "alert";
		}
		return "";
	}

	public String[] getAuftragsDetails(String link) {
		File file = new File(db + link + "/Protokoll.txt");
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			LinkedList<String> det = new LinkedList<String>();
			try {
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.close();
				String[] details = det.toArray(new String[0]);
				return details;
			} catch (Exception e) {
				System.out.println("couldnt read protocoll");
				e.printStackTrace();
				return null;
			}

		} catch (FileNotFoundException e1) {
			System.out.println("couldnt find protokoll");
			e1.printStackTrace();
		}

		return null;
	}

	public String[] getDetails(String link) {
		File file = new File(db + link + "/Protokoll.txt");
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));
			LinkedList<String> det = new LinkedList<String>();
			String line;
			try {
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				det.add(in.readLine());
				in.readLine();
				in.readLine();
				while (true) {
					if ((line = in.readLine()) == null)
						break;
					det.add(line);
				}
				in.close();
				String[] details = det.toArray(new String[0]);
				return details;
			} catch (Exception e) {
				System.out.println("couldnt read protocoll");
				e.printStackTrace();
				return null;
			}

		} catch (FileNotFoundException e1) {
			System.out.println("couldnt find protokoll");
			e1.printStackTrace();
		}

		return null;
	}

	public boolean archive(String link) {
		Entry e = findEntry(link);
		File projectLink = new File(db + e.getLink());
		if (editEntry(e, "#6", "Archiviert")) {
			File customer = new File(db + "/abgeschlossene_Vorgaenge/" + e.getCustomer());
			if (!customer.exists()) {
				if (customer.mkdir()) {
					archivedList.add(e.getCustomer());
				} else {
					System.out.println("Failed to create customer directory in abgeschlossene Vorgänge!");
					return false;
				}
			}
			File project = new File(customer.getPath() + "/" + projectLink.getName());
			try {
				Files.move(projectLink.toPath(), project.toPath(), StandardCopyOption.ATOMIC_MOVE);
				System.out.println(new File(db + "/laufende_Vorgaenge/" + e.getCustomer()).delete());
				for (String[] s : initList) {
					if (e.getLink().contentEquals(s[0])) {
						initList.remove(s);
						break;
					}
				}
				for (Customer c : this.customers) {
					if (c.getEntries().contains(e)) {
						c.getEntries().remove(e);
						if (c.getEntries().size() == 0)
							customers.remove(c);
						break;
					}
				}
				System.out.println("Vorgang " + customer.getName() + "/" + project.getName() + " archiviert");
				ServerGUI.tableEntries.add(new TableEntry(
						"Vorgang " + customer.getName() + "/" + project.getName() + " wurde archiviert"));
				return true;
			} catch (Exception ex) {
				System.out.println("couldnt archive");
				ex.printStackTrace();
				return false;
			}
		}
		return false;
	}

	public HashSet<String> getCustomerList() {
		return this.customerList;
	}

	public boolean checkCustomer(String customer) {
		if (this.customerList.contains(customer)) {
			return false;
		} else {
			customerList.add(customer);
			return true;
		}
	}

	public Map<String, String> getCustInf(String customer) {
		Map<String, String> answer = new HashMap<String, String>();
		File[] files = { new File(db + "/abgeschlossene_Vorgaenge/" + customer),
				new File(db + "/laufende_Vorgaenge/" + customer) };
		BufferedReader in;
		String partner;
		String phone;
		for (File headfile : files) {
			if (headfile.exists()) {
				for (File f : headfile.listFiles()) {
					File temp = new File(f.getPath() + "/Protokoll.txt");
					try {
						in = new BufferedReader(new FileReader(temp));
						try {
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							partner = in.readLine();
							in.readLine();
							in.readLine();
							phone = in.readLine();
							System.out.println(partner + " " + phone);
							in.close();
							answer.put(partner, phone);
						} catch (Exception e) {
							System.out.println("couldnt read protocoll");
							e.printStackTrace();
						}
					} catch (FileNotFoundException e) {
						System.out.println("protokoll not found");
						e.printStackTrace();
					}
				}
			}
		}
		return answer;
	}

	public String[] getArchivedProjects(String customer) {
		File custFile = new File(this.db + "/abgeschlossene_Vorgaenge/" + customer);
		if (!custFile.exists()) {
			System.out.println("fail");
			return null;
		} else {
			LinkedList<String> det = new LinkedList<String>();
			for (File f : custFile.listFiles()) {
				File protokoll = new File(f.getAbsolutePath() + "/Protokoll.txt");
				if (protokoll.exists()) {
					try {
						BufferedReader in = new BufferedReader(new FileReader(protokoll));
						try {
							in.readLine();
							det.add(in.readLine());
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							det.add(in.readLine());
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							in.readLine();
							det.add(in.readLine());
							in.readLine();
							in.readLine();
							det.add(in.readLine());
							System.out.println(f.getAbsolutePath().substring(db.length()));
							det.add(f.getAbsolutePath().substring(db.length()));
							in.close();
						} catch (Exception e) {
							System.out.println("couldnt read protocoll");
							e.printStackTrace();
							return null;
						}

					} catch (FileNotFoundException e1) {
						System.out.println("couldnt find protokoll");
						e1.printStackTrace();
						return null;
					}
				}
			}
			String[] details = det.toArray(new String[0]);
			return details;
		}
	}

	public void midnight() {
		for(Customer c: customers){
			for(Entry e: c.getEntries()){
				if(!e.getLastContact().startsWith("old")){
					//if(checkOld(e)){
					e.setItem("wipwap!");
						for (String[] s : initList) {
							if (e.getLink().contentEquals(s[0])) {
								initList.remove(s);
								initList.add(e.getFirstStatus());
								break;
							}
						}
					//}
				}
			}
		}
	}
}
