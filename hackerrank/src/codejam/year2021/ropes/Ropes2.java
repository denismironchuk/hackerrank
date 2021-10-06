package codejam.year2021.ropes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

public class Ropes2 {

    private static int oneSideTreesCnt = 8;
    private static List<Line> allLines = new ArrayList<>();
    private static Line[][] allLinesMatrix = new Line[oneSideTreesCnt][oneSideTreesCnt];

    private static class Line {
        private int leftTree;
        private int rightTree;
        private long hash;

        public Line(int rightTree, int leftTree, long hash) {
            this.leftTree = leftTree;
            this.rightTree = rightTree;
            this.hash = hash;
        }

        public long getHash() {
            return hash;
        }

        @Override
        public String toString() {
            return "Line{" + rightTree + ", " + leftTree + "}";
        }
    }

    private static class Combination {
        private Map<Long, Line> lines = new HashMap<>();
        private int[] occupiedLeft = new int[oneSideTreesCnt];
        private int[] occupiedRight = new int[oneSideTreesCnt];

        public boolean canAdd(Line l) {
            return occupiedLeft[l.leftTree] == 0 && occupiedRight[l.rightTree] == 0;
        }

        public int getIntercectingLines(Line l) {
            int res = 0;
            for (Line line : lines.values()) {
                if ((l.rightTree < line.rightTree && l.leftTree > line.leftTree) ||
                        (l.rightTree > line.rightTree && l.leftTree < line.leftTree)) {
                    res++;
                }
            }
            return res;
        }

        public void addLine(Line l) {
            lines.put(l.getHash(), l);
            occupiedLeft[l.leftTree] = 1;
            occupiedRight[l.rightTree] = 1;
        }

        public void removeLine(Line l) {
            lines.remove(l.getHash());
            occupiedLeft[l.leftTree] = 0;
            occupiedRight[l.rightTree] = 0;
        }

        private long getHash() {
            long hash = 0;
            for (Long h : lines.keySet()) {
                hash += h;
            }
            return hash;
        }
    }

    public static void main(String[] args) throws IOException {
        List<Combination>[] combinations = new List[oneSideTreesCnt + 1];
        combinations[0] = new ArrayList<>();
        combinations[0].add(new Combination());

        Random rand = new Random();
        for (int i = 0; i < oneSideTreesCnt; i++) {
            for (int j = 0; j < oneSideTreesCnt; j++) {
                Line line = new Line(i, j, rand.nextLong());
                allLines.add(line);
                allLinesMatrix[i][j] = line;
            }
        }

        Map<Long, Integer> combinationScore = new HashMap<>();

        long startTime = System.currentTimeMillis();
        processState(new Combination(), combinationScore, true, true);
        long endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime + "ms");

        Combination currentState = new Combination();
        int score = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            for (int move = 0; move < oneSideTreesCnt; move+=2) {
                /*Line bestChoiseLine = null;
                int bestChoiseScore = Integer.MIN_VALUE;
                for (Line nextLine : allLines) {
                    if (currentState.canAdd(nextLine)) {
                        long nextStateHash = currentState.getHash() + nextLine.getHash();
                        int optimalScore = score + currentState.getIntercectingLines(nextLine) + combinationScore.get(nextStateHash);
                        if (optimalScore > bestChoiseScore) {
                            bestChoiseScore = optimalScore;
                            bestChoiseLine = nextLine;
                        }
                    }
                }

                System.out.println(bestChoiseLine.rightTree + " " + bestChoiseLine.leftTree + "; best choise score = " + bestChoiseScore);
                score += currentState.getIntercectingLines(bestChoiseLine);
                System.out.println("Score = " + score);

                currentState.addLine(bestChoiseLine);*/

                System.out.println("Please enter your move...");
                boolean isValidLine = false;
                Line line = null;
                while (!isValidLine) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    int rightTree = Integer.parseInt(tkn.nextToken());
                    int leftTree = Integer.parseInt(tkn.nextToken());
                    line = allLinesMatrix[rightTree][leftTree];
                    isValidLine = currentState.canAdd(line);
                    if (!isValidLine) {
                        System.out.println("Invalid line!!! Please enter another one.");
                    }
                }
                score += currentState.getIntercectingLines(line);
                System.out.println("Score = " + score);

                currentState.addLine(line);

                Line bestChoiseLine = null;
                int bestChoiseScore = Integer.MAX_VALUE;
                for (Line nextLine : allLines) {
                    if (currentState.canAdd(nextLine)) {
                        long nextStateHash = currentState.getHash() + nextLine.getHash();
                        int optimalScore = score - currentState.getIntercectingLines(nextLine) + combinationScore.get(nextStateHash);
                        if (optimalScore < bestChoiseScore) {
                            bestChoiseScore = optimalScore;
                            bestChoiseLine = nextLine;
                        }
                    }
                }

                System.out.println(bestChoiseLine.rightTree + " " + bestChoiseLine.leftTree + "; best choise score = " + bestChoiseScore);
                score -= currentState.getIntercectingLines(bestChoiseLine);
                System.out.println("Score = " + score);

                currentState.addLine(bestChoiseLine);
            }
        }

        System.out.println();
    }

    private static void processState(Combination state, Map<Long, Integer> combinationScore, boolean firstPlayer, boolean print) {
        int score = firstPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Line line : allLines) {
            /*if (print) {
                System.out.println(line);
            }*/
            if (state.canAdd(line)) {
                long nextStateHash = state.getHash() + line.getHash();
                if (!combinationScore.containsKey(nextStateHash)) {
                    state.addLine(line);
                    processState(state, combinationScore, !firstPlayer, false);
                    state.removeLine(line);
                }
                int scoreToAdd = state.getIntercectingLines(line);
                int nextStateScore = combinationScore.get(nextStateHash);
                if (firstPlayer) {
                    score = Math.max(score, nextStateScore + scoreToAdd);
                } else {
                    score = Math.min(score, nextStateScore - scoreToAdd);
                }
            }
        }

        if (score == Integer.MIN_VALUE || score == Integer.MAX_VALUE) {
            score = 0;
        }
        combinationScore.put(state.getHash(), score);
    }
}
