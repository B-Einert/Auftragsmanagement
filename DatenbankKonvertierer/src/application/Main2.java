package application;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.ProgressMonitorInputStream;

import org.apache.commons.io.FileUtils;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import javafx.scene.Scene;
import javafx.scene.control.ListView;

public class Main2 extends Application {

	// public static File newdb = new
	// File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/dbKonvertTest/newdb");
	// public static File olddb = new
	// File("D:/BJOERN/Documents/Korropol/Auftragsmanagement/dbKonvertTest/olddb");
	// public static File excel = new File(
	// "D:/BJOERN/Documents/Korropol/Auftragsmanagement/dbKonvertTest/Auftragsmanagement.xls");
	// public static File newdb = new
	// File("C:/Users/Pyornez/Documents/Korropol/Auftragsmanagement/dbKonvertTest/newdb");
	// public static File olddb = new
	// File("C:/Users/Pyornez/Documents/Korropol/Auftragsmanagement/dbKonvertTest/olddb");
	// public static File excel = new File(
	// "C:/Users/Pyornez/Documents/Korropol/Auftragsmanagement/dbKonvertTest/Auftragsmanagement.xls");

	public static File newdb = new File("C:/Users/Pyornez/Documents/Korropol/Auftragsmanagement/Datenbank");
	//public static File newdb = new File("X:/Auftragsmanagement(neu)");
	public static File olddb = new File("X:/Auftragsmanagement");
	//public static File olddb = new File("X:/Auftragsmanagement");
	//public static File excel = new File(
	//		"C:/Users/Pyornez/Documents/Korropol/Auftragsmanagement/dbKonvertTest/000_Auftragsmanagement.xls");

	private Stage window;
	private ListView<String> list;
	private static JFrame frame;
	public static ObservableList<String> listEntries = FXCollections.observableArrayList();
	private static LinkedList<String> misslist = new LinkedList<String>();
	private static ArrayList<String> customers = new ArrayList<String>();
	private static DateFormat dateformat = new DateFormat();
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd");

