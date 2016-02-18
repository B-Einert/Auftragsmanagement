package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class AutoCompleteComboBoxListener implements EventHandler<KeyEvent> {

    private ComboBox<String> comboBox;
    private ObservableList<String> data;
    private boolean moveCaretToPos = false;
    private int caretPos;

    public AutoCompleteComboBoxListener(final ComboBox<String> comboBox, final ComboBox<String> partnerBox, final TextField phone) {
        this.comboBox = comboBox;
        data = comboBox.getItems();

        this.comboBox.setEditable(true);
        this.comboBox.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                comboBox.hide();
            }
        });
        this.comboBox.setOnKeyReleased(AutoCompleteComboBoxListener.this);
        
        this.comboBox.setOnAction(e ->{
        	if(data.contains(comboBox.getValue())){
        		ClientGUI.sender.sendString("custInf");
        		ClientGUI.sender.sendString(comboBox.getValue());  
        		while(true){
            		try {
            			if(CreateBox.getReady()){
            				CreateBox.setReady(false);
            				break;
            			}
            			//System.out.println("not ready yet!");
            			Thread.sleep(10);
            		} catch (Exception ex) {
            			System.out.println("interrupted");		
            			ex.printStackTrace();
            		}
            	}
        	}
        });
    }

    @Override
    public void handle(KeyEvent event) {

        if(event.getCode() == KeyCode.UP) {
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.DOWN) {
            if(!comboBox.isShowing()) {
                comboBox.show();
            }
            caretPos = -1;
            moveCaret(comboBox.getEditor().getText().length());
            return;
        } else if(event.getCode() == KeyCode.BACK_SPACE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        } else if(event.getCode() == KeyCode.DELETE) {
            moveCaretToPos = true;
            caretPos = comboBox.getEditor().getCaretPosition();
        }

        if (event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT
                || event.isControlDown() || event.getCode() == KeyCode.HOME
                || event.getCode() == KeyCode.END || event.getCode() == KeyCode.TAB) {
            return;
        }

        ObservableList<String> list = FXCollections.observableArrayList();
        int border = 0;
        for (int i=0; i<data.size(); i++) {
        	if(data.get(i).toString().toLowerCase().startsWith(
                comboBox.getEditor().getText().toLowerCase())) {
                list.add(border, data.get(i));
                border ++;
            }
        	else if(LevenshteinDistance.computeLevenshteinDistance(data.get(i), comboBox.getEditor().getText())<3){
        		list.add(data.get(i));
        	}
        }
        String t = comboBox.getEditor().getText();

        comboBox.setItems(list);
        comboBox.getEditor().setText(t);
        if(!moveCaretToPos) {
            caretPos = -1;
        }
        moveCaret(t.length());
        if(!list.isEmpty()) {
            comboBox.show();
        }
    }

    private void moveCaret(int textLength) {
        if(caretPos == -1) {
            comboBox.getEditor().positionCaret(textLength);
        } else {
            comboBox.getEditor().positionCaret(caretPos);
        }
        comboBox.setValue(comboBox.getEditor().getText());
        moveCaretToPos = false;
    }

}
