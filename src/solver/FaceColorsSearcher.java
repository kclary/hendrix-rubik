package solver;

import cube.Direction;
import cube.Update;
import cube.PaintButtons;
import java.awt.Color;
import java.awt.dnd.Autoscroll;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.PriorityBlockingQueue;

import javax.swing.JOptionPane;

public class FaceColorsSearcher extends Thread {
	PaintButtons GUI;
	Vector<String> instructionsToSolve;
	String solutionsTable;
	StateOfColors startingState;
	HeuristicsForRubik heuristics;
	TypeOfCube cubeType;
	DatabaseCommandsForRubik databaseCommands;
	boolean solveTheCubeAutomatically;
	CubeRecognizer recognizer;
	FacesOfTheCube faces;
	boolean buildingMode;
	ColorChanger changer;
	
	public FaceColorsSearcher(PaintButtons Gui, StateOfColors startingStateInput) {
		GUI = Gui;
		solutionsTable = "solutions";
		startingState = startingStateInput;
		heuristics = new HeuristicsForRubik();
		cubeType = TypeOfCube.cubeNotSolved;
		databaseCommands = new DatabaseCommandsForRubik();
		solveTheCubeAutomatically = false;
		recognizer = new CubeRecognizer();
		buildingMode = false;
		
	}
	
	public void setCubeType(TypeOfCube inputType) {
		cubeType = inputType;
	}
	
	public void setSolveCubeAutomatically() {
		solveTheCubeAutomatically = true;
	}
	
	public void setToBuildingMode() {
		buildingMode = true;
	}
	
	public void run() {
		PriorityBlockingQueue<StateOfColors> queue = new PriorityBlockingQueue<StateOfColors>();
		HashSet<String> previousStates = new HashSet<String>();
		
		changer = new ColorChanger();
		startingState.Depth = 0;
		queue.add(startingState);
		
		faces = new FacesOfTheCube();
		Vector<Update> solutionWithoutDatabase = new Vector<Update>();
		Vector<Update> updatesToPaintButtons = new Vector<Update>();
		
		boolean blockSolved = recognizer.colorCheckCompletedRubixCube(startingState, faces);
		int statesAdded = 0;
		
		int depthLimit = 4;
		double highestHeurValue = 0;
		StateOfColors bestStateAtDepthLimit = startingState;
		
		Vector<StateOfColors> statesFromCurrentToGoal = new Vector<StateOfColors>();
		updatesToPaintButtons = new Vector<Update>();
		
		while( !blockSolved ) {
			if(queue.size() > 0) {
				StateOfColors curStateChecking = queue.poll();
				String curID = curStateChecking.getID();
				if( previousStates.contains(curID) ) {
					continue;
				}
				else if ( recognizer.colorCheckCompletedRubixCube(curStateChecking, faces) ) {
					statesFromCurrentToGoal = getPreviousStates(curStateChecking);
					solutionWithoutDatabase = setupUpdatesFromSolutionPath(statesFromCurrentToGoal);
					if(solveTheCubeAutomatically ) {
						addAllUpdates( solutionWithoutDatabase );
					}
					databaseCommands.addAllSolutionsToDatabase(statesFromCurrentToGoal, solutionWithoutDatabase);
					
					if(recognizer.colorCheckCompletedRubixCube(curStateChecking, faces)) {
						Vector<StateOfColors> statesFromCurrentToGoalShorter = new Vector<StateOfColors>();
						Vector<Update> solutionUpdatesShorter = new Vector<Update>();
						if(statesFromCurrentToGoal.size() > 3) {
							for(int i = solutionWithoutDatabase.size()-3; i < solutionWithoutDatabase.size(); i++) {
								Update curUpdateToAdd = solutionWithoutDatabase.get(i);
								StateOfColors curColors = statesFromCurrentToGoal.get(i);
								solutionUpdatesShorter.add(curUpdateToAdd);
								statesFromCurrentToGoalShorter.add(curColors);
							}
							databaseCommands.addAllSolutionsToDatabase(statesFromCurrentToGoalShorter, solutionUpdatesShorter);
						} else {
							databaseCommands.addAllSolutionsToDatabase(statesFromCurrentToGoal, solutionWithoutDatabase);
						}
					}
					blockSolved = true;
					updatesToPaintButtons = solutionWithoutDatabase;
				} else if( databaseCommands.databaseContainsID(curID) ) {
					Vector<StateOfColors> statesFromCurrentToGoalDatabase = getPreviousStates(curStateChecking);					
					solutionWithoutDatabase = setupUpdatesFromSolutionPath(statesFromCurrentToGoalDatabase);
					Vector<Update> UpdatesFromDatabase = databaseCommands.getEndingUpdatesFromDatabase(curID);
					
					updatesToPaintButtons = solutionWithoutDatabase;
					updatesToPaintButtons.addAll(UpdatesFromDatabase);
					Vector<StateOfColors> databaseStates = makeDatabaseStates(updatesToPaintButtons);
					
					if(solveTheCubeAutomatically ) {
						addAllUpdates(updatesToPaintButtons);
					}
					databaseCommands.addAllSolutionsToDatabase(databaseStates, updatesToPaintButtons);
					blockSolved = true;
				} else {
					if(curStateChecking.getDepth() == depthLimit) {
						double heurValue = curStateChecking.getHeuristicValue();
						if(heurValue > highestHeurValue) {
							highestHeurValue = heurValue;
							bestStateAtDepthLimit = curStateChecking;
						}
						continue;
					}
					Vector<StateOfColors> potentialPositions = changer.getSuccessorStates(curStateChecking);
					for( int j = 0; j < potentialPositions.size(); j++ ){
						StateOfColors potentialState = potentialPositions.get(j);
						
						
						double heur;
						if(cubeType == TypeOfCube.topCrossNotSolved){
							heur = heuristics.heurOfAllTop(curStateChecking);
						} else if(cubeType== TypeOfCube.allCrossesNotSolved) {
							heur = heuristics.heurCrossesFinished(potentialState);
						} else {
							heur = heuristics.heurAllFinishedRows(potentialState);
						}
						potentialState.setHeuristicValue(heur);
						
						statesAdded++;
						
						int depthOfPotentialState = potentialState.getDepth();
						if( ! ( depthOfPotentialState > depthLimit) ) {
							queue.add(potentialState);
						}
					}
					previousStates.add(curStateChecking.getID());
				}
			} else {
				statesFromCurrentToGoal = getPreviousStates(bestStateAtDepthLimit);
				updatesToPaintButtons = setupUpdatesFromSolutionPath(statesFromCurrentToGoal);
				JOptionPane.showMessageDialog(GUI, "Did not find a solution");
				break;
			}
		}
		if(buildingMode){
		} else {
			if(!solveTheCubeAutomatically) { 
			sendUpdatesToInstructionsPanel(updatesToPaintButtons);			
			} 
		}
	}
	
