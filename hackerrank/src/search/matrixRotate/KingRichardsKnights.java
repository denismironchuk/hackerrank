package search.matrixRotate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class KingRichardsKnights {
    private enum RotateAngle {
        ANGLE_90, ANGLE_180, ANGLE_270;
    }

    private static class Square {
        private int row;
        private int col;
        private int side;
        private Square outerSquare;

        public Square(int row, int col, int side, Square outerSquare) {
            this.row = row;
            this.col = col;
            this.side = side;
            this.outerSquare = outerSquare;
        }

        public Square rotateClockwiseInSquare(int rotateTimes) {
            rotateTimes %= 4;

            if (rotateTimes == 1) {
                int[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_90);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0], point[1] - side + 1, side, outerSquare);
            } else if (rotateTimes == 2) {
                int[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_180);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0] - side + 1, point[1] - side + 1, side, outerSquare);
            } else if (rotateTimes == 3) {
                int[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_270);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0] - side + 1, point[1], side, outerSquare);
            }

            return this;
        }

        public Square rotateConterClockwiseInSquare(int rotateTimes) {
            rotateTimes %= 4;

            if (rotateTimes == 1) {
                int[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_270);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0] - side + 1, point[1], side, outerSquare);
            } else if (rotateTimes == 2) {
                int[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_180);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0] - side + 1, point[1] - side + 1, side, outerSquare);
            } else if (rotateTimes == 3) {
                int[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_90);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0], point[1] - side + 1, side, outerSquare);
            }

            return this;
        }

        private Square moveToOtherSquare(Square other) {
            return new Square(row - outerSquare.row + other.row, col - outerSquare.col + other.col, side, other);
        }
    }

    private static int[] rotateRelativePoint(int row, int col, int n, RotateAngle angle) {
        switch (angle) {
            case ANGLE_90:
                return new int[]{col, n - 1 - row};
            case ANGLE_180:
                return new int[]{n - 1 - row, n - 1 - col};
            case ANGLE_270:
                return new int[]{n - 1 - col, row};
            default:
                return null;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int s = Integer.parseInt(br.readLine());

        Square prevSquare = new Square(0 ,0, n, null);
        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < s; i++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int row = Integer.parseInt(tkn.nextToken()) - 1;
            int col = Integer.parseInt(tkn.nextToken()) - 1;
            int side = Integer.parseInt(tkn.nextToken()) + 1;

            Square sqr = new Square(row, col, side, prevSquare);
            squares.add(sqr);
            prevSquare = sqr;
        }

        List<Square> coSquare = new ArrayList<>();
        coSquare.add(squares.get(0));

        for (int i = 1; i < s; i++) {
            Square sqr = squares.get(i);
            coSquare.add(sqr.rotateConterClockwiseInSquare(i).moveToOtherSquare(coSquare.get(i - 1)));
        }

        int l = Integer.parseInt(br.readLine());
        Square outSquare = coSquare.get(0);

        for (int i = 0; i < l; i++) {
            int num = Integer.parseInt(br.readLine());
            int row = num / n;
            int col = num % n;

            if (isPointInSqare(row, col, outSquare)) {
                int sqrIndex = findLastIncludingSquare(row, col, coSquare, 0, coSquare.size() - 1);
                Square coSqr = coSquare.get(sqrIndex);
                Square sqr = squares.get(sqrIndex);
                int rotateTimes = (sqrIndex + 1) % 4;
                row -= coSqr.row;
                col -= coSqr.col;
                int[] point = new int[]{0,0};

                if (rotateTimes == 1) {
                    point = rotateRelativePoint(row, col, coSqr.side, RotateAngle.ANGLE_90);
                } else if (rotateTimes == 2) {
                    point = rotateRelativePoint(row, col, coSqr.side, RotateAngle.ANGLE_180);
                } else if (rotateTimes == 3) {
                    point = rotateRelativePoint(row, col, coSqr.side, RotateAngle.ANGLE_270);
                }

                row = point[0] + sqr.row;
                col = point[1] + sqr.col;
            }

            System.out.printf("%s %s\n", row + 1, col + 1);
        }
    }

    private static int findLastIncludingSquare(int row, int col, List<Square> squares, int start, int end) {
        if (start == end) {
            return start;
        }

        int mid = 1 + (start + end) / 2;

        if (isPointInSqare(row, col, squares.get(mid))) {
            return findLastIncludingSquare(row, col, squares, mid, end);
        } else {
            return findLastIncludingSquare(row, col, squares, start, mid - 1);
        }
    }

    private static boolean isPointInSqare(int row, int col, Square sqr) {
        return col >= sqr.col && col <= sqr.col + sqr.side - 1 && row >= sqr.row && row <= sqr.row + sqr.side - 1;
    }
}