	public static void main(String[] args) throws IOException {
		// create base Files
		File lv = new File(newdb + "/laufende_Vorgaenge");
		System.out.println(lv.mkdir());
		File av = new File(newdb + "/abgeschlossene_Vorgaenge");
		System.out.println(av.mkdir());
		File kv = new File(newdb + "/Kundenverzeichnis");
		System.out.println(kv.mkdir());
		
		frame = new JFrame();
		frame.setVisible(false);
		frame.setSize(20, 20);
		frame.toBack();

		AlertBox.display("Bitte nicht abschalten! Datenbank wird kopiert!!!!!! Bitte auf nächste Meldung warten");
		gogogo();
		AlertBox.display("Fertig mit koppieren. Andere Meldungen nicht beachten!!!");
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setWidth(937);
		window.setTitle("Datenbankkonvertierung");

		list = new ListView<String>();
		list.setItems(listEntries);
		list.setEditable(true);
		list.getItems().addListener((ListChangeListener<String>) (c -> {
			c.next();

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					final int size = list.getItems().size();
					if (size > 0) {
						try {
							list.scrollTo(size - 1);
						} catch (IllegalStateException oi) {
							System.out.println(oi);
						}
					}
				}
			});

		}));

		Scene scene = new Scene(list);
		window.setScene(scene);
		window.show();
	}

	public static void gogogo()  {
		
		for(File entry: olddb.listFiles()){
			System.out.println("Kunde: " + entry);
			if(entry.isDirectory()){
				File customer = new File(newdb + "/Kundenverzeichnis/" + entry.getName());
				if ((!customer.exists())&&customer.isDirectory()&&(!customer.getName().startsWith("1"))) {
					if (customer.mkdir()) {
						for (File f : entry.listFiles()) {
							try {
								Integer.parseInt(f.getName().substring(0, 5));
							} catch (Exception e) {
								File temp = new File(customer.getPath() + "/" + f.getName());
								try {
									if(temp.isDirectory())
										copyDirectory(f, temp);
									System.out.println("Kundenverzeichnis kopiert");
								} catch (IOException e1) {
									}
							}
						}
					} else {
						System.out.println("Failed to create customer directory in Kundenverzeichnis!");
					}

				}
				else System.out.println("Verzeichnis bereits vorhanden");
				customer = new File(newdb + "/abgeschlossene_Vorgaenge/" + customer.getName());
				if (!customer.exists())
					customer.mkdir();
				for(File link : entry.listFiles()){
					try {
						Integer.parseInt(link.getName().substring(0, 5));
						System.out.println("Projekt: " + link.getName());
						File project = new File(customer.getAbsolutePath() + "/" + link.getName());
						if(project.exists()){
							File protocol = new File(project + "/Protokoll.txt");
							if(protocol.exists()){
								try {
									BufferedReader file = new BufferedReader(new FileReader(protocol));
									if(file.readLine().contains("erster")){
										file.close();
										System.out.println("Projektverzeichniss bereits vorhanden");
										continue;
									}
								}catch (IOException e) {
									deleteFile(project);
								}
							}
							System.out.println("Projektverzeichniss wird ersetzt");
							deleteFile(project);
							if(!project.mkdir()){
								listEntries.add("couldnt create project" + project.getPath());
								
							}
						}
						for (File f : link.listFiles()) {
							// copy project
							File temp = new File(project.getPath() + "/" + f.getName());
						
							copyDirectory(f, temp);
							//Files.copy(f.toPath(), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
							
							// write protokoll
							try {
								System.out.println("protokoll");
								String date=convertDate(dateformat.toDate(project.getName().substring(0, 5)));
								File txtfile = new File(project + "/Protokoll.txt");
								PrintWriter writer = new PrintWriter(new FileWriter(txtfile));
								writer.println("//erster Kontakt");
								writer.println(date);
								writer.println("");
								writer.println("//letzter Kontakt");
								writer.println(date + " Anfrage erstellt");
								writer.println("");
								writer.println("//Kunde");
								writer.println(project.getParentFile().getName());
								writer.println("");
								writer.println("//Gegenstand");
								writer.println(project.getName().substring(11));
								writer.println("");
								writer.println("//Link");
								writer.println("/abgeschlossene_Vorgaenge/"+customer.getName()+"/"+project.getName());
								writer.println("");
								writer.println("//Ansprechpartner");
								writer.println("");
								writer.println("");
								writer.println("//Telefon");
								writer.println("");
								writer.println("");
								writer.println("//Bearbeiter");
								writer.println("");
								writer.println("");
								writer.println("//Artikelnummer");
								writer.println("");
								writer.println("");
								writer.println("//AB");
								writer.println("");
								writer.println("");
								writer.println("");
								writer.println(date + " Anfrage erstellt");
								writer.close();
							} catch (Exception e1) {
							System.out.println("Failed to write protocol");
							File txtfile = new File(project + "/Protokoll.txt");
							if(txtfile.exists())txtfile.delete();
							e1.printStackTrace();
							System.out.println("Projektverzeichniss erstellt");
						}
					}
				} catch (Exception e) 
					{
						continue;
					}
				}
			}
		}
	}

	private static void deleteFile(File project) {
		if(!project.delete());
		for(File f:project.listFiles()){
			deleteFile(f);
		}
		
	}

	public static String convertDate(LocalDate localDate) {
		return dateformat.toString(localDate);

	}

	public static void copy(File sourceLocation, File targetLocation) throws IOException {
		if (sourceLocation.isDirectory()) {
			copyDirectory(sourceLocation, targetLocation);
		} else {
			try{
				copyFile(sourceLocation, targetLocation);
			}
			catch(Exception e){
				listEntries.add("couldnt copy " + sourceLocation.getPath());
				System.out.println(e);
				e.printStackTrace();
			};
			
		}
	}

	private static void copyDirectory(File source, File target) throws IOException {
		if (!target.exists()) {
			target.mkdir();
		}
		if (source.list() != null) {
			for (String f : source.list()) {
				copy(new File(source, f), new File(target, f));
			}
		}
	}

	private static void copyFile(File source, File target) throws Exception {
//		try (InputStream in = new BufferedInputStream(
//                new ProgressMonitorInputStream(
//                        frame,
//                        "Reading " + source.getName(),
//                        new FileInputStream(source)
//               )
//            );
//				OutputStream out = new FileOutputStream(target)) {
		try (InputStream in = new FileInputStream(source);
				OutputStream out = new FileOutputStream(target)) {
			frame.toBack();
			byte[] buf = new byte[1024];
			int length;
			while ((length = in.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
			in.close();
			out.close();
		}
	}

	public boolean isProject(String name) {
		try {
			Integer.parseInt(name.substring(0, 5));
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public static String delSpaces(String path){
		String rightPath = path;
		while(true){
			if(rightPath.endsWith(" "))rightPath=rightPath.substring(0, rightPath.length()-1);
			else break;
		}
		return rightPath;
	}
}
