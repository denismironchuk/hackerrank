package codejam.year2018.finals;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public class GoGophers2 {

    private static final int OBSERVE_CNT = 1000;
    private static final int[] GOPHERS = new int[] {2, 2, 3};
    private static final int GOPH_CNT = Arrays.stream(GOPHERS).sum();

    public static void main(String[] args) {
        int[] observations = prepareObservations(GOPH_CNT, GOPHERS[0]);
        //System.out.println(Arrays.stream(observations).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
        Map<Integer, Integer> possibleLen = getPossibleSizes(observations);
        System.out.println(possibleLen);

        int[] observations2 = prepareObservations(GOPH_CNT, GOPHERS[2]);
        Map<Integer, Integer> possibleLen2 = getPossibleSizes(observations2);
        System.out.println(possibleLen2);
    }

    private static int countGophers(int[] observations, int group, int groupLen) {
        int cnt = 0;
        for (int i = group * groupLen; i < (group + 1) * groupLen; i++) {
            cnt += observations[i];
        }
        return cnt;
    }

    private static int[] prepareObservations(int gophCnt, int gophsToObserve) {
        int[] observations = new int[OBSERVE_CNT];
        Random rnd = new Random();
        for (int group = 0; (group + 1) * gophCnt < observations.length; group++) {
            int gophersToSet = gophsToObserve;
            while (gophersToSet != 0) {
                int position = rnd.nextInt(gophCnt);
                if (observations[group * gophCnt + position] != 1) {
                    observations[group * gophCnt + position] = 1;
                    gophersToSet--;
                }
            }
        }
        return observations;
    }

    private static Map<Integer, Integer> getPossibleSizes(int[] observations) {
        Map<Integer, Integer> results = new LinkedHashMap<>();
        for (int len = 2; len < 26; len++) {
            int expectedCnt = countGophers(observations, 0, len);
            boolean allEqual = true;
            for (int group = 1; allEqual && (group + 1) * len < observations.length; group++) {
                int cnt = countGophers(observations, group, len);
                allEqual = expectedCnt == cnt;
            }
            if (allEqual) {
                results.put(len, expectedCnt);
            }
        }
        return results;
    }
}
