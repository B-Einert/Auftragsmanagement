package application;

import java.time.LocalDate;

public class Entry {

    private String date;
    private String customer;
    private String item;
    private String link;
    private String lastContact;
    

    public Entry(String customer, String item){
        this.date = LocalDate.now().toString();
        this.customer = customer;
        this.item = item;  
        this.lastContact = getDate().toString() + " Anfrage";
    }
    
    public Entry(String date, String link, String customer, String item, String lastContact){
        this.link = link;
        this.date = date;
        this.customer = customer;
        this.item = item;  
        this.lastContact = lastContact;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
    
    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    
    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
    
    public String getLastContact() {
		return lastContact;
	}

	public void setLastContact(String lastContact) {
		this.lastContact = lastContact;
	}
	
	public String[] getStatus(){
		String[] status = {getLink(), getDate(), getCustomer(), getItem(), getLastContact()};
		return status;
	}
}

