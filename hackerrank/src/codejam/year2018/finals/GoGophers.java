package codejam.year2018.finals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GoGophers {

    public static int N = 2;
    public static int MAX = 1000;

    public static void main(String[] args) {
        List<Integer> positions = new ArrayList<>();

        Random rnd = new Random();
        for (int i = 0; i * 20 + 20 <= MAX; i++) {
            positions.add(rnd.nextInt(N));
        }

        int[] daysFound = new int[positions.size() * N];
        for(int i = 0; i < positions.size(); i++) {
            daysFound[i * N + positions.get(i)] = 1;
        }

        for (int n = 2; n <= 25; n++) {
            int oneCnt = 0;
            boolean fail = false;
            for (int i = 0; i < daysFound.length; i++) {
                oneCnt += daysFound[i];

                if ((i + 1) % n == 0) {
                    if (oneCnt != 1) {
                        fail = true;
                        break;
                    } else {
                        oneCnt = 0;
                    }
                }
            }
            if (!fail) {
                System.out.println(n + " SUCESS!!!");
            }
        }

        System.out.println();
    }
}
