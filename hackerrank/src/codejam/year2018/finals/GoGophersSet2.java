package codejam.year2018.finals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;

public class GoGophersSet2 {

    public static final int[] GOPH_CNTS = new int[] {4, 4, 2, 1};
    public static final int CNT = Arrays.stream(GOPH_CNTS).sum();

    private List<Integer> TASTES;
    private List<Integer> TASTES_USED;

    private static int MAX_VAL = 1000000;
    private static int MIN_VAL = 1;
    private static int MAX_GOPHERS = 25;
    private static final Random RND = new Random();

    private int observationCount = 0;

    public static void main(String[] args) {
        new GoGophersSet2().run();
    }

    public void run() {
        List<Integer> tastesTmp = new ArrayList<>();
        while (tastesTmp.size() != GOPH_CNTS.length) {
            int candidate = RND.nextInt(MAX_VAL) + 1;
            if (!tastesTmp.contains(candidate)) {
                tastesTmp.add(candidate);
            }
        }

        TASTES = new ArrayList<>();
        TASTES_USED = new ArrayList<>();

        for (int i = 0; i < GOPH_CNTS.length; i++) {
            for (int j = 0; j < GOPH_CNTS[i]; j++) {
                TASTES.add(tastesTmp.get(i));
            }
        }

        System.out.println(TASTES);

        int minTaste = findMinGopherTaste(MIN_VAL, MAX_VAL);

        System.out.println(minTaste);

        //Key - possible len
        //Value - min taste gophers count
        Map<Integer, Integer> possibleLen = new LinkedHashMap<>();

        int binSearchObservationsCnt = observationCount;
        int[] observations = getObservationsLocal(2000, minTaste);
        for (int len = 2; len < MAX_GOPHERS; len++) {
            int startObserve = len * (2 + binSearchObservationsCnt / len) + 1;
            int observedCnt = getObservedCnt(observations, startObserve, len);
            boolean correct = true;
            while (correct && startObserve + len <= observations.length) {
                int testCnt = getObservedCnt(observations, startObserve, len);
                correct = observedCnt == testCnt;
                startObserve += len;
            }
            if (correct) {
                possibleLen.put(len, observedCnt);
                System.out.println(len + " " + observedCnt);
            }
        }

        for (Map.Entry<Integer, Integer> entry : possibleLen.entrySet()) {
            int totalGophCnt = entry.getKey();
            int knownCnt = entry.getValue();

            int startObserve = totalGophCnt * (2 + observationCount / totalGophCnt) + 1;
            skipAttempts(startObserve - observationCount - 1);
            int taste = findGopherTaste(minTaste + 1, MAX_VAL, totalGophCnt, knownCnt);
            System.out.println(taste);
        }
    }

    public void skipAttempts(int skipCnt) {
        for (int i = 0; i < skipCnt; i++) {
            getGopherTasteLocal();
        }
    }

    private int getObservedCnt(int[] observations, int startObserve, int len) {
        return IntStream.range(startObserve, startObserve + len).map(i -> observations[i]).sum();
    }

    private int[] getObservationsLocal(int maxObservations, int snackTaste) {
        int[] observed = new int[maxObservations + 1];
        for (int i = observationCount + 1; i <= maxObservations; i++) {
            int gopherTaste = getGopherTasteLocal();
            observed[i] = gopherTaste > snackTaste ? 0 : 1;
        }
        return observed;
    }

    private int findGopherTaste(int start, int end, int totalGopherCnt, int knownCnt) {
        if (start == end) {
            return start;
        }

        int middle = (start + end) / 2;
        int gophCnt = 0;
        for (int i = 0; i < totalGopherCnt; i++) {
            gophCnt += checkIfGopherWillEatLocal(middle);
        }

        if (gophCnt == knownCnt) {
            return findGopherTaste(middle + 1, end, totalGopherCnt, knownCnt);
        } else {
            return findGopherTaste(start, middle, totalGopherCnt, knownCnt);
        }
    }

    private int findMinGopherTaste(int start, int end) {
        if (start == end) {
            return start;
        }

        int middle = (start + end) / 2;
        int gophCnt = 0;
        for (int i = 0; i < 2 * MAX_GOPHERS; i++) {
            gophCnt += checkIfGopherWillEatLocal(middle);
        }

        if (gophCnt == 0) {
            return findMinGopherTaste(middle + 1, end);
        } else {
            return findMinGopherTaste(start, middle);
        }
    }

    private int checkIfGopherWillEatLocal(int taste) {
        int gopherTaste = getGopherTasteLocal();
        return gopherTaste > taste ? 0 : 1;
    }

    private int getGopherTasteLocal() {
        observationCount++;
        if (TASTES.size() == 0) {
            TASTES.addAll(TASTES_USED);
            TASTES_USED.clear();
        }
        int indexToTake = RND.nextInt(TASTES.size());
        int gopherTaste = TASTES.get(indexToTake);
        TASTES.remove(indexToTake);
        TASTES_USED.add(gopherTaste);
        return gopherTaste;
    }
}
