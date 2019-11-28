package codejam.juggleStrugle.pt1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class JuggleStruggle1 extends JPanel {

    private static final long serialVersionUID = 1438861584964777725L;

    public static int MAX_X = 900;
    public static int MAX_Y = 900;
    public static int POINTS_CNT = 200;

    private static List<Point> points;
    private static List<Line> lines;

    public JuggleStruggle1() {
        JFrame frame = new JFrame();
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0,0, 1000, 1000);
        frame.setVisible(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                extendLinesToIntersection();
                System.out.println("Extension completed!!!");
                System.out.println(points);
                paint(e.getComponent().getGraphics());
            }
        });
    }

    public static void main(String[] args) throws IOException {
        generatePoints();
        generateLines();

        new JuggleStruggle1();
    }

    private static void generateLines() {
        Set<Point> usedPoints = new HashSet<>();
        lines = new ArrayList<>();

        for (int i = 0; i < POINTS_CNT - 1; i++) {
            Point p1 = points.get(i);
            for (int j = i + 1; j < POINTS_CNT; j++) {
                Point p2 = points.get(j);
                if (usedPoints.contains(p2)) {
                    continue;
                }
                int plusCnt = 0;
                int minusCnt = 0;
                Line line = new Line(p1, p2);

                for (int k = 0 ; k < POINTS_CNT; k++) {
                    Point p = points.get(k);
                    if (!usedPoints.contains(p) && !p1.equals(p) && !p2.equals(p)) {
                        if (line.getPointSide(p) > 0) {
                            plusCnt++;
                        } else {
                            minusCnt++;
                        }
                    }
                }

                if (plusCnt == minusCnt) {
                    lines.add(line);
                    usedPoints.add(p1);
                    usedPoints.add(p2);
                }
            }
        }
    }

    /*private static void generatePoints() {
        points = new ArrayList<>();
        points.add(new Point(new Rational(168), new Rational(163)));
        points.add(new Point(new Rational(641), new Rational(720)));
        points.add(new Point(new Rational(580), new Rational(788)));
        points.add(new Point(new Rational(740), new Rational(767)));
    }*/

    private static void generatePoints() {
        Set<Point> pointsSet = new HashSet<>();

        for (int i = 0; i < POINTS_CNT; i++) {
            Point p = Point.random(MAX_X, MAX_Y);
            boolean threePointsOnLine = true;

            while (!threePointsOnLine) {
                while (pointsSet.contains(p)) {
                    p = Point.random(MAX_X, MAX_Y);
                }

                threePointsOnLine = false;

                for (Point p1 : pointsSet) {
                    for (Point p2 : pointsSet) {
                        if (!p1.equals(p2) && new Line(p1, p2).pointBelongs(p)) {
                            threePointsOnLine = true;
                            break;
                        }
                    }
                    if (threePointsOnLine) {
                        break;
                    }
                }
            }

            pointsSet.add(p);
        }

        points = pointsSet.stream().collect(Collectors.toList());
        System.out.println(points);
    }

    private static void extendLinesToIntersection() {
        for (int i = 0; i < (POINTS_CNT / 2) - 1; i++) {
            Line l1 = lines.get(i);
            for (int j = i + 1; j < POINTS_CNT / 2; j++) {
                Line l2 = lines.get(j);

                Point intr = l1.getIntersection(l2);
                extendLineToPoint(l1, intr);
                extendLineToPoint(l2, intr);
            }
        }
    }

    private static void extendLineToPoint(Line l, Point p) {
        if (!l.isPointInSegment(p)) {
            if (l.p1.getSqrDist(p).compareTo(l.p2.getSqrDist(p)) == -1) {
                Point extension = getLineExtension(l.p2, p);
                while (!isValidPoint(extension, l.p1)) {
                    extension = getLineExtension(l.p2, p);
                }

                l.setP1(extension);
            } else {
                Point extension = getLineExtension(l.p1, p);
                while (!isValidPoint(extension, l.p2)) {
                    extension = getLineExtension(l.p1, p);
                }

                l.setP2(extension);
            }
            rebuildPoints();
        }
    }

    private static boolean isValidPoint(Point checked, Point excluded) {
        boolean isValid = true;

        for (int i = 0; isValid && i < POINTS_CNT - 1; i++) {
            Point p1 = points.get(i);

            if (p1.equals(excluded)) {
                continue;
            }

            for (int j = i + 1; isValid && j < POINTS_CNT; j++) {
                Point p2 = points.get(j);

                if (p2.equals(excluded)) {
                    continue;
                }

                Line line = new Line(p1, p2);
                isValid = !line.pointBelongs(checked);
            }
        }

        return isValid;
    }

    private static void rebuildPoints() {
        points = lines.stream().flatMap(line -> Arrays.asList(line.p1, line.p2).stream()).collect(Collectors.toList());
    }

    private static Point getLineExtension(Point linePoint, Point interPoint) {
        Rational part = Rational.random(15).add(Rational.ONE);

        Rational xLen = interPoint.x.substract(linePoint.x);
        Rational yLen = interPoint.y.substract(linePoint.y);

        Point pOnLine = new Point(interPoint.x.substract(xLen.divide(part)), interPoint.y.substract(yLen.divide(part)));

        return pOnLine.flipPoint(interPoint);
    }

    public void paint(Graphics g) {
        paintComponent(g);

        for (Line l : lines) {
            g.drawLine(l.p1.x.getValue(), l.p1.y.getValue(), l.p2.x.getValue(), l.p2.y.getValue());
        }
    }
}
