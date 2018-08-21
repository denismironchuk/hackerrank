import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class FrogInMaze {
    enum CellTypes {
         OBST, INIT, MINE, EXIT, FREE
    }

    static class Node {
        private int num;
        private CellTypes type;
        private Node tunnel;
        private List<Node> transitions = new ArrayList<>();

        public Node(final CellTypes type) {
            this.type = type;
        }

        private void addTransition(Node nd) {
            if (nd.getTunnel() != null) {
                transitions.add(nd.getTunnel());
            } else {
                transitions.add(nd);
            }
        }

        public List<Node> getTransitions() {
            return transitions;
        }

        public CellTypes getType() {
            return type;
        }

        public Node getTunnel() {
            return tunnel;
        }

        public void setTunnel(final Node tunnel) {
            this.tunnel = tunnel;
        }

        public void setNum(final int num) {
            this.num = num;
        }

        public boolean isAbsorb() {
            return transitions.isEmpty();
        }
    }

    static class Matrix {
        private int row;
        private int col;
        private double[][] matrix;

        public Matrix(final double[][] matrix) {
            this.matrix = matrix;
            this.row = matrix.length;
            this.col = matrix[0].length;
        }

        public static Matrix generateUnitMatrix(int n) {
            double[][] unitMatrix = new double[n][n];

            for (int i = 0; i < n; i++) {
                unitMatrix[i][i] = 1;
            }

            return new Matrix(unitMatrix);
        }

        public void substract(Matrix mtr) {
            for (int i = 0; i < getRow(); i++) {
                for (int j = 0; j < getCol(); j++) {
                    matrix[i][j] -= mtr.getCell(i, j);
                }
            }
        }

        public Matrix calculateInvertMatrix() {
            int N = row;
            double[][] invMatr = new double[N][N * 2];

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    invMatr[i][j] = matrix[i][j];
                }
            }

            for (int i = 0; i < N; i++) {
                invMatr[i][N + i] = 1;
            }

            for (int i = 0; i < N; i++) {
                int maxRowIndex = i;
                for (int j = i + 1; j < N; j++) {
                    if (Math.abs(invMatr[j][i]) > Math.abs(invMatr[maxRowIndex][i])) {
                        maxRowIndex = j;
                    }
                }

                if (maxRowIndex != i) {
                    double[] temp = invMatr[i];
                    invMatr[i] = invMatr[maxRowIndex];
                    invMatr[maxRowIndex] = temp;
                }

                divideRow(invMatr[i], invMatr[i][i]);

                for (int j = i + 1; j < N; j++) {
                    mulAndSubstrRow(invMatr[j], invMatr[i], invMatr[j][i]);
                }
            }

            for (int i = N - 2; i >= 0; i--) {
                for (int j = N - 1; j > i; j--) {
                    mulAndSubstrRow(invMatr[i], invMatr[j], invMatr[i][j]);
                }
            }

            double[][] invResult = new double[N][N];

            for (int i = 0; i < N; i++) {
                for (int j = N; j < 2 * N; j++) {
                    invResult[i][j - N] = invMatr[i][j];
                }
            }

            return new Matrix(invResult);
        }

        private void divideRow(double[] row, double mul) {
            for (int i = 0; i < row.length; i++) {
                row[i] /= mul;
            }
        }

        private void mulAndSubstrRow(double[] row, double[] rowToSubstr, double mul) {
            for (int i = 0; i < row.length; i++) {
                row[i] -= rowToSubstr[i] * mul;
            }
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public double getCell(int row, int col) {
            return matrix[row][col];
        }

        public void printMatrix() {
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    System.out.printf("%4.2f ", matrix[i][j]);
                }
                System.out.println();
            }
        }

        public Matrix matrixMul(Matrix mul) {
            int n = this.row;
            int m = mul.getCol();

            double[][] result = new double[n][m];

            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    for (int k = 0; k < n; k++) {
                        result[i][j] += getCell(i, k) * mul.getCell(k, j);
                    }
                }
            }

            return new Matrix(result);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int m = Integer.parseInt(tkn1.nextToken());
        int k = Integer.parseInt(tkn1.nextToken());

        Node[][] nodes = new Node[n][m];
        Node startNode = null;

        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            for (int j = 0; j < m; j++) {
                switch (line.charAt(j)) {
                    case '#':
                        nodes[i][j] = new Node(CellTypes.OBST);
                        break;
                    case 'A':
                        nodes[i][j] = new Node(CellTypes.INIT);
                        startNode = nodes[i][j];
                        break;
                    case '*' :
                        nodes[i][j] = new Node(CellTypes.MINE);
                        break;
                    case '%':
                        nodes[i][j] = new Node(CellTypes.EXIT);
                        break;
                    case 'O':
                        nodes[i][j] = new Node(CellTypes.FREE);
                        break;
                }
            }
        }

        for (int i = 0; i < k; i++) {
            StringTokenizer tunTkn = new StringTokenizer(br.readLine());
            int r1 = Integer.parseInt(tunTkn.nextToken());
            int c1 = Integer.parseInt(tunTkn.nextToken());
            int r2 = Integer.parseInt(tunTkn.nextToken());
            int c2 = Integer.parseInt(tunTkn.nextToken());

            nodes[r1 - 1][c1 - 1].setTunnel(nodes[r2 - 1][c2 - 1]);
            nodes[r2 - 1][c2 - 1].setTunnel(nodes[r1 - 1][c1 - 1]);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Node nd = nodes[i][j];
                if (nd.getType() == CellTypes.INIT || nd.getType() == CellTypes.FREE) {
                    if (i > 0 && nodes[i - 1][j].getType() != CellTypes.OBST) {
                        nd.addTransition(nodes[i - 1][j]);
                    }

                    if (j > 0 && nodes[i][j - 1].getType() != CellTypes.OBST) {
                        nd.addTransition(nodes[i][j - 1]);
                    }

                    if (i < n - 1 && nodes[i + 1][j].getType() != CellTypes.OBST) {
                        nd.addTransition(nodes[i + 1][j]);
                    }

                    if (j < m - 1 && nodes[i][j + 1].getType() != CellTypes.OBST) {
                        nd.addTransition(nodes[i][j + 1]);
                    }
                }
            }
        }

        Set<Node> reachableNodes = new HashSet<>();
        deepSearch(startNode, reachableNodes);

        int numTrans = 0;
        int numAbsorb = 0;

        List<Node> transNodes = new ArrayList<>();
        List<Node> absorbNodes = new ArrayList<>();
        List<Node> exitNodes = new ArrayList<>();

        for (Node nd : reachableNodes) {
            if (!nd.isAbsorb()) {
                nd.setNum(numTrans);
                numTrans++;
                transNodes.add(nd);
            } else {
                nd.setNum(numAbsorb);
                numAbsorb++;
                absorbNodes.add(nd);

                if (nd.getType() == CellTypes.EXIT) {
                    exitNodes.add(nd);
                }
            }
        }

        if (0 == numTrans) {
            System.out.printf("%.6f", 0.0);
            return;
        }

        double[][] Q = new double[numTrans][numTrans];
        double[][] R = new double[numTrans][numAbsorb];

        for (Node nd : transNodes) {
            int transAmnt = nd.getTransitions().size();
            for (Node transNode : nd.getTransitions()) {
                if (!transNode.isAbsorb()) {
                    Q[nd.num][transNode.num] = 1.0 / transAmnt;
                } else {
                    R[nd.num][transNode.num] = 1.0 / transAmnt;
                }
            }
        }

        Matrix qMatr = new Matrix(Q);
        Matrix RMatr = new Matrix(R);
        Matrix unitMatr = Matrix.generateUnitMatrix(numTrans);
        unitMatr.substract(qMatr);
        Matrix nMatr = unitMatr.calculateInvertMatrix();

        Matrix bMatr = nMatr.matrixMul(RMatr);
        //bMatr.printMatrix();

        double result = 0.0;

        for (Node exitNode : exitNodes) {
            result += bMatr.getCell(startNode.num, exitNode.num);
        }

        System.out.printf("%.6f", result);
    }

    private static void deepSearch(Node nd, Set<Node> processedNodes) {
        processedNodes.add(nd);

        for (Node transNd : nd.getTransitions()) {
            if (!processedNodes.contains(transNd)) {
                deepSearch(transNd, processedNodes);
            }
        }
    }
}
