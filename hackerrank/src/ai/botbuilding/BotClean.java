package ai.botbuilding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 1/28/2019.
 */
public class BotClean {
    private static final int SIDE = 5;

    private static class Point {
        private int row;
        private int col;

        public Point(final int row, final int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public void setRow(final int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(final int col) {
            this.col = col;
        }

        public int manhDist(Point p2) {
            return Math.abs(row - p2.row) + Math.abs(col - p2.col);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer botPosTkn = new StringTokenizer(br.readLine());
        int botRow = Integer.parseInt(botPosTkn.nextToken());
        int botCol = Integer.parseInt(botPosTkn.nextToken());

        Point botPos = new Point(botRow, botCol);

        List<Point> dirtyCells = new ArrayList<>();

        for (int row = 0; row < SIDE; row++) {
            String rowStr = br.readLine();
            int col = 0;
            for (char c : rowStr.toCharArray()) {
                if (c == 'd') {
                    if (row == botRow && col == botCol) {
                        System.out.println("CLEAN");
                        return;
                    } else {
                        dirtyCells.add(new Point(row, col));
                    }
                }
                col++;
            }
        }

        dirtyCells.sort(Comparator.comparingInt(p -> p.manhDist(botPos)));

        Point dest = dirtyCells.get(0);

        String horizontal = botCol < dest.getCol() ? "RIGHT" : "LEFT";
        String vertical = botRow < dest.getRow() ? "DOWN" : "UP";

        if (Math.abs(botCol - dest.getCol()) != 0) {
            System.out.println(horizontal);
        } else if (Math.abs(botRow - dest.getRow()) != 0) {
            System.out.println(vertical);
        }
    }
}
