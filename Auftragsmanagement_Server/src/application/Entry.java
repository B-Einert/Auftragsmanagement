package application;

import java.time.LocalDate;

public class Entry {

    private LocalDate date;
    private String customer;
    private String item;
    private String contact;
    private String phone;
    private String agent;
    private String link;

    public Entry(LocalDate date, String customer, String item, String contact, String phone, String agent){
        this.date = date;
        this.customer = customer;
        this.item = item;
        this.contact = contact;
        this.setPhone(phone);
        this.setAgent(agent);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
    
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}
}

