import java.awt.BorderLayout;
import java.awt.Color;
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
}