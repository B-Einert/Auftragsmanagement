package application;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseManager {
	
    private File projectModel=new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/Muster");
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
    	Entry entry1 = new Entry(LocalDate.now(), "KundeA", "ItemA", "KontaktA");
    	Entry entry2 = new Entry(LocalDate.now(), "KundeB", "ItemB", "KontaktB");
    	Entry entry3 = new Entry(LocalDate.now(), "KundeC", "ItemC", "KontaktC");
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
    	//directoryCheck();
    	File file = new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/" + entry.getCustomer());
    	if (!file.exists()) {
    		if (file.mkdir()) {
    			System.out.println("Directory is created!");
    			System.out.println(projectModel.canRead());
    			System.out.println(projectModel.canWrite());
    			
//    			new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/" + entry.getCustomer() + "/Archiv").mkdir();
//    			new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/" + entry.getCustomer() + "/QS-Vereinbarungen").mkdir();
//    			new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/" + entry.getCustomer() + "/Vertraulichkeitsvereinbarungen").mkdir();
//    			new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/" + entry.getCustomer() + "/Zeichnungen").mkdir();
//    			file = new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/" + entry.getCustomer() + "/" + entry.getDate().format(formatter) + "_" + entry.getItem());
//    			file.mkdir();  
    			
    			
    			for(File f:projectModel.listFiles().clone())
    			{
    				File temp = new File(file.getPath() + "/" + f.getName());
    				copy(f,temp);
    			}
    			return true;
    		} else {
    			System.out.println("Failed to create directory!");
    			return false;
    		}
    	}
    	else
    	{
    		System.out.println("Directory already exists");
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
		Entry e= new Entry(LocalDate.now(), entry[0], entry[1], entry[2]);
		addToCustomer(e);
		try {
			createNewProject(e);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String[] newEntry = {e.getDate().toString(), e.getCustomer(), e.getItem(), e.getContact()};
		initList.add(newEntry);
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
