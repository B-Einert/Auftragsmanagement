package application;

import java.io.*;
import java.util.ArrayList;

public class DatabaseManager {
	
    private File projectModel=new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/Muster");
    //private ObservableList<Entry> entries;
    private ArrayList<String[]> initList;

    public DatabaseManager()
    {
    	//TODO: load actual entries
    	//entries = FXCollections.observableArrayList();
    	initList = new ArrayList<String[]>();
    	//entries.add(new Entry(LocalDate.now(), "KundeA", "ItemA", "KontaktA"));
        //entries.add(new Entry(LocalDate.now(), "KundeB", "ItemB", "KontaktB"));
        //entries.add(new Entry(LocalDate.now(), "KundeC", "ItemC", "KontaktC"));
        String[] entry1 = {"KundeA", "ItemA", "KontaktA"};
        String[] entry2 = {"KundeB", "ItemB", "KontaktB"};
        String[] entry3 = {"KundeC", "ItemC", "KontaktC"};
        initList.add(entry1);
        initList.add(entry2);
        initList.add(entry3);
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
