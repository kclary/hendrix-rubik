

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private PaintInstruct instructPanel;
	private PaintButtons buttonPanel;
	private PaintCameraControls cameraPanel;
	private RubikPane pane;
	private RearPane reversePane;
	
	public MainPanel(){
		super();
		try {
			setLayout(null);
			pane = new RubikPane();
			reversePane = new RearPane(pane);
			instructPanel = new PaintInstruct();
	        cameraPanel = new PaintCameraControls(pane);
	        buttonPanel = new PaintButtons(instructPanel, pane);
	        buttonPanel.setBounds(15,15,1000,1000);
	        add(buttonPanel);
	        pane.setBounds(150, 65, 800, 600);
	        add(pane);
	        reversePane.setBounds(900, 700, 400, 300);
	        add(reversePane);
	        instructPanel.setBounds(1090,50,200,500);
	        add(instructPanel);
	        cameraPanel.setBounds(1090,700,200,200);
	        add(cameraPanel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected void paintComponent(Graphics g) { 
		pane.repaint();
	}
	
	public JTextArea getInstructions() { 
		return instructPanel.getInstructBox();
	}
}