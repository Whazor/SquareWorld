package ne.nan.squareworld;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;
import java.awt.event.*;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import ne.nan.squareworld.generators.Finder;
import ne.nan.squareworld.generators.levels.Region;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import javax.swing.*;

public class Viewer {

    public static void main( String [] args ) {
        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities( glprofile );
        final GLCanvas glcanvas = new GLCanvas( glcapabilities );

        glcanvas.addGLEventListener( new GLEventListener() {

            @Override
            public void reshape( GLAutoDrawable glautodrawable, int x, int y, int width, int height ) {
                OneTriangle.setup( glautodrawable.getGL().getGL2(), width, height );
            }

            @Override
            public void init( GLAutoDrawable glautodrawable ) {
            }

            @Override
            public void dispose( GLAutoDrawable glautodrawable ) {
            }

            @Override
            public void display( GLAutoDrawable glautodrawable ) {
                OneTriangle.render( glautodrawable.getGL().getGL2(), glautodrawable.getSurfaceWidth(), glautodrawable.getSurfaceHeight() );
            }
        });

        final JFrame jframe = new JFrame( "SquareWorld" );
        jframe.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowevent ) {
                jframe.dispose();
                System.exit( 0 );
            }
        });

        jframe.getContentPane().add( glcanvas, BorderLayout.CENTER );
        jframe.setSize( 1200, 800 );
        jframe.setVisible( true );

        glcanvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                super.mouseWheelMoved(e);
            }

            int x;
            int y;
//            bool isMoving = false

            @Override
            public void mousePressed(MouseEvent e) {
//                super.mousePressed(e);
                x = e.getX();
                y = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                int nx = e.getX();
                int ny = e.getY();
                System.out.println((nx - x) + " - " + (ny - y));
                OneTriangle.move(nx - x, ny - y);
                glcanvas.repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
//                super.mouseMoved(e);
//                int nx = e.getX();
//                int ny = e.getY();
//                OneTriangle.move(nx - width, ny - height);
            }
        });
    }

    public static class OneTriangle {
        private static int x;
        private static int y;

        protected static void setup(GL2 gl2, int width, int height ) {
//            gl2.glMatrixMode( GL2.GL_PROJECTION );
//            gl2.glLoadIdentity();

            // coordinate system origin at lower left with width and height same as the window
            GLU glu = new GLU();

            gl2.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);         // black background
            gl2.glMatrixMode(GL2.GL_PROJECTION);              // setup viewing projection
            gl2.glLoadIdentity();                           // start with identity matrix
//            gl2.glOrtho(0.0, width, 0.0, height, -1.0, 1.0);   // setup a 10x10x2 viewing world
            glu.gluOrtho2D( 0.0f, width, 0.0f, height);
            gl2.glMatrixMode(GL2.GL_MODELVIEW);
            gl2.glLoadIdentity();
        }

        public static void rect(GL2 gl2, int x, int y, int width, int height/*, int w, int h*/) {
            gl2.glBegin(GL2.GL_POLYGON);
            gl2.glVertex2d(x, y);
            gl2.glVertex2d(x + width, y);
            gl2.glVertex2d(x + width, y + height);
            gl2.glVertex2d(x, y + height);

            gl2.glEnd();
        }

        static Region fl = new Region(1337);
        static Finder finder = new Finder(fl);


        protected static void render( GL2 gl2, int width, int height ) {
            System.out.println("...");
            gl2.glClear( GL.GL_COLOR_BUFFER_BIT );
            gl2.glLoadIdentity();

            int scale = 5;
            for (int x = 0; x < width; x+=16) {
                for (int y = 0; y < height; y+=16) {
                    MaterialData[][][] chunk = finder.getChunk(OneTriangle.x + x, OneTriangle.y + y);

                    for (int i = 0; i < chunk.length; i++) {
                        for (int j = 0; j < chunk[i].length; j++) {
                            Material item = chunk[i][j][0].getItemType();
                            for (int k = 0; k < chunk[i][j].length; k++) {
                                if (chunk[i][j][k] != null && chunk[i][j][k].getItemType() != Material.AIR) {
                                    item = chunk[i][j][k].getItemType();
                                }
                            }
                            if (item != null) {
                                switch (item) {
                                    case GRASS: //green on top id = 2
                                        gl2.glColor3ub((byte) (24), (byte) (174), (byte) (24));
//                                    gl2.glColor3f( 0.0416f, 0.0057471264f, 0.041666668f);
                                        break;
                                    case STONE: // grey id = 1
                                        gl2.glColor3ub((byte) (140), (byte) (140), (byte) (140));
                                        break;
                                    case DIRT: // brown id = 3
                                        gl2.glColor3ub((byte) (125), (byte) (75), (byte) (0));
                                        break;
                                    case BRICK: // brick
                                        gl2.glColor3ub((byte) (125), (byte) (0), (byte) (0));
                                        break;
                                    case WOOD:
                                        gl2.glColor3ub((byte) (227), (byte) (136), (byte) (0));
                                        break;
                                    case COAL_BLOCK: // black, id = 173
                                        gl2.glColor3ub((byte) (0), (byte) (0), (byte) (0));
                                        break;
                                    case WOOL: // white, id = 35
//                                    gl2.glColor3f( 255, 255, 255 );
                                        gl2.glColor3ub((byte) (255), (byte) (255), (byte) (255));
                                        break;
                                    default: // white id = 1
                                        gl2.glColor3f(255, 255, 255);
                                }
                                rect(gl2, x + i, y + j, 1, 1);
                            }
                        }
                    }
                }
            }
        }

        public static void move(int x, int y) {
            OneTriangle.x =- x;
            OneTriangle.y =- y;
        }
    }
}
//package ne.nan.squareworld;
//
//import ne.nan.squareworld.generators.Finder;
//import ne.nan.squareworld.generators.levels.Region;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class Viewer extends Canvas {
//
//    public Viewer() {
//        //...
//    }
//
//    public void paint(Graphics graphics) {
//        /* We override the method here. The graphics
//         * code comes here within the method body. */
//
//        Region fl = new Region(1337);
//        Finder finder = new Finder(fl);
//
//
//        for (int width = 0; width < 1000; width++) {
//            for (int height = 0; height < 1000; height++) {
//                switch (finder.getChunk(width, height)) {
//                    case GRASS:
//                        graphics.drawRect(width*2, height*2, 2, 2);
//                        break;
//                }
//            }
//        }
//    }
//
//    public static void main(String[] args) {
//        // We initialize our class here
//        Viewer canvas = new Viewer();
//        JFrame frame = new JFrame();
//        frame.setSize(1200, 1000);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        // Here we add it to the frame
//        frame.getContentPane().add(canvas);
//        frame.setVisible(true);
//    }
//}