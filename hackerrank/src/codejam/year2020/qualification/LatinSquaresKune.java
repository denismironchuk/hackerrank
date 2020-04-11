package codejam.year2020.qualification;

import java.util.*;

public class LatinSquaresKune {
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

    private static int sum(int[] dec) {
        int sum = 0;
        for (int d : dec) {
            sum += d;
        }
        return sum;
    }

    public static void main(String[] args) {
        for (N = 4; N <= 4; N++) {
            System.out.println(N);
            for (int i = N; i <= N * N; i++) {
                if (i == N + 1 || i == (N * N) - 1) {
                    continue;
                }

                int[] dec = decomposeToSum(i);
                if (sum(dec) != i) {
                    throw new RuntimeException("Sum not match");
                }
                while (!buildSquare(dec)) {
                    makeFluctualions(dec);
                    if (sum(dec) != i) {
                        throw new RuntimeException("Sum not match");
                    }
                }

                /*System.out.printf("%5d = ", i);
                for (int j = 0; j < N; j++) {
                    System.out.printf("%2d ", dec[j]);
                }
                System.out.println();*/

                //System.out.println("SUCCESS");
            }
        }
    }

    private static boolean buildSquare(int[] diag) {
        int[][] sqr = new int[N][N];
        Set<Integer>[] rowSet = new Set[N];
        int[] colValues = new int[N];

        for (int i = 0; i < N; i++) {
            sqr[i][i] = diag[i];
            rowSet[i] = new HashSet<>();
            rowSet[i].add(sqr[i][i]);
            colValues[i] = sqr[i][i];
        }

        Date start = new Date();

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
                    //throw new RuntimeException("Failed build");
                    noPair++;
                    continue;
                }

                sqr[nodes[pairs[i]].row - 1][col] = nodes[i].value;
                rowSet[nodes[pairs[i]].row - 1].add(nodes[i].value);
            }

            if (noPair > 1) {
                //printSquare(sqr);
                //throw new RuntimeException("sdfsdfsd");
                return false;
            }
        }

        Date end = new Date();

        //printSquare(sqr);

        //System.out.println((end.getTime() - start.getTime()) + "ms");

        return true;
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

    private static void printSquare(int[][] sqr) {
        for (int i = 0; i < sqr.length; i++) {
            for (int j = 0; j < sqr[0].length; j++) {
                System.out.printf("%2d ", sqr[i][j]);
            }
            System.out.println();
        }
    }
}
