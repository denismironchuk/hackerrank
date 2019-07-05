package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class PotteryLotteryFinal {
    private static int min(int... a) {
        int min = Integer.MAX_VALUE;
        for(int i : a) {
            if (i < min) {
                min = i;
            }
        }
        return min;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        int winVasesCnt = 5;
        for (int t = 0; t < T; t++) {
            for (int vase = 1; vase <= winVasesCnt; vase++) {
                readDay(br);
                System.out.printf("%s %s\n", vase, 100);
            }
            int checkedVase = 0;
            for (int i = winVasesCnt + 1; i < 100 - (2 * winVasesCnt - 2); i++) {
                readDay(br);

                System.out.printf("%s %s\n", checkedVase + winVasesCnt + 1, 100);
                checkedVase++;
                checkedVase%=(20 - winVasesCnt);
            }

            int[] state = new int[winVasesCnt];

            for (int vase = 0; vase < winVasesCnt; vase++) {
                readDay(br);
                System.out.printf("%s %s\n", vase + 1, 0);
                StringTokenizer resp = new StringTokenizer(br.readLine());
                int vaseCnt = Integer.parseInt(resp.nextToken());
                state[vase] = vaseCnt;
            }

            int min1 = min(state);
            boolean found = false;
            for (int vase = 0; vase < winVasesCnt; vase++) {
                if (found) {
                    readDay(br);
                    System.out.printf("%s %s\n", vase+1, 100);
                } else {
                    if (state[vase] == min1) {
                        found = true;
                    } else {
                        readDay(br);
                        System.out.printf("%s %s\n", vase+1, 100);
                    }
                }
            }
        }
    }

    private static void readDay(BufferedReader br) throws IOException {
        int inpt = Integer.parseInt(br.readLine());
        if (inpt == -1) {
            throw new RuntimeException();
        }
    }

}
