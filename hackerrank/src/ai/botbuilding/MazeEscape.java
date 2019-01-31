package ai.botbuilding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denis_Mironchuk on 1/28/2019.
 */
public class MazeEscape {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        br.readLine();

        List<String> directions = new ArrayList<>();

        String row1 = br.readLine();
        if (row1.charAt(1) != '#') {
            directions.add("UP");
        }
        String row2 = br.readLine();
        if (row2.charAt(0) != '#') {
            directions.add("LEFT");
        }
        if (row2.charAt(2) != '#') {
            directions.add("RIGHT");
        }
        String row3 = br.readLine();
        if (row3.charAt(1) != '#') {
            directions.add("DOWN");
        }

        int exitRow = -1;
        int exitCol = -1;

        for (int col = 0; col < 3; col++) {
            if (row1.charAt(col) == 'e') {
                exitRow = 0;
                exitCol = col;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (row2.charAt(col) == 'e') {
                exitRow = 0;
                exitCol = col;
            }
        }

        for (int col = 0; col < 3; col++) {
            if (row3.charAt(col) == 'e') {
                exitRow = 0;
                exitCol = col;
            }
        }

        if (exitRow != -1) {
            if (exitRow == 0) {
                if (row1.charAt(2) != '#') {
                    System.out.println("UP");
                    return;
                }
            }

            if (exitRow == 1) {
                if (row3.charAt(2) != '#') {
                    System.out.println("DOWN");
                    return;
                }
            }

            if (exitCol == 0) {
                if (row2.charAt(0) != '#') {
                    System.out.println("LEFT");
                    return;
                }
            } else {
                if (row2.charAt(2) != '#') {
                    System.out.println("RIGHT");
                    return;
                }
            }
        }

        for (String move : directions) {
            List<String> moves = new ArrayList<>();
            if (isByTheWall(move, row1, row2,row3)) {
                moves.add(move);
            }

            if (!moves.isEmpty()) {
                directions = moves;
            }
        }

        System.out.println(directions.get((int)(directions.size() * Math.random())));
    }

    private static boolean isByTheWall(String move, String row1, String row2, String row3) {
        switch(move) {
            case "UP":
                return row1.contains("#") || row2.contains("#");
                //return row1.charAt(0) == '#' || row1.charAt(2) == '#';
            case "DOWN":
                //return row3.charAt(0) == '#' || row3.charAt(2) == '#';
                return row3.contains("#") || row2.contains("#");
            case "LEFT":
                return row1.charAt(2) == '#' || row3.charAt(2) == '#' || row1.charAt(1) == '#' || row3.charAt(1) == '#';
            case "RIGHT":
                return row1.charAt(0) == '#' || row3.charAt(0) == '#' || row1.charAt(1) == '#' || row3.charAt(1) == '#';
        }

        return false;
    }
}
