package ai.botbuilding.clickomania;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Denis_Mironchuk on 3/29/2019.
 */
public class NextMoveGenerator {


    private char[][] board;
    private int rows;
    private int cols;

    public NextMoveGenerator(char[][] board, int rows, int cols) {
        this.board = board;
        this.rows = rows;
        this.cols = cols;
    }

    public int[] generateNextMove() throws IOException {
        Set<Integer> processedBoards = new HashSet<>();
        List<GameResult> results = getNextPoints(board, null, processedBoards);
        List<GameResult> finalStates = new ArrayList<>();

        int depth = 0;
        while (depth < ClickOMania.DEPTH_LIMIT) {
            depth++;

            List<GameResult> nextResults = new ArrayList<>();

            for (GameResult res : results) {
                List<GameResult> nextStates = getNextPoints(res.getBoard(), res.getPoint(), processedBoards);
                if (nextStates.isEmpty()) {
                    finalStates.add(res);
                } else {
                    nextResults.addAll(nextStates);
                }
            }

            if (nextResults.isEmpty()) {
                break;
            } else {
                nextResults.sort(Comparator.comparingInt(GameResult::getResult));
                results = new ArrayList<>();
                int cnt = 0;
                for (int i = 0;  i < nextResults.size() && cnt < ClickOMania.MAX_DESC; i++) {
                    results.add(nextResults.get(i));
                    cnt++;
                }
            }
        }

        finalStates.addAll(results);
        finalStates.sort(Comparator.comparingInt(GameResult::getResult));

        return finalStates.isEmpty() ? null : finalStates.get(0).getPoint();
    }

    private List<GameResult> getNextPoints(char[][] board, int[] initPoint, Set<Integer> processedBoards) {
        int[][] isles = new int[rows][cols];
        char[] islesColor = new char[rows * cols];
        int[][] islePoint = new int[rows * cols][2];
        List<int[]> sizes = new ArrayList<>();

        BoardUtils.convertToIsles(board, islesColor, isles, islePoint, sizes, rows, cols);

        List<GameResult> gamesResults = new ArrayList<>();
        sizes.sort((o1, o2) -> Integer.compare(countIsleValue(o2), countIsleValue(o1)));

        int processedIsles = 0;
        for (int[] isleSize : sizes) {
            int isle = isleSize[0];

            if (processedIsles > ClickOMania.MAX_ISLE_TO_PROCESS) {
                break;
            }

            if (isleSize[1] == 1) {
                continue;
            }

            char[][] nextBoard = BoardUtils.convertToColors(BoardUtils.removeIsle(isle, isles, rows, cols), islesColor, rows, cols);
            int newNumbers = BoardUtils.convertToIsles(nextBoard, new char[rows * cols], new int[rows][cols], new int[rows * cols][2], rows, cols);

            int[] pointToSet = initPoint == null ? islePoint[isle] : initPoint;
            GameResult grNew = new GameResult(pointToSet, nextBoard, newNumbers);
            if (!processedBoards.contains(grNew.hashCode())) {
                gamesResults.add(grNew);
                processedBoards.add(grNew.hashCode());
            }
            processedIsles++;
        }

        return gamesResults;
    }

    private int countIsleValue(final int[] o2) {
        return o2[1] * ClickOMania.SIZE_FACT + o2[2] * ClickOMania.HEIGHT_FACT;
    }
}
