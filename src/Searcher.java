import java.awt.Color;
import java.nio.DoubleBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import org.lwjgl.opengl.Drawable;

import java.sql.*;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument.Iterator;


public class Searcher {
	
	
	
	Statement stat;
	SQLCommands commandClass;
	
	StateOfBlock startingstate;
	Vector<Integer> startingPositions;
	VectorOfCubes startingNeverToChangeIndividualBricks;
	
	String rubTable = "rubTest";
	String solutionsTable = "rubiksSolutions";
	BuildGUI GUI;
	PaintButtons pb;
	Vector<String> instructionsToSolve;
	
	public Searcher(StateOfBlock startingState, VectorOfCubes startingCubesOnRubiks, PaintButtons pb) {
		this.pb = pb;
		this.startingstate = startingState;
		startingNeverToChangeIndividualBricks = startingCubesOnRubiks;
		this.GUI = GUI;
	}
	
	public void run() {
		boolean blockSolved = false;
		PriorityBlockingQueue<StateOfBlock> queue = new PriorityBlockingQueue<StateOfBlock>();
		HashSet<String> previousStates = new HashSet<String>();
		VectorOfCubes startingPositions = startingstate.getVectorOfCubes();
		Vector<StateOfBlock> statesLeadingToThis = new Vector<StateOfBlock>();
		StateOfBlock firstState = new StateOfBlock(startingPositions, statesLeadingToThis, MovementsOfCube.noMovement);
		
		RubiksIndexChanger changer = new RubiksIndexChanger(firstState);
		
        try {
			createConnection();
			System.out.println("worked");
        } catch (ClassNotFoundException e) {
			System.out.println("Didn't work");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("Didn't work");
			e.printStackTrace();
		}

		/*
        String fieldTwo = "website VARCHAR(40)";
        String fieldThree = "DECIMAL(4,2)";
        String fieldFour = "DECIMAL(4,2)";
        fieldNames.add(fieldTwo);
        fieldNames.add(fieldThree);
        fieldNames.add(fieldFour);
        */
		commandClass = new SQLCommands();
		//createSolutionsTables();
		HashMap solutionsFromPositions = new HashMap<String, String>();		
		//printEverythingInADatabase(solutionsTable);
		StateOfBlock randomState = randomizeBlock(firstState, 5);
		System.out.println("starting random state is : " + randomState.getHomePositions());
		changer = new RubiksIndexChanger(randomState);
		//randomState.cubePositionsVectorObject.cubeBody.get(0).flipCubeDown();
		queue.add(randomState);
		
		//System.out.println(thirdState.getHomePositions());
		//System.exit(0);
		
		int positionAdded = 300;
		boolean first = false;
		while( !blockSolved) {
			if(queue.size() > 0) {
				StateOfBlock curStateChecking = queue.poll();
				//System.out.println("Heuristic value of the thing that got polled off is "+ curStateChecking.getHeuristicValue());
				String curID = curStateChecking.getID();
				//System.out.println(" the cur home positions are  : " + curStateChecking.getHomePositions());
				VectorOfCubes curVectorObject = curStateChecking.getVectorOfCubes();
				if( previousStates.contains(curID) ) {
					//System.out.println("The ID " + curID + " is in there already");
					continue;
				} else if ( colorCheckCompletedRubixCube(curStateChecking, curVectorObject) && !first ) {
					//Vector<Integer> curStatesPositions = curStateChecking.getHomePositions();
					System.out.println("Finished Cube with positions: ");
					Vector<StateOfBlock> statesLeadingToThisPosition = curStateChecking.getStatesLeadingToThisState();
					String movementsToFinal = "";
					HashMap<String, String> finalPositionsFound = new HashMap<String, String>();
					for(int i = 0 ; i < statesLeadingToThisPosition.size(); i++) {
						StateOfBlock curState = statesLeadingToThisPosition.get(i);
						String curFinishedID = curState.getID();
						MovementsOfCube movementToThisID = curState.getMovementToThisState();
						System.out.println("previous movement was : " + movementToThisID);
						System.out.println("home positions are : " + curState.getHomePositions());
						movementsToFinal += movementToThisID;
						movementsToFinal += ":";
						finalPositionsFound.put(curFinishedID, movementsToFinal);
						//Vector<Integer> curPositionsLeadingToThisState = curState.getHomePositions();
						//System.out.println("One position is : " + curPositionsLeadingToThisState);
					}
					addAFinalPosition(solutionsTable, finalPositionsFound);
					//printEverythingInADatabase(solutionsTable);
					//System.out.println(curStatesPositions);
					System.out.println(curStateChecking.getID());
					blockSolved = true;
				} 
				
				else if( databaseContainsID(solutionsTable, "Position", curID) ){
					System.out.println("entries leading to cur id");
					String result = getSolutionMovementsforID(solutionsTable, "Position", curID);
					Vector<StateOfBlock> statesLeadingHere = curStateChecking.getStatesLeadingToThisState();
					
					//System.out.println(result);
					blockSolved = true;
					makeListOfInstructions(result, statesLeadingHere);
				}
				/*
				else if (!databaseContainsID(solutionsTable, "Position", curID)){
					System.out.println("database does not contain that entry");
					//System.exit(0);
				}
				*/
				else {
					//System.out.println("in the else");
					changer.setChangerForNewState(curStateChecking);
					//changer = new RubiksIndexChanger(curStateChecking);
					Vector<StateOfBlock> potentialPositions = changer.getSuccessorCubes();
					for( int j = 0; j < potentialPositions.size(); j++ ) {
						StateOfBlock potentialState = potentialPositions.get(j);
						//changer.setChangerForNewState(potentialState);
						//Vector<StateOfBlock> nextStates = changer.getSuccessorCubes();
						double heur = heurFinishedRows(potentialState)*10;
						heur += heurCrossesOnCube(potentialState)*20;
						heur += heurCubeNumberOfColorsOnAFace(potentialState);
						if(positionAdded > 0 ) {
							heur += positionAdded;
							positionAdded--;
						}
						//heur += curStateChecking.getHeuristicValue()/8;
						System.out.println("currently adding to the queue: " + heur );
						//double heur = heurSameColorNextToOneAnother(potentialState);
						//double heur = heurPercentSidesOneColor(potentialState);
						//double heurColors = heurPercentSidesOneColor(potentialState);
						//System.out.println("HeurColors is " + heurColors);
						//int heurScore = scoreCubeOrientationPureDistanceAway(potentialState);
						potentialState.setHeuristicValue(heur);
						//if(!previousStates.contains(potentialState.getID())) {
						queue.add(potentialState);
						//positionAdded++;
						//}
					}
					if(!first) {
						previousStates.add(curStateChecking.getID());
					}
					first = false;
				}
			}
			else {
				System.out.println("Failure");
				break;
			}
		}
	}
	
