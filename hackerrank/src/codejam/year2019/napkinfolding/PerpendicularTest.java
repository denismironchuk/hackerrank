package codejam.year2019.napkinfolding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PerpendicularTest extends JPanel {

    private static final long serialVersionUID = 2716461184159166830L;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new PerpendicularTest());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0,0, 700,700);
        frame.setVisible(true);
    }

    public PerpendicularTest() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (!point1) {
                    lx1 = me.getX();
                    ly1 = me.getY();
                    point1 = true;
                } else if (!point2) {
                    lx2 = me.getX();
                    ly2 = me.getY();
                    point2 = true;
                    paint(me.getComponent().getGraphics());
                } else if (!point3) {
                    ax = me.getX();
                    ay = me.getY();
                    point3 = true;
                    paint(me.getComponent().getGraphics());

                    point1 = false;
                    point2 = false;
                    point3 = false;
                }
            }
        });
    }

    private boolean point1 = false;
    private int lx1 = 5;
    private int ly1 = 10;

    private boolean point2 = false;
    private int lx2 = 5;
    private int ly2 = 10;

    private boolean point3 = false;
    private int ax = 5;
    private int ay = 10;

    public void paint(Graphics g) {
        if (point1 && point2) {
            g.drawLine(lx1, ly1, lx2, ly2);
        }

        if (point3) {
            int A = ly2 - ly1;
            int B = lx2 - lx1;
            int C = lx1 * ly2 - lx2 * ly1;

            int x = (B * (B * ax + A * ay) + C * A) / (A * A + B * B);
            int y = (A * (B * ax + A * ay) - C * B) / (A * A + B * B);

            x = 2 * x - ax;
            y = 2 * y - ay;

            g.drawLine(ax, ay, x, y);
        }
    }
}
