package solver;
import java.util.Vector;


public class HeuristicsForRubik {
	public HeuristicsForRubik() {
		
	}
	
	public Vector<Vector<ColorsOfCube>> makeArraysOfColorsOnSides( Vector<ColorsOfCube> colorsInput ) {
		Vector<Vector<ColorsOfCube>> colorsOnFaces = new Vector<Vector<ColorsOfCube>>();
		Vector<ColorsOfCube> faceOne = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> faceTwo = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> faceThree = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> faceFour = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> faceFive = new Vector<ColorsOfCube>();
		Vector<ColorsOfCube> faceSix = new Vector<ColorsOfCube>();
		int tempI;
		for(int i = 0 ; i < 54; i++){
			if(i < 9) {
				faceOne.add(colorsInput.get(i));
			}
			if(i >= 9 && i <= 44) {
				if(i > 32) tempI = i - 24;
				else if(i > 20) tempI = i - 12;
				else tempI = i;
				if(tempI <= 11 ) faceTwo.add(colorsInput.get(i));
				else if(tempI >= 12 && tempI <= 14) faceThree.add(colorsInput.get(i));
				else if(tempI >= 15 && tempI <= 17) faceFour.add(colorsInput.get(i));
				else if(tempI >= 18 && tempI <= 20) faceFive.add(colorsInput.get(i));
			}
			if(i >= 45) {
				faceSix.add(colorsInput.get(i));
			}
		}
		colorsOnFaces.add(faceOne);
		colorsOnFaces.add(faceTwo);
		colorsOnFaces.add(faceThree);
		colorsOnFaces.add(faceFour);
		colorsOnFaces.add(faceFive);
		colorsOnFaces.add(faceSix);
		
		return colorsOnFaces;
	}
	
	public int heurOfTopCross(StateOfColors colors) {
		Vector<ColorsOfCube> vectorOfColors = colors.getColorVector();
		Vector<Vector<ColorsOfCube>> colorsOnSides = makeArraysOfColorsOnSides(vectorOfColors);
		int placesOnTopCrossComplete = 0;
		
		Vector<ColorsOfCube> colorsOnTopSide = colorsOnSides.get(1);
		//System.out.println("colors on the top side are : " + colorsOnTopSide);
		ColorsOfCube colorOnTopSideTopPosition = colorsOnTopSide.get(4);
		for(int i = 0; i < colorsOnTopSide.size(); i++) {
			if(i == 1 || i == 3 || i == 4 || i ==5 || i == 7) {
				ColorsOfCube curColor = colorsOnTopSide.get(i);
				if(curColor == colorOnTopSideTopPosition) {
					placesOnTopCrossComplete++;
				}
			}
		}
		int oneHundredPercent = placesOnTopCrossComplete*100/5;
		return oneHundredPercent;
	}
	
	public int heurCrossesFinished(StateOfColors colors) {
		Vector<ColorsOfCube> vectorOfColors = colors.getColorVector();
		Vector<Vector<ColorsOfCube>> colorsOnSides = makeArraysOfColorsOnSides(vectorOfColors);
		int placesInCrossesComplete = 0;
		for(int side = 0; side < colorsOnSides.size(); side++) {	
			boolean completedCross = true;
			Vector<ColorsOfCube> colorsOnCurrentSide = colorsOnSides.get(side);
			ColorsOfCube colorOnTopSideTopPosition = colorsOnCurrentSide.get(4);
			for(int i = 0; i < colorsOnCurrentSide.size(); i++) {
				if(i == 1 || i == 3 || i == 4 || i ==5 || i == 7) {
					ColorsOfCube curColor = colorsOnCurrentSide.get(i);
					if(curColor != colorOnTopSideTopPosition) {
						completedCross = false;
						break;
					}
				}
			}
			if(completedCross){
				placesInCrossesComplete += 5;
			}
		}
		int oneHundredPercent = placesInCrossesComplete*100/30;
		return oneHundredPercent;
	}

	public int partnerColors(StateOfColors colors) {
		Vector<ColorsOfCube> vectorOfTheColors = colors.getColorVector();
		Vector<Vector<ColorsOfCube>> allColors = makeArraysOfColorsOnSides(vectorOfTheColors);
		int heur = colorsThatHavePartnerColor(allColors);
		return heur;
	}
	
	private int colorsThatHavePartnerColor(Vector<Vector<ColorsOfCube>> allColors) {
		int colorsNextToOneAnother = 0;
		for(int i = 0; i < allColors.size(); i++) {
			Vector<ColorsOfCube> colorsOneSide = allColors.get(i);
			int colorsTheHavePartners = colorsNextToOneAnotherOneSide(colorsOneSide);
			colorsNextToOneAnother += colorsTheHavePartners;
		}
		return colorsNextToOneAnother;
	}
	
	
	private int colorsNextToOneAnotherOneSide( Vector<ColorsOfCube> colorsOnOneSide ) {
		int sameColorOnSide = 0;
		for(int i = 0; i< colorsOnOneSide.size(); i++) {
			ColorsOfCube thisColor = colorsOnOneSide.get(i);
			int left = -1;
			int right = -1;
			int down = -1;
			int up = -1;
			left = i-1;
			right = i+1 ;
			up = i+3;
			down = i-3;
			int[] places = {left, right, up, down};
			for(int p : places) if(p>=0 && p<=8){
				if(thisColor == colorsOnOneSide.get(p)){
					sameColorOnSide++;
					break;
				}
			}
		}
		return sameColorOnSide;
	}
	
