package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ArchiveEntry{
	
	private String name;
	private String date;
	private String anum;
	private String abn;
	private String link;
	private Button linkBtn;
	private Button duplicate;
	private Button details;
	private boolean root;
	
	public ArchiveEntry(String name){
		this.name=name;
		root=true;
	}

	public ArchiveEntry(String date, String name, String anum, String abn, String link) {
		this.name=name;
		this.date=date;
		this.anum=anum;
		this.abn=abn;
		this.link=link;
		this.details = new Button();
        details.getStyleClass().add("detailbtn");
        this.details.setOnAction(e -> detailClicked());
        this.duplicate = new Button();
        duplicate.getStyleClass().add("duplicatebtn");
        this.duplicate.setOnAction(e -> duplicateClicked());
        this.linkBtn = new Button();
        this.linkBtn.setMaxSize(10, 10);
        this.linkBtn.setOnAction(e -> linkBtnClicked());
        linkBtn.getStyleClass().add("folderbtn");
	}
	
	 public void linkBtnClicked(){
	    	File dir = new File(link);
	    	if(Desktop.isDesktopSupported()){
	    		try {
					Desktop.getDesktop().open(dir);
				} catch (IOException e) {
					System.out.println("could not open directory");
					e.printStackTrace();
				}
	    	}
	    }

	public void detailClicked(){
    	ClientGUI.sender.sendString("detail");
    	ClientGUI.sender.sendString(this.link);
    	while(true){
    		try {
    			if(DetailBox.getReady()){
    				DetailBox.setReady(false);
    				DetailBox.display();
    				break;
    			}
    			Thread.sleep(10);
    		} catch (Exception e) {
    			System.out.println("interrupted");
    			e.printStackTrace();
    		}
    	}
    }
	
	public void duplicateClicked(){
		ClientGUI.sender.sendString("miniDetail");
    	ClientGUI.sender.sendString(this.link);
    	while(true){
    		try {
    			if(AuftragsBox.getReady()){
    				AuftragsBox.setReady(false);
    				String[] edits = AuftragsBox.display();
    				if(edits[0].contentEquals(""))break;
    				ClientGUI.sender.sendString("duplicate");
    				ClientGUI.sender.sendString(this.getLink());
    				for(int i = 0; i<=5; i++){
    					ClientGUI.sender.sendString(edits[i]);
    				}
    				ClientGUI.back();
    				break;
    			}
    			Thread.sleep(10);
    		} catch (Exception e) {
    			System.out.println("interrupted");
    			e.printStackTrace();
    		}
    	}
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

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public boolean isRoot() {
		return root;
	}

	public Button getDetails() {
		return details;
	}

	public void setDetails(Button details) {
		this.details = details;
	}

	public Button getDuplicate() {
		return duplicate;
	}

	public void setDuplicate(Button duplicate) {
		this.duplicate = duplicate;
	}

	public Button getLinkBtn() {
		return linkBtn;
	}

	public void setLinkBtn(Button linkBtn) {
		this.linkBtn = linkBtn;
	}

}
