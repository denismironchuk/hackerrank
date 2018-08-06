package cardsPermutations.treap;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TreapApp {
    private static final long Y_DISP = 100l;

    public static void main(String[] args) {
        Date start = new Date();
        Set<Integer> usedX = new HashSet<>();
        Set<Long> usedY = new HashSet<>();
        int n = 10;
        int x = generateX(usedX, n);
        usedX.add(x);

        long y = generateY(usedY);
        usedY.add(y);

        Treap t = new Treap(x, y, null, null);

        for (int i = 1; i < n; i++) {
            x = generateX(usedX, n);
            usedX.add(x);

            y = generateY(usedY);
            usedY.add(y);

            Treap res = new Treap(x, y, null, null);

            Treap[] splitRes = t.split(x);
            if (null != splitRes[0]) {
                res = Treap.merge(splitRes[0], res);
            }

            if (null != splitRes[1]) {
                res = Treap.merge(res, splitRes[1]);
            }
            t = res;
        }

        System.out.println("Height = " + height(t));
        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");
    }

    private static int height(Treap t) {
        if (t == null) {
            return 0;
        }

        return Math.max(height(t.getLeft()), height(t.getRight())) + 1;
    }


    private static long generateY(final Set<Long> usedY) {
        long y;

        do {
            y = (long)(Y_DISP * Math.random());
        } while (usedY.contains(y));
        return y;
    }

    private static int generateX(final Set<Integer> usedX, final int n) {
        int x;

        do {
            x = (int)(n * Math.random() * 1000);
        } while (usedX.contains(x));
        return x;
    }
}
