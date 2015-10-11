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
    	File file = new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/Testxyz");
    	if (!file.exists()) {
    		if (file.mkdir()) {
    			System.out.println("Directory is created!");
    			//folderCount++;
    			for(File f:projectModel.listFiles())
    			{
    				copyDirectory(f,file);
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
    
    public void copyFolder(File src, File dest) throws IOException {
    	  if (src.isDirectory()) {
    	    //if directory not exists, create it
    	    if (!dest.exists()) {
    	      dest.mkdir();
    	    }
    	    //list all the directory contents
    	    String files[] = src.list();
    	    for (String file : files) {
    	      //construct the src and dest file structure
    	      File srcFile = new File(src, file);
    	      File destFile = new File(dest+"\\"+src.getName(), file);
    	      //recursive copy
    	      copyFolder(srcFile,destFile);
    	    }
    	  } else {
    	    //if file, then copy it
    	    //Use bytes stream to support all file types
    	    InputStream in = new FileInputStream(src);
    	    OutputStream out = new FileOutputStream(dest); 
    	    byte[] buffer = new byte[1024];
    	    int length;
    	    //copy the file content in bytes 
    	    while ((length = in.read(buffer)) > 0){
    	      out.write(buffer, 0, length);
    	    }
    	    in.close();
    	    out.close();
    	    System.out.println("File copied from " + src + " to " + dest);
    	  }
    	}
    
    public void copy(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
        	System.out.println("hiyO");
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    private void copyDirectory(File source, File target) throws IOException {
    	System.out.println("hallo?");
        System.out.println(target.mkdir());
        System.out.println("hey");

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
