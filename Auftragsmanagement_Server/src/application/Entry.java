package application;

public class Entry {

    private String date;
    private String customer;
    private String item;
    private String link;
    private String lastContact;
    private String state;
    

    public Entry(String date, String customer, String item){
        this.date = date;
        this.customer = customer;
        this.item = item;  
        this.lastContact = getDate() + " Anfrage erstellt";
        this.state = "1";
    }
    
    public Entry(String date, String link, String customer, String item, String lastContact, String state){
        this.link = link;
        this.date = date;
        this.customer = customer;
        this.item = item;  
        this.lastContact = lastContact;
        this.state = state;
        System.out.println(date);
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String[] getFirstStatus(){
		String[] status = {getLink(), getDate(), getCustomer(), getItem(), getLastContact(), getState()};
		return status;
	}
}

