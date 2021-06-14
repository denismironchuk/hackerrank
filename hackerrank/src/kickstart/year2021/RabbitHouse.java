package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import java.util.function.Function;

public class RabbitHouse {

    public class AggregationContext<T extends Comparable, R> {

        private Function<TreapNode<T, R>, R> aggregationFunction;
        private Function<TreapNode<T, R>, R> initValSupplier;

        public AggregationContext(Function<TreapNode<T, R>, R> initValSupplier, Function<TreapNode<T, R>, R> aggregationFunction) {
            this.initValSupplier = initValSupplier;
            this.aggregationFunction = aggregationFunction;
        }

        public Function<TreapNode<T, R>, R> getAggregationFunction() {
            return aggregationFunction;
        }

        public Function<TreapNode<T, R>, R> getInitValSupplier() {
            return initValSupplier;
        }
    }

    public class TreapNode<T extends Comparable, R> {

        private AggregationContext<T, R> aggregationContext;

        private T x;
        private int y;

        private TreapNode<T, R> left;
        private TreapNode<T, R> right;

        private R treeAggregatedValue;

        public TreapNode(final T x, final AggregationContext<T, R> aggregationContext) {
            this.x = x;
            this.y = (int)(Math.random() * Integer.MAX_VALUE);
            this.aggregationContext = aggregationContext;

            if (aggregationContext != null) {
                this.treeAggregatedValue = aggregationContext.getInitValSupplier().apply(this);
            }
        }

        public TreapNode(final T x, final int y, final TreapNode<T, R> left, final TreapNode<T, R> right, final AggregationContext<T, R> aggregationContext) {
            this.x = x;
            this.y = y;

            this.right = right;
            this.left = left;

            this.aggregationContext = aggregationContext;
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
            if (aggregationContext != null) {
                this.treeAggregatedValue = aggregationContext.getAggregationFunction().apply(this);
            }
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

                res = new TreapNode<>(this.x, this.y, this.left, newTreap, aggregationContext);
            } else {
                TreapNode<T, R> newTreap = r.left == null ? this : this.merge(r.left);

                if (newTreap != null) {
                    newTreap.recalculateAggregation();
                }

                res = new TreapNode<>(r.x, r.y, newTreap, r.right, aggregationContext);
            }

            res.recalculateAggregation();

            return res;
        }

