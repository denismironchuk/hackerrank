import java.math.BigDecimal;

public class RoadsInHackerlandTest {

    public static final BigDecimal TWO = BigDecimal.valueOf(2);

    public static void main(String[] args) {
        int c = 0;
        while(true) {
            if (c % 1 == 0) {
                System.out.println(c);
            }
            c++;

            int n = 100000;
            int[] vals = new int[n];

            for (int i = 0; i < n; i++) {
                vals[i] = (int) (1000 * Math.random());
            }

            String resOpt = countOptimal(n, vals);
            String resTriv = countTrivial(n, vals);

            if (!resOpt.equals(resTriv)) {
                System.out.println(resOpt);
                System.out.println(resTriv);
                throw new RuntimeException();
            }
        }
    }

    private static String countTrivial(final int n, final int[] vals) {
        BigDecimal res = BigDecimal.ZERO;
        BigDecimal pow = BigDecimal.ONE;

        for (int i = 0; i < n; i++) {
            res = res.add(BigDecimal.valueOf(vals[i]).multiply(pow));
            pow = pow.multiply(TWO);
        }

        StringBuilder resStr = new StringBuilder();
        while (!res.equals(BigDecimal.ZERO)) {
            BigDecimal[] devAndRem = res.divideAndRemainder(TWO);
            resStr.insert(0, devAndRem[1].toString());
            res = devAndRem[0];
        }

        return resStr.toString();
    }

    private static String countOptimal(final int n, final int[] vals) {
        int[] res = new int[n + 100];

        for (int i = 0; i < n; i++) {
            long edgeCnt = vals[i];
            int resIndex = i;
            int perenos = 0;

            while (edgeCnt != 0) {
                int sum = ((int)(edgeCnt % 2) + res[resIndex] + perenos);
                res[resIndex] = sum % 2;
                perenos = sum > 1 ? 1 : 0;
                resIndex++;
                edgeCnt /= 2;
            }

            while (perenos != 0) {
                int sum = res[resIndex] + perenos;
                res[resIndex] = sum % 2;
                perenos = sum > 1 ? 1 : 0;
                resIndex++;
            }
        }

        boolean started = false;
        int i = res.length - 1;

        for (; i >= 0 && !started; i--) {
            if (res[i] == 1) {
                started = true;
            }
        }

        StringBuilder builder = new StringBuilder();
        i++;
        for (; i >= 0; i--) {
            builder.append(res[i]);
        }

        return builder.toString();
    }
}
