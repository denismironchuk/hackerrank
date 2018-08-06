package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Denis_Mironchuk on 6/20/2018.
 */
public class Test {
    public static void main(String[] args) {
        List<Double> testList = new ArrayList<>();
        testList.add(0.1);
        testList.add(0.22);
        System.out.println(testList.stream().collect(Collectors.summingDouble(a -> a)));


    }
}
