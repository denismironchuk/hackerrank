package search;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class AlmostIntegerRockGarden {
    private static final double EPS = 0.000001;
    private static final double MUL = 1000000;
    private static final int CNT = 6;

    public static void main(String[] args) {
        Set<Integer> squares = new HashSet<>();

        for (int i = 0; i * i <= 400; i++) {
            squares.add(i * i);
        }

        List<int[]> points = new ArrayList<>();

        for (int x = -12; x <= 12; x++) {
            for (int y = -12; y <= 12; y++) {
                int sqrDist = x * x + y * y;
                if (!squares.contains(sqrDist)) {
                    points.add(new int[]{x, y});
                }
            }
        }

        List<Double> dists = points.stream().map(p -> Math.sqrt(p[0] * p[0] + p[1] * p[1])).distinct().collect(Collectors.toList());
        dists.sort(new Comparator<Double>() {
            @Override
            public int compare(Double o1, Double o2) {
                return Double.compare(getAfterPoint(o1), getAfterPoint(o2));
            }

            private Double getAfterPoint(Double d) {
                return d - d.intValue();
            }
        });
        dists.forEach(System.out::println);
        Set<Integer> procPos = new HashSet<>();
        //List<Integer> procPos = new ArrayList<>();
        System.out.println("==========");
        Date start = new Date();
        search(procPos, 0.0, dists, 0);
        Date end = new Date();
        System.out.println(end.getTime() - start.getTime() + "ms");
        System.out.println("==========");
        procPos.stream().map(dists::get).forEach(System.out::println);
        System.out.println(procPos.stream().map(dists::get).reduce((a, b) -> a + b));
        System.out.println(procPos);
    }

    private static boolean search(Set<Integer> procPos, Double sum, List<Double> dists, int startIndex) {
        if (procPos.size() == CNT) {
            //System.out.println("Solution found");

            if (Math.abs(Math.round(sum) - sum) < EPS) {
                //System.out.println(procPos);
                //procPos.stream().map(dists::get).forEach(d -> System.out.printf("%s ", d));
                //System.out.println();
                System.out.println(procPos.stream().map(dists::get).reduce((a, b) -> a + b).get());
                return true;
            } else {
                return false;
            }
            //System.out.println();
            //return Math.abs(Math.round(sum) - sum) < EPS;
        }

        for (int i = startIndex; i < dists.size(); i++) {
            if (!procPos.contains(i)) {
                procPos.add(i);
                sum += MUL * dists.get(i);
                boolean res = search(procPos, sum, dists, i+1);
                /*if (res == true) {
                    return true;
                } else {*/
                    //Integer wrwer = procPos.remove(procPos.size() - 1);
                procPos.remove(i);
                    sum -= MUL * dists.get(i);
                //}
            }
        }

        return false;
    }
}
