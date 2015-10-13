package application;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseManager {
	
    private File directoryModel=new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/Muster");
    private File projectModel=new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/Project");
    private String db = "D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/";
    private ArrayList<String[]> initList;
    private ArrayList<Customer> customers;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

    public DatabaseManager()
    {
    	loadData();
    	buildInitList();
    }
    
    public void loadData()
    {
    	//TODO: load actual entries
    	customers = new ArrayList<Customer>();
    	Entry entry1 = new Entry(LocalDate.now(), "KundeA", "ItemA", "KontaktA", "phonea", "agenta");
    	Entry entry2 = new Entry(LocalDate.now(), "KundeB", "ItemB", "KontaktB", "phoneb", "agentb");
    	Entry entry3 = new Entry(LocalDate.now(), "KundeC", "ItemC", "KontaktC", "phonec", "agentc");
    	customers.add(new Customer(entry1));
    	customers.add(new Customer(entry2));
    	customers.add(new Customer(entry3));
    }
    
    public void buildInitList(){
    	this.initList = new ArrayList<String[]>();
    	for(Customer c:customers)
    	{
    		for(Entry e:c.getEntries()){
    			String[] initEntry = {e.getDate().toString(), e.getCustomer(), e.getItem(), e.getContact()};
    		    initList.add(initEntry);
    		}
    	}
    }
    
    public boolean createNewProject(Entry entry) throws IOException
    {
    	File file = new File(db + "/laufende_Vorgaenge/" + entry.getCustomer());
    	if (!file.exists()) {
    		if (file.mkdir()) {
    			
    			for(File f:directoryModel.listFiles().clone())
    			{
    				File temp = new File(file.getPath() + "/" + f.getName());
    				copyDirectory(f,temp);
    			}
    		} else {
    			System.out.println("Failed to create directory!");
    			return false;
    		}
    	}
    	File project = new File(file.getPath() + "/" + entry.getDate().format(formatter) + "_" + entry.getItem());
    	if (project.mkdir()){
    		copyDirectory(projectModel, project);
    		entry.setLink(project.getPath());
    		
    		try{
    			File txtfile = new File(project + "/Protokoll.txt");
    			System.out.println(txtfile.canRead());
    			System.out.println(txtfile.exists());
    			PrintWriter writer = new PrintWriter(new FileWriter(txtfile));
    			writer.println("//Kunde");
    			writer.println(entry.getCustomer());writer.println("");
    			writer.println("//Gegenstand");
    			writer.println(entry.getItem());writer.println("");
    			writer.println("//Link");
    			writer.println(entry.getLink());writer.println("");
    			writer.println("//Ansprechpartner");
    			writer.println(entry.getContact());writer.println("");
    			writer.println("//Telefon");
    			writer.println(entry.getPhone());writer.println("");
    			writer.println("//Bearbeiter");
    			writer.println(entry.getAgent());writer.println("");
    			writer.println(entry.getDate().toString() + " Anfrage");
    			writer.close();
    		}
    		catch (Exception e1) {
    			System.out.println("Failed to write protocol");
    			e1.printStackTrace();
    			return false;
    		}
    		return true;
    	}
    	else {
    		System.out.println("Failed to create Project!");
    		return false;
    	}
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
        try (
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target)
        ) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }
    
    public ArrayList<String[]> getInitList()
    {
    	return initList;
    }

	public String[] manageEntry(String[] entry) {
		Entry e= new Entry(LocalDate.now(), entry[0], entry[1], entry[2], entry[3], entry[4]);
		try {
			if(createNewProject(e)) {
				addToCustomer(e);
				String[] newEntry = {e.getDate().toString(), e.getCustomer(), e.getItem(), e.getContact()};
				initList.add(newEntry);
				return newEntry;
			}
			else System.out.println("creation failed");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String[] newEntry={"-1"};
		return newEntry;
	}

	public void addToCustomer(Entry entry) {
		for(Customer c:customers){
			if(c.getName()==entry.getCustomer()){
				c.addEntry(entry);
				return;
			}
		}
		customers.add(new Customer(entry));
	}
}