	public int heurAllFinishedRows(StateOfColors colors) {
		Vector<ColorsOfCube> vectorOfTheColors = colors.getColorVector();
		Vector<Vector<ColorsOfCube>> allColors = makeArraysOfColorsOnSides(vectorOfTheColors);
		int rowsfinished = heurFinishedRows(allColors);
		int oneHundredPercent = rowsfinished*100/36;
		return oneHundredPercent;
	}
	
	private int heurFinishedRows( Vector<Vector<ColorsOfCube>> startingColors ) {
		int totalFinishedRows = 0;
		int rowsFinishedOnOneSide = 0;
		for(Vector<ColorsOfCube> side : startingColors) {
			rowsFinishedOnOneSide = getFinishedRowsOnOneSide(side);
			totalFinishedRows += rowsFinishedOnOneSide;
		}
		return totalFinishedRows;
	}
	
	private int getFinishedRowsOnOneSide(Vector<ColorsOfCube> side ) {
		int rowsOnOneSide = 0;
		for(int i = 0; i < 9; i ++) {
			if(i < 3) {
				ColorsOfCube posOneVert = side.get(i);
				ColorsOfCube posTwoVert = side.get(i+3);
				ColorsOfCube posThreeVert = side.get(i+6);
				if(posOneVert == posTwoVert && posTwoVert == posThreeVert) {
					rowsOnOneSide++;
				}
			}
			if(i % 3 == 0) {
				ColorsOfCube posOneHorizontal = side.get(i);
				ColorsOfCube posTwoHorizontal = side.get(i+1);
				ColorsOfCube posThreeHorizontal = side.get(i+2);
				if(posOneHorizontal == posTwoHorizontal && posTwoHorizontal == posThreeHorizontal) {
					rowsOnOneSide++;
				}
			}
		}
		return rowsOnOneSide;
	}
	
	/*
	public int heurOfAllTop(StateOfColors colors) {
		Vector<ColorsOfCube> vectorOfTheColors = colors.getColorVector();
		int heurTopLayers = heurTopLayer(vectorOfTheColors);
		//oneHundredPercent
		return heurTopLayers;
	}
	
	private int heurCorners(Vector<ColorsOfCube> colorsOnCube) {
		int[] sideOneCrosses = {1, 3, 4, 5, 7};
		int[] sideTwoCrosses = {10, 21, 22, 23, 34};
		int[] sideThreeCrosses = {13, 24, 25, 26, 37};
		int[] sideFourCrosses = {16, 27, 28, 29, 40};
		int[] sideFiveCrosses = {19, 30, 31, 32, 43};
		int[] sideSixCrosses = {46, 48, 49, 50, 52};
		
		int[][] allSides = {sideOneCrosses, sideTwoCrosses, sideThreeCrosses, sideFourCrosses, sideFiveCrosses, sideSixCrosses};
		
		for(int[] side : allSides) {
			getSimilarColorsOnSide();
		}
		return topLayer;
	}
	*/
	public int heurOfAllTop(StateOfColors colors) {
		Vector<ColorsOfCube> vectorOfTheColors = colors.getColorVector();
		int heurTopLayers = heurTopLayer(vectorOfTheColors);
		//oneHundredPercent
		return heurTopLayers;
	}
	
	private int heurTopLayer(Vector<ColorsOfCube> colorsOnCube) {
		int topLayer = 0;
		ColorsOfCube pos13 = colorsOnCube.get(12);
		ColorsOfCube pos24 = colorsOnCube.get(12);
		ColorsOfCube pos25 = colorsOnCube.get(12);
		ColorsOfCube pos26 = colorsOnCube.get(12);
		ColorsOfCube pos37 = colorsOnCube.get(12);
		ColorsOfCube[] colors = {pos13, pos24, pos25, pos26, pos37};
		for( ColorsOfCube color : colors ) {
			if( color == pos25 ) {
				topLayer++;
			}
		}
		return topLayer;
	}
	
	private int getTopFinished(Vector<ColorsOfCube> side ) {
		int rowsOnOneSide = 0;
		for(int i = 0; i < 9; i ++) {
			if(i < 3) {
				ColorsOfCube posOneVert = side.get(i);
				ColorsOfCube posTwoVert = side.get(i+3);
				ColorsOfCube posThreeVert = side.get(i+6);
				if(posOneVert == posTwoVert && posTwoVert == posThreeVert) {
					rowsOnOneSide++;
				}
			}
			if(i % 3 == 0) {
				ColorsOfCube posOneHorizontal = side.get(i);
				ColorsOfCube posTwoHorizontal = side.get(i+1);
				ColorsOfCube posThreeHorizontal = side.get(i+2);
				if(posOneHorizontal == posTwoHorizontal && posTwoHorizontal == posThreeHorizontal) {
					rowsOnOneSide++;
				}
			}
		}
		return rowsOnOneSide;
	}
}