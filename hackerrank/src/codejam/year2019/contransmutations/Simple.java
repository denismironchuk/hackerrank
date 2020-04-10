package codejam.year2019.contransmutations;

public class Simple {
    public static long count(int m, int[][] metalls, int[] gramms) {
        gramms = runSimulation(m, metalls, gramms);
        int res1 = gramms[1];
        gramms = runSimulation(m, metalls, gramms);
        int res2 = gramms[1];

        if (res1 != res2) {
            return -1;
        } else {
            int gram1 = gramms[1];
            gramms[1] = 0;
            gramms[metalls[1][0]] += gram1;
            gramms[metalls[1][1]] += gram1;

            gramms = runSimulation(m, metalls, gramms);
            int res3 = gramms[1];

            if (res3 > res2) {
                return -1;
            } else {
                return res2;
            }
        }
    }

    private static int[] runSimulation(int m, int metalls[][], int[] grammsNext) {
        int[] gramms;

        for (int sim = 0; sim < m - 1; sim++) {
            gramms = grammsNext;
            grammsNext = new int[m + 1];
            grammsNext[1] = gramms[1];
            for (int i = 2; i <= m; i++) {
                grammsNext[metalls[i][0]] += gramms[i];
                grammsNext[metalls[i][1]] += gramms[i];
            }
        }

        return grammsNext;
    }
}