	private void makeListOfInstructions(String databaseReturn, Vector<StateOfBlock> statesLeadingToState) {
		instructionsToSolve = new Vector<String>();
		//Vector<StateOfBlock> statesLeadingToState = finalState.getStatesLeadingToThisState();
		for(int i = 0 ; i < statesLeadingToState.size(); i++){
			StateOfBlock curState = statesLeadingToState.get(i);
			String movement = curState.getMovementToThisState().toString();
			instructionsToSolve.add(movement);
		}
		/*
		if(databaseReturn.length() > 2) {
			String[] a = databaseReturn.split(":");
			for(int i = 0; i < a.length; i++){
				instructionsToSolve.add(a[i]);
			}
		}
		*/
		for(String i : instructionsToSolve) {
			System.out.println(i);
		}
	}
	
	private boolean databaseContainsID( String tableName, String position, String idChecking ){
		Vector<String> fieldsForRubTableSelect = new Vector<String>();
		fieldsForRubTableSelect.add("*");
		String cmd = commandClass.selectAllWhere(tableName, position, idChecking);
		try {
			stat.execute(cmd);
			ResultSet results = stat.getResultSet();
			if(results.next()) return true; 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private String getSolutionMovementsforID(String tableName, String position, String idChecking) {
		Vector<String> fieldsForRubTableSelect = new Vector<String>();
		fieldsForRubTableSelect.add("*");
		String cmd = commandClass.selectAllWhere(tableName, position, idChecking);
		ResultSet results = null;
		try {
			stat.execute(cmd);
			results = stat.getResultSet();
			return results.getString(2); 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "didn't find a result";
	}
	
	private void printEverythingInADatabase(String tableName) {
		Vector<String> fieldsForRubTableSelect = new Vector<String>();
		fieldsForRubTableSelect.add("*");
		String cmd = commandClass.selectFrom(tableName, fieldsForRubTableSelect);
		try {
			stat.execute(cmd);
			ResultSet results = stat.getResultSet();
			while (results.next()) {
                for (int c = 1; c <= 2; ++c) {
                    System.out.print("printed\t" + results.getString(c) + "\t");
                }
                System.out.println();
            } 
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void addAFinalPosition(String tableName, HashMap<String, String> positionsWithMovementsFromThenOn){
		Set<Entry<String, String>> pastPositions = positionsWithMovementsFromThenOn.entrySet();
	    for(Map.Entry<String, String> curEntry : pastPositions) {
			//System.out.println("cur key " + curEntry.getKey() + " cur value " + curEntry.getValue());
			Vector<String> curAdditions = new Vector<String>();
			curAdditions.add(curEntry.getKey());
			curAdditions.add(curEntry.getValue());
			String cmd = commandClass.insertToTable(tableName, curAdditions);
			System.out.println("this is the add command " + cmd);
            try {
    			stat.execute(cmd);
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
        }				
	}
	
	private void createSolutionsTables() {
        String cmd = commandClass.dropTable(rubTable);
        String cmd2 = commandClass.dropTable(solutionsTable);
        try {
			stat.execute(cmd);
			stat.execute(cmd2);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        String fieldOne = "Name VARCHAR(40)";
        Vector<String> fieldsForRubTableCreation = new Vector<String>();
        fieldsForRubTableCreation.add(fieldOne);
        
        String One = "Position VARCHAR(40)";
        String Two = "Movements VARCHAR(40)";
        Vector<String> fieldsForSolutionsCreation = new Vector<String>();
        
        fieldsForSolutionsCreation.add(One);
        fieldsForSolutionsCreation.add(Two);
        
        String cmdRubTest = commandClass.createTable(rubTable, fieldsForRubTableCreation);
        String cmdSolutions = commandClass.createTable(solutionsTable, fieldsForSolutionsCreation);
        
        try {
			stat.execute(cmdRubTest);
		} catch (SQLException e) {
			e.printStackTrace();
		}
        try {
        	System.out.println(cmdSolutions);
			stat.execute(cmdSolutions);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void createConnection() throws ClassNotFoundException, SQLException  {
		Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection("jdbc:sqlite:testdb");
        stat = con.createStatement();
	}
	
	private StateOfBlock randomizeBlock(StateOfBlock startingState, int numberOfMoves) {
		RubiksIndexChanger changer = new RubiksIndexChanger(startingState);
		StateOfBlock randomState = startingState;
		for (int i = 0; i < numberOfMoves; i++) {
			Random randomGenerator = new Random();
			Float rand = randomGenerator.nextFloat();
			Float randRow = randomGenerator.nextFloat();
			int rowToChange = (int) Math.floor(randRow * 3);
			StateOfBlock newState;
			if(rand > .5f) {
				this.pb.twistCol(rowToChange, 1);
				newState = changer.moveColumnDownNewState(rowToChange);
			} else {
				this.pb.rotateRow(rowToChange, 1);
				newState = changer.moveRowToTheLeftNewState(rowToChange);
			}
			randomState = newState;
			changer.setChangerForNewState(randomState);
		}
		return randomState;
	}
	
	public boolean rotationAndFlipCheckCompletedRubixCube(StateOfBlock curStateChecking, VectorOfCubes inputCubesVectorObject) {
		Vector<IndividualCubeOnRubiks> indiCubes = inputCubesVectorObject.getCubesVector();
		int rotationCheck;
		int flipCheck;
		int curFlipCheck;
		int curRotationCheck;
		rotationCheck = indiCubes.get(0).Rotation;
		flipCheck = indiCubes.get(0).Flip;
		for(int i = 1; i < indiCubes.size(); i ++) {
			curRotationCheck = indiCubes.get(i).Rotation;
			curFlipCheck = indiCubes.get(i).Flip;
			if(curFlipCheck != flipCheck) {
				//System.out.println("flip not the same " + "cur flip " + curFlipCheck + " flip check is : " + flipCheck);
				return false;
			}
			else if (curRotationCheck != rotationCheck) {
				//System.out.println("rotations are not the same " + "cur rotation " + curRotationCheck + " rotations check is : " + rotationCheck);
				return false;
			}
		}
		return true;
	}
	
	public boolean idCheckCompletedRubixCube(StateOfBlock curStateChecking) {
		if(curStateChecking.getID().equalsIgnoreCase("01234567891011121314151617181920212223242526") || curStateChecking.getID().equalsIgnoreCase("67815161724252634512131421222301291011181920")){
			return true;
		} else {
			return false;
		}
	}
	
	public boolean colorCheckCompletedRubixCube(StateOfBlock curStateChecking, VectorOfCubes inputCubesVectorObject) {
		Vector<IndividualCubeOnRubiks> cubes = inputCubesVectorObject.getCubesVector();
		for(int face = 1; face <= 6; face++) {
			boolean checkSide = checkColorsOnCubeFace(cubes, face);
			if(checkSide == false) return false;
		}
		return true;
	}
	
	private boolean checkColorsOnCubeFace(Vector<IndividualCubeOnRubiks> cubes, int face) {
		List<Integer> faceOne = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
		List<Integer> faceTwo = Arrays.asList(2, 5, 8, 11, 14, 17, 20, 23, 26);
		List<Integer> faceThree = Arrays.asList(18, 19, 20, 21, 22, 23, 24, 25, 26);
		List<Integer> faceFour = Arrays.asList(0, 3, 6, 9, 12, 15, 18, 21, 24);
		List<Integer> faceFive =  Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20);
		List<Integer> faceSix =  Arrays.asList(6, 7, 8, 15, 16, 17, 24, 25, 26);
		List<Integer> FaceToCheck;
		if(face == 1){
			FaceToCheck = faceOne;
		} else if(face == 2) {
			FaceToCheck = faceTwo;
		}  else if(face == 3) {
			FaceToCheck = faceThree;
		}  else if(face == 4) {
			FaceToCheck = faceFour;
		}  else if(face == 5) {
			FaceToCheck = faceFive;
		}  else if(face == 6) {
			FaceToCheck = faceSix;
		}  else {
			FaceToCheck = faceOne;
		}
		Integer curLocation = FaceToCheck.get(0);
		IndividualCubeOnRubiks curCubeOnFace = cubes.get(curLocation);
		ColorsOfCube colorCheck = curCubeOnFace.getColorForFace(face);
		for(int i = 0; i < FaceToCheck.size(); i++) {
			curLocation = FaceToCheck.get(i);
			curCubeOnFace = cubes.get(curLocation);
			ColorsOfCube curColorCheck = curCubeOnFace.getColorForFace(face);
			//System.out.println("cur location is : " + curLocation + " and the color on face " + face + " is " + curColorCheck);
			if(curColorCheck != colorCheck) {
				//System.out.println("returning false");
				//System.out.println("Color check is : " + colorCheck + " cur color check is  " + curColorCheck);
				return false;
			}
		}
		return true;
	}
	
	private int heurFinishedRows(StateOfBlock stateToAnalyze ) {
		int totalFinishedRows = 0;
		VectorOfCubes cubes = stateToAnalyze.getVectorOfCubes();
		Vector<IndividualCubeOnRubiks> cubesInState = cubes.getCubesVector();
		Vector<Vector<ColorsOfCube>> colors = getColorsOnCube(cubesInState);
		int rowsFinishedOnOneSide = 0;
		for(Vector<ColorsOfCube> side : colors) {
			rowsFinishedOnOneSide = getFinishedRowsOnOneSide(side);
			totalFinishedRows += rowsFinishedOnOneSide;
		}
		return totalFinishedRows;
	}
	
	public int getFinishedRowsOnOneSide(Vector<ColorsOfCube> side ) {
		int rowsOnOneSide = 0;
		for(int i = 0; i < 3; i ++) {
			ColorsOfCube curColor = side.get(i);
			if(i < 3) {
				ColorsOfCube posOneHorizontal = side.get(i);
				ColorsOfCube posTwoHorizontal = side.get(i+1);
				ColorsOfCube posThreeHorizontal = side.get(i+3);
				if(posOneHorizontal == posTwoHorizontal && posTwoHorizontal == posThreeHorizontal) {
					rowsOnOneSide++;
				}
			}
			if(i % 3 == 0) {
				ColorsOfCube posOneVert = side.get(i);
				ColorsOfCube posTwoVert = side.get(i+3);
				ColorsOfCube posThreeVert = side.get(i+6);
				if(posOneVert == posTwoVert && posTwoVert == posThreeVert) {
					rowsOnOneSide++;
				}
			}
		}
		return rowsOnOneSide;
	}
	
	private int heurSameColorNextToOneAnother(StateOfBlock stateToAnalyze) {
		int totalColorsNextToOneAnother = 0;
		VectorOfCubes cubes = stateToAnalyze.getVectorOfCubes();
		Vector<IndividualCubeOnRubiks> cubesInState = cubes.getCubesVector();
		Vector<Vector<ColorsOfCube>> colors = getColorsOnCube(cubesInState);
		int sameColorsOnOneSide = 0;
		for(Vector<ColorsOfCube> side : colors) {
			sameColorsOnOneSide = getColorsNextToEachOtherOneSide(side);
			totalColorsNextToOneAnother += sameColorsOnOneSide;
		}
		return totalColorsNextToOneAnother;
	}
	
	private int getColorsNextToEachOtherOneSide(Vector<ColorsOfCube> side){
		int totalForSide = 0;
		for(int i = 0; i < side.size(); i ++) {
			ColorsOfCube curColor = side.get(i);
			if(i < 6) {
				ColorsOfCube verticalNeighborColor = side.get(i + 3);
				if(verticalNeighborColor == curColor) {
					totalForSide++;
				}
			}
			if(i < 8){
				ColorsOfCube horizontalNeighborColor = side.get(i + 1);
				if(curColor == horizontalNeighborColor){
					totalForSide++;
				}
			}
		}
		return totalForSide;
	}
	
	public int heurCrossesOnCube(StateOfBlock stateToAnalyze) {
		VectorOfCubes cubes = stateToAnalyze.getVectorOfCubes();
		Vector<IndividualCubeOnRubiks> cubesInState = cubes.getCubesVector();
		Vector<Vector<ColorsOfCube>> colors = getColorsOnCube(cubesInState);
		int totalCrosses = 0;
		for(Vector<ColorsOfCube> side : colors) {
			if(isCross(side)){
				totalCrosses++;
			}
		}
		return totalCrosses;
	}
	
	private boolean isCross(Vector<ColorsOfCube> side) {
		ColorsOfCube sidePieceOne = side.get(1);
		ColorsOfCube sidePieceTwo = side.get(3);
		ColorsOfCube CenterPiece = side.get(4);
		ColorsOfCube sidePieceThree = side.get(5);
		ColorsOfCube sidePieceFour = side.get(7);
		if(sidePieceOne == sidePieceTwo && sidePieceTwo == CenterPiece && CenterPiece == sidePieceThree && sidePieceThree == sidePieceFour) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int heurCubeOrientationPureDistanceAway(StateOfBlock stateToAnalyze) {
		VectorOfCubes cubes = stateToAnalyze.getVectorOfCubes();
		Vector<IndividualCubeOnRubiks> cubesInState = cubes.getCubesVector();
		int totalAway = 0;
		Vector<Integer> positions = new Vector<Integer>();
		for(int i = 0; i < cubesInState.size(); i++) {
			IndividualCubeOnRubiks curPosition = cubesInState.get(i);
			int val = curPosition.getHomePosition();
			int curPlacesAway = Math.abs(val - i);
			totalAway += curPlacesAway;
		}
		return totalAway;
	}
	
	public double heurNumberOfStatesOut(RubiksIndexChanger changer, Vector<StateOfBlock> potentialStates, int stepsToGo) {
		if (stepsToGo == 0){
			double max = 0;
			for(StateOfBlock curState : potentialStates) {
				double heurCur = heurPercentSidesOneColor(curState);
				if(heurCur > max) max = heurCur;
			}
			return max;	
		}
		else {
			double max = 0;
			for(StateOfBlock curState : potentialStates){
				changer.setChangerForNewState(curState);
				Vector<StateOfBlock> nextStates = changer.getSuccessorCubes();
				max = heurNumberOfStatesOut( changer, nextStates, stepsToGo-1 );
			}
			return max;
		}
	}
	
	public double heurCombinedRotationsAndFlips(StateOfBlock stateToAnalyze) {
		VectorOfCubes cubes = stateToAnalyze.getVectorOfCubes();
		Vector<IndividualCubeOnRubiks> cubesInState = cubes.getCubesVector();
		Vector<Integer> rotations = getAllRotations(cubesInState);
		Vector<Integer> flips = getAllFlips(cubesInState);
		int totalRotationAndFlip = 0;
		for (Integer curRotation : rotations) {
			totalRotationAndFlip += curRotation;
		}
		for (Integer curFlip : flips) {
			totalRotationAndFlip += curFlip;
		}
		return totalRotationAndFlip;
	}
	
	public Vector<Integer> getAllRotations(Vector<IndividualCubeOnRubiks> cubesInput ) {
		Vector<Integer> rotations = new Vector<Integer>();
		for(IndividualCubeOnRubiks cube: cubesInput) {
			int curRotation = cube.getRotation();
			rotations.add(curRotation);
		}
		return rotations;
	}
	
	public Vector<Integer> getAllFlips(Vector<IndividualCubeOnRubiks> cubesInput ) {
		Vector<Integer> flips = new Vector<Integer>();
		for(IndividualCubeOnRubiks cube: cubesInput) {
			int curFlip = cube.getFlip();
			flips.add(curFlip);
		}
		return flips;
	}
	
	public double heurPercentSidesOneColor(StateOfBlock stateToAnalyze) {
		VectorOfCubes cubes = stateToAnalyze.getVectorOfCubes();
		Vector<IndividualCubeOnRubiks> cubesInState = cubes.getCubesVector();
		Vector<Vector<ColorsOfCube>> allColors = getColorsOnCube(cubesInState);
		System.out.println(allColors);
		double percentForWholeCube = getPercentForWholeCube(allColors);
		double oneHundredPercentAdjusted = percentForWholeCube * 100;
		return oneHundredPercentAdjusted;
	}
	
	public double getPercentForWholeCube(Vector<Vector<ColorsOfCube>> inputColors){
		double sideOne = getPercentOneColorOnSide(inputColors.get(0));
		double sideTwo = getPercentOneColorOnSide(inputColors.get(1));
		double sideThree = getPercentOneColorOnSide(inputColors.get(2));
		double sideFour = getPercentOneColorOnSide(inputColors.get(3));
		double sideFive = getPercentOneColorOnSide(inputColors.get(4));
		double sideSix = getPercentOneColorOnSide(inputColors.get(5));
		double total = ( sideOne + sideTwo + sideThree + sideFour + sideFive + sideSix ) / 6;
		return total;
	}
	
	public double getPercentOneColorOnSide(Vector<ColorsOfCube> inputColors) {
		Vector<Integer> colors = new Vector<Integer>();
		for(int i = 0 ; i < 6 ; i ++) {
			colors.add(new Integer(0));
		}
		for(int i = 0; i < inputColors.size(); i ++) {
			ColorsOfCube curColor = inputColors.get(i);
			if(curColor == ColorsOfCube.Blue) {
				colors.set(0, colors.get(0) + 1 );
			} if(curColor == ColorsOfCube.Green) {
				colors.set(1, colors.get(1) + 1 );
			} if(curColor == ColorsOfCube.Orange) {
				colors.set(2, colors.get(2) + 1 );
			} if(curColor == ColorsOfCube.Red) {
				colors.set(3, colors.get(3) + 1 );
			} if(curColor == ColorsOfCube.White) {
				colors.set(4, colors.get(4) + 1 );
			} if(curColor == ColorsOfCube.Yellow) {
				colors.set(5, colors.get(5) + 1 );
			}
		}
		double max = 0;
		for(int i = 0 ; i < colors.size(); i ++) {
			if(colors.get(i) > max) {
				max = colors.get(i);
			}
		}
		System.out.println("one of the max values is : " + max);
		return max / 9;
	}
	
	public int heurCubeNumberOfColorsOnAFace(StateOfBlock stateToAnalyze) {
		VectorOfCubes cubes = stateToAnalyze.getVectorOfCubes();
		Vector<IndividualCubeOnRubiks> cubesInState = cubes.getCubesVector();
		Vector<Vector<ColorsOfCube>> colorsOfCube = getColorsOnCube(cubesInState);
		int totalColorsOnEveryFace = 0;
		return totalColorsOnEveryFace;
	}
	
	public Vector<Vector<ColorsOfCube>> getColorsOnCube(Vector<IndividualCubeOnRubiks> inputCubes) {
		Vector<ColorsOfCube> colorSideOne = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> colorSideTwo = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> colorSideThree = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> colorSideFour = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> colorSideFive = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> colorSideSix = new Vector<ColorsOfCube>();
		
		List<Integer> faceOne = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8);
		List<Integer> faceTwo = Arrays.asList(2, 5, 8, 11, 14, 17, 20, 23, 26);
		List<Integer> faceThree = Arrays.asList(18, 19, 20, 21, 22, 23, 24, 25, 26);
		List<Integer> faceFour = Arrays.asList(0, 3, 6, 9, 12, 15, 18, 21, 24);
		List<Integer> faceFive =  Arrays.asList(0, 1, 2, 9, 10, 11, 18, 19, 20);
		List<Integer> faceSix =  Arrays.asList(6, 7, 8, 15, 16, 17, 24, 25, 26);
		
		for(int i = 0; i < inputCubes.size(); i++) {
			IndividualCubeOnRubiks indi = inputCubes.get(i);
			if(faceOne.contains(i)){
				colorSideOne.add(indi.getColorForFace(1));
			} if( faceTwo.contains(i) ){
				colorSideTwo.add(indi.getColorForFace(2));
			} if( faceThree.contains(i) ){
				colorSideThree.add(indi.getColorForFace(3));
			} if( faceFour.contains(i) ){
					colorSideFour.add(indi.getColorForFace(4));
			} if( faceFive.contains(i) ){
				colorSideFive.add(indi.getColorForFace(5));
			} if( faceSix.contains(i) ){
				colorSideSix.add(indi.getColorForFace(6));
			}
		}
		Vector<Vector<ColorsOfCube>> allColors = new Vector<Vector<ColorsOfCube>>();
		allColors.add(colorSideOne);
		allColors.add(colorSideTwo);
		allColors.add(colorSideThree);
		allColors.add(colorSideFour);
		allColors.add(colorSideFive);
		allColors.add(colorSideSix);
		return allColors;
	}
}