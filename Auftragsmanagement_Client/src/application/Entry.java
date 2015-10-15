package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Entry {

    private LocalDate date;
    private Button link;
    private String linkString;
	private SimpleStringProperty customer;
    private SimpleStringProperty item;
    private SimpleStringProperty contact;
    private Button pursue;
    private Button detail;
    private boolean old = true;

    public Entry(String link, LocalDate date, String customer, String item, String contact){
        this.date = date;
        this.linkString = link;
        this.link = new Button("Link");
        this.link.setOnAction(e -> linkClicked());
        this.customer = new SimpleStringProperty(customer);
        this.item= new SimpleStringProperty(item);
        this.contact= new SimpleStringProperty(contact);
        this.pursue = new Button("Weiterführen");
        this.pursue.setOnMouseClicked(e -> pursueClicked(e));
        this.detail = new Button("Detail");
        this.detail.setOnAction(e -> linkClicked());
    }
    
    public void linkClicked(){
    	File dir = new File(linkString);
    	if(Desktop.isDesktopSupported()){
    		try {
				Desktop.getDesktop().open(dir);
			} catch (IOException e) {
				System.out.println("could not open directory");
				e.printStackTrace();
			}
    	}
    }
    
    public void pursueClicked(MouseEvent event) {
    	String answer = ChoiceBox.display(event.getScreenX(), event.getScreenY(), "Angebot erstellen", "Kontakt aufnehmen");
    	if(answer != ""){
    		ClientGUI.sender.sendString("edit");
    		ClientGUI.sender.sendString(this.linkString);
    		ClientGUI.sender.sendString(answer);
    	}
    }
    
    public void detailClicked(){
    	
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Button getLink() {
        return link;
    }

    public void setLink(Button link) {
        this.link = link;
    }
    
    public String getLinkString() {
		return linkString;
	}

	public void setLinkString(String linkString) {
		this.linkString = linkString;
	}
    
    public String getCustomer() {
        return customer.get();
    }

    public void setCustomer(String customer) {
        this.customer.set(customer);;
    }
    
    public String getItem() {
        return item.get();
    }

    public void setItem(String item) {
        this.item.set(linkString);
    }
    
    public String getContact() {
        return contact.get();
    }

    public void setContact(String contact) {
        this.contact.set(contact);
    }
    
    public Button getPursue() {
        return pursue;
    }

    public void setPursue(Button pursue) {
        this.pursue = pursue;
    }
    
    public Button getDetail() {
        return detail;
    }

    public void setDetail(Button detail) {
        this.detail = detail;
    }
    
    public boolean tooOld(){
    	return old;
    }
}