        public TreapNode[] split(T x) {
            TreapNode<T, R> newLeft = null;
            TreapNode<T, R> newRight = null;

            if (x.compareTo(this.x) == -1) {
                if (this.left == null) {
                    newRight = new TreapNode<>(this.x, this.y, this.left, this.right, aggregationContext);
                } else {
                    TreapNode<T, R>[] splitResult = this.left.split(x);
                    newLeft = splitResult[0];
                    newRight = new TreapNode<>(this.x, this.y, splitResult[1], this.right, aggregationContext);
                }
            } else {
                if (this.right == null) {
                    newLeft = new TreapNode<>(this.x, this.y, this.left, this.right, aggregationContext);
                } else {
                    TreapNode<T, R>[] splitResult = this.right.split(x);
                    newLeft = new TreapNode<>(this.x, this.y, this.left, splitResult[0], aggregationContext);
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
            TreapNode<T, R> newNode = new TreapNode<>(x, aggregationContext);

            TreapNode<T, R> res = null;

            if (splitRes[0] != null) {
                res = splitRes[0].merge(newNode);
            }

            if (res == null) {
                res = newNode.merge(splitRes[1]);
            } else {
                res = res.merge(splitRes[1]);
            }

            return res;
        }

        public TreapNode<T, R> erase(T x) {
            if (x.compareTo(this.x) == 0) {
                if (this.left == null && this.right == null) {
                    return null;
                } else if (this.left == null) {
                    return this.right;
                } else if (this.right == null) {
                    return this.left;
                }
                return this.left.merge(this.right);
            } else if (x.compareTo(this.x) == 1) {
                this.right = this.right.erase(x);
                return this;
            } else {
                this.left = this.left.erase(x);
                return this;
            }
        }

        public TreapNode<T, R> getMinNode() {
            if (this.left == null) {
                return this;
            } else {
                return this.left.getMinNode();
            }
        }

        public TreapNode<T, R> getMaxNode() {
            if (this.right == null) {
                return this;
            } else {
                return this.right.getMaxNode();
            }
        }

        public R getTreeAggregatedValue() {
            return treeAggregatedValue;
        }

        public void setTreeAggregatedValue(R treeAggregatedValue) {
            this.treeAggregatedValue = treeAggregatedValue;
        }
    }

    public class TreapUniversal<T extends Comparable, R> {

        private TreapNode<T, R> root;
        private AggregationContext<T, R> aggregationContext;

        public TreapUniversal() {
        }

        public void add(T x) {
            if (this.root == null) {
                this.root = new TreapNode<>(x, aggregationContext);
            } else {
                this.root = root.add(x);
            }
        }

        public void erase(T x) {
            this.root = root.erase(x);
        }

        public TreapNode<T, R> getMinNode() {
            return root.getMinNode();
        }

        public TreapNode<T, R> getMaxNode() {
            return root.getMaxNode();
        }

        public void setAggregationConfig(final Function<utils.universalTreap.TreapNode<T, R>, R> initValSupplier,
                                         final Function<utils.universalTreap.TreapNode<T, R>, R> aggregationFunction) {
            this.aggregationContext = new AggregationContext(initValSupplier, aggregationFunction);
        }
    }

    public void run() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int r = Integer.parseInt(tkn1.nextToken());
                int c = Integer.parseInt(tkn1.nextToken());

                int[][] grid = new int[r][c];
                Cell[] line = new Cell[r * c];
                TreapUniversal<Cell, Integer> treap = new TreapUniversal<>();
                int index = 0;

                for (int i = 0; i < r; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    for (int j = 0; j < c; j++) {
                        grid[i][j] = Integer.parseInt(tkn2.nextToken());
                        line[index] = new Cell(index, grid[i][j]);
                        treap.add(line[index]);
                        index++;
                    }
                }

                long res = 0;

                for (int i = 0; i < r * c; i++) {
                    TreapNode<Cell, Integer> node = treap.getMaxNode();
                    Cell cell = node.getX();
                    treap.erase(cell);

                    res += processNeighbour(cell, cell.getLeft(line, r, c), treap);
                    res += processNeighbour(cell, cell.getRight(line, r, c), treap);
                    res += processNeighbour(cell, cell.getAbove(line, r, c), treap);
                    res += processNeighbour(cell, cell.getBelow(line, r, c), treap);
                }

                System.out.printf("Case #%s: %s", t, res);
            }
        }
    }

    private long processNeighbour(Cell cell, Cell neighbour, TreapUniversal<Cell, Integer> treap) {
        long res = 0;
        if (neighbour != null) {
            if (cell.height - neighbour.height > 1) {
                res += cell.height - neighbour.height - 1;
                treap.erase(neighbour);
                neighbour.height = cell.height - 1;
                treap.add(neighbour);
            }
        }
        return res;
    }

    public class Cell implements Comparable<Cell> {

        private int num;
        private int height;

        public Cell(int num, int height) {
            this.num = num;
            this.height = height;
        }

        public Cell getLeft(Cell[] line, int r, int c) {
            if (num % c == 0) {
                return null;
            }
            return line[num - 1];
        }

        public Cell getRight(Cell[] line, int r, int c) {
            if ((num + 1) % c == 0) {
                return null;
            }
            return line[num + 1];
        }

        public Cell getAbove(Cell[] line, int r, int c) {
            if (num < c) {
                return null;
            }
            return line[num - c];
        }

        public Cell getBelow(Cell[] line, int r, int c) {
            if (num + c >= r * c) {
                return null;
            }
            return line[num + c];
        }

        @Override
        public int compareTo(Cell o) {
            int comp1 = Integer.compare(height, o.height);
            if (comp1 == 0) {
                return Integer.compare(num, o.num);
            } else {
                return comp1;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new RabbitHouse().run();
    }
}
