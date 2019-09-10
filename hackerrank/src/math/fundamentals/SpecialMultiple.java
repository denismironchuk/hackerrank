package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SpecialMultiple {
    public static void generate(int n, List<Long> itrRes, List<Long> res) {
        if (n == 0) {
            return;
        }

        List<Long> temp = new ArrayList<>();
        for (Long l : itrRes) {
            temp.add(l * 10);
            temp.add(l * 10 + 9);
        }
        res.addAll(temp);
        generate(n - 1, temp, res);
    }

    public static void main(String[ ]args) throws IOException {
        List<Long> res = new ArrayList<>();
        res.add(9l);

        generate(12, Arrays.asList(9l), res);

        long[] divs = new long[501];

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            int i = Integer.parseInt(br.readLine());
            if (divs[i] != 0) {
                System.out.println(divs[i]);
            } else {
                for (Long div9 : res) {
                    if (div9 % i == 0) {
                        System.out.println(div9);
                        break;
                    }
                }
            }
        }
    }
}
