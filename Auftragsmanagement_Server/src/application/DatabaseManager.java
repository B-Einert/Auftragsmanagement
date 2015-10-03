package application;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseManager {
	
    private File projectModel=new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/Muster");
    private ArrayList<String[]> initList;
    private ArrayList<Customer> customers;

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
    	customers.add(new Customer("KundeA", entry1));
    	customers.add(new Customer("KundeB", entry2));
    	customers.add(new Customer("KundeC", entry3));
    }
    
    public void buildInitList(){
    	this.initList = new ArrayList<String[]>();
    	for(Customer c:customers)
    	{
    		for(Entry e:c.getEntries()){
    			String[] initEntry = {e.getCustomer(), e.getItem(), e.getContact()};
    		    initList.add(initEntry);
    		}
    	}
    }
    
    public boolean createNewProject(String name) throws IOException
    {
    	File file = new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/Test" + name);
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
    
//    public ObservableList<Entry> getEntries()
//    {
//    	return this.entries;
//    }
    
    public ArrayList<String[]> getInitList()
    {
    	return initList;
    }
}
