package search.matrixRotate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class KingRichardsKnights {
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

        public Square rotateClockwiseInSquare(int rotateTimes) {
            rotateTimes %= 4;

            if (rotateTimes == 1) {
                long[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_90);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0], point[1] - side + 1, side, outerSquare);
            } else if (rotateTimes == 2) {
                long[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_180);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0] - side + 1, point[1] - side + 1, side, outerSquare);
            } else if (rotateTimes == 3) {
                long[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_270);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0] - side + 1, point[1], side, outerSquare);
            }

            return this;
        }

        public Square rotateConterClockwiseInSquare(int rotateTimes) {
            rotateTimes %= 4;

            if (rotateTimes == 1) {
                long[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_270);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0] - side + 1, point[1], side, outerSquare);
            } else if (rotateTimes == 2) {
                long[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_180);
                point[0] += outerSquare.row;
                point[1] += outerSquare.col;
                return new Square(point[0] - side + 1, point[1] - side + 1, side, outerSquare);
            } else if (rotateTimes == 3) {
                long[] point = rotateRelativePoint(row - outerSquare.row, col - outerSquare.col, outerSquare.side, RotateAngle.ANGLE_90);
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

    private static long[] rotateRelativePoint(long row, long col, long n, RotateAngle angle) {
        switch (angle) {
            case ANGLE_90:
                return new long[]{col, n - 1 - row};
            case ANGLE_180:
                return new long[]{n - 1 - row, n - 1 - col};
            case ANGLE_270:
                return new long[]{n - 1 - col, row};
            default:
                return null;
        }
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
        //while (true) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            n = Integer.parseInt(br.readLine());
            s = Integer.parseInt(br.readLine());

            Square prevSquare = new Square(0, 0, n, null);
            List<Square> squares = generateSquaresFromInput(br, s, prevSquare);
            //List<Square> squares = generateSquaresRandom(s, prevSquare);

            /*int[][] knights = new int[n][n];
            int val = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    knights[i][j] = val;
                    val++;
                }
            }

            printKnights(knights);
            System.out.println("================");
            for (Square sqr : squares) {
                knights = rotateKnights(knights, sqr);
                printKnights(knights);
                System.out.println("================");
            }*/

            List<Square> coSquare = new ArrayList<>();
            coSquare.add(squares.get(0));

            for (int i = 1; i < squares.size(); i++) {
                Square sqr = squares.get(i);
                coSquare.add(sqr.rotateConterClockwiseInSquare(i).moveToOtherSquare(coSquare.get(i - 1)));
            }

            int l = Integer.parseInt(br.readLine());
            Square outSquare = coSquare.get(0);

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
                        point = rotateRelativePoint(row, col, coSqr.side, RotateAngle.ANGLE_90);
                    } else if (rotateTimes == 2) {
                        point = rotateRelativePoint(row, col, coSqr.side, RotateAngle.ANGLE_180);
                    } else if (rotateTimes == 3) {
                        point = rotateRelativePoint(row, col, coSqr.side, RotateAngle.ANGLE_270);
                    }

                    row = point[0] + sqr.row;
                    col = point[1] + sqr.col;
                }

                /*int[] trivialPos = fintKnightPos(knights, num);

                if (trivialPos[0] != row || trivialPos[1] != col) {
                    throw new RuntimeException("My excp!!!");
                }*/

                System.out.printf("%s %s\n", row + 1, col + 1);
            }
        //}
    }

    /*private static int[] fintKnightPos(int[][] knights, int val) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (knights[i][j] == val) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private static void printKnights(int[][] knight) {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("%3d", knight[i][j]);
            }
            System.out.println();
        }
    }

    private static int[][] rotateKnights(int[][] knights, Square square) {
        int[][] knightsRotated = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (isPointInSqare(i, j, square)) {
                    long row = i - square.row;
                    long col = j - square.col;
                    long[] point = rotateRelativePoint(row, col, square.side, RotateAngle.ANGLE_90);
                    point[0] += square.row;
                    point[1] += square.col;
                    knightsRotated[(int)point[0]][(int)point[1]] = knights[i][j];
                } else {
                    knightsRotated[i][j] = knights[i][j];
                }
            }
        }

        return knightsRotated;
    }*/

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
