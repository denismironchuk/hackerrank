package codejam.year2018.round3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
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

                if (N == 10) {
                    if (-1 == process10(br)) {
                        return;
                    }
                } else {
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

                                int invariant = buildInvariant(nd, restoredNodes, nodes);
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

                /*System.out.println("=============");
                for (Node nd : nodes) {
                    for (Node neigh : nd.neighbours) {
                        if (nd.num < neigh.num) {
                            System.out.println((((nd.num + 2) % N) + 1) + " " + (((neigh.num + 2) % N) + 1));
                        }
                    }
                }
                System.out.println("=============");*/

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

                            int invariant = buildInvariant(nd, restoredNodes, respNet);
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
                                hashNode.hash = hash;
                            }
                        }
                    }
                    System.out.println(Arrays.stream(nodes)
                            .map(nd -> nd.assignedNode.num + 1)
                            .map(String::valueOf)
                            .collect(Collectors.joining(" ")));
                }
            }
        }
    }

    private static int process10(BufferedReader br) throws IOException {
        int[][] graph = new int[][] {
                     // 0 1 2 3 4 5 6 7 8 9
                /*0*/  {0,0,1,1,1,1,0,0,0,0},
                /*1*/  {0,0,0,0,0,0,1,1,1,1},
                /*2*/  {1,0,0,1,1,0,1,0,0,0},
                /*3*/  {1,0,1,0,0,0,1,1,0,0},
                /*4*/  {1,0,1,0,0,0,0,0,1,1},
                /*5*/  {1,0,0,0,0,0,0,1,1,1},
                /*6*/  {0,1,1,1,0,0,0,0,1,0},
                /*7*/  {0,1,0,1,0,1,0,0,0,1},
                /*8*/  {0,1,0,0,1,1,1,0,0,0},
                /*9*/  {0,1,0,0,1,1,0,1,0,0},
        };

        System.out.println(N);
        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                if (graph[i][j] == 1) {
                    System.out.println((i + 1) + " " + (j + 1));
                }
            }
        }

        /*System.out.println("================");
        for (int i = 0; i < N - 1; i++) {
            for (int j = i + 1; j < N; j++) {
                if (graph[i][j] == 1) {
                    System.out.println((((i - 1 + N) % N) + 1) + " " + (((j - 1 + N) % N) + 1));
                }
            }
        }
        System.out.println("================");*/

        int resp1 = Integer.parseInt(br.readLine());
        if (resp1 == -1) {
            return -1;
        }

        int[][] graph2 = new int[N][N];
        for (int i = 0; i < 2 * N; i++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(tkn.nextToken()) - 1;
            int v2 = Integer.parseInt(tkn.nextToken()) - 1;
            graph2[v1][v2] = 1;
            graph2[v2][v1] = 1;
        }

        int[] coresp = new int[N];
        int left = -1;
        int right = -1;
        for (int v1 = 0; v1 < N - 1; v1++) {
            for (int v2 = v1 + 1; v2 < N; v2++) {
                if (graph2[v1][v2] == 1) {
                    continue;
                }

                boolean hasCommon = false;
                for (int i = 0; !hasCommon && i < N; i++) {
                    if (graph2[v1][i] == 1 && graph2[v2][i] == 1) {
                        hasCommon = true;
                    }
                }
                if (!hasCommon) {
                    left = v1 ;
                    right = v2;
                }
            }
        }

        Set<Integer> leftSide = new HashSet<>();
        Set<Integer> rightSide = new HashSet<>();

        for (int i = 0; i < N; i++) {
            if (graph2[left][i] == 1) {
                leftSide.add(i);
            }
            if (graph2[right][i] == 1) {
                rightSide.add(i);
            }
        }

        boolean found = false;
        for (Integer node : leftSide) {
            int rightCorresp = 0;
            for (int i = 0; i < N; i++) {
                if (graph2[node][i] == 1 & rightSide.contains(i)) {
                    rightCorresp++;
                }
            }

            if (rightCorresp == 3) {
                found = true;
                coresp[0] = left;
                coresp[1] = right;
                coresp[5] = node;
                for (Integer rightNode : rightSide) {
                    if (graph2[rightNode][node] == 0) {
                        coresp[6] = rightNode;
                    }
                }
                break;
            }
        }

        if (!found) {
            coresp[0] = right;
            coresp[1] = left;
            Set<Integer> temp = leftSide;
            leftSide = rightSide;
            rightSide = temp;

            for (Integer node : leftSide) {
                int rightCorresp = 0;
                for (int i = 0; i < N; i++) {
                    if (graph2[node][i] == 1 && rightSide.contains(i)) {
                        rightCorresp++;
                    }
                }

                if (rightCorresp == 3) {
                    coresp[5] = node;
                    for (Integer rightNode : rightSide) {
                        if (graph2[rightNode][node] == 0) {
                            coresp[6] = rightNode;
                        }
                    }
                    break;
                }
            }
        }

        for (Integer node : leftSide) {
            if (node != coresp[5] && graph2[node][coresp[6]] == 0) {
                coresp[4] = node;
                break;
            }
        }

        for (Integer node : leftSide) {
            if (graph2[node][coresp[4]] == 1) {
                coresp[2] = node;
                break;
            }
        }

        for (Integer node : leftSide) {
            if (node != coresp[4] && graph2[node][coresp[2]] == 1) {
                coresp[3] = node;
                break;
            }
        }

        for (Integer node : rightSide) {
            if (node != coresp[6] && graph2[node][coresp[3]] == 1) {
                coresp[7] = node;
                break;
            }
        }

        for (Integer node : rightSide) {
            if (graph2[coresp[6]][node] == 1) {
                coresp[8] = node;
                break;
            }
        }

        for (Integer node : rightSide) {
            if (graph2[coresp[7]][node] == 1) {
                coresp[9] = node;
                break;
            }
        }

        System.out.println(Arrays.stream(coresp).map(i -> i + 1).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
        return 1;
    }

    private static int buildInvariant(Node nd, Set<Integer> restoredNodes, Node[] nodes) {
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

        TreeSet<Node> restoredSorted = new TreeSet<>(Comparator.comparing(node -> node.hash));
        for (Integer resNodeIndex : restoredNodes) {
            restoredSorted.add(nodes[resNodeIndex]);
        }
        List<Integer> distsSortedByHash = new ArrayList<>();
        for (Node node : restoredSorted) {
            distsSortedByHash.add(dist[node.num]);
        }

        return Arrays.hashCode(new int[] {Arrays.hashCode(invariant), distsSortedByHash.hashCode()});
    }
}
