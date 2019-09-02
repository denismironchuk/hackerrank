package codejam.napkinfolding;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.geom.*;
import java.util.ArrayList;

public class PoligonFlipTest extends JPanel {

    private static final long serialVersionUID = 1261228559781103615L;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.add(new PoligonFlipTest());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0,0, 700,700);
        frame.setVisible(true);
    }

    private java.util.List<Point2D> poligon = new ArrayList<>();
    private java.util.List<java.util.List<Point2D>> poligons = new ArrayList<>();

    private boolean poligonIsDone = false;
    private double distThreshold = 10;
    private double pointDistThreshold = 2;

    public PoligonFlipTest() {
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                Point p = me.getPoint();

                if (!poligonIsDone) {
                    if (isExistingPoint(p)) {
                        poligonIsDone = true;
                        poligons.add(new ArrayList<>(poligon));
                    } else {
                        poligon.add(me.getPoint());
                    }
                } else {
                    addFlippedPoligon(p);
                }

                paint(me.getComponent().getGraphics());
            }

            private void addFlippedPoligon(Point p) {
                int selectedLine = getSelectedLine(p, poligon);
                if (selectedLine != -1) {
                    Line2D symLine = selectedLine == 0 ? new Line2D.Double(poligon.get(0), poligon.get(poligon.size() - 1)) :
                            new Line2D.Double(poligon.get(selectedLine), poligon.get(selectedLine - 1));

                    java.util.List<Point2D> polyToFlip = getPoligonToFlip(symLine.getP1(), symLine.getP2());
                    java.util.List<Point2D> flippedPoly = getFlippedPoligon(symLine, polyToFlip);
                    poligons.add(flippedPoly);
                    mergePolygon(selectedLine, symLine, flippedPoly);
                }
            }

            private void mergePolygon(int selectedLine, Line2D symLine, java.util.List<Point2D> polyToMerge) {
                int index = -1;

                for (int i = 0; i < polyToMerge.size(); i++) {
                    Point2D curr = polyToMerge.get(i);

                    if (symLine.getP2().distance(curr) < pointDistThreshold) {
                        index = i;
                        break;
                    }
                }

                for (int i = index - 2; i >= 0; i--) {
                    poligon.add(selectedLine, polyToMerge.get(i));
                }

                for (int i = polyToMerge.size() - 1; i > index; i--) {
                    poligon.add(selectedLine, polyToMerge.get(i));
                }
            }

            private java.util.List<Point2D> getFlippedPoligon(Line2D symLine, java.util.List<Point2D> polygon) {
                java.util.List<Point2D> newPoly = new ArrayList<>();

                for (int i = 0; i < polygon.size(); i++) {
                    newPoly.add(0, symmetryPoint(symLine, polygon.get(i)));
                }

                return newPoly;
            }

            private Point2D symmetryPoint(Line2D line, Point2D p) {
                Point2D p1 = line.getP1();
                Point2D p2 = line.getP2();

                double A = p2.getY() - p1.getY();
                double B = p2.getX() - p1.getX();
                double C = p1.getX() * p2.getY() - p2.getX() * p1.getY();

                double x = (B * (B * p.getX() + A * p.getY()) + C * A) / (A * A + B * B);
                double y = (A * (B * p.getX() + A * p.getY()) - C * B) / (A * A + B * B);

                x = 2 * x - p.getX();
                y = 2 * y - p.getY();

                return new Point2D.Double(x, y);
            }

            private java.util.List<Point2D> getPoligonToFlip(Point2D p1, Point2D p2) {
                for (java.util.List<Point2D> completedPoly : poligons) {
                    Point2D prev = completedPoly.get(completedPoly.size() - 1);

                    for (int i = 0; i < completedPoly.size(); i++) {
                        Point2D curr = completedPoly.get(i);

                        if ((prev.distance(p1) < pointDistThreshold && curr.distance(p2) < pointDistThreshold)
                                || (prev.distance(p2) < pointDistThreshold && curr.distance(p1) < pointDistThreshold)) {
                            return completedPoly;
                        }

                        prev = curr;
                    }
                }

                return null;
            }

            private int getSelectedLine(Point p, java.util.List<Point2D> poly) {
                Point2D p1 = poly.get(poly.size() - 1);

                for (int i = 0; i < poly.size(); i++) {
                    Point2D p2 = poly.get(i);
                    double dist = new Line2D.Double(p1, p2).ptLineDist(p);
                    if (dist < distThreshold && (p.x - p1.getX()) * (p.x - p2.getX()) <= pointDistThreshold
                            && (p.y - p1.getY()) * (p.y - p2.getY()) <= pointDistThreshold) {
                        return i;
                    }
                    p1 = p2;
                }

                return -1;
            }

            private boolean isExistingPoint(Point newP) {
                return poligon.stream().map(newP::distance).anyMatch(dist -> dist < distThreshold);
            }
        });
    }

    public void paint(Graphics g) {
        super.paintComponent(g);

        if (poligon.size() > 1) {


            if (poligonIsDone) {
                drawPolygon(g, poligon);
                /*for (java.util.List<Point2D> poligon : poligons) {
                    drawPolygon(g, poligon);
                }*/
            } else {
                Point2D prev = null;
                for (Point2D p : poligon) {
                    if (prev == null) {
                        prev = p;
                    } else {
                        g.drawLine((int)prev.getX(), (int)prev.getY(), (int)p.getX(), (int)p.getY());
                        prev = p;
                    }
                }
            }
        }
    }

    public void drawPolygon(Graphics g, java.util.List<Point2D> poligon) {
        Point2D prev = null;
        for (Point2D p : poligon) {
            if (prev == null) {
                prev = p;
            } else {
                g.drawLine((int)prev.getX(), (int)prev.getY(), (int)p.getX(), (int)p.getY());
                prev = p;
            }
        }

        g.drawLine((int)prev.getX(), (int)prev.getY(), (int)poligon.get(0).getX(), (int)poligon.get(0).getY());
    }
}
