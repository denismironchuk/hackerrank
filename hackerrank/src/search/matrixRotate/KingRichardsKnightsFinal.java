package search.matrixRotate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

public class KingRichardsKnightsFinal {
    private static long n = 10;
    private static int s = 10;
    private static int l = 10;

    private enum RotateAngle {
        ANGLE_90, ANGLE_180, ANGLE_270;
    }

    private static class Square {
        private long row;
        private long col;
        private long side;
        private Square outerSquare;

        public Square(long row, long col, long side, Square outerSquare) {
            this.row = row;
            this.col = col;
            this.side = side;
            this.outerSquare = outerSquare;
        }

        public void rotateClockwiseInSquare(int rotateTimes) {
            rotateTimes %= 4;

            if (rotateTimes == 1) {
                long[] point = rotate90(row - outerSquare.row, col - outerSquare.col, outerSquare.side);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;

                row = point[0];
                col = point[1] - side + 1;
            } else if (rotateTimes == 2) {
                long[] point = rotate180(row - outerSquare.row, col - outerSquare.col, outerSquare.side);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;

                row = point[0] - side + 1;
                col = point[1] - side + 1;
            } else if (rotateTimes == 3) {
                long[] point = rotate270(row - outerSquare.row, col - outerSquare.col, outerSquare.side);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;

                row = point[0] - side + 1;
                col = point[1];
            }
        }

        public void rotateConterClockwiseInSquare(int rotateTimes) {
            rotateTimes %= 4;

            if (rotateTimes == 1) {
                long[] point = rotate270(row - outerSquare.row, col - outerSquare.col, outerSquare.side);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;

                row = point[0] - side + 1;
                col = point[1];
            } else if (rotateTimes == 2) {
                long[] point = rotate180(row - outerSquare.row, col - outerSquare.col, outerSquare.side);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;

                row = point[0] - side + 1;
                col = point[1] - side + 1;
            } else if (rotateTimes == 3) {
                long[] point = rotate90(row - outerSquare.row, col - outerSquare.col, outerSquare.side);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;

                row = point[0];
                col = point[1] - side + 1;
            }
        }

        private void moveToOtherSquare(Square other) {
            row = row - outerSquare.row + other.row;
            col = col - outerSquare.col + other.col;
            outerSquare = other;
        }

        public Square clone() {
            return new Square(row, col, side, outerSquare);
        }
    }

    private static long[] rotate90(long row, long col, long n) {
        return new long[]{col, n - 1 - row};
    }

    private static long[] rotate180(long row, long col, long n) {
        return new long[]{n - 1 - row, n - 1 - col};
    }

    private static long[] rotate270(long row, long col, long n) {
        return new long[]{n - 1 - col, row};
    }

    private static List<Square> generateSquaresFromInput(BufferedReader br, int s, Square prevSquare) throws IOException {
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

        return squares;
    }

    private static List<Square> generateSquaresRandom(int s, Square prevSquare) {
        List<Square> squares = new ArrayList<>();

        for (int i = 0; i < s; i++) {
            long row = prevSquare.row + (int)(Math.random() * (prevSquare.side / 2));
            long col = prevSquare.col + (int)(Math.random() * (prevSquare.side / 2));
            int side = 1 + (int)(Math.random() * (prevSquare.side - Math.max(row - prevSquare.row, col - prevSquare.col) - 1));

            Square sqr = new Square(row, col, side, prevSquare);
            squares.add(sqr);
            prevSquare = sqr;

            if (side <= 1) {
                break;
            }
        }

        return squares;
    }

    public static void main(String[] args) throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Date start = new Date();
        BufferedReader br = new BufferedReader(new FileReader("/Users/denis_mironchuk/Downloads/input01.txt"));
        n = Integer.parseInt(br.readLine());
        s = Integer.parseInt(br.readLine());

        Square prevSquare = new Square(0, 0, n, null);
        List<Square> squares = generateSquaresFromInput(br, s, prevSquare);
        //List<Square> squares = generateSquaresRandom(s, prevSquare);

        List<Square> coSquare = new ArrayList<>();
        for (Square sqr : squares) {
            coSquare.add(sqr.clone());
        }

        for (int i = 1; i < squares.size(); i++) {
            Square coSqr = coSquare.get(i);
            coSqr.rotateConterClockwiseInSquare(i);
            coSqr.moveToOtherSquare(coSquare.get(i - 1));
        }

        int l = Integer.parseInt(br.readLine());
        Square outSquare = coSquare.get(0);

        StringBuilder res = new StringBuilder();
        for (int i = 0; i < l; i++) {
            long num = Long.parseLong(br.readLine());
            //int num = (int)(Math.random() * (n * n));
            long row = num / n;
            long col = num % n;

            if (isPointInSqare(row, col, outSquare)) {
                int sqrIndex = findLastIncludingSquare(row, col, coSquare, 0, coSquare.size() - 1);
                Square coSqr = coSquare.get(sqrIndex);
                Square sqr = squares.get(sqrIndex);
                int rotateTimes = (sqrIndex + 1) % 4;
                row -= coSqr.row;
                col -= coSqr.col;
                long[] point = new long[]{row, col};

                if (rotateTimes == 1) {
                    point = rotate90(row, col, coSqr.side);
                } else if (rotateTimes == 2) {
                    point = rotate180(row, col, coSqr.side);
                } else if (rotateTimes == 3) {
                    point = rotate270(row, col, coSqr.side);
                }

                row = point[0] + sqr.row;
                col = point[1] + sqr.col;
            }

            res.append(String.format("%s %s\n", row + 1, col + 1));
        }

        System.out.println(res);
        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static int findLastIncludingSquare(long row, long col, List<Square> squares, int start, int end) {
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

    private static boolean isPointInSqare(long row, long col, Square sqr) {
        return col >= sqr.col && col <= sqr.col + sqr.side - 1 && row >= sqr.row && row <= sqr.row + sqr.side - 1;
    }
}
