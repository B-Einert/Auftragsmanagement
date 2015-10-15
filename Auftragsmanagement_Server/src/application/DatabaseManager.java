package application;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseManager {
	
    private File directoryModel=new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/Muster");
    private File projectModel=new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/Project");
    public static String db = "D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/";
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
    	customers = new ArrayList<Customer>();
    	File files = new File(db + "laufende_Vorgaenge");
    	BufferedReader in;
    	String date; 
    	String link; 
    	String customer; 
    	String item; 
    	String lastContact;
    	for(File f: files.listFiles()){
    		if(f.isDirectory()){
	    		Customer cust = new Customer(f.getName());
	    		customers.add(cust);
	    		for(File project: f.listFiles()){
	    			if(isProject(project.getName())){
	    				File temp = new File(project.getPath() + "/Protokoll.txt");
	    				try {
							in = new BufferedReader(new FileReader(temp));
							try{
		    					in.readLine();
		    					date=in.readLine();in.readLine();in.readLine();
		    					lastContact=in.readLine();in.readLine();in.readLine();
		    					customer=in.readLine();in.readLine();in.readLine();
		    					item=in.readLine();in.readLine();in.readLine();
		    					link=in.readLine();
		    					cust.addEntry(new Entry(date, link, customer, item, lastContact));
		    					in.close();
		    				}
		    				catch(Exception e){
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
    	}
    }
    
    public boolean isProject(String name){
    	try  
    	{  
    		Integer.parseInt(name.substring(0, 5));  
    	}  
    	catch(NumberFormatException nfe)  
    	{
    		return false;  
    	}
    	return true;
    }
    
    public void buildInitList(){
    	this.initList = new ArrayList<String[]>();
    	for(Customer c:customers)
    	{
    		for(Entry e:c.getEntries()){
    		    initList.add(e.getFirstStatus());
    		}
    	}
    }
    
    public boolean createNewProject(Entry entry, String contact, String phone, String agent) throws IOException
    {
    	File file = new File(db + "/laufende_Vorgaenge/" + entry.getCustomer());
    	if (!file.exists()) {
    		if (file.mkdir()) {
    			
    			for(File f:directoryModel.listFiles())
    			{
    				File temp = new File(file.getPath() + "/" + f.getName());
    				copyDirectory(f,temp);
    			}
    		} else {
    			System.out.println("Failed to create directory!");
    			return false;
    		}
    	}
    	File project = new File(file.getPath() + "/" + LocalDate.parse(entry.getDate()).format(formatter) + "_" + entry.getItem());
    	if (project.mkdir()){
    		copyDirectory(projectModel, project);
    		entry.setLink(project.getPath());
    		
    		try{
    			File txtfile = new File(project + "/Protokoll.txt");
    			PrintWriter writer = new PrintWriter(new FileWriter(txtfile));
    			writer.println("//erster Kontakt");
    			writer.println(entry.getLastContact().subSequence(0, 10));writer.println("");
    			writer.println("//letzter Kontakt");
    			writer.println(entry.getLastContact());writer.println("");
    			writer.println("//Kunde");
    			writer.println(entry.getCustomer());writer.println("");
    			writer.println("//Gegenstand");
    			writer.println(entry.getItem());writer.println("");
    			writer.println("//Link");
    			writer.println(entry.getLink());writer.println("");
    			writer.println("//Ansprechpartner");
    			writer.println(contact);writer.println("");
    			writer.println("//Telefon");
    			writer.println(phone);writer.println("");
    			writer.println("//Bearbeiter");
    			writer.println(agent);writer.println("");writer.println("");
    			writer.println(entry.getLastContact());
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
		Entry e= new Entry(entry[0], entry[1]);
		try {
			if(createNewProject(e, entry[2], entry[3], entry[4])) {
				addToCustomer(e);
				
				initList.add(e.getFirstStatus());
				return e.getFirstStatus();
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

	public Entry findEntry(String link){
		for(Customer c: customers){
			for(Entry e : c.getEntries()){
				System.out.println(e.getLink());
				if(link.contains(e.getLink())){
					return e;
				}
			}
		}
		System.out.println("entry not found");
		return null;
	}
	
	public void editEntry(Entry entry, String edit) {
		String newContact = LocalDate.now().toString() + " " + edit;
		System.out.println(entry.getLink());
		File txtfile = new File(entry.getLink() + "/Protokoll.txt");
		File tmp = new File(entry.getLink() + "/tmp.txt");
		try {
			BufferedReader file = new BufferedReader(new FileReader(txtfile));
	        String line;
	        PrintWriter writer = new PrintWriter(new FileWriter(tmp));
	        int i=0;
	        while ((line = file.readLine()) != null)
	        {
	        	if(i==4){
	        		writer.println(newContact);
	        	}
	        	else writer.println(line);
	        	i++;
	        }
	        writer.println(newContact);
	        file.close();
	        writer.close();
	        System.out.println(txtfile.delete());
	        System.out.println(tmp.renameTo(new File(entry.getLink() + "/Protokoll.txt")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		entry.setLastContact(newContact);
		for(String[] s : initList){
			if(entry.getLink().contains(s[0])){
				initList.remove(s);
				initList.add(entry.getFirstStatus());
				break;
			}
		}
	}
}
