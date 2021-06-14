package utils.universalTreap;

import java.util.function.Function;

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

    public void setAggregationConfig(final Function<TreapNode<T, R>, R> initValSupplier,
                                     final Function<TreapNode<T, R>, R> aggregationFunction) {
        this.aggregationContext = new AggregationContext(initValSupplier, aggregationFunction);
    }
}
