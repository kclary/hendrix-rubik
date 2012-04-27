package cube;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;
import java.util.Stack;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu implements ActionListener {
	private JMenuItem save;
	private JMenuItem load;
	private MainPanel main;
	
	public JMenuBar makeMenu(MainPanel tab){
		JMenuBar menuBar = new JMenuBar();
		main = tab;
		
		JMenu file = new JMenu("File");
		menuBar.add(file);
		save = new JMenuItem("Save");
		save.addActionListener(this);
		file.add(save);
		load = new JMenuItem("Load");
		load.addActionListener(this);
		file.add(load);
		
		return menuBar;
	}

	@Override
	public void actionPerformed(ActionEvent evt){
		
		if(evt.getSource() == save){
			String[] s = main.getHistory();
			
			JFileChooser chooser = new JFileChooser();
			int choice = chooser.showSaveDialog(null);
			if(choice == JFileChooser.APPROVE_OPTION) { 
					saveNew(chooser.getSelectedFile(), s);
			}
		}
		else { 
			JFileChooser chooser = new JFileChooser();
			int choice = chooser.showOpenDialog(null);
			if(choice == JFileChooser.APPROVE_OPTION) { 
				main.load(load(chooser.getSelectedFile()));
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
	
	private Stack<String> load(File f) {
		Stack<String> history = new Stack<String>();
		boolean loadSuccessful = true;
		try { 
			Scanner s = new Scanner(f);
			while(s.hasNextLine()) { 
				history.push(s.nextLine());
			}
			
		} catch(IOException e) { loadSuccessful = false; } 
		return history;
	}
	
	
}