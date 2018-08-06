import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HackerRankCityTrivial {
    private class Edge {
        int v1;
        int v2;
        int dist;

        public Edge(final int v1, final int v2, final int dist) {
            this.v1 = v1;
            this.v2 = v2;
            this.dist = dist;
        }
    }

    private int getDist(int start, int end, int[][] matrix, int[] visited) {
        int res = 0;
        for (int i = 0; i < visited.length; i++) {
            if (visited[i] == 0 && matrix[start][i] != 0){
                if (i == end) {
                    return matrix[start][i];
                } else {
                    visited[i] = 1;
                    int dist = getDist(i, end, matrix, visited);
                    if (0 != dist) {
                        return  dist + matrix[start][i];
                    }
                }
            }
        }
        return 0;
    }

    private int findAllDistsSum(int startVert, int[][] matrix, int distFromStart, int[] visited) {
        int res = distFromStart;
        for (int i = 0; i < visited.length; i++) {
            if (visited[i] == 0 && matrix[startVert][i] != 0) {
                visited[i] = 1;
                res += findAllDistsSum(i, matrix, distFromStart + matrix[startVert][i], visited);
            }
        }
        return res;
    }

    public void run() {
        List<Edge> edges = new ArrayList<>();
        edges.add(new Edge(0,2,2));
        edges.add(new Edge(1,3,2));
        edges.add(new Edge(2,3,2));
        edges.add(new Edge(2,4,2));
        edges.add(new Edge(3,5,2));

        edges.add(new Edge(6,8,2));
        edges.add(new Edge(7,9,2));
        edges.add(new Edge(8,9,2));
        edges.add(new Edge(8,10,2));
        edges.add(new Edge(9,11,2));

        edges.add(new Edge(5, 12, 1));
        edges.add(new Edge(10, 13, 1));

        edges.add(new Edge(12, 13, 1));

        int verts = 14;
        //int verts = 6;

        int[][] matrix = new int[verts][verts];

        for (Edge edge : edges) {
            matrix[edge.v1][edge.v2] = edge.dist;
            matrix[edge.v2][edge.v1] = edge.dist;
        }

        int res = 0;
        for (int i = 0; i < verts; i++) {
        //for (int i = 0; i < 1; i++) {
            for (int j = i + 1; j < verts; j++) {
                int[] visited = new int[verts];
                visited[i] = 1;
                res += getDist(i, j, matrix, visited);
            }
        }

        System.out.println(res);
    }

    public static void main(String[] args) throws IOException {
        new HackerRankCityTrivial().run();
    }
}
