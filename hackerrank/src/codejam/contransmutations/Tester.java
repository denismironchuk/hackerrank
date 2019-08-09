package codejam.contransmutations;

public class Tester {
    public static void main(String[] args) {
        while (true) {
            int m = 5;
            int metalls[][] = new int[m + 1][2];
            int[] gramms = new int[m + 1];

            for (int i = 1; i <= m; i++) {
                metalls[i][0] = 1 + (int) (Math.random() * m);
                metalls[i][1] = 1 + (int) (Math.random() * m);

                gramms[i] = (int) (10 * Math.random());
            }

            long resSimple = Simple.count(m, metalls, gramms);
            long resOpt = Optimal.count(m, metalls, gramms);

            System.out.println(resSimple);
            System.out.println(resOpt);
            if (resSimple != resOpt) {
                throw new RuntimeException("!!!!!");
            }
            System.out.println("===========");
        }
    }
}
