package ai.botbuilding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

public class BotCleanPrtiallyObservable {
    private static final int SIDE = 5;
    private static class Point {
        private int row;
        private int col;

        public Point(final int row, final int col) {
            this.row = row;
            this.col = col;
        }

        public int manhDist(Point p) {
            return Math.abs(row - p.row) + Math.abs(col - p.col);
        }

        @Override
        public boolean equals(final Object o) {
            Point point = (Point) o;
            if (row != point.row) return false;
            return col == point.col;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer posTkn = new StringTokenizer(br.readLine());
        int initRow = Integer.parseInt(posTkn.nextToken());
        int initCol = Integer.parseInt(posTkn.nextToken());

        Point initPoint = new Point(initRow, initCol);

        List<Point> dirtPoints = new ArrayList<>();
        List<Point> unknownPoints = new ArrayList<>();

        char[][] prevState;
        File stateFile = new File("my_state_file");
        if (!stateFile.exists()) {
            prevState = new char[][] {{'o', 'o', 'o', 'o', 'o'}, {'o', 'o', 'o', 'o', 'o'},
                    {'o', 'o', 'o', 'o', 'o'}, {'o', 'o', 'o', 'o', 'o'}, {'o', 'o', 'o', 'o', 'o'}};
        } else {
            BufferedReader stateReader = new BufferedReader(new FileReader(stateFile));
            prevState = new char[SIDE][SIDE];
            for (int i = 0; i < SIDE; i++) {
                prevState[i] = stateReader.readLine().toCharArray();
            }
            stateReader.close();
            stateFile.delete();
        }

        boolean cleanPerformed = false;
        char[][] currentState = new char[SIDE][SIDE];

        for (int row = 0; row < SIDE; row++) {
            String line = br.readLine();
            for (int col = 0; col < SIDE; col++) {
                char c = line.charAt(col);
                currentState[row][col] = c == 'b' ? '-' : c;
            }
        }

        mergeWithPrev(currentState, prevState);

        for (int row = 0; row < SIDE; row++) {
            for (int col = 0; col < SIDE; col++) {
                if (currentState[row][col] == 'd') {
                    Point dirtPoint = new Point(row, col);
                    if (dirtPoint.equals(initPoint)) {
                        currentState[row][col] = '-';
                        cleanPerformed = true;
                    } else {
                        dirtPoints.add(dirtPoint);
                    }
                } else if (currentState[row][col] == 'o') {
                    unknownPoints.add(new Point(row, col));
                }
            }
        }

        writeStateToFile(currentState, stateFile);

        if (cleanPerformed) {
            System.out.println("CLEAN");
            return;
        }

        if (!dirtPoints.isEmpty()) {
            dirtPoints.sort(Comparator.comparingInt(p -> p.manhDist(initPoint)));
            Point dest = dirtPoints.get(0);

            if (dest.row != initPoint.row) {
                System.out.println(dest.row < initPoint.row ? "UP" : "DOWN");
            } else {
                System.out.println(dest.col < initPoint.col ? "LEFT" : "RIGHT");
            }
            return;
        } else if (!unknownPoints.isEmpty()) {
            unknownPoints.sort(Comparator.comparingInt(p -> p.manhDist(initPoint)));
            Point dest = unknownPoints.get(0);

            if (dest.row != initPoint.row) {
                System.out.println(dest.row < initPoint.row ? "UP" : "DOWN");
            } else {
                System.out.println(dest.col < initPoint.col ? "LEFT" : "RIGHT");
            }
            return;
        }
    }

    private static void writeStateToFile(char[][] currState, File stateFile) throws IOException {
        stateFile.createNewFile();
        BufferedWriter wr = new BufferedWriter(new FileWriter(stateFile));
        for (int row = 0; row < SIDE; row++) {
            wr.write(currState[row]);
            wr.newLine();
        }
        wr.flush();
        wr.close();
    }

    private static void mergeWithPrev(char[][] currState, char[][] prevObservs) {
        for (int row = 0; row < SIDE; row++) {
            for (int col = 0; col < SIDE; col++) {
                if (currState[row][col] == 'o' && prevObservs[row][col] != 'o') {
                    currState[row][col] = prevObservs[row][col];
                }
            }
        }
    }
}
