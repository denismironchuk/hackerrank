package utils.graphs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class Kune2 {
        public static void main(String[] args) throws IOException {
        List<Node> nodes = GraphGenerator.generateFromInputStream();
        //List<Node> nodes = GraphGenerator.generateRandomBipartiteGraph(1000, 1000, 50000);
        int n = nodes.size();
        int[] pair = new int[n];
        Arrays.fill(pair, -1);

        for (Node nd : nodes) {
            if (pair[nd.getNum()] == -1) {
                increasePath(nd, new int[n], pair, nodes, -1, new int[n]);
            }
        }

        System.out.println();
    }

    private static boolean increasePath(Node nd, int[] processed, int[] pair, List<Node> nodes, int nodeType, int[] parents) {
        int ndNum = nd.getNum();
        processed[ndNum] = nodeType;

        if (nodeType == -1) {
            for (Node neigh : nd.getNeighbours()) {
                int neighNum = neigh.getNum();

                if (processed[neighNum] != 0) {
                    continue;
                }

                parents[neighNum] = ndNum;

                if (increasePath(neigh, processed, pair, nodes, 1, parents)) {
                    pair[neighNum] = ndNum;
                    pair[ndNum] = neighNum;
                    return true;
                }
            }
        } else {
            if (pair[ndNum] == -1) {
                return true;
            } else {
                parents[pair[ndNum]] = ndNum;
                return increasePath(nodes.get(pair[ndNum]), processed, pair, nodes, -1, parents);
            }
        }

        return false;
    }
}
