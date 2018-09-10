package utils.graphs;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class Edmonds3 {
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

    private static Node increasePath(Node nd, int[] processed, int[] pair, List<Node> nodes, int nodeType, int[] parents) {
        int ndNum = nd.getNum();
        processed[ndNum] = nodeType;

        if (nodeType == -1) {
            Node freeNode = processPairEnd(nd, processed, pair, nodes, parents, ndNum);
            if (freeNode != null) {
                return freeNode;
            }
        } else {
            if (pair[ndNum] == -1) {
                return nodes.get(ndNum);
            } else {
                parents[pair[ndNum]] = ndNum;
                Node freeNode = increasePath(nodes.get(pair[ndNum]), processed, pair, nodes, -1, parents);
                if (null != freeNode || !nd.isOddCycleNode()) {
                    return freeNode;
                } else {
                    parents[ndNum] = nd.getNextNodeInCycle().getNum();
                    processed[ndNum] = -1;

                    Node freeNode2 = processPairEnd(nd, processed, pair, nodes, parents, ndNum);
                    if (freeNode2 != null) {
                        return freeNode2;
                    }
                }
            }
        }

        return null;
    }

    private static Node processPairEnd(Node nd, int[] processed, int[] pair, List<Node> nodes, int[] parents, int ndNum) {
        for (Node neigh : nd.getNeighbours()) {
            int neighNum = neigh.getNum();

            if (processed[neighNum] == 1) {
                continue;
            } else if (processed[neighNum] == -1 && !nd.equals(neigh.getNextNodeInCycle())) {
                //odd cycle found
                Node currNode = nd;
                Node nextNode = nodes.get(neighNum);
                while (currNode.getNum() != neighNum) {
                    currNode.setOddCycleNode(true);
                    currNode.setNextNodeInCycle(nextNode);

                    nextNode = currNode;
                    currNode = nodes.get(parents[currNode.getNum()]);
                }
                continue;
            }

            parents[neighNum] = ndNum;

            Node freeNode = increasePath(neigh, processed, pair, nodes, 1, parents);
            if (null != freeNode) {
                return freeNode;
            }
        }

        if (nd.isOddCycleNode()) {
            parents[ndNum] = nd.getNextNodeInCycle().getNum();
            processed[ndNum] = 1;
        }

        return null;
    }
}
