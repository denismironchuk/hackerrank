package codejam.year2018.round3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class NetworkExperiment {

    private static final int N = 10;

    private static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();

        public Node(int num) {
            this.num = num;
        }

        public void addNeighbour(Node nd) {
            neighbours.add(nd);
        }

        public void removeNeighbour(Node nd) {
            neighbours.remove(nd);
        }

        @Override
        public String toString() {
            return num + " : " + neighbours.stream().map(nd -> String.valueOf(nd.num)).collect(Collectors.joining(" "));
        }
    }

    public static void main(String[] args) {
        int graphsCnt = buildAllGraphs();
        System.out.println(graphsCnt);
        /*Node[] nodes = new Node[N];

        while (true) {
            for (int i = 0; i < N; i++) {
                nodes[i] = new Node(i);
            }

            Random rnd = new Random();
            List<Node> processedNodes = new ArrayList<>();
            processedNodes.add(nodes[0]);
            for (int i = 1; i < N; i++) {
                Node candidate = null;
                do {
                    candidate = processedNodes.get(rnd.nextInt(processedNodes.size()));
                } while (candidate == nodes[i]);
                nodes[i].addNeighbour(candidate);
                candidate.addNeighbour(nodes[i]);

                processedNodes.add(nodes[i]);
                if (candidate.neighbours.size() == 4) {
                    processedNodes.remove(candidate);
                }
            }

            while (processedNodes.size() > 4) {
                Node nd = processedNodes.get(0);
                while (nd.neighbours.size() < 4) {
                    Node candidate = null;
                    do {
                        candidate = processedNodes.get(rnd.nextInt(processedNodes.size()));
                    } while (candidate == nd || nd.neighbours.contains(candidate));

                    nd.addNeighbour(candidate);
                    candidate.addNeighbour(nd);

                    if (candidate.neighbours.size() == 4) {
                        processedNodes.remove(candidate);
                    }
                }
                processedNodes.remove(nd);
            }

            boolean success = true;
            for (Node nd : processedNodes) {
                for (Node candidate : processedNodes) {
                    if (candidate == nd || candidate.neighbours.size() == 4) {
                        continue;
                    }

                    if (nd.neighbours.size() == 4) {
                        break;
                    }

                    if (!nd.neighbours.contains(candidate)) {
                        nd.neighbours.add(candidate);
                        candidate.neighbours.add(nd);
                    }
                }
                if (nd.neighbours.size() != 4) {
                    success = false;
                    break;
                }
            }

            if (!success) {
                continue;
            }

            Set<Integer> restoredNodes = new HashSet<>();
            boolean hasUniqueHash = true;

            while (restoredNodes.size() != N && hasUniqueHash) {
                hasUniqueHash = false;
                Map<Integer, Integer> hashCnt = new HashMap<>();
                Map<Integer, List<Integer>> hashNodes = new HashMap<>();

                for (Node nd : nodes) {
                    if (restoredNodes.contains(nd.num)) {
                        continue;
                    }

                    int invariant = buildInvariant(nd, restoredNodes);
                    hashCnt.merge(invariant, 1, (oldVal, newVal) -> oldVal + 1);
                    if (!hashNodes.containsKey(invariant)) {
                        hashNodes.put(invariant, new ArrayList<>());
                    }
                    hashNodes.get(invariant).add(nd.num);
                }


                for (Map.Entry<Integer, Integer> entry : hashCnt.entrySet()) {
                    int hash = entry.getKey();
                    if (entry.getValue() == 1) {
                        restoredNodes.add(hashNodes.get(hash).get(0));
                        hasUniqueHash = true;
                    }
                }
            }

            if (hasUniqueHash) {
                break;
            } else {
                System.out.println(restoredNodes.size());
            }
        }

        System.out.println("SUCESS!!!!");*/
    }

    private static int canBeRestored(Node[] nodes) {
        Set<Integer> restoredNodes = new HashSet<>();
        boolean hasUniqueHash = true;

        while (restoredNodes.size() != N && hasUniqueHash) {
            hasUniqueHash = false;
            Map<Integer, Integer> hashCnt = new HashMap<>();
            Map<Integer, List<Integer>> hashNodes = new HashMap<>();

            for (Node nd : nodes) {
                if (restoredNodes.contains(nd.num)) {
                    continue;
                }

                int invariant = buildInvariant(nd, restoredNodes);
                hashCnt.merge(invariant, 1, (oldVal, newVal) -> oldVal + 1);
                if (!hashNodes.containsKey(invariant)) {
                    hashNodes.put(invariant, new ArrayList<>());
                }
                hashNodes.get(invariant).add(nd.num);
            }


            for (Map.Entry<Integer, Integer> entry : hashCnt.entrySet()) {
                int hash = entry.getKey();
                if (entry.getValue() == 1) {
                    restoredNodes.add(hashNodes.get(hash).get(0));
                    hasUniqueHash = true;
                }
            }
        }

        return restoredNodes.size();
    }

    private static int buildAllGraphs() {
        Node[] nodes = new Node[10];
        for (int i = 0; i < 10; i++) {
            nodes[i] = new Node(i);
        }
        for (int i = 2; i <= 5; i++) {
            nodes[0].addNeighbour(nodes[i]);
            nodes[i].addNeighbour(nodes[0]);

            nodes[1].addNeighbour(nodes[i + 4]);
            nodes[i + 4].addNeighbour(nodes[1]);
        }

        return addEdges(2, 2, nodes);
    }

    private static int addEdges(int currNodeIndex, int lastAddedNode, Node[] nodes) {
        if (nodes[currNodeIndex].neighbours.size() == 4) {
            if (currNodeIndex == 9) {
                /*if (canBeRestored(nodes)) {
                    for (Node nd : nodes) {
                        System.out.println(nd);
                    }
                    System.out.println("===============");
                }*/
                int res = canBeRestored(nodes);
                if (res > 2) {
                    System.out.println(res);
                }
                return 1;
            } else {
                return addEdges(currNodeIndex + 1, currNodeIndex + 1, nodes);
            }
        } else {
            int res = 0;
            for (int i = lastAddedNode + 1; i < 10; i++) {
                if (nodes[i].neighbours.size() == 4) {
                    continue;
                }
                nodes[currNodeIndex].addNeighbour(nodes[i]);
                nodes[i].addNeighbour(nodes[currNodeIndex]);
                res += addEdges(currNodeIndex, i, nodes);
                nodes[currNodeIndex].removeNeighbour(nodes[i]);
                nodes[i].removeNeighbour(nodes[currNodeIndex]);
            }
            return res;
        }
    }

    private static int buildInvariant(Node nd, Set<Integer> restoredNodes) {
        int[] invariant = new int[N];
        Queue<Node> q = new LinkedList<>();
        int[] processed = new int[N];
        int[] dist = new int[N];

        q.add(nd);
        processed[nd.num] = 1;

        while (!q.isEmpty()) {
            Node n = q.poll();
            for (Node neigh : n.neighbours) {
                if (processed[neigh.num] == 0) {
                    q.add(neigh);
                    processed[neigh.num] = 1;
                    dist[neigh.num] = dist[n.num] + 1;
                    invariant[dist[neigh.num]]++;
                }
            }
        }

        for (int i = 0; i < N; i++) {
            if (!restoredNodes.contains(i)) {
                dist[i] = 0;
            }
        }

        return Arrays.hashCode(new int[] {Arrays.hashCode(invariant), Arrays.hashCode(dist)});

    }
}
