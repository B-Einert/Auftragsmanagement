package application;

import javafx.scene.control.Button;

public class ArchiveEntry{
	
	private String name;
	private String date;
	private String anum;
	private String abn;
	private String link;
	private Button pursue;
	
	public ArchiveEntry(String name){
		this.name=name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		if(date==null)return"";
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getAnum() {
		if(anum==null)return"";
		return anum;
	}

	public void setAnum(String anum) {
		this.anum = anum;
	}

	public String getAbn() {
		if(abn==null)return"";
		return abn;
	}

	public void setAbn(String abn) {
		this.abn = abn;
	}

	public Button getPursue() {
		return pursue;
	}

	public void setPursue(Button pursue) {
		this.pursue = pursue;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
