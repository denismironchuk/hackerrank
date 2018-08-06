import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Date;
import java.util.StringTokenizer;

public class PrimeXOR2 {
    private static final long MODULO = 1000000007;
    public static final int MAX_XOR_SUM_VAL = 8192;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        int[] primes = new int[MAX_XOR_SUM_VAL];
        int[] processed = new int[MAX_XOR_SUM_VAL];
        primes[1] = 0;
        processed[0] = 1;
        processed[1] = 1;
        for (int k = 2; k < MAX_XOR_SUM_VAL; k++) {
            if (processed[k] == 0) {
                processed[k] = 1;
                primes[k] = 1;

                for (int l = 2; l*k < MAX_XOR_SUM_VAL; l++) {
                    processed[l*k] = 1;
                }
            }
        }

        StringBuilder results = new StringBuilder();

        for (int q = 0; q < Q; q++) {
            int n = Integer.parseInt(br.readLine());
            String aStr = br.readLine();
            StringTokenizer tkn = new StringTokenizer(aStr, " ");
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = Integer.parseInt(tkn.nextToken());
            }
            /*int n = 100000;
            int[] a = new int[n];
            for (int i = 0; i < n; i++) {
                a[i] = (int)(3500 + (Math.random() * 1000));
            }*/


            int[] uniqueA = new int[n];
            int[] cnts = new int[n];

            Arrays.sort(a);

            uniqueA[0] = a[0];
            cnts[0] = 1;

            int uniqueIndex = 0;

            for (int i = 1; i < n; i++) {
                if (a[i] == a[i - 1]) {
                    cnts[uniqueIndex]++;
                } else {
                    uniqueIndex++;
                    uniqueA[uniqueIndex] = a[i];
                    cnts[uniqueIndex] = 1;
                }
            }

            long[] countsPrev = new long[MAX_XOR_SUM_VAL];
            int[] valuesPrev = new int[MAX_XOR_SUM_VAL];

            valuesPrev[0] = 0;
            countsPrev[0] = 1;
            int valuesPrevAmnt = 1;

            for (int i = 0; i < uniqueIndex + 1; i++) {
                long[] countsBeforePrev = countsPrev;
                int[] valuesBeforePrev = valuesPrev;
                int valuesBeforePrevAmnt = valuesPrevAmnt;

                countsPrev = new long[MAX_XOR_SUM_VAL];
                valuesPrev = new int[MAX_XOR_SUM_VAL];
                valuesPrevAmnt = 0;

                for (int j = 0; j < valuesBeforePrevAmnt; j++) {
                    int val = valuesBeforePrev[j];
                    int newVal = val ^ uniqueA[i];
                    countsPrev[newVal] = countsBeforePrev[val];
                    valuesPrev[valuesPrevAmnt] = newVal;
                    valuesPrevAmnt++;
                }

                if (cnts[i] > 1) {
                    long mult1 = 1 + (cnts[i] / 2);

                    for (int j = 0; j < valuesBeforePrevAmnt; j++) {
                        int val = valuesBeforePrev[j];
                        countsBeforePrev[val] = (countsBeforePrev[val] * mult1) % MODULO;
                    }

                    long mult2 = 1 + ((cnts[i] - 1) / 2);

                    for (int j = 0; j < valuesPrevAmnt; j++) {
                        int val = valuesPrev[j];
                        countsPrev[val] = (countsPrev[val] * mult2) % MODULO;
                    }
                }

                for (int j = 0; j < valuesBeforePrevAmnt; j++) {
                    int val = valuesBeforePrev[j];
                    long cnt = countsBeforePrev[val];

                    if (countsPrev[val] == 0) {
                        valuesPrev[valuesPrevAmnt] = val;
                        valuesPrevAmnt++;
                    }

                    countsPrev[val] = (countsPrev[val] + cnt) % MODULO;
                }
            }


            long result = 0;

            for (int j = 0; j < valuesPrevAmnt; j++) {
                int val = valuesPrev[j];
                if (primes[val] == 1) {
                    result = (result + countsPrev[val]) % MODULO;
                }
            }

            results.append(result).append("\n");
        }
        System.out.println(results.toString());
    }
}
