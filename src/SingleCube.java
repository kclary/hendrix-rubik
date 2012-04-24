import com.threed.jpct.*;

public class SingleCube extends Object3D {

	public SingleCube(String color1, String color2, String color3, String color4, String color5, String color6, World world, int x_offset, int y_offset, int z_offset) {
		super(12);
		setTexture("black");
	    
	    SimpleVector upperLeftFront = new SimpleVector(-5-x_offset,-5-y_offset,-5-z_offset);
	    SimpleVector upperRightFront = new SimpleVector(5-x_offset,-5-y_offset,-5-z_offset);
	    SimpleVector lowerLeftFront = new SimpleVector(-5-x_offset,5-y_offset,-5-z_offset);
	    SimpleVector lowerRightFront = new SimpleVector(5-x_offset,5-y_offset,-5-z_offset);
	    
	    SimpleVector upperLeftBack = new SimpleVector( -5-x_offset, -5-y_offset, 5-z_offset);
	    SimpleVector upperRightBack = new SimpleVector(5-x_offset, -5-y_offset, 5-z_offset);
	    SimpleVector lowerLeftBack = new SimpleVector( -5-x_offset, 5-y_offset, 5-z_offset);
	    SimpleVector lowerRightBack = new SimpleVector(5-x_offset, 5-y_offset, 5-z_offset);
	    
	    // Front
	   addTriangle(upperLeftFront,0,0, lowerLeftFront,0,1, upperRightFront,1,0, TextureManager.getInstance().getTextureID(color1));
	   addTriangle(upperRightFront,1,0, lowerLeftFront,0,1, lowerRightFront,1,1, TextureManager.getInstance().getTextureID(color1));
	    
	    // Back
	    addTriangle(upperLeftBack,0,0, upperRightBack,1,0, lowerLeftBack,0,1, TextureManager.getInstance().getTextureID(color2));
	    addTriangle(upperRightBack,1,0, lowerRightBack,1,1, lowerLeftBack,0,1, TextureManager.getInstance().getTextureID(color2));
	    
	    // Upper
	    addTriangle(upperLeftBack,0,0, upperLeftFront,0,1, upperRightBack,1,0, TextureManager.getInstance().getTextureID(color3));
	    addTriangle(upperRightBack,1,0, upperLeftFront,0,1, upperRightFront,1,1, TextureManager.getInstance().getTextureID(color3));
	    
	    // Lower
	    addTriangle(lowerLeftBack,0,0, lowerRightBack,1,0, lowerLeftFront,0,1, TextureManager.getInstance().getTextureID(color4));
	    addTriangle(lowerRightBack,1,0, lowerRightFront,1,1, lowerLeftFront,0,1, TextureManager.getInstance().getTextureID(color4));
	    
	    // Left
	    addTriangle(upperLeftFront,0,0, upperLeftBack,1,0, lowerLeftFront,0,1, TextureManager.getInstance().getTextureID(color5));
	    addTriangle(upperLeftBack,1,0, lowerLeftBack,1,1, lowerLeftFront,0,1, TextureManager.getInstance().getTextureID(color5));
	    
	    // Right
	    addTriangle(upperRightFront,0,0, lowerRightFront,0,1, upperRightBack,1,0, TextureManager.getInstance().getTextureID(color6));
	    addTriangle(upperRightBack,1,0, lowerRightFront, 0,1, lowerRightBack,1,1, TextureManager.getInstance().getTextureID(color6));
		
	}
	
	public void print() { 
		System.out.print(" " + this.getName());
	}
}