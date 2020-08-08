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
					TextEditorModel model = new TextEditorModel("Marko\nradi\nnesto");
					TextEditor te = new TextEditor(model);
					te.setLocation(20, 20);
					te.setVisible(true);
//					te.addKeyListener(new KeyListener() {
//						
//						@Override
//						public void keyTyped(KeyEvent arg0) {
//							// TODO Auto-generated method stub
//							
//						}
//						
//						@Override
//						public void keyReleased(KeyEvent arg0) {
//							if(arg0.getKeyCode() == KeyEvent.VK_ENTER) {
//								te.setVisible(false);
//								System.exit(0);
//							}
//							
//						}
//						
//						@Override
//						public void keyPressed(KeyEvent arg0) {
//							// TODO Auto-generated method stub
//							
//						}
//					
//					});
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
