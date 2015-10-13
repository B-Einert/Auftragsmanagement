package application;

import java.time.LocalDate;

import javafx.scene.control.Button;

public class Entry {

    private LocalDate date;
    private Button link;
    private String customer;
    private String item;
    private String contact;
    private Button pursue;
    private Button detail;

    public Entry(String link, LocalDate date, String customer, String item, String contact){
        this.date = date;
        this.link = new Button("Link");
        this.customer = customer;
        this.item=item;
        this.contact=contact;
        this.pursue = new Button("pursue");
        this.detail = new Button("detail");
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
}
