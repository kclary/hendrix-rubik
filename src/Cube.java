import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;

import com.threed.jpct.Camera;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;


public class Cube {
	final static float ROTATION = (float)(Math.PI/2)/13;
	
	private World world;
	SingleCube[] boxes;
	int index;
	ArrayList<Rotation> rotations;
	private Camera camera;
	private Camera rearCamera;
//	private SimpleVector initialPosition;
//	private SimpleVector newPosition;
	
	public Cube() { 
		index = 0;
		boxes = new SingleCube[27];
		rotations = new ArrayList<Rotation>();
		world = new World();
		camera = world.getCamera();
		rearCamera = world.getCamera();
//		initialPosition = camera.getPosition();

		String path = "/export/home/s11/claryka/Images/";

		TextureManager.getInstance().addTexture("red", new Texture(path + "red.png"));
		TextureManager.getInstance().addTexture("blue", new Texture(path + "blue.png"));
		TextureManager.getInstance().addTexture("white", new Texture(path + "white.png"));
		TextureManager.getInstance().addTexture("pink", new Texture(path + "orange.png"));
		TextureManager.getInstance().addTexture("green", new Texture(path + "green.png"));
		TextureManager.getInstance().addTexture("yellow", new Texture(path + "yellow.png"));
		TextureManager.getInstance().addTexture("black", new Texture(2, 2, java.awt.Color.BLACK));
		
		//frame
		add(new SingleCube("black", "black", "black", "black", "black", "black", world, 0, 0, 0));  //0
		add(new SingleCube("black", "black", "black", "black", "yellow", "black",  world, 10, 0, 0)); //1
		add(new SingleCube("black", "black", "black", "black", "black", "red", world, -10, 0, 0));  //2
		add(new SingleCube("black", "black", "blue", "black", "black", "black", world, 0, 10, 0));  //3
		add(new SingleCube("black", "black", "black", "green", "black", "black", world, 0, -10, 0));  //4
		add(new SingleCube("white", "black", "black", "black", "black", "black", world, 0, 0, 10));  //5
		add(new SingleCube("black", "pink", "black", "black", "black", "black", world, 0, 0, -10));   //6
		
		//side
		add(new SingleCube("black", "black", "blue", "black", "yellow", "black", world, 10, 10, 0)); //7
		add(new SingleCube("black", "black", "black", "green", "yellow", "black", world, 10, -10, 0));  //8
		add(new SingleCube("black", "black", "blue", "black", "black", "red", world, -10, 10, 0));  //9
		add(new SingleCube("black", "black", "black", "green", "black", "red", world, -10, -10, 0));  //10
		add(new SingleCube("white", "black", "black", "black", "yellow", "black", world, 10, 0, 10));  //11
		add(new SingleCube("black", "pink", "black", "black", "yellow", "black", world, 10, 0, -10));  //12
		add(new SingleCube("white", "black", "black", "black", "black", "red", world, -10, 0, 10));  //13
		add(new SingleCube("black", "pink", "black", "black", "black", "red", world, -10, 0, -10));  //14
		add(new SingleCube("white", "black", "blue", "black", "black", "black", world, 0, 10, 10));  //15
		add(new SingleCube("black", "pink", "blue", "black", "black", "black", world, 0, 10, -10));  //16
		add(new SingleCube("white", "black", "black", "green", "black", "black", world, 0, -10, 10));  //17
		add(new SingleCube("black", "pink", "black", "green", "black", "black", world, 0, -10, -10));  //18

		//corners
		add(new SingleCube("white", "black", "blue", "black", "yellow", "black", world, 10, 10, 10));  //19
		add(new SingleCube("black", "pink", "blue", "black", "yellow", "black", world, 10, 10, -10));  //20
		add(new SingleCube("white", "black", "black", "green", "yellow", "black", world, 10, -10, 10));  //21
		add(new SingleCube("black", "pink", "black", "green", "yellow", "black", world, 10, -10, -10));  //22
		add(new SingleCube("white", "black", "blue", "black", "black", "red", world, -10, 10, 10));   //23
		add(new SingleCube("black", "pink", "blue", "black", "black", "red", world, -10, 10, -10));  //24
		add(new SingleCube("white", "black", "black", "green", "black", "red", world, -10, -10, 10));  //25
		add(new SingleCube("black", "pink", "black", "green", "black", "red", world, -10, -10, -10));  //26  28
		
		for(int i = 0; i < boxes.length; i++) { 
			boxes[i].build();
			boxes[i].setRotationPivot(new SimpleVector(0, 0, 0));
			world.addObject(boxes[i]);
			boxes[i].print();
		}
		
		rotations.add(new Rotation(makeArray(24, 23, 19, 20), makeArray(9, 15, 7, 16))); 
		rotations.add(new Rotation(makeArray(13, 11, 12, 14), makeArray(2, 5, 1, 6))); 
		rotations.add(new Rotation(makeArray(25, 21, 22, 26), makeArray(10, 17, 8, 18)));  
		rotations.add(new Rotation(makeArray(23, 19, 21, 25), makeArray(13, 15, 11, 17))); 
		rotations.add(new Rotation(makeArray(9, 7, 8, 10), makeArray(3, 1, 4, 2))); 
		rotations.add(new Rotation(makeArray(24, 20, 22, 26), makeArray(14, 16, 12, 18))); 
		rotations.add(new Rotation(makeArray(21, 19, 20, 22), makeArray(11, 7, 12, 8)));
		rotations.add(new Rotation(makeArray(23, 24, 26, 25), makeArray(9, 14, 10, 13)));
		rotations.add(new Rotation(makeArray(5, 3, 6, 4), makeArray(19, 20, 22, 25)));
		
		
		world.setAmbientLight(255, 255, 255);
		world.getCamera().setPosition(70, -70, -65);
		world.getCamera().lookAt(boxes[0].getTransformedCenter());
	}
	
