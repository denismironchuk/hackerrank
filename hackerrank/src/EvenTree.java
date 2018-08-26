import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Влада on 26.08.2018.
 */
public class EvenTree {
    static class Node {
        private int num;
        private Node parent;
        private List<Node> children = new ArrayList<>();

        public Node(int num) {
            this.num = num;
        }

        public void addNeighbour() {

        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int nodes = Integer.parseInt(tkn1.nextToken());
        int edges = Integer.parseInt(tkn1.nextToken());

        int[][] matr = new int[nodes][nodes];

        for (int i = 0; i < edges; i++) {
            StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
            int node1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
            int node2 = Integer.parseInt(edgeTkn.nextToken()) - 1;

            matr[node1][node2] = 1;
            matr[node2][node1] = 1;
        }


    }
}