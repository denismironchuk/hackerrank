package utils.universalTreap;

import java.util.function.BiFunction;
import java.util.function.Function;

public class TreapUniversal<T extends Comparable, R> {

    private TreapNode<T, R> root;
    private Function<TreapNode<T, R>, R> aggregationFunction;
    private Function<TreapNode<T, R>, R> initValSupplier;

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

    public void setAggregationConfig(final Function<TreapNode<T, R>, R> initValSupplier,
                                     final Function<TreapNode<T, R>, R> aggregationFunction) {
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
