package utils.universalTreap;

public class TreapUniversal<T extends Comparable> {

    private TreapNode<T> root;

    public TreapUniversal() {
    }

    public void add(T x) {
        if (this.root == null) {
            this.root = new TreapNode<>(x, this);
        } else {
            this.root = root.add(x);
        }
    }

    public void erase(T x) {
        this.root = root.erase(x);
    }


    private class TreapNode<T extends Comparable> {

        private TreapUniversal<T> treapContext;

        private T x;
        private int y;

        private TreapNode<T> left;
        private TreapNode<T> right;

        public TreapNode(final T x, final TreapUniversal<T> treapContext) {
            this.x = x;
            this.y = (int)(Math.random() * Integer.MAX_VALUE);
            this.treapContext = treapContext;
        }

        public TreapNode(final T x, final int y, final TreapNode<T> left, final TreapNode<T> right) {
            this.x = x;
            this.y = y;

            this.right = right;
            this.left = left;
        }

        public T getX() {
            return x;
        }

        public TreapNode<T> getLeft() {
            return left;
        }

        public TreapNode<T> getRight() {
            return right;
        }

        public TreapNode<T> merge(TreapNode<T> r) {
            if (null == r) {
                return this;
            }

            TreapNode<T> res;

            if (this.y > r.y) {
                TreapNode<T> newTreap = this.right == null ? r : this.right.merge(r);
                res = new TreapNode<>(this.x, this.y, this.left, newTreap);
            } else {
                TreapNode<T> newTreap = r.left == null ? this : this.merge(r.left);
                res = new TreapNode<>(r.x, r.y, newTreap, r.right);
            }

            return res;
        }

        public TreapNode[] split(T x) {
            TreapNode<T> newLeft = null;
            TreapNode<T> newRight = null;

            if (x.compareTo(this.x) == -1) {
                if (this.left == null) {
                    newRight = new TreapNode<>(this.x, this.y, this.left, this.right);
                } else {
                    TreapNode<T>[] splitResult = this.left.split(x);
                    newLeft = splitResult[0];
                    newRight = new TreapNode<>(this.x, this.y, splitResult[1], this.right);
                }
            } else {
                if (this.right == null) {
                    newLeft = new TreapNode<>(this.x, this.y, this.left, this.right);
                } else {
                    TreapNode<T>[] splitResult = this.right.split(x);
                    newLeft = new TreapNode<>(this.x, this.y, this.left, splitResult[0]);
                    newRight = splitResult[1];
                }
            }

            return new TreapNode[]{newLeft, newRight};
        }

        public TreapNode<T> add(T x) {
            TreapNode<T>[] splitRes = split(x);
            TreapNode<T> newNode = new TreapNode<>(x, treapContext);
            return splitRes[0].merge(newNode).merge(splitRes[1]);
        }

        public TreapNode<T> erase(T x) {
            if (x.compareTo(this.x) == 0) {
                return this.left.merge(this.right);
            } else if (x.compareTo(this.x) == 1) {
                return this.right.erase(x);
            } else {
                return this.left.erase(x);
            }
        }
    }
}
