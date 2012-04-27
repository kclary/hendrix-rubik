package cube;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class BuildGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame frame;
	private MainPanel pane; 
	private Menu menu = new Menu();
	
	public BuildGUI() throws Exception {
		pane = new MainPanel();
		frame = new JFrame("Cube Cruncher");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pane.setPreferredSize(new Dimension(1300, 900));
		frame.add(pane, BorderLayout.CENTER);
		frame.add(menu.makeMenu(pane), BorderLayout.PAGE_START);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
		        UIManager.put("swing.boldMetal", Boolean.FALSE);
		        try {
					new BuildGUI();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
    }
}