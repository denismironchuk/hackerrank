package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class WierdFunction3 {
    private static class Elmt {
        private long val;
        private int build1;
        private int build2;

        public Elmt(int build1, int build2) {
            this.build1 = build1 % 2 == 0 ? build1 / 2 : build1;
            this.build2 = build2 % 2 == 0 ? build2 / 2 : build2;
            this.val = (long) this.build1 * (long) this.build2;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        long[][] requests = new long[T][2];

        long maxR = -1;

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long l = Long.parseLong(tkn.nextToken());
            long r = Long.parseLong(tkn.nextToken());
            requests[t][0] = l;
            requests[t][1] = r;

            if (r > maxR) {
                maxR = r;
            }
        }

        List<Elmt> elements = new ArrayList<>();

        long maxBuild = 1;

        for (; (maxBuild * (maxBuild + 1)) / 2 <= maxR; maxBuild++) {
            elements.add(new Elmt((int) maxBuild, (int)maxBuild + 1));
        }

        int n = (int)maxBuild + 1;

        int[] isPrime = new int[n + 1];
        int[] eulers = new int[n + 1];

        for (int i = 0; i <= n; i++) {
            eulers[i] = i;
            isPrime[i] = 1;
        }

        for (int i = 2; i <= n; i++) {
            if (isPrime[i] == 1) {
                eulers[i] = i - 1;
                for (int j = 2; i * j <= n; j++) {
                    eulers[i * j] -= eulers[i * j] / i;
                    isPrime[i * j] = 0;
                }
            }
        }

        int elementsCnt = elements.size();
        long[] cumSum = new long[elementsCnt];

        Elmt el0 = elements.get(0);
        cumSum[0] = (long)eulers[el0.build1] * (long)eulers[el0.build2];

        for (int i = 1; i < elementsCnt; i++) {
            Elmt el = elements.get(i);
            cumSum[i] = cumSum[i - 1] + (long)eulers[el.build1] * (long)eulers[el.build2];
        }

        StringBuilder output = new StringBuilder();

        for (long[] req : requests) {
            double posStartDouble = (Math.sqrt(8 * req[0] + 1) - 1) / 2;
            int posStart = Math.floor(posStartDouble) == posStartDouble ? (int)posStartDouble - 1 : (int)posStartDouble;

            double posEndDouble = (Math.sqrt(8 * req[1] + 1) - 1) / 2;
            int posEnd = (int)posEndDouble - 1;

            if (posStart == 0) {
                output.append(cumSum[posEnd]).append("\n");
            } else {
                long res = cumSum[posEnd] - cumSum[posStart - 1];
                output.append(res < 0 ? 0 : res).append("\n");
            }
        }

        System.out.println(output);
    }
}
