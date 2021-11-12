package codejam.year2018.round2;

public class GracefullChainsawJugglingSolution {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        int aMax = 10;
        int bMax = 10;
        int[][][] maxLen = new int[aMax + 1][bMax + 1][Math.max(aMax, bMax) + 2];

        for (int a = 0; a <= aMax; a++) {
            for (int b = 0; b <= bMax; b++) {
                //val = 0
                int len = 1;
                while (true) {
                    int risingSum = len + ((len * (len - 1)) / 2);
                    if (risingSum > b) {
                        break;
                    }
                    maxLen[a][b][0] = Math.max(maxLen[a][b - risingSum][1] + len, maxLen[a][b][0]);
                    len++;
                }
                //val > 0
                for (int val = 1; val <= a; val++) {
                    for (len = 1; val * len <= a; len++) {
                        int nonZeroLen = len - 1;
                        int risingSum = nonZeroLen + ((nonZeroLen) * (nonZeroLen - 1) / 2);
                        if (risingSum > b) {
                            break;
                        }
                        if (a == val * len) {
                            risingSum = b;
                        }

                        maxLen[a][b][val] = Math.max(maxLen[a - val * len][b - risingSum][val + 1] + len, maxLen[a][b][val]);
                    }
                }

                for (int i = maxLen[0][0].length - 2; i >= 0; i--) {
                    maxLen[a][b][i] = Math.max(maxLen[a][b][i], maxLen[a][b][i + 1]);
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(end - start + "ms");

        for (int i = 0; i <= aMax; i++) {
            for (int j = 0; j <= bMax; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int trivRes = SumSplit.countMaxLen(i, j);
                System.out.println(i + ", " + j);
                System.out.println(trivRes);
                System.out.println(maxLen[i][j][0]);
                System.out.println("=====================");
                if (trivRes != maxLen[i][j][0]) {
                    throw new RuntimeException("!!!!!!!!!!!!!FAIL!!!!!!!!!!!!!");
                }
            }
        }
    }
}