	private Vector<StateOfColors> makeDatabaseStates(Vector<Update> updates) {
		Vector<StateOfColors> colors = new Vector<StateOfColors>();
		colors.add(startingState);
		StateOfColors curState = startingState;
		for(Update update : updates) {
			int index0to2 = update.getRow()-1;
			StateOfColors stateMade = makeNextStateWithUpdate(curState, new Update( index0to2, update.getD(), update.getDir()));
			colors.add(stateMade);
			curState = stateMade;
		}
		return colors;
	}
	
	private StateOfColors makeNextStateWithUpdate(StateOfColors color, Update update) {
		int index = update.getRow();
		cube.Direction direction = update.getDir();
		int dir = update.getD();
		StateOfColors newState;
		if(direction == Direction.Y) {
			if(dir == 1) {
				newState = changer.rowLeftNewState(color, index);
			} else {
				newState = changer.rowRightNewState(color, index);
			}
		} else {
			if(dir == 1) {
				newState = changer.colUpNewState(color, index);
			} else {
				newState = changer.colDownNewState(color, index);
			}
		}
		return newState;
	}
	
	private void addAllUpdates(Vector<Update> updates) {
		for(Update curUpdate : updates ) {
			if(curUpdate.getDir() == Direction.Y) {
				GUI.rotateRow(curUpdate.getRow(), curUpdate.getD());	
			} else {
				GUI.rotateCol(curUpdate.getRow(), curUpdate.getD());
			}
		}
	}
	
	private boolean typeOfCubeFinished(TypeOfCube cubeType, StateOfColors colors) {
		if(cubeType == TypeOfCube.topCrossNotSolved) {
			return recognizer.topCrossFinished(colors);
		} else if(cubeType == TypeOfCube.allCrossesNotSolved) {
			return recognizer.allCrossesFinished(colors);
		} else if(cubeType == TypeOfCube.cubeNotSolved) {
			return recognizer.colorCheckCompletedRubixCube(colors, faces);
		} else {
			return false;
		}
	}
	
