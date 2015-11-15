package application;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DatabaseManager {

	private String customerModel;
	private String projectModel;
	public String db;
	private ArrayList<String[]> initList;
	private ArrayList<Customer> customers;
	private ArrayList<String> customerList;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");
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
				ExitBox.display("Konfigurationen Konnten nicht geladen werden.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("config not found");
			e.printStackTrace();
			ExitBox.display("Konfigurationen wurden nicht gefunden.");
		}
	}

	public void loadData() {
		ServerGUI.tableEntries.add(new TableEntry("Daten werden geladen"));
		customerList = new ArrayList<String>();
		File custs = new File(db + "/Kundenverzeichnis");
		for (File f : custs.listFiles()) {
			if (f.isDirectory())
				customerList.add(f.getName());
		}

		customers = new ArrayList<Customer>();
		File files = new File(db + "/laufende_Vorgaenge");
		BufferedReader in;
		String date = null;
		String link = null;
		String customer = null;
		String item = null;
		String lastContact = null;
		String state = null;
		for (File f : files.listFiles()) {
			if (f.isDirectory()) {
				Customer cust = new Customer(f.getName());
				customers.add(cust);
				for (File project : f.listFiles()) {
					if (isProject(project.getName())) {
						File temp = new File(project.getPath() + "/Protokoll.txt");
						try {
							in = new BufferedReader(new FileReader(temp));
							try {
								in.readLine();
								date = in.readLine();
								in.readLine();
								in.readLine();
								lastContact = in.readLine();
								state = in.readLine().substring(1);
								in.readLine();
								in.readLine();
								customer = in.readLine();
								in.readLine();
								in.readLine();
								item = in.readLine();
								in.readLine();
								in.readLine();
								link = in.readLine();
								Entry e = new Entry(date, link, customer, item, lastContact, state);
								cust.addEntry(e);
								in.close();
								checkOld(e);
							} catch (Exception e) {
								System.out.println("couldnt read protocoll");
								e.printStackTrace();
								ExitBox.display(
										"Protokoll von " + customer + "_" + item + " konnte nicht geladen werden.");
							}
						} catch (FileNotFoundException e) {
							System.out.println("protokoll not found");
							e.printStackTrace();
							ExitBox.display("Protokoll von " + customer + "_" + item + " wurde nicht gefunden.");
						}
					}
				}
			}
		}
		ServerGUI.tableEntries.add(new TableEntry("Daten geladen"));
	}

	public void checkOld(Entry e) {
		try{
			switch (Integer.parseInt(e.getState())) {
			case 1:
				if (LocalDate.now().isAfter(LocalDate.parse(e.getDate()).plusDays(addWeekend(LocalDate.now(), this.daysAfterAnfrage)))) {
					e.setLastContact("old " + e.getLastContact());
				}
				break;
			case 4:
				LocalDate reference = LocalDate.parse(e.getLastContact().substring(21, 31));
				if (LocalDate.now().isAfter(reference.plusDays(addWeekend(reference, daysAfterDate)))) {
					e.setLastContact("old " + e.getLastContact());
				}
				break;
			default:
				LocalDate ref = LocalDate.parse(e.getLastContact().substring(0, 10));
				if (LocalDate.now().isAfter(ref.plusDays(addWeekend(LocalDate.now(), daysAfterContact)))) {
					e.setLastContact("old " + e.getLastContact());
				}
				break;
			}
		}
		catch (DateTimeParseException dtpe){
			ExitBox.display(
					"Letzter Kontakt vom Protokoll von " + e.getCustomer() + "_" + e.getItem() + " konnte nicht geladen werden.");
		}
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
		this.initList = new ArrayList<String[]>();
		for (Customer c : customers) {
			for (Entry e : c.getEntries()) {
				initList.add(e.getFirstStatus());
			}
		}

	}

	public boolean createNewProject(Entry entry, String contact, String phone, String agent) throws IOException {
		File projectCustomer = new File(db + "/laufende_Vorgaenge/" + entry.getCustomer());
		if (!projectCustomer.exists()) {
			if (projectCustomer.mkdir())
				ServerGUI.tableEntries.add(new TableEntry(projectCustomer.getAbsolutePath() + " angelegt"));
			else {
				System.out.println("Failed to create customer directory in laufende Vorgänge!");
				return false;
			}
		}
		File project = new File(projectCustomer.getPath() + "/" + LocalDate.parse(entry.getDate()).format(formatter)
				+ "_" + entry.getItem());
		if (project.mkdir()) {
			copyDirectory(new File(projectModel), project);
			entry.setLink(project.getPath());
			try {
				File txtfile = new File(project + "/Protokoll.txt");
				PrintWriter writer = new PrintWriter(new FileWriter(txtfile));
				writer.println("//erster Kontakt");
				writer.println(entry.getLastContact().subSequence(0, 10));
				writer.println("");
				writer.println("//letzter Kontakt");
				writer.println(entry.getLastContact());
				writer.println("#" + entry.getState());
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
				writer.println("//Telefon");
				writer.println(phone);
				writer.println("");
				writer.println("//Bearbeiter");
				writer.println(agent);
				writer.println("");
				writer.println("//Bestätigungsnummer");
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
					copyDirectory(f, temp);
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

	public ArrayList<String[]> getInitList() {
		return initList;
	}

	public String[] manageEntry(String[] entry) {
		Entry e = new Entry(LocalDate.now().toString(), entry[0], entry[1], LocalDate.now().toString() + " Anfrage");
		try {
			if (createNewProject(e, entry[2], entry[3], entry[4])) {
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
		String newContact = LocalDate.now().toString() + " " + edit;
		File txtfile = new File(entry.getLink() + "/Protokoll.txt");
		File tmp = new File(entry.getLink() + "/tmp.txt");
		try {
			BufferedReader file = new BufferedReader(new FileReader(txtfile));
			String line;
			PrintWriter writer = new PrintWriter(new FileWriter(tmp));
			int i = 0;
			while ((line = file.readLine()) != null) {
				if (i == 4) {
					if(entry.getLastContact().contains("Los")){
						if(newContact.contains("Kontakt")){
							writer.println(line);
						}
						else writer.println(newContact);
					}
					else writer.println(newContact);
				} else if (i == 5)
					writer.println(state);
				else if (i == 26){
					if(edit.contains("Bestätigung")) writer.println(edit.substring(12));
					else writer.println(line);
				}
				else writer.println(line);
				i++;
			}
			writer.println(newContact);
			file.close();
			writer.close();
			txtfile.delete();
			tmp.renameTo(new File(entry.getLink() + "/Protokoll.txt"));
			ServerGUI.tableEntries
					.add(new TableEntry(entry.getCustomer() + "_" + entry.getItem() + " wurde bearbeitet"));
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		entry.setLastContact(newContact);
		for (String[] s : initList) {
			if (entry.getLink().contentEquals(s[0])) {
				initList.remove(s);
				initList.add(entry.getFirstStatus());
				break;
			}
		}
		if(edit.contains("Projekt beendet")){
			archive(entry.getLink());
		}
		return true;
	}

	public String[] getDetails(String link) {
		File file = new File(link + "/Protokoll.txt");

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
		File projectLink = new File(e.getLink());
		if (editEntry(e, "#6", "Archiviert")) {
			File customer = new File(db + "/abgeschlossene_Vorgaenge/" + e.getCustomer());
			if (!customer.exists()) {
				if (customer.mkdir())
					;
				else {
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

	public ArrayList<String> getCustomerList() {
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
}
