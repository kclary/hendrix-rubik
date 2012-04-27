
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.threed.jpct.*;

import javax.swing.*;

@SuppressWarnings("serial")
public class RubikPane extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

	private FrameBuffer buffer;
	private BlockingQueue<Update> queue;
	private Interact2D interaction;
	private World world;
	private Cube cube;
	private Dimension size;
	private Camera cam;
	
	private SimpleVector init;
	private int idx;

	public RubikPane() throws Exception {
		super();
		
		size = new Dimension(800, 600);
		queue = new ArrayBlockingQueue<Update>(100000);
		buffer = new FrameBuffer(size.width, size.height, FrameBuffer.SAMPLINGMODE_NORMAL);
		cube = new Cube();
		buffer = new FrameBuffer(size.width, size.height, FrameBuffer.SAMPLINGMODE_NORMAL);
		world = cube.getWorld();
		init = new SimpleVector();
		cam = world.getCamera();
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}
	
	public void mouseClicked(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseMoved(MouseEvent e) { }
	
	public void mouseReleased(MouseEvent e) { 
		cube.setCamera();
	}

	public void mousePressed(MouseEvent e) { 
		cam = world.getCamera();
		init = interaction.reproject2D3D(cam, buffer, e.getX(), e.getY());
		idx = getID(e);
		
	}
	
	public void mouseDragged(MouseEvent e) { 
		if(idx == -100) { 
			//camera
			int x = e.getX();
		    int y = e.getY();
		    SimpleVector vec = interaction.reproject2D3D(cam, buffer, x, y);
		    SimpleVector v = vec.calcSub(init);
		    
	        Camera c = world.getCamera();
	        
	        float d = cube.getCamPos().distance(new SimpleVector(0,0,0));
	        c.moveCamera(Camera.CAMERA_MOVEIN, d);
	        c.rotateX(v.y*5);
	        c.rotateY(v.x*5);
	        c.rotateZ(v.y*5);
	        c.moveCamera(Camera.CAMERA_MOVEOUT, d);
	        
	        init = interaction.reproject2D3D(cam, buffer, e.getX(), e.getY());
		}
		else { 
			//cube movement
			
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e) { 
		int notches = e.getWheelRotation();
		if(notches > 0) { 
			zoomCamera(-1);
		} else { 
			zoomCamera(1);
		}
		System.out.println("event");
	}
	
	@SuppressWarnings("static-access")
	private int getID(MouseEvent e) { 
		java.awt.Point pt = e.getPoint();
        SimpleVector vec = interaction.reproject2D3D(world.getCamera(), buffer, pt.x, pt.y);

        int[] res=Interact2D.pickPolygon(world.getVisibilityList(), new SimpleVector(vec.x, vec.y, vec.z));
        if(res != null) {
                if(res.length > 0) {
                        Object3D picked = world.getObject(res[0]);
                        return picked.getID();             
                }
        }
        
        return -100;
	}
	
	public void addUpdate(int row, int d, Direction dir) { 
		for(int i = 0; i < 13; i++) { 
			queue.add(new Update(row, d, dir));
		}
	}
	
	public void update() {
		Update u = queue.poll();
		if(u.getDir() == Direction.Y) { 
			cube.rotateLeft(u.getRow(), u.getD());
		}
		else {
			cube.rotateUp(u.getRow(), u.getD());
		}
	}
	
	public World getWorld() { 
		return world;
	}
	
	protected void paintComponent(Graphics g) {
		buffer.clear(java.awt.Color.GRAY);
		world.renderScene(buffer);
		world.draw(buffer);
		buffer.update();
		buffer.display(g);	
		if(!queue.isEmpty()) { 
			update();
		}
		repaint();
	}
	
	public FrameBuffer getBuffer() {
		return buffer;
	}
	
	public SingleCube[] getBoxes() { 
		return cube.getBoxes();
	}
	
	public void changeCamera(int d, Direction dir) { 
		for(int i = 0; i < 13; i++) { 
			queue.add(new Update(1, d, dir));
			queue.add(new Update(2, d, dir));
			queue.add(new Update(3, d, dir));
		}
	}
	
	public void zoomCamera(int parity) { 
		cube.zoomCamera(parity);
	}
	
	

}
