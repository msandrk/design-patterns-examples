package design.patterns.example.editor;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import design.patterns.example.editor.model.TextEditorModel;



public class Main {
	
	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait( new Runnable() {
				
				@Override
				public void run() {
					TextEditorModel model = new TextEditorModel("Uspješno napravljen i iterator!\nIdemo dalje na sljedeći zadatak...");
					TextEditor te = new TextEditor(model);
					te.setLocation(100, 80);
					te.setVisible(true);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
