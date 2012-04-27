package solver;
import java.util.EnumMap;
import java.util.Vector;

public class CubeRecognizer {
	HeuristicsForRubik heuristics;
	
	public CubeRecognizer() {
		heuristics = new HeuristicsForRubik();
	}
	
	public TypeOfCube getCubeState(StateOfColors colorOrientation) {
		TypeOfCube cubeType;
		if(!topCrossFinished(colorOrientation)) {
			cubeType = TypeOfCube.topCrossNotSolved;
		} else if(!allCrossesFinished(colorOrientation)) {
			cubeType = TypeOfCube.allCrossesNotSolved;
		} else {
			cubeType = TypeOfCube.cubeNotSolved;
		}
		return cubeType;
	}
	
	public boolean topCrossFinished(StateOfColors colors) {
		Vector<ColorsOfCube> vectorOfColors = colors.getColorVector();
		Vector<Vector<ColorsOfCube>> colorsOnSides = heuristics.makeArraysOfColorsOnSides(vectorOfColors);
		Vector<ColorsOfCube> colorsOnFirstSide = colorsOnSides.get(0);
		ColorsOfCube colorOnTopSideTopPosition = colorsOnFirstSide.get(1);
		for(int i = 0; i < colorsOnFirstSide.size(); i++) {
			if(i == 1 || i == 3 || i == 4 || i ==5 || i == 7) {
				ColorsOfCube curColor = colorsOnFirstSide.get(i);
				if(curColor != colorOnTopSideTopPosition){
					return false;
				}
			}	
		}
		return true;
	}
	
	public boolean allCrossesFinished(StateOfColors colors) {
		Vector<ColorsOfCube> vectorOfColors = colors.getColorVector();
		Vector<Vector<ColorsOfCube>> colorsOnSides = heuristics.makeArraysOfColorsOnSides(vectorOfColors);
		for(int side = 0; side < colorsOnSides.size(); side++) {	
			Vector<ColorsOfCube> colorsOnCurrentSide = colorsOnSides.get(side);
			ColorsOfCube colorOnTopSideTopPosition = colorsOnCurrentSide.get(4);
			for(int i = 0; i < colorsOnCurrentSide.size(); i++) {
				if(i == 1 || i == 3 || i == 4 || i ==5 || i == 7) {
					ColorsOfCube curColor = colorsOnCurrentSide.get(i);
					if(curColor != colorOnTopSideTopPosition){
						return false;
					}
				}	
			}
		}
		return true;
	}
	
	public boolean colorCheckCompletedRubixCube(StateOfColors colors, FacesOfTheCube faces) {
		EnumMap<CubeFaces, Vector<Integer>> map = faces.getEnumMap();
		Vector<ColorsOfCube> vectorOfCubeColors = colors.getColorVector();
		for(CubeFaces face : CubeFaces.values() ) {
			Vector<Integer> indicesForFace = map.get(face);
			Integer firstIndexForSide = indicesForFace.get(0);
			ColorsOfCube firstColor = vectorOfCubeColors.get(firstIndexForSide);
			for(Integer index : indicesForFace) {
				ColorsOfCube curColor = vectorOfCubeColors.get(index);
				if(curColor != firstColor) {
					return false;
				}
			}
		}
		return true;
	}
}