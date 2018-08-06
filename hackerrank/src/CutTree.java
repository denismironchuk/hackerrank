import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class CutTree {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line1 = br.readLine();
        StringTokenizer line1Tkn = new StringTokenizer(line1);
        int n = Integer.parseInt(line1Tkn.nextToken());
        int k = Integer.parseInt(line1Tkn.nextToken());

        int[][] graph = new int[n][n];

        for (int i = 0; i < n - 1; i++) {
            String edge = br.readLine();
            StringTokenizer edgeTokenizer = new StringTokenizer(edge, " ");
            int v1 = Integer.parseInt(edgeTokenizer.nextToken());
            int v2 = Integer.parseInt(edgeTokenizer.nextToken());
            graph[v1 - 1][v2 - 1] = 1;
            graph[v2 - 1][v1 - 1] = 1;
        }

        long subtries = 2;
        long[][][] pairsAmnt = new long[n][n][k + 1];

        pairsAmnt[0][0][0] = 1;

        int[] processed = new int[n];
        processed[0] = 1;

        System.out.println(processTree(0, graph, processed, subtries, pairsAmnt, n, k));

        System.out.println();
    }

    private static long processTree(int v, int[][] graph, int[] processed, long subtries, long[][][] pairsAmnt, int n, int k) {
        long newSubtriesAmnt = subtries;
        for (int i = 0; i < n; i++) {
           if (graph[v][i] == 1 && processed[i] == 0) {
               processed[i] = 1;
               newSubtriesAmnt = addEdge(pairsAmnt, newSubtriesAmnt, v, i, k);
               newSubtriesAmnt = processTree(i, graph, processed, newSubtriesAmnt, pairsAmnt, n, k);
           }
        }
        return newSubtriesAmnt;
    }

    private static long addEdge(long[][][] pairsAmnt, long subtries, int processedVert, int toAddVert, int k) {
        long newSubtriesAmnt = subtries + 1;
        for (int i = 0; i < k; i++) {
            newSubtriesAmnt += pairsAmnt[processedVert][processedVert][i];
        }

        for (int i = 0; i <= k; i++) {
            pairsAmnt[toAddVert][toAddVert][i] = pairsAmnt[processedVert][processedVert][i];
        }

        pairsAmnt[toAddVert][toAddVert][1]++;

        for (int v = 0; v < toAddVert; v++) {
            for (int i = 0; i <= k; i++) {
                pairsAmnt[toAddVert][v][i] = pairsAmnt[processedVert][v][i];
                pairsAmnt[v][toAddVert][i] = pairsAmnt[v][processedVert][i];
            }
        }

        for (int v = 0; v < toAddVert; v++) {
            for (int i = k; i > 0; i--) {
                pairsAmnt[processedVert][v][i] += pairsAmnt[processedVert][v][i - 1];
                if (v != processedVert) {
                    pairsAmnt[v][processedVert][i] += pairsAmnt[v][processedVert][i - 1];
                }
            }
        }

        return newSubtriesAmnt;
    }
}
