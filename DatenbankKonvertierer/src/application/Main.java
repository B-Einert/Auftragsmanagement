package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

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

public class Main extends Application {

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

	public static File newdb = new File("X:/Auftragsmanagement(neu)");
	public static File olddb = new File("X:/Auftragsmanagement");
	public static File excel = new File(
			"C:/Users/Pyornez/Documents/Korropol/Auftragsmanagement/dbKonvertTest/000_Auftragsmanagement.xls");

	private Stage window;
	private ListView<String> list;
	public static ObservableList<String> listEntries = FXCollections.observableArrayList();
	private static LinkedList<String> misslist = new LinkedList<String>();
	private static ArrayList<String> customers = new ArrayList<String>();
	private static DateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy");

	public static void main(String[] args) throws IOException {
		// create base Files
		File lv = new File(newdb + "/laufende_Vorgaenge");
		System.out.println(lv.mkdir());
		File av = new File(newdb + "/abgeschlossene_Vorgaenge");
		System.out.println(av.mkdir());
		File kv = new File(newdb + "/Kundenverzeichnis");
		System.out.println(kv.mkdir());

		AlertBox.display("Bitte nicht abschalten! Datenbank wird koppiert!!!!!! Bitte auf nächste Meldung warten");
		searchExcel();
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

	public static void searchExcel() {
		Workbook w;
		try {
			try {
				System.out.println(excel.exists());
				w = Workbook.getWorkbook(excel);
				Sheet sheet = w.getSheet(0);

				// iterate through excell table
				for (int i = 2; i < 10; i++) {
					File link = new File(sheet.getCell(1, i).getContents());
					System.out.println("now: " + i + " - " + link.getName());
					if (!link.exists()) {
						if (link.getName() != "" || link.getName() != null)
							listEntries.add((i + 1) + "_" + sheet.getCell(2, i).getContents() + "_"
									+ sheet.getCell(3, i).getContents() + " fehlt in Datenbank");
					} else {
						File parent = link.getParentFile();

						// Create customer file
						if (!customers.contains(parent.getName())) {
							File customer = new File(newdb + "/Kundenverzeichnis/" + parent.getName());
							if (!customer.exists()) {
								if (customer.mkdir()) {
									for (File f : parent.listFiles()) {
										try {
											Integer.parseInt(f.getName().substring(0, 5));
										} catch (Exception e) {
											File temp = new File(customer.getPath() + "/" + f.getName());
											copyDirectory(f, temp);
										}
									}
								} else {
									System.out.println("Failed to create customer directory in Kundenverzeichnis!");
								}
							}
						}

						File customer = new File(newdb + "/abgeschlossene_Vorgaenge/" + parent.getName());
						if (!customer.exists())
							customer.mkdir();
						File project = new File(customer.getAbsolutePath() + "/" + link.getName());
						if (!project.exists() && project.mkdir()) {
							for (File f : link.listFiles()) {
								// copy project
								File temp = new File(project.getPath() + "/" + f.getName());
								copyDirectory(f, temp);
								//Files.copy(f.toPath(), temp.toPath(), StandardCopyOption.REPLACE_EXISTING);
							}

							// write protokoll
							try {
								System.out.println("protokoll");
								// System.out.println(dateformat.format(((DateCell)
								// sheet.getCell(0, i)).getDate()));
								// System.out.println(sheet.getCell(1,
								// i-10).getContents());
								// System.out.println(sheet.getCell(2,
								// i-10).getContents());
								// System.out.println(sheet.getCell(3,
								// i-10).getContents());
								// System.out.println(sheet.getCell(4,
								// i-10).getContents());
								// System.out.println(sheet.getCell(5,
								// i-10).getContents());
								// System.out.println(sheet.getCell(6,
								// i-10).getContents());
								// System.out.println(sheet.getCell(7,
								// i-10).getContents());
								// System.out.println(sheet.getCell(8,
								// i-10).getContents());
								// System.out.println(sheet.getCell(9,
								// i-10).getContents());
								// System.out.println(sheet.getCell(10,
								// i-10).getContents());
								// System.out.println(sheet.getCell(11,
								// i-10).getContents());
								// System.out.println(sheet.getCell(12,
								// i-10).getContents());
								// System.out.println(sheet.getCell(13,
								// i-10).getContents());
								// System.out.println(sheet.getCell(14,
								// i-10).getContents());
								// System.out.println(sheet.getCell(15,
								// i-10).getContents());
								// System.out.println(sheet.getCell(16,
								// i-10).getContents());
								// System.out.println(sheet.getCell(17,
								// i-10).getContents());
								// System.out.println(sheet.getCell(18,
								// i-10).getContents());
								// System.out.println(sheet.getCell(19,
								// i-10).getContents());

								File txtfile = new File(project + "/Protokoll.txt");
								PrintWriter writer = new PrintWriter(new FileWriter(txtfile));
								writer.println("//erster Kontakt");
								writer.println(convertDate(((DateCell) sheet.getCell(0, i)).getDate()));
								writer.println("");
								writer.println("//letzter Kontakt");
								writer.println(convertDate(((DateCell) sheet.getCell(0, i)).getDate()) + " Anfrage erstellt");
								writer.println("");
								writer.println("//Kunde");
								writer.println(sheet.getCell(2, i).getContents());
								writer.println("");
								writer.println("//Gegenstand");
								writer.println(sheet.getCell(3, i).getContents());
								writer.println("");
								writer.println("//Link");
								writer.println("/abgeschlossene_Vorgaenge/"+customer.getName()+"/"+project.getName());
								writer.println("");
								writer.println("//Ansprechpartner");
								writer.println(sheet.getCell(15, i).getContents());
								writer.println("");
								writer.println("//Telefon");
								writer.println(sheet.getCell(16, i).getContents());
								writer.println("");
								writer.println("//Bearbeiter");
								writer.println(sheet.getCell(17, i).getContents());
								writer.println("");
								writer.println("//Artikelnummer");
								writer.println(sheet.getCell(10, i).getContents());
								writer.println("");
								writer.println("//AB");
								writer.println("");
								writer.println("");
								writer.println("");
								writer.println(convertDate(((DateCell) sheet.getCell(0, i)).getDate()) + " Anfrage erstellt");
								writer.close();
							} catch (Exception e1) {
								System.out.println("Failed to write protocol");
								e1.printStackTrace();
							}
						}

					}
				}
			} catch (BiffException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String convertDate(Date date) {
		return dateformat.format(date);

	}

	public static void copy(File sourceLocation, File targetLocation) throws IOException {
		if (sourceLocation.isDirectory()) {
			copyDirectory(sourceLocation, targetLocation);
		} else {
			copyFile(sourceLocation, targetLocation);
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

	private static void copyFile(File source, File target) throws IOException {
		try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(target)) {
			byte[] buf = new byte[1024];
			int length;
			while ((length = in.read(buf)) > 0) {
				out.write(buf, 0, length);
			}
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

}
