package dynamicProgramming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class BlackAndWhiteTree {
    private static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();
        private int islandNum = -1;
        private int color = -1;

        public Node(int num) {
            this.num = num;
        }

        public void addNeighbour(Node neighbour) {
            neighbours.add(neighbour);
        }

        public void inverseColor() {
            this.color = (this.color + 1) % 2;
        }
    }

    private static void markTree(Node nd, int islandNum, int color, int[] processed) {
        nd.islandNum = islandNum;
        nd.color = color;

        for (Node neigh : nd.neighbours) {
            if (processed[neigh.num] == 0) {
                processed[neigh.num] = 1;
                markTree(neigh, islandNum, (color + 1) % 2, processed);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int m = Integer.parseInt(tkn1.nextToken());

            Node[] nodes = new Node[n + 1];

            for (int i = 1; i <= n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < m; i++) {
                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                int v1 = Integer.parseInt(tkn2.nextToken());
                int v2 = Integer.parseInt(tkn2.nextToken());
                nodes[v1].addNeighbour(nodes[v2]);
                nodes[v2].addNeighbour(nodes[v1]);
            }

            int[] processed = new int[n + 1];
            int island = 0;

            for (int i = 1; i <= n; i++) {
                if (processed[i] == 0) {
                    markTree(nodes[i], island, 0, processed);
                    island++;
                }
            }

            Map<Integer, List<Node>> islandMap = new HashMap<>();

            for (Node nd : nodes) {
                if (nd != null) {
                    if (!islandMap.containsKey(nd.islandNum)) {
                        islandMap.put(nd.islandNum, new ArrayList<>());
                    }
                    islandMap.get(nd.islandNum).add(nd);
                }
            }

            int[][] blackWhiteCnt = new int[island][2];

            for (Node nd : nodes) {
                if (nd != null) {
                    blackWhiteCnt[nd.islandNum][nd.color]++;
                }
            }

            Map<Integer, List<Integer>> valsIslandsMap = new HashMap<>();
            List<Integer> vals = new ArrayList<>();
            Map<Integer, Integer> valsCnt = new HashMap<>();

            for (int i = 0; i < island; i++) {
                int black = blackWhiteCnt[i][0];
                int white = blackWhiteCnt[i][1];

                if (black > white) {
                    blackWhiteCnt[i][0] = white;
                    blackWhiteCnt[i][1] = black;
                    for (Node node : islandMap.get(i)) {
                        node.inverseColor();
                    }
                }

                int val = 2 * Math.abs(black - white);

                if (!valsIslandsMap.containsKey(val)) {
                    valsIslandsMap.put(val, new ArrayList<>());
                }
                valsIslandsMap.get(val).add(i);

                if (!valsCnt.containsKey(val)) {
                    valsCnt.put(val, 0);
                }
                valsCnt.put(val, valsCnt.get(val) + 1);
                vals.add(val);
            }

            int maxVal = 0;
            for (int val : vals) {
                maxVal += val;
            }

            int[][] dynTable = new int[valsCnt.size()][maxVal + 1];
            int[][] cntTable = new int[valsCnt.size()][maxVal + 1];
            List<Integer> valsOrder = new ArrayList<>();
            int row = fillDynTables(dynTable, cntTable, valsOrder, valsCnt, maxVal);

            int sum = maxVal / 2;
            row--;
            int disp = 0;

            while (dynTable[row][sum - disp] == 0 && dynTable[row][sum + disp] == 0){
                disp++;
            }

            System.out.printf("%s %s\n", disp, island - 1);

            List<Integer> islandsToInvert = new ArrayList<>();
            restoreIslands(cntTable, valsOrder, valsIslandsMap, sum + disp, row, islandsToInvert);

            for (Integer islnd : islandsToInvert) {
                for (Node nd : islandMap.get(islnd)) {
                    nd.inverseColor();
                }
            }

            Map<Integer, List<Node>[]> islandColorMap = new HashMap<>();
            for (int i = 0; i < island; i++) {
                islandColorMap.put(i, new List[] {new ArrayList<Node>(), new ArrayList<Node>()});
                for (Node nd : islandMap.get(i)) {
                    islandColorMap.get(i)[nd.color].add(nd);
                }
            }

            int biColourIsland = -1;

            for (Map.Entry<Integer, List<Node>> entry : islandMap.entrySet()) {
                int islnd = entry.getKey();
                List<Node> islndNodes = entry.getValue();
                int blackCount = 0;
                int whiteCount = 0;

                for (Node node : islndNodes) {
                    if (node.color == 0) {
                        blackCount += 1;
                    } else {
                        whiteCount += 1;
                    }
                }

                if (blackCount != 0 && whiteCount != 0){
                    biColourIsland = islnd;
                    break;
                }
            }

            if (biColourIsland != -1) {
                int biColourIslandBlackNodeNum = islandColorMap.get(biColourIsland)[0].get(0).num;
                int biColourIslandWhiteNodeNum = islandColorMap.get(biColourIsland)[1].get(0).num;

                for (int islnd = 0; islnd < island; islnd++) {
                    if (islnd == biColourIsland) {
                        continue;
                    }

                    if (!islandColorMap.get(islnd)[0].isEmpty()) {
                        System.out.printf("%s %s\n", biColourIslandWhiteNodeNum, islandColorMap.get(islnd)[0].get(0).num);
                    } else {
                        System.out.printf("%s %s\n", biColourIslandBlackNodeNum, islandColorMap.get(islnd)[1].get(0).num);
                    }
                }
            } else {
                int whiteColourIsland = -1;
                int blackColorIsland = -1;

                for (Map.Entry<Integer, List<Node>> entry : islandMap.entrySet()) {
                    int islnd = entry.getKey();
                    List<Node> islndNodes = entry.getValue();

                    if (whiteColourIsland != -1 && blackColorIsland != -1) {
                        break;
                    }

                    if (whiteColourIsland == -1 && islndNodes.get(0).color == 0) {
                        whiteColourIsland = islnd;
                    }

                    if (blackColorIsland == -1 && islndNodes.get(0).color == 1) {
                        blackColorIsland = islnd;
                    }
                }

                if (whiteColourIsland != -1 && blackColorIsland != -1) {
                    int whiteNodeNum = islandMap.get(whiteColourIsland).get(0).num;
                    int blackNodeNum = islandMap.get(blackColorIsland).get(0).num;

                    for (Map.Entry<Integer, List<Node>> entry : islandMap.entrySet()) {
                        int islnd = entry.getKey();
                        List<Node> islndNodes = entry.getValue();

                        if (islnd == whiteColourIsland || islnd ==blackColorIsland){
                            continue;
                        }

                        if (islndNodes.get(0).color == 0) {
                            System.out.printf("%s %s\n", blackNodeNum, islndNodes.get(0).num);
                        } else {
                            System.out.printf("%s %s\n", whiteNodeNum, islndNodes.get(0).num);
                        }

                        System.out.printf("%s %s\n", whiteNodeNum, blackNodeNum);
                    }
                }
            }
        }
    }

    private static void restoreIslands(int[][] cntTable, List<Integer> valsOrder, Map<Integer, List<Integer>> valsIslandsMap, int sum, int row, List<Integer> islandsToInvert) {
        while (sum != 0) {
            if (cntTable[row][sum] == 0) {
                row--;
            } else {
                List<Integer> islands = valsIslandsMap.get(valsOrder.get(row));
                islandsToInvert.add(islands.get(islands.size() - 1));
                islands.remove(islands.size() - 1);
                sum -= valsOrder.get(row);
            }
        }
    }

    private static int fillDynTables(int[][] dynTable, int[][] cntTable, List<Integer> valsOrder, Map<Integer, Integer> valsCnt, int maxVal) {
        int row = 0;
        for (Map.Entry<Integer, Integer> entry : valsCnt.entrySet()) {
            int val = entry.getKey();
            int cnt = entry.getValue();

            valsOrder.add(val);
            dynTable[row][0] = 1;

            if (row == 0) {
                for (int col = 0; col <= maxVal; col++) {
                    if (col - val < 0) {
                        continue;
                    }

                    if (cntTable[row][col - val] < cnt && dynTable[row][col - val] == 1){
                        dynTable[row][col] = 1;
                        cntTable[row][col] = cntTable[row][col - val] + 1;
                    }
                }
            } else {
                for (int col = 0; col <= maxVal; col++) {
                    if (col - val < 0) {
                        dynTable[row][col] = dynTable[row - 1][col];
                        continue;
                    }

                    if (cntTable[row][col - val] < cnt && dynTable[row][col - val] > dynTable[row - 1][col]) {
                        dynTable[row][col] = 1;
                        cntTable[row][col] = cntTable[row][col - val] + 1;
                    } else {
                        dynTable[row][col] = dynTable[row - 1][col];
                    }
                }
            }
            row++;
        }

        return row;
    }
}
