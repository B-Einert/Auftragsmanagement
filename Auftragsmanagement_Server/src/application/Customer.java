package application;

import java.util.LinkedList;

public class Customer {
	
	private String name;
	private LinkedList<Entry> entries;
	
	public Customer(String name, Entry entry){
		this.name=name;
		entries=new LinkedList<Entry>();
		entries.add(entry);
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
