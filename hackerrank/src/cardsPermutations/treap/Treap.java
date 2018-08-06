package cardsPermutations.treap;

public class Treap {
    private int x;
    private long y;

    private Treap left;
    private Treap right;

    private int size;

    public Treap(final int x, final long y, final Treap left, final Treap right) {
        this.x = x;
        this.y = y;
        this.right = right;
        this.left = left;
    }

    public int getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public Treap getLeft() {
        return left;
    }

    public Treap getRight() {
        return right;
    }

    private static void recalculateSize(Treap t) {
        if (null != t) {
            t.recalculateSize();
        }
    }
    private void recalculateSize() {
        size = (null == left ? 0 : left.size) + (null == right ? 0 : right.size) + 1;
    }

    public static Treap merge(Treap l, Treap r) {
        if (null == l) {
            return r;
        }

        if (null == r) {
            return l;
        }

        Treap res;

        if (l.y > r.y) {
            Treap newTreap = Treap.merge(l.right, r);
            recalculateSize(newTreap);
            res = new Treap(l.x, l.y, l.left, newTreap);
        } else {
            Treap newTreap = Treap.merge(l, r.left);
            recalculateSize(newTreap);
            res = new Treap(r.x, r.y, newTreap, r.right);
        }

        recalculateSize(res);
        return res;
    }

    public Treap[] split(int x) {
        Treap newLeft = null;
        Treap newRight = null;

        if (x < this.x) {

            if (this.left == null) {
                newRight = new Treap(this.x, this.y, this.left, this.right);
            } else {
                Treap[] splitResult = this.left.split(x);
                newLeft = splitResult[0];
                newRight = new Treap(this.x, this.y, splitResult[1], this.right);
            }
        } else {
            if (this.right == null) {
                newLeft = new Treap(this.x, this.y, this.left, this.right);
            } else {
                Treap[] splitResult = this.right.split(x);
                newLeft = new Treap(this.x, this.y, this.left, splitResult[0]);
                newRight = splitResult[1];
            }
        }

        recalculateSize(newLeft);
        recalculateSize(newRight);

        return new Treap[]{newLeft, newRight};
    }
}
