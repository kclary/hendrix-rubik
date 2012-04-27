

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.plaf.basic.BasicArrowButton;

public class PaintButtons extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private BasicArrowButton row1L = new BasicArrowButton(BasicArrowButton.WEST);
	private BasicArrowButton row2L = new BasicArrowButton(BasicArrowButton.WEST);
	private BasicArrowButton row3L = new BasicArrowButton(BasicArrowButton.WEST);
	private BasicArrowButton col1U = new BasicArrowButton(BasicArrowButton.NORTH);
	private BasicArrowButton col2U = new BasicArrowButton(BasicArrowButton.NORTH);
	private BasicArrowButton col3U = new BasicArrowButton(BasicArrowButton.NORTH);
	private BasicArrowButton row1R = new BasicArrowButton(BasicArrowButton.EAST);
	private BasicArrowButton row2R = new BasicArrowButton(BasicArrowButton.EAST);
	private BasicArrowButton row3R = new BasicArrowButton(BasicArrowButton.EAST);
	private BasicArrowButton col1D = new BasicArrowButton(BasicArrowButton.SOUTH);
	private BasicArrowButton col2D = new BasicArrowButton(BasicArrowButton.SOUTH);
	private BasicArrowButton col3D = new BasicArrowButton(BasicArrowButton.SOUTH);
	private JButton hint = new JButton("Hint");
	private JButton scramble = new JButton("Scramble");
	private PaintInstruct temp;
	RubikPane rubPane;
	private Stack<String> history = new Stack<String>();
	private boolean isHint = false;
	
	/*public void mouseClicked(MouseEvent e) {
	        System.out.println("In the mouse checked");
	        java.awt.Point pt = e.getPoint();
	        System.out.println(pt.x);
	        SimpleVector vec = Interaction.reproject2D3D(world.getCamera(), buffer, pt.x, pt.y);
	        System.out.println(vec);
	                               
	        int[] res=Interact2D.pickPolygon(world.getVisibilityList(), vec);
	        if(res != null) {
	                if(res.length > 0) {
	                        Object3D picked = world.getObject(res[0]);
	                        System.out.println("The object's name is :  " + picked.getName());             
	                }
	        }
	        prevX = e.getX();
	}*/
	
	public PaintButtons(PaintInstruct instruct, RubikPane pane){
		super();
		
		temp = instruct;
		try {
			rubPane = pane;
		rubPane.setPreferredSize(new Dimension(300, 300));
		rubPane.setBackground(Color.WHITE);
        setLayout(null);
        row1L.setBounds(81,108,35,35);
        row1L.addActionListener(this);
        add(row1L);
        row2L.setBounds(81,338,35,35);
        row2L.addActionListener(this);
        add(row2L);
        row3L.setBounds(81,568,35,35);
        row3L.addActionListener(this);
        add(row3L);
        add(rubPane);
        
        col1U.setBounds(180,0,35,35);
        col1U.addActionListener(this);
        add(col1U);
        col2U.setBounds(480,0,35,35);
        col2U.addActionListener(this);
        add(col2U);
        col3U.setBounds(780,0,35,35);
        col3U.addActionListener(this);
        add(col3U);
        
        row1R.setBounds(960,108,35,35);
        row1R.addActionListener(this);
        add(row1R);
        row2R.setBounds(960,338,35,35);
        row2R.addActionListener(this);
        add(row2R);
        row3R.setBounds(960,568,35,35);
        row3R.addActionListener(this);
        add(row3R);
        
        col1D.setBounds(180,660,35,35);
        col1D.addActionListener(this);
        add(col1D);
        col2D.setBounds(480,660,35,35);
        col2D.addActionListener(this);
        add(col2D);
        col3D.setBounds(780,660,35,35);
        col3D.addActionListener(this);
        add(col3D);
        
        hint.setBounds(0,0,100,35);
        hint.addActionListener(this);
        add(hint);
        
        scramble.setBounds(0, 40, 100, 35);
        scramble.addActionListener(this);
        add(scramble);
        
		}
	
	 catch (Exception e) {
	 }
	}
	


	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getSource() == row1L){
			rotateRow(1, 1);
		}else if(evt.getSource() == row2L){
			rotateRow(2, 1);
		}else if(evt.getSource() == row3L){
			rotateRow(3, 1);
		}else if(evt.getSource() == row1R) {
			rotateRow(1, -1);
		}else if(evt.getSource() == row2R){
			rotateRow(2, -1);
		}else if(evt.getSource() == row3R){
			rotateRow(3, -1);
		}else if(evt.getSource() == col1U){
			rotateCol(1, 1);
		} else if(evt.getSource() == col2U){
			rotateCol(2, 1);
		}else if(evt.getSource() == col3U){
			rotateCol(3, 1);
		}else if(evt.getSource() == col1D){
			rotateCol(1, -1);
		}else if(evt.getSource() == col2D){
			rotateCol(2, -1);
		}else if(evt.getSource() == col3D){
			rotateCol(3, -1);			
		}else if(evt.getSource() == scramble){
			scrambleBlock(20);
		}
		else{
			JTextArea instruct = temp.getInstructBox();
			if(history.empty()==true){
				instruct.append("Already solved!\n");
			}else{
				instruct.append("Hint: " + history.peek()+"\n");
			}
		}
	}
	
	public String[] getHistory() { 
		String[] s = new String[history.size()];
		for(int i = 0; i < s.length; i++) {
			s[i] = history.get(i);
		}
		return s;
	}
	
	public void rotateRow(int row, int dir) {
		JTextArea instruct = temp.getInstructBox();	
		String instruction = "Twist row " + new Integer(row).toString();
		if(dir == 1) {
			rotateLeft(instruct, instruction + " Left\n", row, dir);
			if(history.peek().equals(instruction + " Left")) {
				history.pop();
			}
			else { 
				history.push(instruction + " Right");
			}
		} else {
			rotateLeft(instruct, instruction +  " Right\n", row, dir);
			if(history.peek().equals(instruction + " Right")) {
				history.pop();
			}
			else { 
				history.push(instruction + " Left");
			}
		}
	}
	
	public void rotateCol(int col, int dir) {
		JTextArea instruct = temp.getInstructBox();
		String instruction = "Twist col " + new Integer(col).toString();
		if(dir == 1) {
			rotateUp(instruct, instruction + " Up\n", col, 1);
			if(history.peek().equals(instruction + " Up")){
				history.pop();
			}
			else { 
				history.push(instruction + " Down");
			}
		} else {
			rotateUp(instruct, instruction + " Down\n", col, -1);
			if(history.peek().equals(instruction + " Down")) { 
				history.pop();
			}
			else { 
				history.push(instruction + " Up");
			}
		}
	}
	public void rotateUp(JTextArea instruct, String s, int col, int d) { 
		instruct.append(s);
		rubPane.addUpdate(col, d, Direction.Z);
		rubPane.repaint();
	}
	
	public void rotateLeft(JTextArea instruct, String s, int row, int d) { 
		instruct.append(s);
		rubPane.addUpdate(row, d, Direction.Y);
		rubPane.repaint();
	}
	
	public void scrambleBlock(int numberOfMoves) {
		for (int i = 0; i < numberOfMoves; i++) {
			Random randomGenerator = new Random();
			Float rand = randomGenerator.nextFloat();
			Float randRow = randomGenerator.nextFloat();
			int rowToChange = (int) Math.floor(randRow * 3);
			if(rand > .5f) {
				rand = randomGenerator.nextFloat();
				if(rand > .5f) { 
					scrambleCol(rowToChange + 1, -1);
				}
				else { 
					scrambleCol(rowToChange + 1, 1);
				}
			} else {
				rand = randomGenerator.nextFloat();
				if(rand > .5f) { 
					scrambleRow(rowToChange + 1, -1);
				}
				else { 
					scrambleRow(rowToChange + 1, 1);
				}
			}
		}
	}
	
	public void scrambleCol(int col, int dir){
		if(dir == 1) {
			scrambleUp(col, 1);
			history.push("Twist col "+new Integer(col).toString()+" Down");
		} else {
			scrambleUp(col, -1);
			history.push("Twist col "+new Integer(col).toString()+" Up");
		}
	}
	
	public void scrambleUp(int col, int d){
		rubPane.addUpdate(col, d, Direction.Z);
		rubPane.repaint();
	}
	
	public void scrambleRow(int row, int dir){	
		if(dir == 1) {
			scrambleLeft(row, dir);
			history.push("Twist row "+new Integer(row).toString()+ " Right");
		} else {
			scrambleLeft(row, dir);
			history.push("Twist row "+new Integer(row).toString()+" Left");
		}
	}
	
	public void scrambleLeft(int row, int d){
			rubPane.addUpdate(row, d, Direction.Y);
			rubPane.repaint();
	}

}