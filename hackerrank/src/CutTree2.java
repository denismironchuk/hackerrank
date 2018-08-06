import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CutTree2 {
    private int k;
    private int[][] graph;
    private int n;

    private class Verticle {
        int num;
        int parentNum;

        long subtriesWithoutN;
        long[] subtriesWithNDistribution;

        public Verticle(final int num, int parentNum) {
            this.num = num;
            this.parentNum = parentNum;

            this.subtriesWithoutN = 0;

            int children = 0;
            for (int i = 0; i < graph.length; i++) {
                if (graph[num][i] == 1 && i != parentNum) {
                    children++;
                }
            }
            this.subtriesWithNDistribution = new long[n + 1];
            this.subtriesWithNDistribution[children] = 1;
        }

        public void processChildren() {
            for (int i = 0; i < graph.length; i++) {
                if (graph[i][num] == 1 && i != parentNum) {
                    Verticle v = new Verticle(i, num);
                    v.processChildren();
                    addVerticle(v);
                }
            }
        }

        public void addVerticle(Verticle v) {
            subtriesWithoutN += v.subtriesWithoutN;
            for (int i = 0; i < k; i++) {
                subtriesWithoutN += v.subtriesWithNDistribution[i];
            }

            long[] subtriesWithNDistributionNew = new long[n + 1];
            for (int i = 1; i <= n; i++) {
                for (int j = 0; j <= n; j++) {
                    if (i - 1 + j <= n) {
                        subtriesWithNDistributionNew[i - 1 + j] += subtriesWithNDistribution[i] * v.subtriesWithNDistribution[j];
                    }
                }
            }

            for (int i = 0; i <= n; i++) {
                subtriesWithNDistribution[i] += subtriesWithNDistributionNew[i];
            }
        }
    }

    private void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line1 = br.readLine();
        StringTokenizer line1Tkn = new StringTokenizer(line1);
        n = Integer.parseInt(line1Tkn.nextToken());

        k = Integer.parseInt(line1Tkn.nextToken());
        graph = new int[n][n];

        for (int i = 0; i < n - 1; i++) {
            String edge = br.readLine();
            StringTokenizer edgeTokenizer = new StringTokenizer(edge, " ");
            int v1 = Integer.parseInt(edgeTokenizer.nextToken());
            int v2 = Integer.parseInt(edgeTokenizer.nextToken());
            graph[v1 - 1][v2 - 1] = 1;
            graph[v2 - 1][v1 - 1] = 1;
        }

        Verticle root = new Verticle(0, -1);
        root.processChildren();

        long result = root.subtriesWithoutN + 1;
        for (int i = 0; i <= k; i++) {
            result += root.subtriesWithNDistribution[i];
        }
        System.out.println(result);
    }

    public static void main(String[] args) throws IOException {
        new CutTree2().run();
    }
}
