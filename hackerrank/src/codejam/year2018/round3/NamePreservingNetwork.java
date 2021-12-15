package codejam.year2018.round3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class NamePreservingNetwork {

    private static int N;

    private static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();
        private int hash;
        private Node assignedNode;

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

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int l = Integer.parseInt(tkn1.nextToken());
                int r = Integer.parseInt(tkn1.nextToken());
                N = r;

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
                                Integer hashNodeIndex = hashNodes.get(hash).get(0);
                                restoredNodes.add(hashNodeIndex);
                                Node hashNode = nodes[hashNodeIndex];
                                hashNode.hash = hash;
                                hasUniqueHash = true;
                            }
                        }
                    }

                    if (hasUniqueHash) {
                        break;
                    }
                }

                Map<Integer, Node> hashMap = Arrays.stream(nodes).collect(Collectors.toMap(nd -> nd.hash, nd -> nd));

                System.out.println(N);
                for (Node nd : nodes) {
                    for (Node neigh : nd.neighbours) {
                        if (nd.num < neigh.num) {
                            System.out.println((nd.num + 1) + " " + (neigh.num + 1));
                        }
                    }
                }

                System.out.println("=============");
                for (Node nd : nodes) {
                    for (Node neigh : nd.neighbours) {
                        if (nd.num < neigh.num) {
                            System.out.println((((nd.num + 1) % N) + 1) + " " + (((neigh.num + 1) % N) + 1));
                        }
                    }
                }
                System.out.println("=============");

                int resp1 = Integer.parseInt(br.readLine());
                if (resp1 == -1) {
                    return;
                }
                Node[] respNet = new Node[N];
                for (int i = 0; i < N; i++) {
                    respNet[i] = new Node(i);
                }
                for (int i = 0; i < 2 * N; i++) {
                    StringTokenizer edgeTkn = new StringTokenizer(br.readLine());
                    int node1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
                    int node2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
                    respNet[node1].addNeighbour(respNet[node2]);
                    respNet[node2].addNeighbour(respNet[node1]);
                }

                //Restoration process
                Set<Integer> restoredNodes = new HashSet<>();

                while (restoredNodes.size() != N) {
                    Map<Integer, Integer> hashCnt = new HashMap<>();
                    Map<Integer, List<Integer>> hashNodes = new HashMap<>();

                    for (Node nd : respNet) {
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
                            Integer hashNodeIndex = hashNodes.get(hash).get(0);
                            restoredNodes.add(hashNodeIndex);
                            Node hashNode = respNet[hashNodeIndex];
                            hashMap.get(hash).assignedNode = hashNode;
                        }
                    }
                }
                System.out.println(Arrays.stream(nodes)
                        .map(nd -> nd.assignedNode.num)
                        .map(String::valueOf)
                        .collect(Collectors.joining(" ")));
            }
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
