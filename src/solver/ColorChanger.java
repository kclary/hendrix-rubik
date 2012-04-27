package solver;

import cube.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class ColorChanger {
	
	public ColorChanger() { }
	
	public Vector<StateOfColors> getSuccessorStates(StateOfColors pastState) {
		Vector<StateOfColors> colorsOnCube = new Vector<StateOfColors>();
		for(int i = 0; i < 3; i++) {
			StateOfColors rowLeftState = rowLeftNewState(pastState, i);
			colorsOnCube.add(rowLeftState);
			
			StateOfColors rowRightState = rowRightNewState(pastState, i);
			colorsOnCube.add(rowRightState);
			
			StateOfColors colDownState = colDownNewState(pastState, i);
			colorsOnCube.add(colDownState);
		
			StateOfColors colUpState = colUpNewState(pastState, i);
			colorsOnCube.add(colUpState);
		}
		return colorsOnCube;
	}
	
	public StateOfColors rowLeftNewState(StateOfColors pastState, int row) {
		Vector<ColorsOfCube> colors = pastState.getColorVector();
		Vector<ColorsOfCube> newColors = new Vector<ColorsOfCube>();
		for(ColorsOfCube color : colors) {
			newColors.add(color);
		}
		if(row == 3) {System.exit(0);}
		int firstPosition = 9 + 12 * row;
		int lastPosition = 20 + 12 * row;
		int newPos;
		for(int pos = firstPosition; pos < lastPosition+1; pos++) {
			newPos = pos - 3;
			if(newPos < firstPosition) {
				newPos = newPos += 12;
			}
			
			newColors.set(newPos, colors.get(pos));
		}
		
		Vector<Integer> rowOneTopSideIndexes = new Vector<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
		Vector<Integer> rowThreeBottomSideIndexes = new Vector<Integer>(Arrays.asList(45, 46, 47, 48, 49, 50, 51, 52, 53));
		
		Vector<Integer> rowOneMovements = new Vector<Integer>(Arrays.asList(2, 4, 6, -2, 0, 2, -6, -4, -2));
		Vector<Integer> rowThreeMovements = new Vector<Integer>(Arrays.asList(6, 2, -2, 4, 0, -4, 2, -2, -6));
		
		if(row == 0) {
			newColors = newColorArrayFromMovements(newColors, rowOneTopSideIndexes, rowOneMovements);
		} else if(row == 2) {
			newColors = newColorArrayFromMovements(newColors, rowThreeBottomSideIndexes, rowThreeMovements);
		}
		StateOfColors newState = setupNewState(newColors, pastState, MovementsOfCube.leftMovement, row);
		return newState;
	}
	
	public StateOfColors rowRightNewState(StateOfColors pastState, int row) {
		Vector<ColorsOfCube> colors = pastState.getColorVector();
		Vector<ColorsOfCube> newColors = new Vector<ColorsOfCube>();
		for(ColorsOfCube color : colors) {
			newColors.add(color);
		}
		int firstPosition = 9 + 12 * row;
		int lastPosition = 20 + 12 * row;
		int newPos;
		for(int pos = firstPosition; pos < lastPosition+1; pos++) {
			newPos = pos + 3;
			if(newPos > lastPosition) {
				newPos = newPos -= 12;
			}

			
			newColors.set(newPos, colors.get(pos));
		}
		Vector<Integer> rowOneTopSideIndexes = new Vector<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8));
		Vector<Integer> rowThreeBottomSideIndexes = new Vector<Integer>(Arrays.asList(53, 50, 47, 52, 49, 46, 51, 48, 45));
		Vector<Integer> rowOneMovements = new Vector<Integer>(Arrays.asList(6, 2, -2, 4, 0, -4, 2, -2, -6));

		Vector<Integer> rowThreeMovements = new Vector<Integer>(Arrays.asList(-2, 2, 6, -4, 0, 4, -6, -2, 2));
		if(row == 0) {
			newColors = newColorArrayFromMovements(newColors, rowOneTopSideIndexes, rowOneMovements);
		} else if(row == 2) {
			newColors = newColorArrayFromMovements(newColors, rowThreeBottomSideIndexes, rowThreeMovements);
		}
		StateOfColors newState = setupNewState(newColors, pastState, MovementsOfCube.rightMovement, row);
		return newState;
	}
	
	public StateOfColors colUpNewState(StateOfColors pastState, int col) {

		Vector<Integer> colOneIndexes = new Vector<Integer>(Arrays.asList(0, 3, 6, 12, 24, 36, 45, 48, 51, 44, 32, 20, 9, 10, 11, 21, 22, 23, 33, 34, 35));
		Vector<Integer> colTwoIndexes = new Vector<Integer>(Arrays.asList(1, 4, 7, 13, 25, 37, 46, 49, 52, 43, 31, 19));
		Vector<Integer> colThreeIndexes = new Vector<Integer>(Arrays.asList(2, 5, 8, 14, 26, 38, 47, 50, 53, 42, 30, 18, 15, 16, 17, 27, 28, 29, 39, 40, 41));
		
		Vector<Integer> colOneMovements = new Vector<Integer>(Arrays.asList(44, 29, 14, -12, -21, -30, -33, -24, -15, 1, 16, 31, 24, 11, -2, 13, 0, -13, 2, -11, -24));
		Vector<Integer> colTwoMovements = new Vector<Integer>(Arrays.asList(42, 27, 12, -12, -21, -30, -33, -24, -15, 3, 18, 33));
		Vector<Integer> colThreeMovements = new Vector<Integer>(Arrays.asList(40, 25, 10, -12, -21, -30, -33, -24, -15, 5, 20, 35, 2, 13, 24, -11, 0, 11, -24, -13, -2));
		
		Vector<Integer> indices;
		Vector<Integer> movements;
		if(col == 0 ) {
			indices = colOneIndexes;
			movements = colOneMovements;
		} else if(col == 1 ) {
			indices = colTwoIndexes;
			movements = colTwoMovements;
		} else if(col == 2 ) {
			indices = colThreeIndexes;
			movements = colThreeMovements;
		} else {
			indices = colOneIndexes;
			movements = colOneMovements;
		}
		Vector<ColorsOfCube> newColors = newColorArrayFromMovements(pastState.getColorVector(), indices, movements);
		StateOfColors newState = setupNewState(newColors, pastState, MovementsOfCube.upwardMovement, col);
		return newState;
	}
	
	public StateOfColors colDownNewState(StateOfColors pastState, int col) {

		Vector<Integer> colOneIndexes = new Vector<Integer>(Arrays.asList(0, 3, 6, 12, 24, 36, 45, 48, 51, 44, 32, 20, 9, 10, 11, 21, 22, 23, 33, 34, 35));
		Vector<Integer> colTwoIndexes = new Vector<Integer>(Arrays.asList(1, 4, 7, 13, 25, 37, 46, 49, 52, 43, 31, 19));
		Vector<Integer> colThreeIndexes = new Vector<Integer>(Arrays.asList(2, 5, 8, 14, 26, 38, 47, 50, 53, 42, 30, 18, 15, 16, 17, 27, 28, 29, 39, 40, 41));
		
		Vector<Integer> colOneMovements = new Vector<Integer>(Arrays.asList(12, 21, 30, 33, 24, 15, -1, -16, -31, -44, -29, -14, 2, 13, 24, -11, 0, 11, -24, -13, -2));
		Vector<Integer> colTwoMovements = new Vector<Integer>(Arrays.asList(12, 21, 30, 33, 24, 15, -3, -18, -33, -42, -27, -12));
		Vector<Integer> colThreeMovements = new Vector<Integer>(Arrays.asList(12, 21, 30, 33, 24, 15, -5, -20, -35, -40, -25, -10, 24, 11, -2, 13, 0, -13, 2, -11, -24));
		
		Vector<Integer> indices;
		Vector<Integer> movements;
		if(col == 0 ) {
			indices = colOneIndexes;
			movements = colOneMovements;
		} else if(col == 1 ) {
			indices = colTwoIndexes;
			movements = colTwoMovements;
		} else if(col == 2 ) {
			indices = colThreeIndexes;
			movements = colThreeMovements;
		} else {
			indices = colOneIndexes;
			movements = colOneMovements;
		}
		Vector<ColorsOfCube> newColors = newColorArrayFromMovements(pastState.getColorVector(), indices, movements);
		StateOfColors newState = setupNewState(newColors, pastState, MovementsOfCube.downwardMovement, col);
		return newState;
	}
	
	private Vector<ColorsOfCube> newColorArrayFromMovements(Vector<ColorsOfCube> colors, Vector<Integer> indexes, Vector<Integer> movements) {
		Vector<ColorsOfCube> newColors = new Vector<ColorsOfCube>();
		for(ColorsOfCube color : colors) {
			newColors.add(color);
		}
		int newPos;
		for(int movementNum = 0; movementNum < movements.size(); movementNum++) {
			
			Integer curIndex = indexes.get(movementNum);
			ColorsOfCube colorAtIndex = colors.get(curIndex);
			int movementToNewIndex = movements.get(movementNum);
			newPos = curIndex + movementToNewIndex;
			
			newColors.set(newPos, colorAtIndex);
		}
		return newColors;
	}
	
	private StateOfColors setupNewState(Vector<ColorsOfCube> newColors, StateOfColors pastState, MovementsOfCube pastMovement, int indexChanged){
		StateOfColors newState = new StateOfColors(newColors, pastState, pastMovement, pastState.getDepth()+1);
		newState.setIndexChanged(indexChanged);
		return newState;
	}

	
	public StateOfColors randomState(StateOfColors startingState, int rotations, PaintButtons pb) {
		StateOfColors randomOrientation = startingState;
		Vector<Update> updates = new Vector<Update>();
		for(int i = 0; i < rotations; i++) {
			Random randomGenerator = new Random();
			Float rand = randomGenerator.nextFloat();
			int rowToChange = (int) Math.floor(rand * 3);
			StateOfColors newState;
			if(rand > .5f) {
				newState = colUpNewState(randomOrientation, rowToChange);
		//		pb.rubPane.addUpdate(rowToChange+1, 1, Direction.Y);
			} else {
				newState = rowLeftNewState(randomOrientation, rowToChange);
		//		pb.rubPane.addUpdate(rowToChange+1, 1, Direction.X);
			}
			newState.setAsRandomGenerated();
			randomOrientation = newState;
		}
		return randomOrientation;
	}
}