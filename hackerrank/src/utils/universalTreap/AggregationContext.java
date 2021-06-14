package utils.universalTreap;

import java.util.function.Function;

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
