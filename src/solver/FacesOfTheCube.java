package solver;
import java.util.EnumMap;

import java.util.Vector;
public class FacesOfTheCube {
	EnumMap<CubeFaces, Vector<Integer>> faceIndexesMap;
	
	public FacesOfTheCube() {
		int[] face1Indexes = {0, 1, 2, 3, 4, 5, 6, 7, 8};
		int[] face2Indexes = {9, 10, 11, 21, 22, 23, 33, 34, 35};
		int[] face3Indexes = {12, 13, 14, 24, 25, 26, 36, 37, 38};
		int[] face4Indexes = {15, 16, 17, 27, 28, 29, 39, 40, 41};
		int[] face5Indexes = {18, 19, 20, 30, 31, 32, 42, 43, 44};
		int[] face6Indexes = {45, 46, 47, 48, 49, 50, 51, 52, 53};
		Vector<Integer> face1Ind = new Vector<Integer>();
		Vector<Integer> face2Ind = new Vector<Integer>();
		Vector<Integer> face3Ind = new Vector<Integer>();
		Vector<Integer> face4Ind = new Vector<Integer>();
		Vector<Integer> face5Ind = new Vector<Integer>();
		Vector<Integer> face6Ind = new Vector<Integer>();
		
		for(int i = 0; i < face1Indexes.length; i++) {
			face1Ind.add(face1Indexes[i]);
			face2Ind.add(face2Indexes[i]);
			face3Ind.add(face3Indexes[i]);
			face4Ind.add(face4Indexes[i]);
			face5Ind.add(face5Indexes[i]);
			face6Ind.add(face6Indexes[i]);
		}
		
		faceIndexesMap = new EnumMap<CubeFaces, Vector<Integer>>(CubeFaces.class);
	    faceIndexesMap.put(CubeFaces.Face1, face1Ind);
	    faceIndexesMap.put(CubeFaces.Face2, face2Ind);
	    faceIndexesMap.put(CubeFaces.Face3, face3Ind);
	    faceIndexesMap.put(CubeFaces.Face4, face4Ind);
	    faceIndexesMap.put(CubeFaces.Face5, face5Ind);
	    faceIndexesMap.put(CubeFaces.Face6, face6Ind);	
	}
	
	public EnumMap<CubeFaces, Vector<Integer>> getEnumMap() {
		return faceIndexesMap;
	}
}