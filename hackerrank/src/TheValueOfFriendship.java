import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 10/1/2018.
 */
public class TheValueOfFriendship {
    static class DisjointSet {
        private int[] parents;
        private int[] rank;

        public DisjointSet(int n) {
            parents = new int[n];
            rank = new int[n];
        }

        public void makeSet(int x) {
            parents[x] = x;
        }

        public int find(int x) {
            if (parents[x] == x) {
                return x;
            } else {
                parents[x] = find(parents[x]);
                return parents[x];
            }
        }

        public void unite(int x, int y) {
            int px = find(x);
            int py = find(y);

            if (rank[px] > rank[py]) {
                parents[py] = px;
            } else {
                parents[px] = py;
                if (rank[px] == rank[py]) {
                    rank[py]++;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0 ; q < Q; q++) {
            StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(line1Tkn.nextToken());
            int m = Integer.parseInt(line1Tkn.nextToken());

            DisjointSet dSet = new DisjointSet(n);
            long[] conCompSize = new long[n];

            for (int i = 0; i < n; i++) {
                dSet.makeSet(i);
                conCompSize[i] = 1;
            }

            long localRes = 0;
            long res = 0;
            for (int i = 0; i < m; i++) {
                StringTokenizer friendTkn = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(friendTkn.nextToken()) - 1;
                int y = Integer.parseInt(friendTkn.nextToken()) - 1;

                int xParent = dSet.find(x);
                int yParent = dSet.find(y);

                if (xParent != yParent) {
                    long xSize = conCompSize[xParent];
                    long ySize = conCompSize[yParent];

                    localRes -= (xSize * (xSize - 1));
                    localRes -= (ySize * (ySize - 1));

                    dSet.unite(x, y);

                    conCompSize[dSet.find(x)] = xSize + ySize;

                    localRes += (xSize + ySize) * (xSize + ySize - 1);
                }
                res += localRes;
            }

            System.out.println(res);
        }
    }
}
