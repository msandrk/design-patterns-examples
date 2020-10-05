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
					TextEditorModel model = new TextEditorModel("DEMO\n\nFor demonstration purposes\nMultiline text. Need a bit more text in this line\n");
					TextEditor te = new TextEditor(model);

					te.setVisible(true);
					model.insert("Some more text\nIn multiple lines");
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
