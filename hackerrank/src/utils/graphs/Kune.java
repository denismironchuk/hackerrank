package utils.graphs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class Kune {
        public static void main(String[] args) throws IOException {
        //Node[] nodes = GraphGenerator.generateFromInputStream();
        List<Node> nodes = GraphGenerator.generateRandomBipartiteGraph(1000, 1000, 50000);
        int n = nodes.size();
        int[] pair = new int[n];
        Arrays.fill(pair, -1);

        for (Node nd : nodes) {
            if (pair[nd.getNum()] == -1) {
                increasePath(nd, new int[n], pair, nodes);
            }
        }
    }

    private static boolean increasePath(Node nd, int[] processed, int[] pair, List<Node> nodes) {
        int ndNum = nd.getNum();
        processed[ndNum] = 1;

        for (Node neigh : nd.getNeighbours()) {
            int neighNum = neigh.getNum();

            if (processed[neighNum] == 1) {
                continue;
            }

            processed[neighNum] = 1;

            if (pair[neighNum] == -1 || increasePath(nodes.get(pair[neighNum]), processed, pair, nodes)) {
                pair[neighNum] = ndNum;
                pair[ndNum] = neighNum;
                return true;
            }
        }

        return false;
    }
}
