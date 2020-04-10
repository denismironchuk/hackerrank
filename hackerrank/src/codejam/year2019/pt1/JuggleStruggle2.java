package codejam.year2019.pt1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class JuggleStruggle2 extends JPanel {

    private static final long serialVersionUID = 1438861584964777725L;

    public static int MAX_X = 500;
    public static int MAX_Y = 500;
    public static int POINTS_CNT = 6;

    private static List<Point> points;
    private static Line divideLine;

    public JuggleStruggle2() {
        JFrame frame = new JFrame();
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0,0, 1000, 1000);
        frame.setVisible(true);

        Point center = points.get((int)(Math.random() * POINTS_CNT));
        //Point center = points.get(4);

        divideLine = getDividingLine(points);


        if (!validate(divideLine)) {
            points.forEach(System.out::println);
            throw new RuntimeException();
        }

        addMouseListener(new CustomAdapter(center));
    }

    private class CustomAdapter extends MouseAdapter {
        Point center;
        Point end;

        public CustomAdapter(Point center) {
            this.center = center;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("Mouse clicked");
            /*if (end == null) {
                end = getRandomPoint(center);
            }

            end = getEqualDivideLine(center, end);

            divideLine = new Line(center, end);
            paint(e.getComponent().getGraphics());

            if (!validate(center, end)) {
                throw new RuntimeException();
            }*/

            divideLine = getDividingLine(points);


            if (!validate(divideLine)) {
                points.forEach(System.out::println);
                throw new RuntimeException();
            }
        }
    }

    public static void main(String[] args) {
        /*while (true) {
            generatePoints();

            Point center = points.get((int) (Math.random() * POINTS_CNT));
            //System.out.println(center);

            Point end = getEqualDivideLine(center);


            if (!validate(center, end)) {
                throw new RuntimeException();
            }

            Line line = getDividingLine(points);


            if (!validate(line)) {
                points.forEach(System.out::println);
                throw new RuntimeException();
            }

            System.out.println("===========");
        }*/
        generatePoints();

        new JuggleStruggle2();
    }

    private static Point getRandomPoint(Point exclude) {
        Point point = exclude;

        while (point.equals(exclude)) {
            point = points.get((int)(Math.random() * POINTS_CNT));
        }

        //System.out.println(point);

        return point;
    }

    private static Line getDividingLine(List<Point> points) {
        //Point center = points.get((int) (Math.random() * points.size()));
        Point center = points.get(4);
        System.out.println("Center = " + center);
        List<Point> noCenterPoints = points.stream().filter(p -> !p.equals(center)).collect(Collectors.toList());
        Point end = sort(noCenterPoints, center, 0, noCenterPoints.size() - 1, noCenterPoints.size() / 2);
        return new Line(center, end);
    }

    private static Point sort(List<Point> points, Point center, int start, int end, int expectedPivot) {
        try {
            if (start >= end) {
                return points.get(start);
            }
            int index = 3;//start + (int) (Math.random() * (end - start + 1));
            System.out.println("index = " + index);
            Point pivotPoint = points.get(index);
            points.set(index, points.get(end));
            points.set(end, pivotPoint);

            Line l = new Line(center, pivotPoint);

            int newPivotIndex = start;

            for (int i = start; i < end; i++) {
                Point p = points.get(i);

                if (l.getPointSide(p) < 0) {
                    points.set(i, points.get(newPivotIndex));
                    points.set(newPivotIndex, p);
                    newPivotIndex++;
                }
            }

            points.set(end, points.get(newPivotIndex));
            points.set(newPivotIndex, pivotPoint);

            if (newPivotIndex == expectedPivot) {
                return pivotPoint;
            } else if (newPivotIndex > expectedPivot) {
                return sort(points, center, start, newPivotIndex - 1, expectedPivot);
            } else {
                return sort(points, center, newPivotIndex + 1, end, expectedPivot);
            }
        } catch (Throwable ex) {
            System.out.println();
        }

        return null;
    }

    private static Point getEqualDivideLine(Point center) {
        return getEqualDivideLine(center, getRandomPoint(center));
    }

    private static Point getEqualDivideLine(Point center, Point end) {
        Line line = new Line(center, end);
        Vector v1 = line.getVector();

        List<PointAndAngle> pointAndAngles = new ArrayList<>();

        int rightCnt = 0;
        int leftCnt = 0;

        for (Point p : points) {
            if (!p.equals(center) && !p.equals(end)) {
                if (line.getPointSide(p) == -1) {
                    Vector v2 = new Line(p, center).getVector();
                    pointAndAngles.add(new PointAndAngle(p, getAngleCos(v1, v2), false));
                    leftCnt++;
                } else {
                    Vector v2 = new Line(center, p).getVector();
                    pointAndAngles.add(new PointAndAngle(p, getAngleCos(v1, v2), true));
                    rightCnt++;
                }
            }
        }

        Collections.sort(pointAndAngles);
        int index = 0;
        boolean isRight = true;

        while (rightCnt != leftCnt) {
            PointAndAngle curr = pointAndAngles.get(index);
            index++;
            end = curr.p;

            if (curr.right) {
                if (isRight) {
                    rightCnt--;
                    leftCnt++;
                } else {
                    isRight = !isRight;
                }
            } else {
                if (!isRight) {
                    leftCnt--;
                    rightCnt++;
                } else {
                    isRight = !isRight;
                }
            }
        }

        return end;
    }

    private static double getAngleCos(Vector v1, Vector v2) {
        Rational scalarMul = v1.scalarMul(v2);

        Rational sqrLen1 = v1.sqrLen();
        Rational sqrLen2 = v2.sqrLen();

        double len1 = Math.sqrt(sqrLen1.getDoubleValue());
        double len2 = Math.sqrt(sqrLen2.getDoubleValue());

        return scalarMul.getDoubleValue() / (len1 * len2);
    }

    private static class PointAndAngle implements Comparable<PointAndAngle> {
        private Point p;
        private double angle;
        private boolean right;

        public PointAndAngle(Point p, double angle) {
            this.p = p;
            this.angle = angle;
        }

        public PointAndAngle(Point p, double angle, boolean right) {
            this.p = p;
            this.angle = angle;
            this.right = right;
        }

        @Override
        public int compareTo(PointAndAngle o) {
            return Double.compare(o.angle, angle);
        }
    }

    private static void generatePoints() {
        points = Arrays.asList(
                new Point(4, new Rational(107), new Rational(5)),
                new Point(1, new Rational(478), new Rational(142)),
                new Point(3, new Rational(232), new Rational(31)),
                new Point(5, new Rational(277), new Rational(217)),
                new Point(2, new Rational(243), new Rational(107)),
                new Point(0, new Rational(288), new Rational(86))
        );
    }

    /*private static void generatePoints() {
        Set<Point> pointsSet = new HashSet<>();

        for (int i = 0; i < POINTS_CNT; i++) {
            Point p = null;
            boolean threePointsOnLine = true;

            while (threePointsOnLine) {
                p = Point.random(MAX_X, MAX_Y);
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
            p.index = i;
        }

        points = pointsSet.stream().collect(Collectors.toList());
        //System.out.println(points);
    }*/

    private static boolean validate(Line line) {
        int leftCnt = 0;
        int rightCount = 0;

        for (Point p : points) {
            if (!p.equals(line.p1) && !p.equals(line.p2)) {
                if (line.getPointSide(p) == -1) {
                    leftCnt++;
                } else {
                    rightCount++;
                }
            }
        }

        return leftCnt == rightCount;
    }

    private static boolean validate(Point p1, Point p2) {
        int leftCnt = 0;
        int rightCount = 0;

        Line line = new Line(p1, p2);

        for (Point p : points) {
            if (!p.equals(p1) && !p.equals(p2)) {
                if (line.getPointSide(p) == -1) {
                    leftCnt++;
                } else {
                    rightCount++;
                }
            }
        }

        return leftCnt == rightCount;
    }

    public void paint(Graphics g) {
        paintComponent(g);

        for (Point p : points) {
            g.drawOval(p.x.getValue(), p.y.getValue(), 5, 5);
            g.drawString(String.valueOf(p.index), p.x.getValue() + 6, p.y.getValue() + 6);
        }

        if (divideLine != null) {
            drawLine(divideLine, g);
        }
    }

    private void drawLine(Line l, Graphics g) {
        g.drawLine(l.p1.x.getValue(), l.p1.y.getValue(), l.p2.x.getValue(), l.p2.y.getValue());
    }
}
