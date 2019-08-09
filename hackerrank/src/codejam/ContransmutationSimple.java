package codejam;

public class ContransmutationSimple {
    private static final int m = 10;

    public static void main(String[] args) {
        while (true) {
            int metalls[][] = new int[m + 1][2];
            int[] initialGrams = new int[m + 1];
            int[] gramms = new int[m + 1];

            for (int i = 1; i <= m; i++) {
                metalls[i][0] = 1 + (int) (Math.random() * m);
                metalls[i][1] = 1 + (int) (Math.random() * m);

                initialGrams[i] = (int) (10 * Math.random());
                gramms[i] = initialGrams[i];
            }

            gramms = runSimulation(metalls, gramms);
            int res1 = gramms[1];
            gramms = runSimulation(metalls, gramms);
            int res2 = gramms[1];

            if (res1 != res2) {
                System.out.println("UNBOUNED");
            } else {
                int gram1 = gramms[1];
                gramms[1] = 0;
                gramms[metalls[1][0]] += gram1;
                gramms[metalls[1][1]] += gram1;

                gramms = runSimulation(metalls, gramms);
                int res3 = gramms[1];

                if (res3 > res2) {
                    System.out.println("UNBOUNED");
                } else {
                    System.out.println(res2);
                }
            }
        }
    }

    private static int[] runSimulation(int metalls[][], int[] grammsNext) {
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
