package utils.graphs;

/**
 * Created by Denis_Mironchuk on 11/6/2018.
 */
public class FloydWarshal {
    public static void main(String[] args) {
        int n = 5;
        long[][] dists = new long[][]{{0, 10, 2, 5, 1},
                {10, 0, 7, Integer.MAX_VALUE, Integer.MAX_VALUE},
                {2, 7, 0, 1, 8},
                {5, Integer.MAX_VALUE, 1, 0, Integer.MAX_VALUE},
                {1, Integer.MAX_VALUE, 8, Integer.MAX_VALUE, 0}};

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k < n; k++) {
                    dists[i][j] = Math.min(dists[i][j], dists[i][k] + dists[k][j]);
                }
            }
        }

        System.out.println();
    }
}
