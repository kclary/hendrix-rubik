package cube;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;

public class PaintCameraControls extends JPanel implements ActionListener{
        private static final long serialVersionUID = 1L;
        private BasicArrowButton north = new BasicArrowButton(BasicArrowButton.NORTH);
        private BasicArrowButton east = new BasicArrowButton(BasicArrowButton.EAST);
        private BasicArrowButton south = new BasicArrowButton(BasicArrowButton.SOUTH);
        private BasicArrowButton west = new BasicArrowButton(BasicArrowButton.WEST);
        private JButton zoomIn = new JButton("+");
        private JButton zoomOut = new JButton("-");

        private RubikPane pane;
        private MainPanel main;
       
        public PaintCameraControls(RubikPane pane, MainPanel main){
                super();
                this.pane = pane;
                this.main = main;
                setLayout(null);
               
                north.setBounds(100,0,35,35);
                north.addActionListener(this);
                add(north);
                zoomIn.setBounds(35,0,55,35);
                zoomIn.addActionListener(this);
                add(zoomIn);
                zoomOut.setBounds(145,0,55,35);
                zoomOut.addActionListener(this);
                zoomIn.addActionListener(this);
                add(zoomOut);
               
                east.setBounds(150,50,35,35);
                east.addActionListener(this);
                add(east);
                south.setBounds(100,100,35,35);
                south.addActionListener(this);
                add(south);
                west.setBounds(50,50,35,35);
                west.addActionListener(this);
                add(west);
                
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
                if(evt.getSource() == north){
                	pane.changeCamera(1, Direction.Z);
                	main.getButtons().changeCamera(Direction.Z, 1);
                }
                else if(evt.getSource() == east){
                	pane.changeCamera(-1, Direction.Y);
                	main.getButtons().changeCamera(Direction.Y, -1);
                }
                else if(evt.getSource() == south){
                	pane.changeCamera(-1, Direction.Z);
                	main.getButtons().changeCamera(Direction.Z, -1);
                }
                else if(evt.getSource() == west){
                	pane.changeCamera(1, Direction.Y);
                	main.getButtons().changeCamera(Direction.Y, 1);
                }
                else if(evt.getSource() == zoomIn){
                    pane.zoomCamera(1);
                }
                else if(evt.getSource() == zoomOut){
                    pane.zoomCamera(-1);
                }
               
        }
}