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

        @Override
        public String toString() {
            return num + " : " + neighbours.stream().map(nd -> String.valueOf(nd.num)).collect(Collectors.joining(" "));
        }
    }

    public static void main(String[] args) {
        Node[] nodes = new Node[N];

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

            Map<Integer, Integer> hashCnt = new HashMap<>();
            for (Node nd : nodes) {
                int[] invariant = buildInvariant(nd);
                //System.out.println(Arrays.stream(invariant).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
                //System.out.println(Arrays.hashCode(invariant));
                hashCnt.merge(Arrays.hashCode(invariant), 1, (oldVal, newVal) -> oldVal + 1);
            }

            boolean hasUniqueHash = hashCnt.values().stream().anyMatch(v -> v == 1);

            if (hasUniqueHash) {
                break;
            }
        }

        Map<Integer, Integer> hashCnt = new HashMap<>();
        Map<Integer, List<Integer>> hashNodes = new HashMap<>();
        for (Node nd : nodes) {
            int[] invariant = buildInvariant(nd);
            System.out.println(Arrays.stream(invariant).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
            //System.out.println(Arrays.hashCode(invariant));
            int hash = Arrays.hashCode(invariant);
            hashCnt.merge(hash, 1, (oldVal, newVal) -> oldVal + 1);
            if (!hashNodes.containsKey(hash)) {
                hashNodes.put(hash, new ArrayList<>());
            }
            hashNodes.get(hash).add(nd.num);
        }

        int uniqueNodeNum = -1;
        for (Map.Entry<Integer, Integer> entry : hashCnt.entrySet()) {
            int hash = entry.getKey();
            if (entry.getValue() == 1) {
                uniqueNodeNum = hashNodes.get(hash).get(0);
                break;
            }
        }

        System.out.println(uniqueNodeNum);

        for (Node nd : nodes[uniqueNodeNum].neighbours) {
            int[] invariant = buildInvariant(nd);
            System.out.println(Arrays.stream(invariant).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
        }

        System.out.println("=================");

        for (Node nd : nodes) {
            if (nodes[uniqueNodeNum].neighbours.contains(nd) || nd.num == uniqueNodeNum) {
                continue;
            }
            int[] invariant = buildInvariant(nd);
            System.out.println(Arrays.stream(invariant).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
        }

        System.out.println("SUCESS!!!!");
    }

    private static int[] buildInvariant(Node nd) {
        return buildInvariant(nd, new int[N]);
    }

    private static int[] buildInvariant(Node nd, int[] processed) {
        int[] invariant = new int[N];
        Queue<Node> q = new LinkedList<>();
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

        return invariant;

    }
}
