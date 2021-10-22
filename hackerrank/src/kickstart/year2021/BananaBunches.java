package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.StringTokenizer;

public class BananaBunches {

    private static final Random rnd = new Random();

    private static class Segment implements Comparable<Segment> {

        private int start;
        private int end;

        public Segment(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public int getLen() {
            return end - start + 1;
        }

        @Override
        public int compareTo(Segment o) {
            int cmp1 = Integer.compare(start, o.start);
            if (cmp1 != 0) {
                return cmp1;
            } else {
                return Integer.compare(end, o.end);
            }
        }

        @Override
        public String toString() {
            return "(" + start + ", " + end + ")";
        }
    }

    private static class Treap {

        private Segment x;
        private long y;

        private Treap left;
        private Treap right;

        private int minLen;

        public Treap(Segment x) {
            this.x = x;
            this.y = rnd.nextLong();
            this.minLen = x.getLen();
        }

        private void recalculateMinLen() {
            this.minLen = Math.min(x.getLen(),
                    Math.min(left == null ? Integer.MAX_VALUE : left.minLen,
                            right == null ? Integer.MAX_VALUE : right.minLen));
        }

        public Treap[] split(Segment s) {
            Treap[] res;
            if (x.compareTo(s) < 0) {
                if (right == null) {
                    return new Treap[] {this, null};
                }

                Treap[] splitRes = right.split(s);
                right = splitRes[0];
                res = new Treap[] {this, splitRes[1]};
            } else {
                if (left == null) {
                    return new Treap[]{null, this};
                }

                Treap[] splitRes = left.split(s);
                left = splitRes[1];
                res = new Treap[]{splitRes[0], this};
            }
            recalculateMinLen();
            return res;
        }

        public static Treap merge(Treap lower, Treap higher) {
            if (lower == null) {
                return higher;
            }

            if (higher == null) {
                return lower;
            }

            Treap res;
            if (lower.y > higher.y) {
                lower.right = merge(lower.right, higher);
                res = lower;
            } else {
                higher.left = merge(lower, higher.right);
                res = higher;
            }
            res.recalculateMinLen();
            return res;
        }

        public Treap addNode(Segment x) {
            Treap[] split = this.split(x);
            return Treap.merge(Treap.merge(split[0], new Treap(x)), split[1]);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int k = Integer.parseInt(tkn1.nextToken());
                long[] b = new long[n];

                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    b[i] = Long.parseLong(tkn2.nextToken());
                }

                long[] aggrCnt = new long[n];
                aggrCnt[0] = b[0];
                for (int i = 1; i < n; i++) {
                    aggrCnt[i] = aggrCnt[i - 1] + b[i];
                }

                Treap[] bananaCntMap = new Treap[k + 1];
                for (int i = 0; i < n; i++) {
                    boolean less = true;
                    for (int j = i; less && j < n; j++) {
                        Segment s = new Segment(i, j);
                        long cnt = i == 0 ? aggrCnt[j] : aggrCnt[j] - aggrCnt[i - 1];
                        if (cnt <= k) {
                            Treap treap = bananaCntMap[(int)(cnt)];
                            Treap sTreap = new Treap(s);
                            if (treap == null) {
                                bananaCntMap[(int)(cnt)] = sTreap;
                            } else {
                                Treap[] split = treap.split(s);
                                bananaCntMap[(int)(cnt)] = Treap.merge(Treap.merge(split[0], sTreap), split[1]);
                            }
                        } else {
                            less = false;
                        }
                    }
                }

                int res = Integer.MAX_VALUE;

                for (int i = 0; i < n; i++) {
                    boolean less = true;
                    for (int j = i; less && j < n; j++) {
                        long cnt = i == 0 ? aggrCnt[j] : aggrCnt[j] - aggrCnt[i - 1];
                        if (cnt == k) {
                            res = Math.min(res, j - i + 1);
                        } else if (cnt < k) {
                            int cnt2 = (int)(k - cnt);
                            Treap treap = bananaCntMap[cnt2];
                            if (null != treap) {
                                Treap[] splitRes = treap.split(new Segment(j + 1, j + 1));
                                if (splitRes[1] != null) {
                                    res = Math.min(res, j - i + 1 + splitRes[1].minLen);
                                }
                                bananaCntMap[cnt2] = Treap.merge(splitRes[0], splitRes[1]);
                            }
                        } else {
                            less = false;
                        }
                    }
                }

                if (res == Integer.MAX_VALUE) {
                    res = -1;
                }

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
