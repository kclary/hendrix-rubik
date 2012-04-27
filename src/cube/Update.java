package cube;
public class Update {
	int row;
	int d;
	Direction dir;
	
	public Update(int row, int d, Direction dir) { 
		this.row = row;
		this.d = d;
		this.dir = dir;
	}
	
	public int getRow() { 
		return row;
	}
	
	public int getD() { 
		return d;
	}
	
	public Direction getDir() { 
		return dir;
	}
}
