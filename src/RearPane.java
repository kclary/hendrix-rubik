import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import com.threed.jpct.*;

import javax.swing.*;

public class RearPane extends JPanel {
	private World world;
	private FrameBuffer buffer;
	private Camera camera;
	private Camera rearCamera;
	private SingleCube[] boxes;
//	private SimpleVector initialPosition;
//	private SimpleVector newPosition;
	
	public RearPane(RubikPane p) { 
		super();
		this.world = p.getWorld();
		this.buffer = new FrameBuffer(200, 150, FrameBuffer.SAMPLINGMODE_NORMAL);
		this.boxes = p.getBoxes();
	}
	
	protected void paintComponent(Graphics g) {
		reverseCamera();
		buffer.clear(java.awt.Color.GRAY);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.update();
		buffer.display(g);	
		repaint();
		reverseCamera();
	}
	
	public void reverseCamera() { 
		rearCamera = world.getCamera();
		rearCamera.setPosition(-1*rearCamera.getPosition().x, -1*rearCamera.getPosition().y, -1*rearCamera.getPosition().z);
		rearCamera.lookAt(boxes[0].getTransformedCenter());
		world.setCameraTo(rearCamera);
	}
}
