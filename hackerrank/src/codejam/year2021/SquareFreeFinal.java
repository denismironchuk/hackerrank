package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SquareFreeFinal {
    private static int rows = 20;
    private static int cols = 20;

    private static void fillCountFromStream(BufferedReader br, int[] rowSlashes, int[] colSlashes) throws IOException {
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        for (int i = 1; i <= rows; i++) {
            rowSlashes[i] = Integer.parseInt(tkn1.nextToken());
        }
        StringTokenizer tkn2 = new StringTokenizer(br.readLine());
        for (int i = 1; i <= cols; i++) {
            colSlashes[i] = Integer.parseInt(tkn2.nextToken());
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                rows = Integer.parseInt(tkn1.nextToken());
                cols = Integer.parseInt(tkn1.nextToken());

                int[] rowSlashes = new int[rows + 1];
                int[] colSlashes = new int[cols + 1];
                fillCountFromStream(br, rowSlashes, colSlashes);

                int expectedRowsFlow = 0;
                for (int i = 1; i <= rows; i++) {
                    expectedRowsFlow += rowSlashes[i];
                }

                int expectedColsFlow = 0;
                for (int i = 1; i <= cols; i++) {
                    expectedColsFlow += colSlashes[i];
                }

                if (expectedRowsFlow != expectedColsFlow) {
                    System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                    continue;
                }

                // 0 -> source
                // [1 .. rows] -> rows
                // [rows + 1 .. rows + cols] -> cols
                // rows + cols + 1 - dest
                int[][] c = new int[rows + cols + 2][rows + cols + 2];
                int[][] f = new int[rows + cols + 2][rows + cols + 2];

                for (int i = 1; i <= rows; i++) {
                    c[0][i] = rowSlashes[i];
                }
                for (int i = rows + 1; i <= rows + cols; i++) {
                    c[i][rows + cols + 1] = colSlashes[i - rows];
                }
                for (int i = 1; i <= rows; i++) {
                    for (int j = rows + 1; j <= rows + cols; j++) {
                        c[i][j] = 1;
                    }
                }

                int flow = dinic(c, f);

                if (flow != expectedRowsFlow) {
                    System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                    continue;
                }

                System.out.printf("Case #%s: POSSIBLE\n", t);
                char[][] slashes = new char[rows + 1][cols + 1];
                for (int row = 1; row <= rows; row++) {
                    for (int col = 1; col <= cols; col++) {
                        if (f[row][rows + col] == 1) {
                            slashes[row][col] = '/';
                        } else {
                            slashes[row][col] = '\\';
                        }
                    }
                }

                while (sortSlashes(slashes));

                for (int row = 1; row <= rows; row++) {
                    for (int col = 1; col <= cols; col++) {
                        System.out.print(slashes[row][col]);
                    }
                    System.out.println();
                }
            }
        }
    }

    private static boolean sortSlashes(char[][] slashes) {
        boolean wasReordered = false;
        for (int col = 1; col < cols; col++) {
            for (int row = 1; row < rows; row++) {
                if (slashes[row][col] == '/' && slashes[row + 1][col] == '\\') {
                    for (int col2 = col + 1; col2 <= cols; col2++) {
                        if (slashes[row][col2] == '\\' && slashes[row + 1][col2] == '/') {
                            slashes[row][col] = '\\';
                            slashes[row + 1][col] = '/';
                            slashes[row][col2] = '/';
                            slashes[row + 1][col2] = '\\';
                            wasReordered = true;
                            break;
                        }
                    }
                }
            }
        }
        return wasReordered;
    }

    private static boolean bfs(int[][] c, int[][] f, int[] d) {
        Arrays.fill(d, -1);
        int qh = 0, qt = 0;
        int[] q = new int[rows + cols + 2];
        q[qt++] = 0;
        d[0] = 0;
        while (qh < qt) {
            int v = q[qh++];
            for (int to = 0; to < rows + cols + 2; to++) {
                if (d[to] == -1 && f[v][to] < c[v][to]) {
                    q[qt++] = to;
                    d[to] = d[v] + 1;
                }
            }
        }
        return d[rows + cols + 1] != -1;
    }

    private static int dfs(int v, int flow, int[][] c, int[][] f, int[] d, int[] ptr) {
        if (flow == 0) {
            return 0;
        }

        if (v == rows + cols + 1)  {
            return flow;
        }

        for (; ptr[v] < rows + cols + 2; ptr[v]++) {
            int to = ptr[v];
            if (d[to] != d[v] + 1) {
                continue;
            }
            int pushed = dfs (to, Math.min(flow, c[v][to] - f[v][to]), c, f, d, ptr);
            if (pushed != 0) {
                f[v][to] += pushed;
                f[to][v] -= pushed;
                return pushed;
            }
        }
        return 0;
    }

    private static int dinic(int[][] c, int[][] f) {
        int flow = 0;
        int[] d = new int[rows + cols + 2];
        int[] ptr = new int[rows + cols + 2];
        while (true) {
            if (!bfs(c, f, d))  {
                break;
            }
            Arrays.fill(ptr, 0);
            int pushed = 0;
            do {
                pushed = dfs(0, Integer.MAX_VALUE, c, f, d, ptr);
                flow += pushed;
            } while (pushed != 0);
        }
        return flow;
    }
}
