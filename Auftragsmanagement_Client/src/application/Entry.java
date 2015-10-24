package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Entry {

    private LocalDate date;
    private Button link;
    private String linkString;
    private int state;
	private String customer;
    private String item;
    private String contact;
    private LocalDate contactDate;
    private Button pursue;
    private Button detail;

    public Entry(String link, LocalDate date, String customer, String item, String contact, int state){
        this.date = date;
        this.linkString = link;
        this.link = new Button("Link");
        this.link.setOnAction(e -> linkClicked());
        this.customer = customer;
        this.item= item;
        checkOld(contact);
        this.pursue = new Button("Weiterführen");
        this.pursue.setOnMouseClicked(e -> pursueClicked(e));
        this.detail = new Button("Detail");
        this.detail.setOnAction(e -> detailClicked());
        this.state = state;
    }
    
    public void checkOld(String contact){
    	if(contact.startsWith("old ")){

            this.contactDate = LocalDate.parse(contact.substring(4, 14));
            this.contact= "old " + contact.substring(15);
    	}
    	else{
            this.contactDate = LocalDate.parse(contact.substring(0, 10));
            this.contact= contact.substring(11);
    	}
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
    	while(true){
	    	String[] answers = getNextSteps();
	    	int answer = ChoiceBox.display(event.getScreenX(), event.getScreenY(), answers[0], answers[1], answers[2]);
	    	if(answer != -1){
	    		if(answer == 2){
	    			ClientGUI.sender.sendString("archive");
		    		ClientGUI.sender.sendString(this.linkString);
		    		break;
	    		}
	    		else{
		    		if(answer == 1){
		    			try {
		    				Desktop dt = Desktop.getDesktop();
							dt.open(new File(this.getLinkString() + "/01_Anfrage/Gesprächsnotizen.ods"));
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		}
		    		if(answer == 0 && answers[0].contains("bestätigen")){
		    			String date = DateBox.display(event.getScreenX(), event.getScreenY());
		    			System.out.println(date);
		    			if (date == "") continue;
		    			answers[3]= answers[3] + date;	
		    		}
		    		ClientGUI.sender.sendString("edit");
		    		ClientGUI.sender.sendString(this.linkString);
		    		ClientGUI.sender.sendString(answers[answer+6]);
		    		ClientGUI.sender.sendString(answers[answer+3]);
		    		break;
	    		}
	    	}
	    	else break;
    	}
    }
    
    private String[] getNextSteps() {
    	System.out.println(getState());
		String[] steps = new String[9];
		switch (state) {
        	case 1 : steps[0] = "Angebot erstellen";
        		steps[3]= "Angebot";
        		steps[6]= "#2";
        		break;
        	
        	case 2 : steps[0] = "Auftrag erstellen";
        		steps[3]= "Auftrag";
        		steps[6]= "#3";
        		break;
        		
        	case 3:
        		steps[0] = "Auftrag bestätigen";
        		steps[3]= "versenden bis ";
        		steps[6]= "#4";
        		break;
        			
        	case 4 : steps[0] = "Versenden";
        		steps[3]= "Versandt";
        		steps[6] = "#5";
        		break;
        			
        	case 5 : steps[0] = "Archivieren";
        		steps[3] = "Archiviert";
        		steps[6] = "#1";
        		break;
        		
        	default:	steps = null;
        		break;
		}
		steps[1] = "Kontakt aufnehmen";
		steps[4] = "Kontakt";
		steps[7] = "#" + Integer.toString(getState());
		
		steps[2] = "Archivieren";
		steps[5] = "Archiviert";
		steps[8] = "#1";
		return steps;
	}

	public void detailClicked(){
    	ClientGUI.sender.sendString("detail");
    	ClientGUI.sender.sendString(this.linkString);
    	while(true){
    		try {
    			if(DetailBox.getReady()){
    				DetailBox.setReady(false);
    				DetailBox.display();
    				break;
    			}
    			//System.out.println("not ready yet!");
    			Thread.sleep(10);
    		} catch (Exception e) {
    			System.out.println("interrupted");		
    			e.printStackTrace();
    		}
    	}
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
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer= customer;
    }
    
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
    
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact=contact;
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

	public LocalDate getContactDate() {
		return contactDate;
	}

	public void setContactDate(LocalDate contactDate) {
		this.contactDate = contactDate;
	}
}
