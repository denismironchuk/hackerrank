package utils.graphs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class Edmonds {
        public static void main(String[] args) throws IOException {
        List<Node> nodes = GraphGenerator.generateFromInputStream();
        int n = nodes.size();
        int[] pair = new int[n];
        Arrays.fill(pair, -1);

        for (Node nd : nodes) {
            if (pair[nd.getNum()] != -1) {
                continue;
            }

            //getIncreasingPath(nd, new HashMap<>(), pair, nodes, new Stack<>());
        }
    }

    /*private static boolean getIncreasingPath(Node nd, Map<Integer, Integer> processed, int[] pair,
                                             List<Node> nodes, Map<Integer, Integer> parent) {
        int ndNum = nd.getNum();
        processed.put(ndNum, 1);
        path.push(nd);

        for (Node neigh : nd.getNeighbours()) {
            int neighNum = neigh.getNum();

            if (processed.containsKey(neighNum) && processed.get(neighNum) == -1) {
                continue;
            } else if (processed.containsKey(neighNum) && processed.get(neighNum) == 1) {
                Set<Integer> nodesToCollapse = new HashSet<>();
                nodesToCollapse.add(neighNum);

                for (Node pathNode = path.pop(); pathNode.getNum() != neighNum; nodesToCollapse.add(pathNode.getNum()));

                Node fakeNode = Node.collapseNodes(nodes, nodesToCollapse);
                nodes.add(fakeNode);

                if (getIncreasingPath(fakeNode, processed, pair, nodes, path)) {
                    return true;
                } else {
                    fakeNode.unwrap();
                    path.pop();
                }
            } else {
                processed.put(neighNum, -1);
                path.push(neigh);

                if (pair[neighNum] == -1 || getIncreasingPath(nodes.get(pair[neighNum]), processed, pair, nodes, path)) {
                    return true;
                } else {
                    path.pop();
                }
            }
        }

        return false;
    }*/
}
