package codejam.year2018.finals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GoGophersSet1_Test {

    private static int MAX_VAL = 50;
    private static int MIN_VAL = 1;
    private static int MAX_GOPHERS = 25;
    private static final Random RND = new Random();

    private int attempts = 0;
    private int GOPHERS_CNT = 25;
    private List<Integer> TASTES;
    private List<Integer> TASTES_USED;

    private void run() throws IOException {
        int s = 10000;
        attempts = 0;
        TASTES = new ArrayList<>();
        TASTES_USED = new ArrayList<>();
        while (TASTES.size() != GOPHERS_CNT) {
            int candidate = RND.nextInt(MAX_VAL) + 1;
            if (!TASTES.contains(candidate)) {
                TASTES.add(candidate);
            }
        }

        int minTaste = find1GopherValue(MIN_VAL, MAX_VAL);
        int[] observed = getObservationsLocal(s, minTaste);

        boolean correct = false;
        int len = 2;
        for (; !correct && len <= MAX_GOPHERS; len++) {
            int startObserve = len * (2 + attempts / len) + 1;
            int onesCnt = 0;
            correct = true;
            for (int i = startObserve; correct && i <= s; i++) {
                onesCnt += observed[i];
                if (i % len == 0) {
                    if (onesCnt != 1) {
                        correct = false;
                    }
                    onesCnt = 0;
                }
            }
        }

        if (!correct || len - 1 != GOPHERS_CNT) {
            throw new RuntimeException();
        } else {
            System.out.println(len - 1);
        }
    }

    private int[] getObservationsLocal(int s, int snackTaste) {
        int[] observed = new int[s + 1];
        for (int i = attempts + 1; i <= s; i++) {
            int gopherTaste = getGopherTasteLocal();
            observed[i] = gopherTaste > snackTaste ? 0 : 1;
        }
        return observed;
    }

    public static void main(String[] args) throws IOException {
        GoGophersSet1_Test inst = new GoGophersSet1_Test();
        while (true) {
            inst.run();
        }
    }

    private int find1GopherValue(int start, int end) throws IOException {
        if (start == end) {
            return start;
        }

        int middle = (start + end) / 2;
        int gophCnt = 0;
        for (int i = 0; i < 2 * MAX_GOPHERS; i++) {
            gophCnt += checkIfGopherWillEatLocal(middle);
        }

        if (gophCnt == 0) {
            return find1GopherValue(middle + 1, end);
        } else {
            return find1GopherValue(start, middle);
        }
    }

    private int checkIfGopherWillEatLocal(int taste) {
        attempts++;
        int gopherTaste = getGopherTasteLocal();
        return gopherTaste > taste ? 0 : 1;
    }

    private int getGopherTasteLocal() {
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
