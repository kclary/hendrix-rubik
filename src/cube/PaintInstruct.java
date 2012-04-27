package cube;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class PaintInstruct extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel label;
	private JScrollPane scroller;
	private JTextArea InstructBox;
	
	public PaintInstruct(){
		super();
		label = new JLabel("Instructions");
		label.setHorizontalAlignment(JLabel.LEFT);
        label.setVerticalAlignment(JLabel.TOP);
		InstructBox = new JTextArea();
		InstructBox.setColumns(15);
		InstructBox.setRows(40);
		InstructBox.setEditable(false);
		InstructBox.setBackground(Color.white);
		scroller = new JScrollPane(InstructBox);
		
		setLayout(new BorderLayout());
		add(label, BorderLayout.PAGE_START);
        add(scroller);
	}
	
	public JTextArea getInstructBox(){
		return InstructBox;
	}
	
	public void appendHints(Vector<Update> updates) {
		if(updates.size() > 0){
			Update update = updates.get(0); 
			int index1to3 = update.getRow();
			int d = update.getD();
			Direction dir = update.getDir();
			String rowOrColumn;
			String upDownRightLeft;
			if(dir == Direction.Z) {
				if(d == 1){
					upDownRightLeft = "up";
				} else{
					upDownRightLeft = "down";
				}
				rowOrColumn = "Col";
			} else {
				if(d == 1){
					upDownRightLeft = "right";
				} else{
					upDownRightLeft = "left";
				}
				rowOrColumn = "Row";
			}
			String instruction = "Move " + rowOrColumn + " "+ index1to3 + " " + upDownRightLeft;
			InstructBox.append("\nHint:" + "\n");
			InstructBox.append(instruction + "\n\n");
		} else {
			System.out.println("wrong updates input");
		}
	}
}