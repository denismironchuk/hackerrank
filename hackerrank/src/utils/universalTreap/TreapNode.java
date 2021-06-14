package utils.universalTreap;

public class TreapNode<T extends Comparable, R> {

    private TreapUniversal<T, R> treapContext;

    private T x;
    private int y;

    private TreapNode<T, R> left;
    private TreapNode<T, R> right;

    private R treeAggregatedValue;

    public TreapNode(final T x, final TreapUniversal<T, R> treapContext) {
        this.x = x;
        this.y = (int)(Math.random() * Integer.MAX_VALUE);
        this.treapContext = treapContext;

        if (treapContext.getInitValSupplier() != null) {
            this.treeAggregatedValue = treapContext.getInitValSupplier().apply(this);
        }
    }

    public TreapNode(final T x, final int y, final TreapNode<T, R> left, final TreapNode<T, R> right) {
        this.x = x;
        this.y = y;

        this.right = right;
        this.left = left;
    }

    public T getX() {
        return x;
    }

    public TreapNode<T, R> getLeft() {
        return left;
    }

    public TreapNode<T, R> getRight() {
        return right;
    }

    private void recalculateAggregation() {
        this.treeAggregatedValue = treapContext.getAggregationFunction().apply(this);
    }

    public TreapNode<T, R> merge(TreapNode<T, R> r) {
        if (null == r) {
            return this;
        }

        TreapNode<T, R> res;

        if (this.y > r.y) {
            TreapNode<T, R> newTreap = this.right == null ? r : this.right.merge(r);

            if (newTreap != null) {
                newTreap.recalculateAggregation();
            }

            res = new TreapNode<>(this.x, this.y, this.left, newTreap);
        } else {
            TreapNode<T, R> newTreap = r.left == null ? this : this.merge(r.left);

            if (newTreap != null) {
                newTreap.recalculateAggregation();
            }

            res = new TreapNode<>(r.x, r.y, newTreap, r.right);
        }

        res.recalculateAggregation();

        return res;
    }

    public TreapNode[] split(T x) {
        TreapNode<T, R> newLeft = null;
        TreapNode<T, R> newRight = null;

        if (x.compareTo(this.x) == -1) {
            if (this.left == null) {
                newRight = new TreapNode<>(this.x, this.y, this.left, this.right);
            } else {
                TreapNode<T, R>[] splitResult = this.left.split(x);
                newLeft = splitResult[0];
                newRight = new TreapNode<>(this.x, this.y, splitResult[1], this.right);
            }
        } else {
            if (this.right == null) {
                newLeft = new TreapNode<>(this.x, this.y, this.left, this.right);
            } else {
                TreapNode<T, R>[] splitResult = this.right.split(x);
                newLeft = new TreapNode<>(this.x, this.y, this.left, splitResult[0]);
                newRight = splitResult[1];
            }
        }

        if (newLeft != null) {
            newLeft.recalculateAggregation();
        }

        if (newRight != null) {
            newRight.recalculateAggregation();
        }

        return new TreapNode[] {newLeft, newRight};
    }

    public TreapNode<T, R> add(T x) {
        TreapNode<T, R>[] splitRes = split(x);
        TreapNode<T, R> newNode = new TreapNode<>(x, treapContext);
        return splitRes[0].merge(newNode).merge(splitRes[1]);
    }

    public TreapNode<T, R> erase(T x) {
        if (x.compareTo(this.x) == 0) {
            return this.left.merge(this.right);
        } else if (x.compareTo(this.x) == 1) {
            return this.right.erase(x);
        } else {
            return this.left.erase(x);
        }
    }

    public R getTreeAggregatedValue() {
        return treeAggregatedValue;
    }

    public void setTreeAggregatedValue(R treeAggregatedValue) {
        this.treeAggregatedValue = treeAggregatedValue;
    }
}
