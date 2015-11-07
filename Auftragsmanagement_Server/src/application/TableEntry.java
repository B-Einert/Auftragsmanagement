package application;

import java.time.LocalTime;

public class TableEntry {

	 private LocalTime time;
	 private String entry;
	 
	 public TableEntry(String details){
		 this.time=LocalTime.now();
		 this.entry = details;
	 }

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String details) {
		this.entry = details;
	}
}
