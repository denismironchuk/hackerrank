package codejam.year2018.finals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GoGophersSet1 {

    private static int MAX_VAL = 1000000;
    private static int MIN_VAL = 1;
    private static int MAX_GOPHERS = 25;
    private static final Random RND = new Random();

    private int attempts = 0;
    private int GOPHERS_CNT = 25;
    private List<Integer> TASTES;
    private List<Integer> TASTES_USED;

    private void run() throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = 1;//Integer.parseInt(br.readLine());
            int s = 100000;//Integer.parseInt(br.readLine());
            for (int t = 0; t < T; t++) {
                attempts = 0;
                TASTES = new ArrayList<>();
                TASTES.add(MIN_VAL);
                TASTES.add(MAX_VAL);
                TASTES_USED = new ArrayList<>();
                while (TASTES.size() != GOPHERS_CNT) {
                    int candidate = RND.nextInt(1000000) + 1;
                    if (!TASTES.contains(candidate)) {
                        TASTES.add(candidate);
                    }
                }
                System.out.println(TASTES);

                int minTaste = find1GopherValue(MIN_VAL, MAX_VAL, br);

                System.out.println(minTaste);

                if (!TASTES.contains(minTaste) && !TASTES_USED.contains(minTaste)) {
                    throw new RuntimeException();
                }

                //int[] observed = getObservations(br, s, minTaste);
                int[] observed = getObservationsLocal(br, s, minTaste);

                boolean correct = false;
                for (int len = 2; !correct && len <= MAX_GOPHERS; len++) {
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
                    if (correct) {
                        System.out.println(len);
                    }
                }
            }
        }
    }

    private int[] getObservations(BufferedReader br, int s, int snackTaste) throws IOException {
        StringBuilder build = new StringBuilder();
        for (int i = attempts + 1; i <= s; i++) {
            build.append(snackTaste).append("\n");
        }
        System.out.println(build);
        int[] observed = new int[s + 1];
        for (int i = attempts + 1; i <= s; i++) {
            observed[i] = Integer.parseInt(br.readLine());
        }
        return observed;
    }

    private int[] getObservationsLocal(BufferedReader br, int s, int snackTaste) {
        int[] observed = new int[s + 1];
        for (int i = attempts + 1; i <= s; i++) {
            int gopherTaste = getGopherTasteLocal();
            observed[i] = gopherTaste > snackTaste ? 0 : 1;
        }
        return observed;
    }

    public static void main(String[] args) throws IOException {
        new GoGophersSet1().run();
    }

    private int find1GopherValue(int start, int end, BufferedReader br) throws IOException {
        if (start == end) {
            return start;
        }

        int middle = (start + end) / 2;
        int gophCnt = 0;
        for (int i = 0; i < 2 * MAX_GOPHERS; i++) {
            gophCnt += checkIfGopherWillEatLocal(middle, br);
            //gophCnt += checkIfGopherWillEat(middle, br);
        }

        if (gophCnt == 0) {
            return find1GopherValue(middle + 1, end, br);
        } else {
            return find1GopherValue(start, middle, br);
        }
    }

    private int checkIfGopherWillEat(int taste, BufferedReader br) throws IOException {
        attempts++;
        System.out.println(taste);
        return Integer.parseInt(br.readLine());
    }

    private int checkIfGopherWillEatLocal(int taste, BufferedReader br) {
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
