package cube;

public class Rotation {
	int[] corners;
	int[] edges;
	
	public Rotation(int[] index1, int[] index2) { 
		corners = index1;
		edges = index2;
	}
	
	public int direct(int i, int d) { 
		if(d == 1) { 
			return getNext(i);
		}
		else { 
			return getPrev(i);
		}
	}
	
	public int getNext(int i) { 
		for(int j = 0; j < corners.length; j++) { 
			if(corners[j] == i) { 
				if(j == corners.length - 1) { return corners[0]; }
				else { return corners[j + 1]; } 
			}
		}
		
		for(int j = 0; j < edges.length; j++) { 
			if(edges[j] == i) { 
				if(j == edges.length - 1) { return edges[0]; }
				else { return edges[j + 1]; }
			}
		}
		
		return -1;
	}
	
	public int getPrev(int i) { 
		for(int j = 0; j < corners.length; j++) { 
			if(corners[j] == i) { 
				if(j == 0) { return corners[corners.length - 1]; }
				else { return corners[j - 1]; } 
			}
		}
		
		for(int j = 0; j < edges.length; j++) { 
			if(edges[j] == i) { 
				if(j == 0) { return edges[edges.length - 1]; }
				else { return edges[j - 1]; }
			}
		}
		
		return -1;
	}
	
	public void print() { 
		for(int j : corners) { System.out.print(j + " "); }
		for(int j: edges) { System.out.print(j + " "); }
		System.out.println();
	}
}
