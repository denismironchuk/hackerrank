package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class Checksum {

    public static final int MAX_DEFINED = 1000000;
    public static final int MAX_UNDEF = 1000;
    private static int N;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                N = Integer.parseInt(br.readLine());
                int[][] a = new int[N][N];
                for (int i = 0; i < N; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    for (int j = 0; j < N; j++) {
                        a[i][j] = Integer.parseInt(tkn.nextToken());
                    }
                }

                int[][] b = new int[N][N];
                for (int i = 0; i < N; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    for (int j = 0; j < N; j++) {
                        b[i][j] = Integer.parseInt(tkn.nextToken());
                    }
                }

                int[] r = new int[N];
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                for (int i = 0; i < N; i++) {
                    r[i] = Integer.parseInt(tkn1.nextToken());
                }

                int[] c = new int[N];
                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                for (int i = 0; i < N; i++) {
                    c[i] = Integer.parseInt(tkn2.nextToken());
                }

                tryToRestore(a, r, c);

                int[][] bForProc = new int[N][N];

                for (int row = 0; row < N; row++) {
                    for (int col = 0; col < N; col++) {
                        bForProc[row][col] = b[row][col];
                    }
                }

                for (int row = 0; row < N; row++) {
                    for (int col = 0; col < N; col++) {
                        if (bForProc[row][col] == 0) {
                            bForProc[row][col] = MAX_DEFINED;
                        } else {
                            bForProc[row][col] = MAX_UNDEF - b[row][col];
                        }
                    }
                }

                int[] cellsToLeave = hungarianAlgo(bForProc);
                int res = 0;

                for (int row = 0; row < N; row++) {
                    for (int col = 0; col < N; col++) {
                        res += b[row][col];
                    }
                }

                for (int i = 0; i < N; i++) {
                    res -= b[i][cellsToLeave[i]];
                }

                System.out.println(res);
            }
        }
    }

    private static void tryToRestore(int[][] a, int[] r, int[] c) {
        boolean updated = true;
        while (updated) {
            updated = false;

            for (int row = 0; row < N; row++) {
                int undefinedCnt = 0;
                int undefCol = -1;
                int oneCnt = 0;
                for (int col = 0; col < N; col++) {
                    if (a[row][col] == -1) {
                        undefinedCnt++;
                        undefCol = col;
                    } else {
                        oneCnt += a[row][col];
                    }
                }

                if (undefinedCnt == 1) {
                    if (oneCnt % 2 == r[row]) {
                        a[row][undefCol] = 0;
                    } else {
                        a[row][undefCol] = 1;
                    }
                    updated = true;
                }
            }

            for (int col = 0; col < N; col++) {
                int undefinedCnt = 0;
                int undefRow = -1;
                int oneCnt = 0;
                for (int row = 0; row < N; row++) {
                    if (a[row][col] == -1) {
                        undefinedCnt++;
                        undefRow = row;
                    } else {
                        oneCnt += a[row][col];
                    }
                }

                if (undefinedCnt == 1) {
                    if (oneCnt % 2 == c[col]) {
                        a[undefRow][col] = 0;
                    } else {
                        a[undefRow][col] = 1;
                    }
                    updated = true;
                }
            }
        }
    }

    private static int[] hungarianAlgo(int[][] a) {
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
        return pairLeft;
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
