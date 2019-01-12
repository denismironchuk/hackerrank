package findPath;

/**
 * Created by Denis_Mironchuk on 1/2/2019.
 */
public class Converter {
    public static Node[][] convertToGraph(int[][] rect, int rows, int cols) {
        Node[][] nodesTable = new Node[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                nodesTable[row][col] = new Node(row, col, rect[row][col]);;
            }
        }

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Node nd = nodesTable[row][col];
                if (row > 0) {
                    nd.addNeigh(nodesTable[row - 1][col], rect[row - 1][col]);
                }

                if (row < rows - 1) {
                    nd.addNeigh(nodesTable[row + 1][col], rect[row + 1][col]);
                }

                if (col > 0) {
                    nd.addNeigh(nodesTable[row][col - 1], rect[row][col - 1]);
                }

                if (col < cols - 1) {
                    nd.addNeigh(nodesTable[row][col + 1], rect[row][col + 1]);
                }
            }
        }

        return nodesTable;
    }
}
