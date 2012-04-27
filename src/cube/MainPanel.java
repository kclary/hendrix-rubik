package cube;


import java.awt.Graphics;
import java.util.Stack;

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
	        cameraPanel = new PaintCameraControls(pane, this);
	        buttonPanel = new PaintButtons(instructPanel, pane);
	        pane.setBounds(150, 65, 800, 600);
	        add(pane);
	        reversePane.setBounds(900, 700, 400, 300);
	        add(reversePane);
	        instructPanel.setBounds(1090,50,200,500);
	        add(instructPanel);
	        cameraPanel.setBounds(1090,700,200,200);
	        add(cameraPanel);
	        buttonPanel.setBounds(15,15,1000,1000);
	        add(buttonPanel);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public PaintButtons getButtons() { 
		return buttonPanel;
	}
	
	protected void paintComponent(Graphics g) { 
		pane.repaint();
	}
	
	public String[] getHistory() { 
		return buttonPanel.getHistory();
	}
	
	public void load(Stack<String> history) { 
		String s;
		int d;
		
		buttonPanel.solve();

		for(int i = 0; i < history.size(); i++) { 
			s = history.get(i);
			System.out.println(s);
			String[] instruct = s.split(" ");
			if(instruct.length == 4) { 
				if(instruct[3].equals("Right") || instruct[3].equals("Down")) { 
					d = 1;
				} else { 
					d = -1;
				}
				if(instruct[1].equals("row")) {
					buttonPanel.rotateRow(new Integer(instruct[2]), d);
				}
				else if(instruct[1].equals("col")) { 
					buttonPanel.rotateCol(new Integer(instruct[2]), d);
				}
			}
		}
	}
}