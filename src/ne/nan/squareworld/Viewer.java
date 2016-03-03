package ne.nan.squareworld;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;

import java.awt.*;
import java.awt.event.*;
import java.util.function.Function;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import ne.nan.squareworld.generators.Finder;
import ne.nan.squareworld.generators.levels.Region;

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

        final JFrame jframe = new JFrame( "One Triangle Swing GLCanvas" );
        jframe.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowevent ) {
                jframe.dispose();
                System.exit( 0 );
            }
        });

        jframe.getContentPane().add( glcanvas, BorderLayout.CENTER );
        jframe.setSize( 1200, 800 );
        jframe.setVisible( true );
        jframe.getContentPane().addMouseListener(new MouseAdapter() {
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
            }

            @Override
            public void mouseMoved(MouseEvent e) {
//                super.mouseMoved(e);
                OneTriangle.move(x, y);
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

            gl2.glColor3f( 1, 0, 0 );
            gl2.glBegin(GL2.GL_POLYGON);
            gl2.glVertex2d(x, y);
            gl2.glVertex2d(x + width, y);
            gl2.glVertex2d(x + width, y + height);
            gl2.glVertex2d(x, y + height);

            gl2.glEnd();
//            gl2.glVertex2i( x, y );
//            gl2.glVertex2i( x + width, y );
//            gl2.glVertex2i( x, height + y );
//            gl2.glVertex2i( x + width, height + y );
        }

        static Region fl = new Region(1337);
        static Finder finder = new Finder(fl);


        protected static void render( GL2 gl2, int width, int height ) {
            System.out.println("...");
            gl2.glClear( GL.GL_COLOR_BUFFER_BIT );

            // draw a triangle filling the window

            gl2.glLoadIdentity();

            int scale = 5;
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    switch (finder.getBlock(OneTriangle.x+x, OneTriangle.y+y)) {
                        case GRASS:
                            rect(gl2, x, y, 1, 1);
                            break;
                    }
                }
            }
        }

        public static void move(int x, int y) {
            OneTriangle.x = x;
            OneTriangle.y = y;
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
//        for (int x = 0; x < 1000; x++) {
//            for (int y = 0; y < 1000; y++) {
//                switch (finder.getBlock(x, y)) {
//                    case GRASS:
//                        graphics.drawRect(x*2, y*2, 2, 2);
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