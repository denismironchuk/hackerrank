package kickstart.year2021;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TruckDelivery {

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

            recalculateAggregation();
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

        public TreapNode<T, R>[] split(T x) {
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
                this.recalculateAggregation();
                return this;
            } else {
                this.left = this.left.erase(x);
                this.recalculateAggregation();
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

        public TreapNode<T, R>[] split(T x) {
            return this.root.split(x);
        }

        public void merge(TreapNode<T, R> l, TreapNode<T, R> r) {
            if (l == null) {
                this.root = r;
            } else if (r == null) {
                this.root = l;
            } else {
                this.root = l.merge(r);
            }
        }

        public void setAggregationConfig(final Function<TreapNode<T, R>, R> initValSupplier,
                                         final Function<TreapNode<T, R>, R> aggregationFunction) {
            this.aggregationContext = new AggregationContext(initValSupplier, aggregationFunction);
        }
    }

    private class Road {
        private Node city1;
        private Node city2;
        private long charge;
        private long loadLimit;

        public Road(Node city1, Node city2, long charge, long loadLimit) {
            this.city1 = city1;
            this.city2 = city2;
            this.charge = charge;
            this.loadLimit = loadLimit;
        }
    }

    private class Node {

        private int cityNum;
        private Node parent = null;
        private List<Node> children = new ArrayList<>();
        private Map<Integer, Long> charges = new HashMap<>();
        private Map<Integer, Long> loadLimits = new HashMap<>();

        public Node(int num) {
            this.cityNum = num;
        }

        public void addRoad(Road road) {
            if (road.city1 == this) {
                children.add(road.city2);
                road.city2.parent = this;
                charges.put(road.city2.cityNum, road.charge);
                loadLimits.put(road.city2.cityNum, road.loadLimit);
            } else {
                children.add(road.city1);
                road.city1.parent = this;
                charges.put(road.city1.cityNum, road.charge);
                loadLimits.put(road.city1.cityNum, road.loadLimit);
            }
        }
    }

    private class Query {

        private int city;
        private long weight;

        private long result;

        public Query(int city, long weight) {
            this.city = city;
            this.weight = weight;
        }

        public long getResult() {
            return result;
        }
    }

    public void run() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int q = Integer.parseInt(tkn1.nextToken());

                Node[] nodes = new Node[n + 1];
                Map<Integer, List<Road>> roads = new HashMap<>();
                for (int i = 1; i <= n; i++) {
                    nodes[i] = new Node(i);
                    roads.put(i, new ArrayList<>());
                }

                for (int i = 0; i < n - 1; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    int city1 = Integer.parseInt(tkn.nextToken());
                    int city2 = Integer.parseInt(tkn.nextToken());
                    long loadLimit = Long.parseLong(tkn.nextToken());
                    long charge = Long.parseLong(tkn.nextToken());
                    Road road = new Road(nodes[city1], nodes[city2], charge, loadLimit);
                    roads.get(city1).add(road);
                    roads.get(city2).add(road);
                }

                buildTree(nodes[1], roads);

                List<Query> queries = new ArrayList<>();
                Map<Integer, Map<Long, List<Query>>> queryMap = new HashMap<>();

                for (int i = 0; i < q; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    int city = Integer.parseInt(tkn.nextToken());
                    long weight = Long.parseLong(tkn.nextToken());
                    Query query = new Query(city, weight);
                    queries.add(query);

                    if (!queryMap.containsKey(city)) {
                        queryMap.put(city, new HashMap<>());
                    }

                    Map<Long, List<Query>> weightMap = queryMap.get(city);

                    if (!weightMap.containsKey(weight)) {
                        weightMap.put(weight, new ArrayList<>());
                    }

                    weightMap.get(weight).add(query);
                }

                TreapUniversal<TreapNodeValue, Long> treap = new TreapUniversal<>() ;
                treap.setAggregationConfig((node) -> node.x.charge,
                        (node) -> Stream.of(node.x.charge,
                                node.left == null ? null : node.left.treeAggregatedValue,
                                node.right == null ? null : node.right.treeAggregatedValue).filter(Objects::nonNull).reduce((a, b) -> gcd(a, b)).get());

                treap.add(new TreapNodeValue(Long.MAX_VALUE, 1, 1));

                dfs(nodes[1], treap, queryMap);
                System.out.printf("Case #%s: %s\n", t, queries.stream().map(Query::getResult).map(String::valueOf).collect(Collectors.joining(" ")));
            }
        }
    }

    private void dfs(Node currentNode, TreapUniversal<TreapNodeValue, Long> treap, Map<Integer, Map<Long, List<Query>>> queryMap) {
        for (Node child : currentNode.children) {
            int childCityNum = child.cityNum;
            long loadLimit = currentNode.loadLimits.get(childCityNum);
            long charge = currentNode.charges.get(childCityNum);
            TreapNodeValue treapNode = new TreapNodeValue(loadLimit, childCityNum, charge);
            treap.add(treapNode);

            if (queryMap.containsKey(childCityNum)) {
                for (Map.Entry<Long, List<Query>> entry : queryMap.get(childCityNum).entrySet()) {
                    long weight = entry.getKey();
                    List<Query> queries = entry.getValue();

                    TreapNode<TreapNodeValue, Long>[] splitResult = treap.split(new TreapNodeValue(weight, Integer.MAX_VALUE, -1));
                    long res = splitResult[0] == null ? 0 : splitResult[0].treeAggregatedValue;

                    for (Query q : queries) {
                        q.result = res;
                    }

                    treap.merge(splitResult[0], splitResult[1]);
                }
            }

            dfs(child, treap, queryMap);
            treap.erase(treapNode);
        }
    }

    private class TreapNodeValue implements Comparable<TreapNodeValue> {

        private long loadLimit;
        private int city;
        private long charge;

        public TreapNodeValue(long loadLimit, int city, long charge) {
            this.loadLimit = loadLimit;
            this.city = city;
            this.charge = charge;
        }

        @Override
        public int compareTo(TreapNodeValue o) {
            int weightLimitCompare = Long.compare(loadLimit, o.loadLimit);
            if (weightLimitCompare == 0) {
                return Integer.compare(city, o.city);
            } else {
                return weightLimitCompare;
            }
        }
    }

    private long gcd(long a, long b) {
        return a > b ? gcdIntern(a, b) : gcdIntern(b, a);
    }

    private long gcdIntern(long a, long b) {
        if (a % b == 0) {
            return b;
        }

        return gcdIntern(b, a % b);
    }

    private void buildTree(Node currentNode, Map<Integer, List<Road>> roads) {
        for (Road road : roads.get(currentNode.cityNum)) {
            if (currentNode == road.city1 && currentNode.parent != road.city2) {
                currentNode.addRoad(road);
                buildTree(road.city2, roads);
            } else if (currentNode == road.city2 && currentNode.parent != road.city1) {
                currentNode.addRoad(road);
                buildTree(road.city1, roads);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new TruckDelivery().run();
    }
}
