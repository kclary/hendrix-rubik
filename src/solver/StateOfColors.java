package solver;
import java.util.Vector;


public class StateOfColors implements Comparable<StateOfColors> {
	Vector<ColorsOfCube> colorPositionsForState;
	String stateID;
	double heuristicValue;
	StateOfColors previousState;
	MovementsOfCube movementToThisState;
	TypeOfMovement movementType;
	int rowChangeToThisState;
	int Depth;
	
	public StateOfColors(Vector<ColorsOfCube> colorsOnTheCube, int DepthInput) {
		colorPositionsForState = colorsOnTheCube;
		makeID(colorsOnTheCube);
		previousState = null;
		movementType = TypeOfMovement.noMovement;
		Depth = DepthInput;
	}
	
	public StateOfColors(Vector<ColorsOfCube> colorsOnTheCube, StateOfColors pastState, MovementsOfCube movementAppliedToGetToNewState, int DepthInput) {
		colorPositionsForState = colorsOnTheCube;
		makeID(colorsOnTheCube);
		previousState = pastState;
		movementToThisState = movementAppliedToGetToNewState;
		movementType = TypeOfMovement.notRandomMovement;
		Depth = DepthInput;
	}
	
	public String makeID(Vector<ColorsOfCube> colorsOnTheCube) {
		String ID = "";
		StringBuilder tempBuilder = new StringBuilder(ID);
		for(ColorsOfCube color: colorsOnTheCube){
			tempBuilder.append(color);
		}
		stateID = tempBuilder.toString();
		return stateID;
	}
	
	public void setDepth(int newDepth ) {
		Depth = newDepth;
	}
	
	public int getDepth() {
		return Depth;
	}
	
	public void setIndexChanged(int index) {
		rowChangeToThisState = index;
	}
	
	public int getIndexChanged() {
		return rowChangeToThisState;
	}
	
	public String getID() {
		return stateID;
	}
	
	public void setAsRandomGenerated() {
		movementType = TypeOfMovement.randomMovement;
	}
	
	public void setAsUserGenerated() {
		movementType = TypeOfMovement.userMovement;
	}
	
	public TypeOfMovement getMovementType() {
		return movementType;
	}
	
	public Vector<ColorsOfCube> getColorVector() {
		return colorPositionsForState;
	}
	
	public void setHeuristicValue(double value) {
		heuristicValue = value;
	}
	
	public double getHeuristicValue() {
		return heuristicValue;
	}
	
	public MovementsOfCube getMovementToThisState() {
		return movementToThisState;
	}
	
	public StateOfColors getPreviousState() {
		return previousState;
	}
	
	@Override
	public int compareTo(StateOfColors o) {
		if(o.getHeuristicValue() > this.heuristicValue) {
			return 1;
		}
		else {
			return -1;
		}
	}
}