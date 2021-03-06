package utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HungarianAlgo {

    private static final int N = 50;

    public static void main(String[] args) {
        while (true) {
            int[][] a = new int[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    a[i][j] = 1 + (int) (999 * Math.random());
                    System.out.printf("%3d ", a[i][j]);
                }
                System.out.println();
            }
        /*int[][] a = new int[][] {
                {15, 5, 1, 11, 17},
                {16, 5, 20, 3, 20},
                {4, 1, 19, 14, 5},
                {4, 8, 17, 19, 8},
                {14, 6, 14, 1, 16},
        };*/

            long start = System.currentTimeMillis();
            int[] pairLeft = new int[N];
            int[] pairsRight = new int[N];
            for (int i = 0; i < N; i++) {
                pairLeft[i] = -1;
                pairsRight[i] = -1;
            }

            for (int c = 0; c < N; c++) {
                Set<Integer> leftPlus = new HashSet<>();
                Set<Integer> rightPlus = new HashSet<>();

                int[] processedLeft = new int[N];
                int[] processedRight = new int[N];

                for (int i = 0; i <= c; i++) {
                    if (pairLeft[i] == -1) {
                        dfs(i, a, pairLeft, pairsRight, processedLeft, processedRight, leftPlus, rightPlus, c);
                    }
                }

                boolean increased = false;
                while (!increased) {
                    int delta = Integer.MAX_VALUE;
                    int leftMin = -1;
                    int rightMin = -1;
                    for (int left = 0; left < N; left++) {
                        for (int right = 0; right < N; right++) {
                            if (leftPlus.contains(left) && !rightPlus.contains(right)) {
                                if (a[left][right] < delta) {
                                    delta = a[left][right];
                                    leftMin = left;
                                    rightMin = right;
                                }
                            }
                        }
                    }

                    for (Integer left : leftPlus) {
                        for (int right = 0; right < N; right++) {
                            a[left][right] -= delta;
                        }
                    }

                    for (Integer right : rightPlus) {
                        for (int left = 0; left < N; left++) {
                            a[left][right] += delta;
                        }
                    }

                    if (pairsRight[rightMin] == -1) {
                        List<Integer> path = new ArrayList<>();
                        path.add(rightMin);
                        path.add(leftMin);

                        int rightCurrent = pairLeft[leftMin];
                        int leftCurrent = leftMin;

                        findPathToFree(leftCurrent, rightCurrent, a, pairLeft, leftPlus, new int[N], new int[N], path, true);

                        for (int i = 0; i < path.size() - 1; i += 2) {
                            //add to pair match
                            int pathNode = path.get(i);
                            int nextPathNode = path.get(i + 1);
                            pairsRight[pathNode] = nextPathNode;
                            pairLeft[nextPathNode] = pathNode;
                        }

                        increased = true;
                    } else {
                        rightPlus.add(rightMin);
                        leftPlus.add(pairsRight[rightMin]);
                    }
                }
            }
            long end = System.currentTimeMillis();
            Set<Integer> pairs = new HashSet<>();

            for (int i = 0; i < N; i++) {
                if (pairLeft[i] == -1) {
                    throw new RuntimeException();
                }
                pairs.add(pairLeft[i]);
            }

            if (pairs.size() != N) {
                throw new RuntimeException();
            }

            System.out.println((end - start) + "ms");
        }
    }

    private static boolean findPathToFree(int leftCurrent, int rightCurrent,
                                       int[][] a, int[] pairLeft, Set<Integer> leftPlus,
                                       int[] processedLeft, int[] processedRight,
                                       List<Integer> path, boolean isPair) {
        if (leftCurrent == -1 || rightCurrent == -1) {
            return false;
        }

        processedLeft[leftCurrent] = 1;
        processedRight[rightCurrent] = 1;

        if (isPair) {
            path.add(rightCurrent);
            for (Integer left : leftPlus) {
                if (a[left][rightCurrent] == 0 && pairLeft[left] != rightCurrent && processedLeft[left] == 0) {
                    processedLeft[left] = 1;
                    boolean foundPath = findPathToFree(left, rightCurrent, a, pairLeft, leftPlus, processedLeft, processedRight, path, !isPair);
                    if (foundPath) {
                        return true;
                    }
                }
            }
        } else {
            path.add(leftCurrent);
            if (pairLeft[leftCurrent] == -1) {
                return true;
            }

            if (processedRight[pairLeft[leftCurrent]] == 0) {
                processedRight[pairLeft[leftCurrent]] = 1;
                boolean foundPath = findPathToFree(leftCurrent, pairLeft[leftCurrent], a, pairLeft, leftPlus, processedLeft, processedRight, path, !isPair);

                if (foundPath) {
                    return true;
                }
            }
        }

        path.remove(path.size() - 1);
        return false;
    }

    private static void dfs(int leftNum, int[][] a,
                            int[] pairLeft, int[] pairsRight,
                            int[] processedLeft, int[] processedRight,
                            Set<Integer> leftPlus, Set<Integer> rightPlus,
                            int maxRow) {
        leftPlus.add(leftNum);
        processedLeft[leftNum] = 1;

        for (int rightNum = 0; rightNum < N; rightNum++) {
            if (processedRight[rightNum] == 0 && a[leftNum][rightNum] == 0 && pairLeft[leftNum] != rightNum) {
                rightPlus.add(rightNum);
                processedRight[rightNum] = 1;

                int nextLeft = pairsRight[rightNum];
                if (nextLeft != -1 && processedLeft[nextLeft] == 0 && nextLeft <= maxRow) {
                    dfs(pairsRight[rightNum], a, pairLeft, pairsRight, processedLeft, processedRight, leftPlus, rightPlus, maxRow);
                }
            }
        }
    }
}
