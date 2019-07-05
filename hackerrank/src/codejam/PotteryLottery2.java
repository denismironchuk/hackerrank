package codejam;

public class PotteryLottery2 {
    private static int min(int... a) {
        int min = Integer.MAX_VALUE;
        for(int i : a) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

    public static void main(String[] args) {
        int allRuns = 0;
        int winRuns = 0;

        int winVasesCnt = 5;
        while(true) {
            int[] vases = new int[20];
            int checkedVase = 0;

            for (int i = 0; i < winVasesCnt; i++) {
                vases[(int) (Math.random() * 20)]++;
                vases[i]++;
            }

            for (int i = winVasesCnt; i < 99 - (2 * winVasesCnt - 2); i++) {
                vases[(int) (Math.random() * 20)]++;
                vases[checkedVase + winVasesCnt]++;
                checkedVase++;
                checkedVase%=(20 - winVasesCnt);
            }

            int[] state = new int[winVasesCnt];

            for (int i = 0; i < winVasesCnt; i++) {
                vases[(int) (Math.random() * 20)]++;
                state[i] = vases[i];
            }

            for (int i = 0; i < winVasesCnt - 2; i++) {
                vases[(int) (Math.random() * 20)]++;
            }

            int min1 = min(state);
            boolean found = false;
            for (int i = 0; i < winVasesCnt; i++) {
                if (found) {
                    vases[i]++;
                } else {
                    if (state[i] == min1) {
                        found = true;
                    } else {
                        vases[i]++;
                    }
                }
            }

            //all steps finished!!!!!!!

            int min = 1000;
            int minCnt = 0;

            for (int i = 0; i < winVasesCnt; i++) {
                if (vases[i] < min) {
                    min = vases[i];
                    minCnt = 1;
                } else if (vases[i] == min) {
                    minCnt++;
                }
            }

            int lessCnt = 0;

            for (int i = winVasesCnt; i < 20; i++) {
                if (vases[i] <= min) {
                    lessCnt++;
                    break;
                }
            }

            if (lessCnt == 0 && minCnt == 1) {
                winRuns++;
            }

            allRuns++;
            System.out.printf("%2.2f%%\n", (double)winRuns * 100 / (double)allRuns);
        }
    }
}
