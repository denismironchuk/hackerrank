package codejam.year2022.round1A;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class EqualSum {

    private static final Long[][] NUMBERS = {
            {1l},
            {2l},
            {4l},
            {8l},
            {16l},
            {32l},
            {64l},
            {128l},
            {256l},
            {512l},
            {1024l},
            {2048l},
            {4096l},
            {8192l},
            {16384l},
            {32768l},
            {65536l},
            {131072l},
            {262144l},
            {524288l},
            {1048576l},
            {2097152l},
            {4194304l},
            {8388608l},
            {16777216l},
            {33554432l},
            {67108864l},
            {134217728l},
            {268435456l},
            {536870912l},
            {1000000000l, 73741824l},
            {999999999l, 999999998l, 147483651l},
            {999999997l, 999999996l, 999999995l, 999999994l, 294967314l},
            {999999993l, 999999992l, 999999991l, 999999990l, 999999989l, 999999988l, 999999987l, 999999986l, 589934676l},
            {999999985l, 999999984l, 999999983l, 999999982l, 999999981l, 999999980l, 999999979l, 999999978l, 999999977l, 999999976l,
                    999999975l, 999999974l, 999999973l, 999999972l, 999999971l, 999999970l, 999999969l, 179869575l},

            //Rest of digits
            {999999968l, 999999967l, 999999966l, 999999965l, 999999964l, 999999963l, 999999962l, 999999961l, 999999960l, 999999959l, 999999958l, 999999957l,
                    999999956l, 999999955l, 999999954l, 999999953l, 999999952l, 999999951l, 999999950l, 999999949l, 999999948l, 999999947l, 999999946l, 999999945l,
                    999999944l, 999999943l, 999999942l, 999999941l, 999999940l, 999999939l, 999999938l, 999999937l, 999999936l}
    };

    public static long effectiveSum = 34359738367l;
    public static long totalSum = 67359736783l;

    public static void main(String[] args) throws IOException {
        /*Set<Long> mySet = new HashSet<>();

        for (int i = 0; i < NUMBERS.length; i++) {
            for (int j = 0; j < NUMBERS[i].length; j++) {
                mySet.add(NUMBERS[i][j]);
            }
        }

        Set<Long> bSet = new HashSet<>();

        Random rnd = new Random();

        for (int i = 0; i < 100; i++) {
            long candidate = 1;
            while (mySet.contains(candidate) || bSet.contains(candidate)) {
                candidate = 1 + (Math.abs(rnd.nextLong()) % 1000000000l);
            }
            bSet.add(candidate);
        }

        if (bSet.stream().reduce((a, b) -> a + b).get() % 2 == 0) {
            long tmp = bSet.iterator().next();
            bSet.remove(tmp);
            bSet.add(tmp + 1);
        }

        System.out.println(bSet.stream().map(String::valueOf).collect(Collectors.joining(" ")));
        System.out.println("=============================");*/

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int N = 100;//Integer.parseInt(br.readLine());
                Set<Long> aSet = new HashSet<>();
                for (int i = 0; i < NUMBERS.length; i++) {
                    for (int j = 0; j < NUMBERS[i].length; j++) {
                        //System.out.print(NUMBERS[i][j] + " ");
                        aSet.add(NUMBERS[i][j]);
                    }
                }

                List<Long> b = getB();
                System.out.println(b);
                long bSum = b.stream().reduce((q, f) -> q + f).get();
                long total = totalSum + bSum;
                long half = total / 2;

                Comparator<Long> cmp = Long::compare;
                b.sort(cmp.reversed());

                List<Long> result = new ArrayList<>();
                int pPointer = 0;
                while (half > effectiveSum) {
                    long bCurr = b.get(pPointer);
                    result.add(bCurr);
                    half -= bCurr;
                    pPointer++;
                }

                int binaryIndex = 0;
                while (half != 0) {
                    if (half % 2 == 1) {
                        Arrays.stream(NUMBERS[binaryIndex]).forEach(result::add);
                    }
                    binaryIndex++;
                    half /= 2;
                }
                System.out.println(result.stream().map(String::valueOf).collect(Collectors.joining(" ")));

                long expected = result.stream().reduce((a, c) -> a + c).get();
                if (expected * 2 != aSet.stream().reduce((a, c) -> a + c).get() + b.stream().reduce((a, c) -> a + c).get()) {
                    throw new RuntimeException();
                }
            }
        }
    }

    private static List<Long> getBFromStream(BufferedReader br, int N) throws IOException {
        StringTokenizer bLine = new StringTokenizer(br.readLine());
        List<Long> b = new ArrayList<>();
        long bSum = 0;
        for (int i = 0; i < N; i++) {
            long b_i = Long.parseLong(bLine.nextToken());
            b.add(b_i);
            bSum += b_i;
        }
        return b;
    }

    private static List<Long> getB() {
        Set<Long> mySet = new HashSet<>();

        for (int i = 0; i < NUMBERS.length; i++) {
            for (int j = 0; j < NUMBERS[i].length; j++) {
                mySet.add(NUMBERS[i][j]);
            }
            //System.out.println(mySet.stream().reduce((a, b) -> a + b).get());
        }

        Set<Long> bSet = new HashSet<>();

        Random rnd = new Random();

        for (int i = 0; i < 100; i++) {
            long candidate = 1;
            while (mySet.contains(candidate) || bSet.contains(candidate)) {
                candidate = 1 + (Math.abs(rnd.nextLong()) % 1000000000l);
            }
            bSet.add(candidate);
        }

        if (bSet.stream().reduce((a, b) -> a + b).get() % 2 == 0) {
            long tmp = bSet.iterator().next();
            bSet.remove(tmp);
            bSet.add(tmp + 1);
        }

        return new ArrayList<>(bSet);
    }
}
