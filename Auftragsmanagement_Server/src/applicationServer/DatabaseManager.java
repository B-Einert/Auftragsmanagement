package applicationServer;

import java.io.*;

public class DatabaseManager {
	
	private int folderCount=0;
    private File projectModel=new File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/Datenbank/laufende_Vorgaenge/Muster");


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
}
