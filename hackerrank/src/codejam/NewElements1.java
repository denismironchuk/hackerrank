package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class NewElements1 {
    private static class Pair {
        private int c;
        private int j;

        public Pair(int c, int j) {
            this.c = c;
            this.j = j;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int n = Integer.parseInt(br.readLine());
            Pair[] moleculs = new Pair[n];
            for (int i = 0; i < n; i++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int c = Integer.parseInt(tkn.nextToken());
                int j = Integer.parseInt(tkn.nextToken());
                moleculs[i] = new Pair(c, j);
            }

            Map<Integer, Set<Integer>> drobs = new HashMap<>();

            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (Integer.compare(moleculs[i].c, moleculs[j].c) * Integer.compare(moleculs[i].j, moleculs[j].j) < 0) {
                        int chisl = Math.abs(moleculs[i].c - moleculs[j].c);
                        int znam = Math.abs(moleculs[i].j - moleculs[j].j);
                        int gcd = gcd(chisl, znam);
                        chisl /= gcd;
                        znam /= gcd;

                        Set<Integer> znams = drobs.get(chisl);

                        if (znams == null) {
                            znams = new HashSet<>();
                            znams.add(znam);
                            drobs.put(chisl, znams);
                        } else {
                            if (!znams.contains(znam)) {
                                znams.add(znam);
                            }
                        }
                    }
                }
            }

            int res = 1;

            for (Set<Integer> znams : drobs.values()) {
                res += znams.size();
            }

            System.out.printf("Case #%s: %s\n", t + 1, res);
        }
    }

    private static int gcd(int a, int b) {
        return a < b ? gcdInner(b, a) : gcdInner(a, b);
    }

    private static int gcdInner(int a, int b) {
        if (b == 0) {
            return a;
        } else {
            return gcdInner(b, a % b);
        }
    }
}
