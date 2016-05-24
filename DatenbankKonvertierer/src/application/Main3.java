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
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import javax.swing.JFrame;
import javax.swing.ProgressMonitorInputStream;

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

public class Main3 extends Application {

	// public static File newdb = new
	// File("C:/Users/Pyornez/Documents/Korropol/Auftragsmanagement/Datenbank");
	public static File newdb = new File("X:/Auftragsmanagement(neu)");
	// public static File olddb = new
	// File("C:/Users/Pyornez/Documents/Korropol/Auftragsmanagement/Datenbank/fuck");
	public static File olddb = new File("X:/Auftragsmanagement");

	private Stage window;
	private ListView<String> list;
	private static JFrame frame;
	public static ObservableList<String> listEntries = FXCollections.observableArrayList();
	private static LinkedList<String> misslist = new LinkedList<String>();
	private static ArrayList<String> customers = new ArrayList<String>();
	private static DateFormat dateformat = new DateFormat();

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
		searchDirect();
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

	public static void searchDirect() {
		for (File oldEntry : olddb.listFiles()) {
			System.out.println(oldEntry.getPath());
			File customer = new File(newdb.getAbsolutePath() + "/Kundenverzeichnis/" + oldEntry.getName());
			if (oldEntry.isDirectory() && (!oldEntry.getName().startsWith("1"))) {
				customer.mkdir();
				for (File link : oldEntry.listFiles()) {
					try {
						Integer.parseInt(link.getName().substring(0, 5));
					} catch (Exception e) {
						File temp = new File(customer.getAbsolutePath() + "/" + link.getName());
						try {
							copy(link, temp);
						} catch (IOException e1) {
							// e1.printStackTrace();
						}
					}
				}
			}

		}
		for (

		File oldEntry : olddb.listFiles())

		{
			if (!oldEntry.isDirectory())
				continue;
			System.out.println(oldEntry.getPath());
			File customer = new File(newdb.getAbsolutePath() + "/abgeschlossene_Vorgaenge/" + oldEntry.getName());
			if ((!customer.exists())) {
				customer.mkdir();
			}
			for (File link : oldEntry.listFiles()) {
				try {
					Integer.parseInt(link.getName().substring(0, 5));
					File temp = new File(customer.getAbsolutePath() + "/" + link.getName());
					// File protocol = new File(temp + "/Protokoll.txt");
					// if (protocol.exists()) {
					// try {
					// BufferedReader file = new BufferedReader(new
					// FileReader(protocol));
					// if (file.readLine().contains("erster")) {
					// file.close();
					// System.out.println("Projektverzeichniss bereits
					// vorhanden");
					// continue;
					// }
					// } catch (IOException e) {
					// }
					// }
					try {
						copy(link, temp);
					} catch (IOException e1) {
					}
					// protocol
					File protocol = new File(temp + "/Protokoll.txt");
					if (protocol.exists()) {
						try {
							BufferedReader file = new BufferedReader(new FileReader(protocol));
							if (file.readLine().contains("erster")) {
								file.close();
								System.out.println("Projektverzeichniss bereits vorhanden");
								continue;
							}
						} catch (IOException e) {
						}
					}
					try {
						System.out.println("protokoll");
						String date = convertDate(dateformat.toDate(temp.getName().substring(0, 6)));
						File txtfile = new File(temp + "/Protokoll.txt");
						PrintWriter writer = new PrintWriter(new FileWriter(txtfile));
						writer.println("//erster Kontakt");
						writer.println(date);
						writer.println("");
						writer.println("//letzter Kontakt");
						writer.println(date + " Anfrage erstellt");
						writer.println("");
						writer.println("//Kunde");
						writer.println(temp.getParentFile().getName());
						writer.println("");
						writer.println("//Gegenstand");
						writer.println(temp.getName().substring(7));
						writer.println("");
						writer.println("//Link");
						writer.println("/abgeschlossene_Vorgaenge/" + customer.getName() + "/" + temp.getName());
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
						File txtfile = new File(temp + "/Protokoll.txt");
						if (txtfile.exists())
							txtfile.delete();
						e1.printStackTrace();
						System.out.println("Projektverzeichniss erstellt");
					}
				} catch (Exception e) {
				}
			}
		}

	}

	public static String convertDate(LocalDate localDate) {
		return dateformat.toString(localDate);

	}

	public static void copy(File sourceLocation, File targetLocation) throws IOException {
		if (sourceLocation.isDirectory()) {
			copyDirectory(sourceLocation, targetLocation);
		} else {
			try {
				copyFile(sourceLocation, targetLocation);
			} catch (Exception e) {
				listEntries.add("couldnt copy " + sourceLocation.getPath());
				System.out.println(e);
				e.printStackTrace();
			}
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
		if (target.exists()) {
			if (source.length() > target.length()) {
				target.delete();
			} else {
				System.out.println(target.getPath() + "file already exists");
				return;
			}
		}
		try (InputStream in = new BufferedInputStream(
				new ProgressMonitorInputStream(frame, "Reading " + source.getAbsolutePath(), new FileInputStream(source)));
				OutputStream out = new FileOutputStream(target)) {
			// try (InputStream in = new FileInputStream(source); OutputStream
			// out = new FileOutputStream(target)) {
			System.out.println("loading file " + target.getPath());
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

	public static String delSpaces(String path) {
		String rightPath = path;
		while (true) {
			if (rightPath.endsWith(" "))
				rightPath = rightPath.substring(0, rightPath.length() - 1);
			else
				break;
		}
		return rightPath;
	}

	public static long folderSize(File directory) {
		long length = 0;
		for (File file : directory.listFiles()) {
			if (file.isFile())
				length += file.length();
			else
				length += folderSize(file);
		}
		return length;
	}

	private static void deleteFile(File project) {
		if (!project.delete())
			;
		for (File f : project.listFiles()) {
			deleteFile(f);
		}
		System.out.println(project.getPath() + "wurde gelöscht");
	}
}