	private void sendUpdatesToInstructionsPanel(Vector<Update> updates) {
		GUI.getInstruction().appendHints(updates);
	}
	
	private Vector<Update> setupUpdates(StateOfColors finishedState) {
		Vector<StateOfColors> statesFromCurrentToGoal = getPreviousStates(finishedState);
		Vector<Update> solutions = setupUpdatesFromSolutionPath(statesFromCurrentToGoal);
		return solutions;
	}
	
	private void printUpdates(Vector<Update> updates) {
		for(int i = 0; i < updates.size(); i++) {
			Update update = updates.get(i);
			int row = update.getRow();
			int direction = update.getD();
			Direction xYDirection = update.getDir();
			String movement;
			if(xYDirection == Direction.Y){
				movement = " row movement ";
			} else if(xYDirection == Direction.Z) {
				movement = " column movement ";
			} else {
				movement = "unknown";
			}
		}
	}

	private void printCubeVisual(StateOfColors colors) {
		Vector<ColorsOfCube> vector = colors.getColorVector();
		for(int i = 0 ; i < vector.size(); i++) {
			if(i%3 == 0 ) {
				System.out.print("\t");
				if(i<9 || i > 44) System.out.print("\t\t");				
			}
			System.out.print(vector.get(i));
			if(i==2|| i==5||i==8 || i == 20|| i == 32|| i == 44||i==47||i==50||i==53){
				System.out.println();
			}
		}
	}
	
	private Vector<StateOfColors> getPreviousStates(StateOfColors backtrackingState) {
		Vector<StateOfColors> allPreviousStates = new Vector<StateOfColors>();
		int statesAdded = 0;
		StateOfColors pastState = backtrackingState;
		
		boolean foundStartingState = false;
		while( !foundStartingState ) {
			TypeOfMovement type = pastState.getMovementType();
			if( type == TypeOfMovement.noMovement) {
				break;
			}
			else if( type == TypeOfMovement.randomMovement || type == TypeOfMovement.userMovement) {
				foundStartingState = true;
			}
			statesAdded++;
			allPreviousStates.add(pastState);
			pastState = pastState.getPreviousState();
		}
		Vector<StateOfColors> allPreviousStatesEndingLast = reverseVector(allPreviousStates);
		return allPreviousStatesEndingLast;
	}
	
	private Vector<StateOfColors> reverseVector(Vector<StateOfColors> backwardColors) {
		Vector<StateOfColors> colorsVector = new Vector<StateOfColors>();
		for(int i = 0; i < backwardColors.size(); i++) {
			StateOfColors curColor = backwardColors.get(backwardColors.size()-i-1);
			colorsVector.add(curColor);
		}
		return colorsVector;
	}
	
	public void printPreviousStatesAndMovements(Vector<StateOfColors> pastColors) {
		for(StateOfColors color : pastColors) {
			printCubeVisual(color);
			MovementsOfCube movement = color.getMovementToThisState();
			int IndexChanged = color.getIndexChanged();
			System.out.println(movement);
			System.out.println(IndexChanged);
		}
	}
	
	private Vector<Update> setupUpdatesFromSolutionPath(Vector<StateOfColors> previousStates) {
		Vector<Update> updates = new Vector<Update>();
		for(int i = 1; i < previousStates.size(); i++) {
			StateOfColors stateAfter = previousStates.get(i);
			Update curUpdate = makeUpdate(stateAfter);
			updates.add(curUpdate);
		}
		return updates;
	}
	
	private Update makeUpdate(StateOfColors after) {
		MovementsOfCube movementToThisState = after.getMovementToThisState();
		int indexChanged = after.getIndexChanged() + 1;
		
		Direction direction;
		int d;
		if(movementToThisState == MovementsOfCube.leftMovement) {
			direction = Direction.Y;
			d = 1;
		} else if(movementToThisState == MovementsOfCube.rightMovement) {
			direction = Direction.Y;
			d = -1;
		} else if(movementToThisState == MovementsOfCube.upwardMovement) {
			direction = Direction.Z;
			d = 1;
		} else if(movementToThisState == MovementsOfCube.downwardMovement) {
			direction = Direction.Z;
			d = -1;
		} else {
			direction = Direction.X;
			d = 0;
			System.exit(0);
		}
		Update curUpdate = new Update(indexChanged, d, direction);
		return curUpdate;
	}
}