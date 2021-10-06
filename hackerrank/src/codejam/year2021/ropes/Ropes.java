package codejam.year2021.ropes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Ropes {

    private static int oneSideTreesCnt = 8;

    private static class Line {
        private int leftTree;
        private int rightTree;
        private long hash;

        public Line(int leftTree, int rightTree, long hash) {
            this.leftTree = leftTree;
            this.rightTree = rightTree;
            this.hash = hash;
        }

        public long getHash() {
            return hash;
        }
    }

    private static class Combination {
        private Map<Long, Line> lines = new HashMap<>();
        private int[] occupiedLeft = new int[oneSideTreesCnt];
        private int[] occupiedRight = new int[oneSideTreesCnt];

        public boolean canAdd(Line l) {
            return occupiedLeft[l.leftTree] == 0 && occupiedRight[l.rightTree] == 0;
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

    public static void main(String[] args) {
        List<Combination>[] combinations = new List[oneSideTreesCnt + 1];
        combinations[0] = new ArrayList<>();
        combinations[0].add(new Combination());

        List<Line> allLines = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < oneSideTreesCnt; i++) {
            for (int j = 0; j < oneSideTreesCnt; j++) {
                allLines.add(new Line(i, j, rand.nextLong()));
            }
        }

        for (int i = 1; i <= oneSideTreesCnt; i++) {
            combinations[i] = new ArrayList<>();
            Set<Long> currentCombinations = new HashSet<>();
            for (Combination prevComb : combinations[i - 1]) {
                for (Line line : allLines) {
                    if (prevComb.canAdd(line) && !currentCombinations.contains(prevComb.getHash() + line.getHash())) {
                        Combination newComb = new Combination();
                        prevComb.lines.values().forEach(newComb::addLine);
                        newComb.addLine(line);
                        currentCombinations.add(prevComb.getHash() + line.getHash());
                        combinations[i].add(newComb);
                    }
                }
            }
            System.out.println(i + " - " + combinations[i].size());
        }
    }


}
