package codejam.year2022.round1A;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Weightlifting {

    private static int e;
    private static int w;
    private static int[][] excrs;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                e = Integer.parseInt(tkn1.nextToken());
                w = Integer.parseInt(tkn1.nextToken());
                excrs = new int[e][w];
                for (int i = 0; i < e; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    for (int j = 0; j < w; j++) {
                        excrs[i][j] = Integer.parseInt(tkn2.nextToken());
                    }
                }

                int[][] commonHeight = new int[e][e];

                for (int i = 0; i < e; i++) {
                    for (int w1 = 0; w1 < w; w1++) {
                        int minW = Integer.MAX_VALUE;
                        for (int len = 0; len + i < e; len++) {
                            minW = Math.min(minW, excrs[i + len][w1]);
                            commonHeight[i][i + len] += minW;
                        }
                    }
                }

                int[][] minMoves = new int[e][e];

                for (int i = 0; i < e; i++) {
                    for (int j = 0; j < e; j++) {
                        if (i != j) {
                            minMoves[i][j] = Integer.MAX_VALUE;
                        }
                    }
                }

                for (int i = 0; i < e; i++) {
                    for (int j = 0; j < w; j++) {
                        minMoves[i][i] += 2 * excrs[i][j];
                    }
                }

                for (int len = 2; len <= e; len++) {
                    for (int start = 0; start <= e - len; start++) {
                        for (int innerPos = 1; innerPos < len; innerPos++) {
                            int comHeihgt = commonHeight[start][start + len - 1];

                            minMoves[start][start + len - 1] = Math.min(minMoves[start][start + len - 1],
                                    minMoves[start][start + innerPos - 1] - comHeihgt +
                                            minMoves[start + innerPos][start + len - 1] - comHeihgt
                                    );
                        }
                    }
                }

                System.out.printf("Case #%s: %s\n", t, minMoves[0][e - 1]);
            }
        }
    }
}
