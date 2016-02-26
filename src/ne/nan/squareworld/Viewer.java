package ne.nan.squareworld;
import ne.nan.squareworld.generators.Finder;
import ne.nan.squareworld.generators.levels.Region;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import javax.swing.*;

public class Viewer {

    public static void main(String[] args) {
        new Viewer();
    }

    public Viewer() {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {
            }

            JFrame frame = new JFrame("Testing");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());


            frame.add(new TestPane());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    public class TestPane extends JPanel {
        private float scale = 2;
        Region fl;Finder finder;
        boolean isDragging = false;

        public TestPane() {
            fl = new Region(1337);
            finder = new Finder(fl);

            addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    float delta = (float) (0.05f * e.getPreciseWheelRotation());

                    scale = Math.max(0.5f, Math.min(5f, scale + delta));
                    System.out.println(scale);

                    revalidate();
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    isDragging = true;

                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    e.
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    isDragging = false;
                }
            });

        }

        @Override
        public Dimension getPreferredSize() {
            Dimension size = new Dimension(1200, 800);
            size.width = Math.round(size.width * scale);
            size.height = Math.round(size.height * scale);
            return size;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();

            for (int x = 0; x < getWidth()/scale; x++) {
                for (int y = 0; y < getHeight()/scale; y++) {
                    switch (finder.getBlock(x, y)) {
                        case GRASS:
                            g2d.drawRect((int)(x*scale), (int)(y*scale), (int)scale, (int)scale);
                            break;
                    }
                }
            }
//            g2d.setTransform(at);
//            g2d.setColor(Color.RED);
//
//            // This is for demonstration purposes only
//            // I prefer to use getWidth and getHeight
//            int width = 1000;
//            int height = 1000;
//
//            Path2D.Float path = new Path2D.Float();
//            int seg = width / 3;
//            path.moveTo(0, height / 2);
//            path.curveTo(0, 0, seg, 0, seg, height / 2);
//            path.curveTo(
//                    seg, height,
//                    seg * 2, height,
//                    seg * 2, height / 2);
//            path.curveTo(
//                    seg * 2, 0,
//                    seg * 3, 0,
//                    seg * 3, height / 2);
//
//            g2d.draw(path);
//

            g2d.dispose();
        }

        public void scrollThing() {
            JViewport pane = (JViewport) this.getParent();
            pane.addChangeListener(e -> {
                revalidate();
                repaint();
            });
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