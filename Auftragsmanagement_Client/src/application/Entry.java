package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class Entry {

    private String date;
    private Button link;
    private String linkString;
    private int state;
	private String customer;
    private String item;
    private String contact;
    private String contactDate;
    private Button pursue;
    private Button detail;

    public Entry(String link, String date, String customer, String item, String contact, int state){
        this.date = date;
        this.linkString = link;
        this.link = new Button();
        this.link.getStyleClass().add("folderbtn");
        this.link.setMaxSize(10, 10);
        this.link.setOnAction(e -> linkClicked());
        this.customer = customer;
        this.item= item;
        checkOld(contact);
        this.pursue = new Button();
        this.pursue.getStyleClass().add("pursuebtn");
        this.pursue.setOnMouseClicked(e -> pursueClicked(e));
        this.detail = new Button();
        this.detail.getStyleClass().add("detailbtn");
        this.detail.setOnAction(e -> detailClicked());
        this.state = state;
    }
    
    public Entry(String link, String date, String customer, String item, String contact, String contactDate, int state){
        this.date = date;
        this.linkString = link;
        this.link = new Button();
        this.link.getStyleClass().add("folderbtn");
        this.link.setMaxSize(10, 10);
        this.link.setOnAction(e -> linkClicked());
        this.customer = customer;
        this.item= item;
        this.contact=contact;
        this.contactDate=contactDate;
        this.pursue = new Button();
        this.pursue.getStyleClass().add("pursuebtn");
        this.pursue.setOnMouseClicked(e -> pursueClicked(e));
        this.detail = new Button();
        this.detail.getStyleClass().add("detailbtn");
        this.detail.setOnAction(e -> detailClicked());
        this.state = state;
    }
    
    public void checkOld(String contact){
    	if(contact.startsWith("old ")){

            this.contactDate = contact.substring(4, 14);
            this.contact= "old " + contact.substring(15);
    	}
    	else{
            this.contactDate = contact.substring(0, 10);
            this.contact= contact.substring(11);
    	}
    }
    
    public void linkClicked(){
    	File dir = new File(ClientGUI.datenbank + linkString);
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
	    		if(answers[answer].contains("Archiv")){
	    			ClientGUI.sender.sendString("archive");
		    		ClientGUI.sender.sendString(this.linkString);
		    		break;
	    		}
	    		else{
		    		if(answer == 1){
		    			try {
		    				Desktop dt = Desktop.getDesktop();
							dt.open(new File(ClientGUI.datenbank + this.getLinkString() + "/01_Anfrage/Gesprächsnotizen.ods"));
						} catch (IOException e) {
							e.printStackTrace();
						}
		    		}
		    		if(answer == 0 && answers[0].contains("Auftrag erhalten")){
		    			ClientGUI.sender.sendString("miniDetail");
		    	    	ClientGUI.sender.sendString(this.linkString);
		    	    	while(true){
		    	    		try {
		    	    			if(AuftragsBox.getReady()){
		    	    				AuftragsBox.setReady(false);
		    	    				String[] edits = AuftragsBox.display();
		    	    				if(edits[0].contentEquals(""))break;
		    	    				ClientGUI.sender.sendString("edit");
		    			    		ClientGUI.sender.sendString(this.linkString);
		    			    		ClientGUI.sender.sendString(answers[answer+3]);
		    			    		ClientGUI.sender.sendString(answers[answer]);
		    			    		
		    	    				ClientGUI.sender.sendString("editDetails");
		    	    				ClientGUI.sender.sendString(this.linkString);
		    	    				for(int i = 1; i<=5; i++){
		    	    					ClientGUI.sender.sendString(edits[i]);
		    	    				}
		    	    				break;
		    	    			}
		    	    			Thread.sleep(10);
		    	    		} catch (Exception e) {
		    	    			System.out.println("interrupted");
		    	    			e.printStackTrace();
		    	    		}
		    	    	}
		    	    	break;
		    		}
		    		if(answer == 0 && answers[0].contains("Auftrag bestätigt ")){
		    			String data = DateBox.display(event.getScreenX(), event.getScreenY(), true, "Los 1");
		    			if (data == "") continue;
		    			answers[0]= answers[0] + data.substring(30);	
		    			ClientGUI.sender.sendString("edit");
			    		ClientGUI.sender.sendString(this.linkString);
			    		ClientGUI.sender.sendString(answers[answer+3]);
			    		ClientGUI.sender.sendString(answers[answer]);
			    		ClientGUI.sender.sendString(data.substring(0, 29));
			    		break;
		    		}
		    		if(answer == 0 && answers[0].contains("Neues Los")){
		    			int neuesLos = Integer.parseInt(answers[0].substring(4, 5))+1;
		    			String data = DateBox.display(event.getScreenX(), event.getScreenY(), false, "Los " + neuesLos);
		    			if (data == "") continue;
		    			ClientGUI.sender.sendString("edit");
			    		ClientGUI.sender.sendString(this.linkString);
			    		ClientGUI.sender.sendString(answers[answer+3]);
			    		ClientGUI.sender.sendString(answers[answer]);
			    		ClientGUI.sender.sendString(data);
			    		break;
			    	}
		    		ClientGUI.sender.sendString("edit");
		    		ClientGUI.sender.sendString(this.linkString);
		    		ClientGUI.sender.sendString(answers[answer+3]);
		    		ClientGUI.sender.sendString(answers[answer]);
		    		break;
	    		}
	    	}
	    	else break;
    	}
    }
    
    private String[] getNextSteps() {
    	String[] steps = new String[6];
    	
    	steps[1] = "Kontakt aufgenommen";
		steps[4] = "#" + Integer.toString(getState());
		
		steps[2] = "Archivieren";
		steps[5] = "#6";
    	
		switch (state) {
        	case 1 : steps[0] = "Angebot erstellt";
        		steps[3]= "#2";
        		break;
        	
        	case 2 : steps[0] = "Auftrag erhalten";
        		steps[3]= "#3";
        		break;
        		
        	case 3:
        		steps[0] = "Auftrag bestätigt ";
        		steps[3]= "#4";
        		break;
        			
        	case 4 : 
        		if(this.getContact().contains("old")) steps[0] = this.getContact().substring(17, 22) + " Versandt + Neues Los";
        		else steps[0] = this.getContact().substring(13, 18) + " Versandt + Neues Los";
        		steps[3] = "#4";
        	
        		if(this.getContact().contains("old")) steps[2] = this.getContact().substring(17, 22) + " Versandt + Projekt abgeschlossen";
        		else steps[2] = this.getContact().substring(13, 18) + " Versandt + Projekt abgeschlossen";
        		steps[5] = "#6";
        		break;
        		
        	default:	steps = null;
        		break;
		}
		
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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

	public String getContactDate() {
		return contactDate;
	}

	public void setContactDate(String contactDate) {
		this.contactDate = contactDate;
	}
}
