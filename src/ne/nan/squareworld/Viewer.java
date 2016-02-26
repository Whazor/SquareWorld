package ne.nan.squareworld;

import javax.swing.*;
import java.awt.*;

public class Viewer extends Canvas {

    public Viewer() {
        //...
    }

    public void paint(Graphics graphics) {
        /* We override the method here. The graphics
         * code comes here within the method body. */
    }

    public static void main(String[] args) {
        // We initialize our class here
        Viewer canvas = new Viewer();
        JFrame frame = new JFrame();
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Here we add it to the frame
        frame.getContentPane().add(canvas);
        frame.setVisible(true);
    }
}