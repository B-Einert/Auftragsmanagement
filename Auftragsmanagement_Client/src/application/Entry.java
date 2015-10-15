package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Entry {

    private LocalDate date;
    private Button link;
    private String linkString;
    private int state;
	private SimpleStringProperty customer;
    private SimpleStringProperty item;
    private SimpleStringProperty contact;
    private Button pursue;
    private Button detail;
    private boolean old = false;

    public Entry(String link, LocalDate date, String customer, String item, String contact, int state){
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
        this.state = state;
        System.out.println(state);
        tryOld();
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
    	String[] answers = getNextSteps();
    	int answer = ChoiceBox.display(event.getScreenX(), event.getScreenY(), answers[0], answers[2]);
    	if(answer != -1){
    		ClientGUI.sender.sendString("edit");
    		ClientGUI.sender.sendString(this.linkString);
    		ClientGUI.sender.sendString(answers[answer+6]);
    		ClientGUI.sender.sendString(answers[answer]);
    	}
    }
    
    private String[] getNextSteps() {
		String[] steps = new String[9];
		switch (state) {
        	case 1 : steps[0] = "Angebot";
        		steps[3]= "Angebot erstellen";
        		steps[6]= "#2";
        			break;
        	case 2 : steps[0] = "Auftrag";
        		steps[3]= "Auftrag erstellen";
        		steps[6]= "#3";
        			break;
        	case 3 : steps[0] = "Versand";
        		steps[3]= "Versenden";
        		steps[6] = "#4";
        			break;
        	case 4 : steps[0] = "Archiviert";
        		steps[3] = "Archivieren";
        		steps[6] = "#5";
        	default:	steps = null;
		}
		steps[2] = "Kontakt";
		steps[5] = "Kontakt aufnehmen";
		steps[8] = "#" + Integer.toString(getState());
		return steps;
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
    
    public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
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
    
    public void tryOld(){
    	LocalDate today= LocalDate.now();
    	LocalDate reference;
    	long days;
    	if (getState() ==1){
    		reference = this.getDate();
    		days = ChronoUnit.DAYS.between(reference, today);
    		if (days>7){
    			this.old=true;
    		}
    	}
    	else{
    		reference = LocalDate.parse(this.getContact().substring(0, 10));
    		days = ChronoUnit.DAYS.between(reference, today);
    		if (days>14){
    			this.old=true;
    		}
    	}
    	System.out.println(days);
    }
}
