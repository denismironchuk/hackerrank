package codejam.year2019.pancakePyramid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class PyramidTrivial {
    private static class PArray {
        private Long val;
        private int pos;

        public PArray(Long val, int pos) {
            this.val = val;
            this.pos = pos;
        }

        public Long getVal() {
            return val;
        }

        public int getPos() {
            return pos;
        }
    }

    public static long count(long[] p) {
        long res = 0;

        for (int i = 0; i < p.length - 1; i++) {
            for (int j = i + 1; j < p.length; j++) {
                //Arrays.copyOf(p, p.length);
                res += count(Arrays.copyOf(p, p.length), i, j);
            }
        }

        return res;
    }

    public static long count(long[] p, int start, int end) {
        int s = p.length;
        List<PArray> pArr = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            pArr.add(new PArray(p[i], i));
        }

        pArr.sort(Comparator.comparingLong(PArray::getVal).reversed().thenComparingInt(PArray::getPos));

        long maxVal = pArr.get(0).val;
        int firstMaxPos = pArr.get(0).pos;
        int lastMaxPos = firstMaxPos;

        for (PArray pa : pArr) {
            if (pa.val != maxVal) {
                break;
            } else {
                lastMaxPos = pa.pos;
            }
        }

        long res = 0;

        for (int i = start + 1; i < firstMaxPos; i++) {
            if (p[i] < p[i - 1]) {
                res += p[i - 1] - p[i];
                p[i] = p[i - 1];
            }
        }

        for (int i = firstMaxPos + 1; i < lastMaxPos; i++) {
            res += p[i - 1] - p[i];
            p[i] = p[i - 1];
        }

        for (int i = end - 1; i > lastMaxPos; i--) {
            if (p[i] < p[i + 1]) {
                res += p[i + 1] - p[i];
                p[i] = p[i + 1];
            }
        }

        return res;
    }
}
