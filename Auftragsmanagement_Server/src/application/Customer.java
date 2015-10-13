package application;

import java.util.LinkedList;

public class Customer {
	
	private String name;
	private LinkedList<Entry> entries;
	
	public Customer(Entry entry){
		this.name=entry.getCustomer();
		entries=new LinkedList<Entry>();
		entries.add(entry);
	}
	public Customer(String name){
		this.name=name;
		entries=new LinkedList<Entry>();
	}
	
	public String getName(){
		return this.name;
	}
	
	public void addEntry(Entry entry){
		entries.add(entry);
	}
	
	public LinkedList<Entry> getEntries(){
		return this.entries;
	}
	

}
