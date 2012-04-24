import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu implements ActionListener {
	private JMenuItem save;
	private MainPanel main;
	
	public JMenuBar makeMenu(MainPanel tab){
		JMenuBar menuBar = new JMenuBar();
		main = tab;
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		save = new JMenuItem("Save");
		save.addActionListener(this);
		file.add(save);
		
		return menuBar;
	}

	@Override
	public void actionPerformed(ActionEvent evt){
		
		if(evt.getSource() == save){
			String st = main.getInstructions().getText();
			String[] s = st.split("/n");
			
			JFileChooser chooser = new JFileChooser();
			int choice = chooser.showSaveDialog(null);
			if(choice == JFileChooser.APPROVE_OPTION) { 
				saveNew(chooser.getSelectedFile(), s);
			}
		}
	}
	
	private boolean saveNew(File f, String[] s) {
		boolean saveSuccessful = true;
		try {
			FileWriter fw = new FileWriter(f);
			BufferedWriter writer = new BufferedWriter(fw);
			writer.newLine();
			for(int i = 0; i < s.length; i ++) {
					writer.write(s[i]);
					writer.newLine();
				}
			writer.close();
		} catch (IOException e) { saveSuccessful = false; }
		
		return saveSuccessful;
	}
	
	
}