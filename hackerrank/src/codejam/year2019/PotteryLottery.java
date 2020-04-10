package codejam.year2019;

public class PotteryLottery {
    public static void main3(String[] args) {
        double allRuns = 0;
        double countRuns = 0;
        while (true) {
            int cnt = 0;
            for (int i = 0; i < 100; i++) {
                if ((int)(Math.random() * 20) == 0) {
                    cnt++;
                }
            }

            if (cnt > 5) {
                countRuns++;
            }
            allRuns++;

            System.out.printf("%2.2f%%\n", countRuns * 100 / allRuns);
        }
    }

    public static void main2(String[] args) {
        int allRuns = 0;
        int countRuns = 0;
        while (true) {
            int cnt = 0;
            int[] vases = new int[20];

            int incrVase = 0;

            for (int i = 0; i < 99; i++) {
                vases[(int)(Math.random() * 20)]++;
                vases[incrVase + 1]++;
                incrVase++;
                incrVase %= 19;
            }
            vases[0]++;

            int lessCnt = 0;
            int maxDiff = -1;
            int diffSum = 0;
            for (int i = 1; i < 20; i++) {
                if (vases[i] <= vases[0]) {
                    lessCnt++;
                    if (vases[0] - vases[i] > maxDiff) {
                        maxDiff = vases[0] - vases[i];
                    }
                    diffSum += 1 + vases[0] - vases[i];
                }
            }

            if (diffSum > 15) {
                countRuns++;
            }

            allRuns++;

            System.out.printf("%2.2f%%\n", (double)countRuns * 100 / (double)allRuns);
        }
    }

    public static void main(String[] args) {
        int allRuns = 0;
        int winRuns = 0;

        while (true) {
            int[] vases = new int[20];

            for (int i = 0; i < 100; i++) {
                vases[(int) (Math.random() * 20)]++;
            }

            int min = 1000;
            int minCnt = 0;

            for (int i = 0; i < 20; i++) {
                if (vases[i] < min) {
                    min = vases[i];
                    minCnt = 1;
                } else if (vases[i] == min) {
                    min = vases[i];
                    minCnt++;
                }
            }

            if (minCnt == 1) {
                winRuns++;
            }
            allRuns++;
            System.out.printf("%2.2f%%\n", (double)winRuns * 100 / (double)allRuns);
        }
    }

    public static void main4(String[] args) {
        int allRuns = 0;
        int winRuns = 0;

        while (true) {
            int[] vases = new int[20];
            int targetVaseNum = (int)(Math.random() * 20);
            int checkedVaseNum = 0;
            int step = 0;
            int targetVaseCnt = 0;
            int checkedVaseCnt = 0;

            int limit = 99;

            for (int i = 0; i < limit; i++) {
                vases[(int)(Math.random() * 20)]++;
                vases[checkedVaseNum]++;
                checkedVaseNum++;
                checkedVaseNum %= 20;

                if (checkedVaseNum == targetVaseNum) {
                    checkedVaseNum++;
                    checkedVaseNum %= 20;
                }
            }

            /*for (int i = limit; i < 99; i++) {
                vases[(int)(Math.random() * 20)]++;
                if (step == 0) {
                    targetVaseCnt = vases[0];
                    step = 1;
                } else if (step == 1) {
                    checkedVaseCnt = vases[checkedVaseNum + 1];
                    if (targetVaseCnt < checkedVaseCnt) {
                        checkedVaseNum++;
                        checkedVaseNum %= 19;
                        step = 0;
                    } else {
                        step = 2;
                    }
                } else if (step == 2) {
                    vases[checkedVaseNum + 1]++;
                    checkedVaseNum++;
                    checkedVaseNum %= 19;
                    step = 0;
                }
            }*/


            vases[targetVaseNum]++;

            int lessCnt = 0;
            for (int i = 0; i < 20; i++) {
                if (i != targetVaseNum && vases[i] <= vases[targetVaseNum]) {
                    lessCnt++;
                }
            }

            if (lessCnt == 0) {
                winRuns++;
            }
            allRuns++;
            System.out.printf("%2.2f%%\n", (double)winRuns * 100 / (double)allRuns);
        }
    }
}