	private void add(SingleCube c) { 
		boxes[index] = c;
		index++;
	}
	
	private int[] makeArray(int a, int b, int c, int d) { 
		int[] ret = new int[4];
		ret[0] = a;
		ret[1] = b;
		ret[2] = c;
		ret[3] = d;
		return ret;
	}
	
	public void setCamera() { 
		world.getCamera().setPosition(70, -70, -65);
		world.getCamera().lookAt(boxes[0].getTransformedCenter());
	}
	
	public void rotateUp(int col, int d) { 
		int index = getZRotation(col);
		int[] cubes = getZCubes(col);
		
		SingleCube[] b = Arrays.copyOf(boxes, boxes.length);
		for(int i = 0; i < cubes.length; i++) { 
			boxes[cubes[i]].rotateZ(ROTATION*d);
			
			int next = rotations.get(index).direct(cubes[i], d);
			if(next == -1) { b[cubes[i]] = boxes[cubes[i]]; } 
			else { b[next] = boxes[cubes[i]]; }
		}
		boxes = b;
		
	}
	
	private int[] getZCubes(int col) { 
		int[] cubes;
		int[] one = {5, 11, 13, 15, 17, 19, 21, 23, 25};
		int[] two = {0, 1, 2, 3, 4, 7, 8, 9, 10};
		int[] three = {6, 12, 14, 16, 18, 20, 22, 24, 26};
		if (col == 1) { 
			cubes = one;
		} else if(col == 3) { 
			cubes = three;
		} else { 
			cubes = two;
		}
		return cubes;
	}
	
	private int getZRotation(int col) { 
		if(col == 1) { return 3; }
		else if(col == 3) { return 5; } 
		else { return 4; }
	}
	
	public void rotateLeft(int row, int d) { 
		int index = getYRotation(row);
		int[] cubes = getYCubes(row);
		
		SingleCube[] b = Arrays.copyOf(boxes, boxes.length);
		for(int i = 0; i < cubes.length; i++) { 
			boxes[cubes[i]].rotateY(ROTATION*d);
			
			int next = rotations.get(index).direct(cubes[i], d);
			if(next == -1) { b[cubes[i]] = boxes[cubes[i]]; } 
			else { b[next] = boxes[cubes[i]]; }
		}
		
		boxes = b;
	}
	
	private int[] getYCubes(int row) { 
		int[] cubes;
		int[] one = {3, 7, 9, 15, 16, 19, 20, 23, 24};
		int[] two = {0, 1, 2, 5, 6, 11, 12, 13, 14};
		int[] three = {4, 8, 10, 17, 18, 21, 22, 25, 26};
		
		if (row == 1) { 
			cubes = one;
		} else if(row == 3) { 
			cubes = three;
		} else { 
			cubes = two;
		}
		return cubes;
	}
	
	private int getYRotation(int row) { 
		if(row == 1) { return 0; } 
		else if(row == 3) { return 2; }
		else { return 1; } 
	}
	
	public void reverseCamera() { 
		rearCamera = world.getCamera();
		rearCamera.setPosition(-1*rearCamera.getPosition().x, rearCamera.getPosition().y, -1*rearCamera.getPosition().z);
		rearCamera.lookAt(boxes[0].getTransformedCenter());
		world.setCameraTo(rearCamera);
	}
	
	public void zoomCamera(int parity){
        SimpleVector newDistance = camera.getPosition();
        newDistance.scalarMul(1 - parity*0.1f);
        camera.setPosition(newDistance);
	}
	
	public void rotateCamera(int direction){
	        camera.rotateAxis(new SimpleVector(0,5,0), direction*25);
	        //camera.lookAt(new SimpleVector(0, 0, 0));
	}
	
	public SingleCube[] getBoxes() { 
		return boxes;
	}
	
	public World getWorld() {
		return world;
	}
	
}
