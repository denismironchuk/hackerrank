package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class SillySubstitutions {

    private static class Pair {
        int leftIndex;
        int rightIndex;

        public Pair(int leftIndex, int rightIndex) {
            this.leftIndex = leftIndex;
            this.rightIndex = rightIndex;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair pair = (Pair) o;
            return leftIndex == pair.leftIndex && rightIndex == pair.rightIndex;
        }

        @Override
        public int hashCode() {
            return Objects.hash(leftIndex, rightIndex);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                String s = br.readLine();
                int[] a = new int[n];
                int[] next = new int[n];
                int[] prev = new int[n];
                for (int i = 0; i < n; i++) {
                    a[i] = s.charAt(i) - '0';
                    prev[i] = i > 0 ? i - 1 : -1;
                    next[i] = i < n - 1 ? i + 1 : -1;
                }
                Set<Pair>[] sets = new Set[10];
                for (int i = 0; i < 10; i++) {
                    sets[i] = new HashSet<>();
                }
                for (int i = 0; i < n - 1; i++) {
                    if ((a[i] + 1) % 10 == a[i + 1]) {
                        sets[a[i]].add(new Pair(i, i + 1));
                    }
                }
                boolean subs = true;
                while (subs) {
                    subs = false;
                    for (int i = 0; i < 10; i++) {
                        if (!sets[i].isEmpty()) {
                            subs = true;
                            Iterator<Pair> itr = sets[i].iterator();
                            while (itr.hasNext()) {
                                Pair pair = itr.next();
                                itr.remove();

                                int index1 = pair.leftIndex;
                                int index2 = pair.rightIndex;
                                a[index1] = (i + 2) % 10;
                                if (prev[index1] != -1) {
                                    sets[a[prev[index1]]].remove(new Pair(prev[index1], index1));
                                }
                                if (next[index2] != -1) {
                                    sets[a[index2]].remove(new Pair(index2, next[index2]));
                                }
                                a[index2] = -1;
                                next[index1] = next[index2];
                                if (next[index2] != -1) {
                                    prev[next[index2]] = index1;
                                }
                                if (prev[index1] != -1 && (a[prev[index1]] + 1) % 10 == a[index1]) {
                                    sets[a[prev[index1]]].add(new Pair(prev[index1], index1));
                                }
                                if (next[index1] != -1 && (a[index1] + 1) % 10 == a[next[index1]]) {
                                    sets[a[index1]].add(new Pair(index1, next[index1]));
                                }

                            }
                        }
                    }
                }
                StringBuilder res = new StringBuilder();
                for (int i = 0; i < n; i++) {
                    if (a[i] != -1) {
                        res.append(a[i]);
                    }
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
