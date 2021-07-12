package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Retiling {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int r = Integer.parseInt(tkn.nextToken());
                int c = Integer.parseInt(tkn.nextToken());
                int f = Integer.parseInt(tkn.nextToken());
                int s = Integer.parseInt(tkn.nextToken());

                int[][] src = new int[r][c];
                int srcCnt = 0;
                int[][] dest = new int[r][c];
                int destCnt = 0;

                for (int i = 0; i < r; i++) {
                    String row = br.readLine();
                    for (int j = 0; j < c; j++) {
                        if (row.charAt(j) == 'G') {
                            src[i][j] = 1;
                            srcCnt++;
                        }
                    }
                }

                for (int i = 0; i < r; i++) {
                    String row = br.readLine();
                    for (int j = 0; j < c; j++) {
                        if (row.charAt(j) == 'G') {
                            dest[i][j] = 1;
                            destCnt++;
                        }
                    }
                }

                int n = Math.max(srcCnt, destCnt);
                int[][] a = new int[n + 1][n + 1];

                int srcIndex = 1;
                for (int srcRow = 0; srcRow < r; srcRow++) {
                    for (int srcCol = 0; srcCol < c; srcCol++) {
                        if (src[srcRow][srcCol] == 1) {
                            int destIndex = 1;
                            for (int destRow = 0; destRow < r; destRow++) {
                                for (int destCol = 0; destCol < c; destCol++) {
                                    if (dest[destRow][destCol] == 1) {
                                        a[srcIndex][destIndex] = Math.min(2 * f, s * (Math.abs(srcRow - destRow) + Math.abs(srcCol - destCol)));
                                        destIndex++;
                                    }
                                }
                            }
                            srcIndex++;
                        }
                    }
                }

                if (srcCnt < destCnt) {
                    for (int index1 = srcCnt + 1; index1 <= destCnt; index1++) {
                        for (int index2 = 1; index2 <= destCnt; index2++) {
                            a[index1][index2] = f;
                        }
                    }
                } else if (srcCnt > destCnt) {
                    for (int index1 = 1; index1 <= srcCnt; index1++) {
                        for (int index2 = destCnt + 1; index2 <= srcCnt; index2++) {
                            a[index1][index2] = f;
                        }
                    }
                }

                int[] u = new int[n + 1];
                int[] v = new int[n + 1];
                int[] p = new int[n + 1];
                int[] way = new int[n + 1];

                for (int i = 1; i <= n; i++) {
                    p[0] = i;
                    int j0 = 0;
                    int[] minv = new int[n + 1];
                    Arrays.fill(minv, Integer.MAX_VALUE);
                    int[] used = new int[n + 1];

                    do {
                        used[j0] = 1;
                        int i0 = p[j0],  delta = Integer.MAX_VALUE,  j1 = 0;
                        for (int j = 1; j <= n; j++) {
                            if (used[j] == 0) {
                                int cur = a[i0][j] - u[i0] - v[j];
                                if (cur < minv[j]) {
                                    minv[j] = cur;
                                    way[j] = j0;
                                }
                                if (minv[j] < delta) {
                                    delta = minv[j];
                                    j1 = j;
                                }
                            }
                        }

                        for (int j = 0; j <= n; j++) {
                            if (used[j] == 1) {
                                u[p[j]] += delta;
                                v[j] -= delta;
                            } else {
                                minv[j] -= delta;
                            }
                        }
                        j0 = j1;
                    } while (p[j0] != 0);

                    do {
                        int j1 = way[j0];
                        p[j0] = p[j1];
                        j0 = j1;
                    } while (j0 != 0);
                }

                System.out.printf("Case #%s: %s\n", t, -v[0]);
            }
        }
    }
}
