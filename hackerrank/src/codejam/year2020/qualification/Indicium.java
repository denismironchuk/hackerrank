package codejam.year2020.qualification;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Indicium {
    private static int N;

    private static class Node {
        private int num;
        private Set<Node> neighbours = new HashSet<>();
        int value;
        int row;

        public Node(int num, int value, int row) {
            this.num = num;
            this.value = value;
            this.row = row;
        }

        public int getNum() {
            return num;
        }

        public void addNeighbour(Node n) {
            neighbours.add(n);
        }

        public Set<Node> getNeighbours() {
            return neighbours;
        }
    }

    private static boolean makeFluctualions(int[] dec) {
        do {
            List<Integer> posToIncrease = new ArrayList<>();
            List<Integer> posToDecrease = new ArrayList<>();

            for (int i = 0; i < N; i++) {
                if (dec[i] != N) {
                    posToIncrease.add(i);
                }

                if (dec[i] != 1) {
                    posToDecrease.add(i);
                }
            }

            Integer incrPos = 0;
            Integer decrPos = 0;

            while (incrPos == decrPos) {
                incrPos = posToIncrease.get((int)(Math.random() * posToIncrease.size()));
                decrPos = posToDecrease.get((int)(Math.random() * posToDecrease.size()));
            }

            dec[incrPos]++;
            dec[decrPos]--;
        } while (!isValid(dec));

        return true;
    }

    private static boolean isValid(int[] dec) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (Integer d : dec) {
            cnt.merge(d, 1, (oldVal, newVal) -> oldVal + 1);
        }

        Set<Integer> vals = cnt.keySet();

        if (vals.size() != 2) {
            return true;
        }

        Iterator<Integer> itr = vals.iterator();
        int val1 = itr.next();
        int val2 = itr.next();

        return cnt.get(val1) != N - 1 || cnt.get(val2) != N - 1;
    }

    private static int[] decomposeToSum(int s) {
        int[] res = new int[N];
        int val = s / N;
        int rem = s % N;

        for (int i = 0; i < N; i++) {
            res[i] = val;
        }

        for (int i = 0; i < rem; i++) {
            res[i]++;
        }

        if (rem == (N - 1)) {
            res[0]++;
            res[N - 2]--;
        } else if (rem == 1) {
            res[1]++;
            res[N - 1]--;
        }

        return res;
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());

            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                N = Integer.parseInt(tkn.nextToken());
                int k = Integer.parseInt(tkn.nextToken());

                if (N == 2) {
                    if (k == 2) {
                        System.out.printf("Case #%s: POSSIBLE\n", t);
                        printSquare(new int[][]{{1, 2}, {2, 1}});
                    } else if (k == 4) {
                        System.out.printf("Case #%s: POSSIBLE\n", t);
                        printSquare(new int[][]{{2, 1}, {1, 2}});
                    } else {
                        System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                    }
                    continue;
                }

                if (N == 3) {
                    if (k == 4 || k == 5 || k == 7 || k == 8) {
                        System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                    } else if (k == 3) {
                        System.out.printf("Case #%s: POSSIBLE\n", t);
                        printSquare(new int[][] {{1, 3, 2}, {2, 1, 3},{3, 2, 1}});
                    } else if (k == 6) {
                        System.out.printf("Case #%s: POSSIBLE\n", t);
                        printSquare(new int[][] {{1, 3, 2}, {3, 2, 1},{2, 1, 3}});
                    } else {
                        System.out.printf("Case #%s: POSSIBLE\n", t);
                        printSquare(new int[][] {{3, 2, 1}, {1, 3, 2},{2, 1, 3}});
                    }
                    continue;
                }

                if (k == N + 1 || k == (N * N) - 1) {
                    System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                    continue;
                }

                int[] dec = decomposeToSum(k);
                int[][] sqr = new int[N][N];
                while (!buildSquare(dec, sqr)) {
                    makeFluctualions(dec);
                    sqr = new int[N][N];
                }
                System.out.printf("Case #%s: POSSIBLE\n", t);
                printSquare(sqr);
            }
        }
    }

    private static void printSquare(int[][] sqr) {
        for (int i = 0; i < sqr.length; i++) {
            for (int j = 0; j < sqr[0].length; j++) {
                System.out.printf("%d ", sqr[i][j]);
            }
            System.out.println();
        }
    }

    private static boolean buildSquare(int[] diag, int[][] sqr) {
        Set<Integer>[] rowSet = new Set[N];
        int[] colValues = new int[N];

        for (int i = 0; i < N; i++) {
            sqr[i][i] = diag[i];
            rowSet[i] = new HashSet<>();
            rowSet[i].add(sqr[i][i]);
            colValues[i] = sqr[i][i];
        }

        for (int col = 0; col < N; col++) {
            Node[] valueNodes = new Node[N + 1];
            Node[] rowsNodes = new Node[N + 1];
            Node[] nodes = new Node[2 * N];

            int index = 0;
            for (int i = 1; i <= N; i++) {
                valueNodes[i] = new Node(index, i, -1);
                nodes[index] = valueNodes[i];
                index++;

                rowsNodes[i] = new Node(index, -1, i);
                nodes[index] = rowsNodes[i];
                index++;
            }

            for (int val = 1; val <= N; val++) {
                if (colValues[col] == val) {
                    continue;
                }

                for (int row = 1; row <= N; row++) {
                    if (sqr[row - 1][col] != 0 || rowSet[row - 1].contains(val)) {
                        continue;
                    }

                    valueNodes[val].addNeighbour(rowsNodes[row]);
                    rowsNodes[row].addNeighbour(valueNodes[val]);
                }
            }

            int[] pairs = new int[2 * N];
            Arrays.fill(pairs, -1);

            for (Node nd : nodes) {
                if (pairs[nd.getNum()] == -1) {
                    increasePath(nd, new int[2 * N], pairs, nodes);
                }
            }

            int noPair = 0;

            for (int i = 0; i < 2 * N; i += 2) {
                if (pairs[i] == -1) {
                    noPair++;
                    continue;
                }

                sqr[nodes[pairs[i]].row - 1][col] = nodes[i].value;
                rowSet[nodes[pairs[i]].row - 1].add(nodes[i].value);
            }

            if (noPair > 1) {
                return false;
            }
        }

        return true;
    }

    private static boolean increasePath(Node nd, int[] processed, int[] pair, Node[] nodes) {
        int ndNum = nd.getNum();
        processed[ndNum] = 1;

        for (Node neigh : nd.getNeighbours()) {
            int neighNum = neigh.getNum();

            if (processed[neighNum] == 1) {
                continue;
            }

            processed[neighNum] = 1;

            if (pair[neighNum] == -1 || increasePath(nodes[pair[neighNum]], processed, pair, nodes)) {
                pair[neighNum] = ndNum;
                pair[ndNum] = neighNum;
                return true;
            }
        }

        return false;
    }
}
