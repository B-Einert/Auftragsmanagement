package application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFormat {
	
	private DateTimeFormatter date = DateTimeFormatter.ofPattern("dd.MM.yyyy");
	
	public String toString(LocalDate date){
		return this.date.format(date);
	}
	
	public LocalDate toDate(String string){
		return LocalDate.parse(string.substring(6) + "-" + string.substring(3, 5) + "-" + string.substring(0, 2));
	}

}
