package application;

import java.time.LocalDate;

public class Entry {

    private LocalDate date;
    private String customer;
    private String item;
    private String contact;

    public Entry(LocalDate date, String customer, String item, String contact){
        this.date = date;
        this.customer = customer;
        this.item=item;
        this.contact=contact;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

//    public Button getLink() {
//        return link;
//    }
//
//    public void setLink(Button link) {
//        this.link = link;
//    }
    
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
}

