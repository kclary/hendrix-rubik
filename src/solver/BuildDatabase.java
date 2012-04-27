package solver;

import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import cube.PaintButtons;

public class BuildDatabase {
	
	DatabaseCommandsForRubik database;
	PaintButtons GUI;
	int depthLimit;
	boolean SearcherWorking;
	StateOfColors firstState;
	ArrayBlockingQueue<StateOfColors> positionsToFindSolutions;
	ColorChanger changer;
	
	public BuildDatabase(PaintButtons GUI, int depthLimit, StateOfColors first) {
		firstState = first;
		database = new DatabaseCommandsForRubik();
		this.GUI = GUI;
		this.depthLimit = depthLimit;
	}
	
	public void initiateSolutions() {
		positionsToFindSolutions = new ArrayBlockingQueue<StateOfColors>(100000);
		positionsToFindSolutions.add(firstState);
		changer = new ColorChanger();
		SearcherWorking = false;
	}
	
	public void buildSolutionsTable() {
		while(positionsToFindSolutions.size() > 0) {
			StateOfColors curState = positionsToFindSolutions.poll();
			if(curState.getDepth() < depthLimit){
				Vector<StateOfColors> futureColors = changer.getSuccessorStates(curState);
				for(int i = 0; i < futureColors.size(); i++) {
					if( i%2 == 0 ) {
						StateOfColors color = futureColors.get(i);
						color.setAsRandomGenerated();
						positionsToFindSolutions.add(color);
					}
				}
			}
			FaceColorsSearcher searcher = new FaceColorsSearcher(GUI, curState);
			searcher.setToBuildingMode();
			searcher.run();
		}
	}
	
	public void setSearcherToNotWorking() {
		SearcherWorking = false;
	}
}