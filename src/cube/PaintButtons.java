package cube;

import solver.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.plaf.basic.BasicArrowButton;

import solver.FaceColorsSearcher;
import solver.TypeOfCube;

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
	private JButton solve1 = new JButton("Solve");
	private JButton solve2 = new JButton("Solve A*");
	private PaintInstruct instructions;
	RubikPane rubPane;
	private Stack<String> history = new Stack<String>();
	StateOfColors currentState;
	ColorChanger changer;
	CubeRecognizer cubeRecognizer;
	BuildDatabase builder;
	
	public PaintButtons(PaintInstruct instruct, RubikPane pane){
		super();
		
		Vector<ColorsOfCube> startingColors = setupStartingColorsForSearcher();
		currentState = new StateOfColors(startingColors, 0);
		changer = new ColorChanger();
		cubeRecognizer = new CubeRecognizer();
		instructions = instruct;
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
        
        solve1.setBounds(0, 750, 100, 35);
        solve1.addActionListener(this);
        add(solve1);
        
        solve2.setBounds(0, 790, 100, 35);
        solve2.addActionListener(this);
        add(solve2);
        
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
		}else if(evt.getSource() == solve1) { 
			solve();
			instructions.getInstructBox().setText("Solved!\n");
		}else if(evt.getSource() == solve2) { 
			TypeOfCube cubeState = cubeRecognizer.getCubeState(currentState);
			FaceColorsSearcher searcher = new FaceColorsSearcher(this, currentState);
			searcher.setCubeType(cubeState);
			searcher.setSolveCubeAutomatically();
			searcher.run();
			instructions.getInstructBox().setText("Solved!\n");
		}
		else{
			JTextArea instruct = instructions.getInstructBox();
			if(history.empty()==true){
				instruct.append("Already solved!\n");
			}else{
				instruct.append("Hint: " + history.peek()+"\n");
			}
		}
	}
	
	public PaintInstruct getInstruction() { 
		return instructions;
	}
	
	public String[] getHistory() { 
		String[] s = new String[history.size()];
		for(int i = 0; i < s.length; i++) {
			s[i] = history.get(i);
		}
		return s;
	}
	
	public void rotateRow(int row, int dir) {
		JTextArea instruct = instructions.getInstructBox();	
		String instruction = "Twist row " + new Integer(row).toString();
		if(dir == 1) {
			rotateLeft(instruct, instruction + " Left\n", row, dir);
			currentState = changer.rowLeftNewState(currentState, row-1);
			currentState.setAsUserGenerated();
			if(!history.empty()) { 
				if(history.peek().equals(instruction + " Left")) {
					history.pop();
				}
				else { 
					history.push(instruction + " Right");
				}
			}
			else { 
				history.push(instruction + " Right");
			}
		} else {
			rotateLeft(instruct, instruction +  " Right\n", row, dir);
			currentState = changer.rowRightNewState(currentState, row-1);
			currentState.setAsUserGenerated();
			if(!history.empty()) { 
				if(history.peek().equals(instruction + " Right")) {
					history.pop();
				}
				else { 
					history.push(instruction + " Left");
				}
			} else { 
				history.push(instruction + " Left");
			}
		}
	}
	
	public void rotateCol(int col, int dir) {
		JTextArea instruct = instructions.getInstructBox();
		String instruction = "Twist col " + new Integer(col).toString();
		if(dir == 1) {
			rotateUp(instruct, instruction + " Up\n", col, 1);
			currentState = changer.colUpNewState(currentState, col-1);
			currentState.setAsUserGenerated();
			if(!history.empty()) { 
				if(history.peek().equals(instruction + " Up")){
					history.pop();
				}
				else { 
					history.push(instruction + " Down");
				}
			} 
		else { 
			history.push(instruction + " Down");
		}
		} else {
			rotateUp(instruct, instruction + " Down\n", col, -1);
			currentState = changer.colDownNewState(currentState, col-1);
			currentState.setAsUserGenerated();
			if(!history.empty()) { 
				if(history.peek().equals(instruction + " Down")) { 
					history.pop();
				}
				else { 
					history.push(instruction + " Up");
				}
			}
			else { 
				history.push(instruction + " Up");
			}
		}
	}
	
	public void changeCamera(Direction dir, int d) { 
		if(dir == Direction.Y){ 
			for(int i = 1; i < 4; i++) { 
				if(d > 0) { 
					System.out.println(d);
					history.push("Twist row " + i + " Right");
				}
				else { 
					history.push("Twist row " + i + " Left");
				}
			}
		}
		else { 
			for(int i = 1; i < 4; i++) { 
				if(d > 0) { 
					System.out.println(d);
					history.push("Twist col " + i + " Down");
				}
				else { 
					history.push("Twist col " + i + " Up");
				}
			}
		}
	}
	
	public void rotateUp(JTextArea instruct, String s, int col, int d) { 
		instruct.append(s);
		rubPane.addUpdate(col, d, Direction.Z);
	}
	
	public void rotateLeft(JTextArea instruct, String s, int row, int d) { 
		instruct.append(s);
		rubPane.addUpdate(row, d, Direction.Y);
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
	
	public void solve() { 
		while(!history.empty()) { 
			int d = 1;
			String s = history.pop();
			String[] instruct = s.split(" ");
			if(instruct[3].equals("Left") || instruct[3].equals("Up")) { 
				d = 1;
			} else { 
				d = -1;
			}
			if(instruct[1].equals("row")) {
				if(d > 0) { 
					currentState = changer.rowLeftNewState(currentState, new Integer(instruct[2])-1);
					currentState.setAsUserGenerated();
				} else { 
					currentState = changer.rowRightNewState(currentState, new Integer(instruct[2])-1);
					currentState.setAsUserGenerated();
				}
				rubPane.addUpdate(new Integer(instruct[2]), d, Direction.Y);
			}
			else if(instruct[1].equals("col")) { 
				if(d > 0) { 
					currentState = changer.colUpNewState(currentState, new Integer(instruct[2])-1);
					currentState.setAsUserGenerated();
				} else { 
					currentState = changer.colDownNewState(currentState, new Integer(instruct[2])-1);
					currentState.setAsUserGenerated();
				}
				rubPane.addUpdate(new Integer(instruct[2]), d, Direction.Z);
			}
		}
		instructions.getInstructBox().setText("");
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
		if(d > 0 ) { 
			currentState = changer.colUpNewState(currentState, col-1);
			currentState.setAsUserGenerated();
		}
		else { 
			currentState = changer.colDownNewState(currentState, col-1);
			currentState.setAsUserGenerated();
		}
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
			if(d > 0) { 
				currentState = changer.rowLeftNewState(currentState, row-1);
				currentState.setAsUserGenerated();
			}
			else { 
				currentState = changer.rowRightNewState(currentState, row-1);
				currentState.setAsUserGenerated();
			}
			rubPane.repaint();
	}
	
	public Vector<ColorsOfCube> setupStartingColorsForSearcher() {
		Vector<ColorsOfCube> startingColors = new Vector<ColorsOfCube>();
		int tempI;
		for(int i = 0 ; i < 54; i++){
			if(i < 9) {
				startingColors.add(ColorsOfCube.Blue);
			}
			if(i >= 9 && i <= 44) {
				if(i > 32) tempI = i - 24;
				else if(i > 20) tempI = i - 12;
				else tempI = i;
				if(tempI <= 11 ) startingColors.add(ColorsOfCube.White);
				else if(tempI >= 12 && tempI <= 14) startingColors.add(ColorsOfCube.Red);
				else if(tempI >= 15 && tempI <= 17) startingColors.add(ColorsOfCube.Orange);
				else if(tempI >= 18 && tempI <= 20) startingColors.add(ColorsOfCube.Yellow);
			}
			if(i >= 45) {
				startingColors.add(ColorsOfCube.Green);
			}
		}
		return startingColors;
	}

}