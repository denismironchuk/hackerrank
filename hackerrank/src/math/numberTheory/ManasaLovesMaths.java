package math.numberTheory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ManasaLovesMaths {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            char[] digitsStr = br.readLine().toCharArray();
            int len = digitsStr.length;
            int[] digits = new int[len];

            for (int i = 0; i < len; i++) {
                digits[i] = digitsStr[i] - '0';
            }

            boolean isDiv = false;

            if (len == 1) {
                isDiv = digits[0] % 8 == 0;
            } else if (len == 2) {
                isDiv = (digits[0] + 10 * digits[1]) % 8 == 0 || (digits[1] + 10 * digits[0]) % 8 == 0;
            } else {
                for (int i = 0; !isDiv && i < len; i++) {
                    for (int j = 0; !isDiv && j < len; j++) {
                        if (i == j) {
                            continue;
                        }

                        for (int k = 0; !isDiv && k < len; k++) {
                            if (k == j || k == i) {
                                continue;
                            }

                            int n = 100 * digits[i] + 10 * digits[j] + digits[k];
                            isDiv = n % 8 == 0;
                        }
                    }
                }
            }

            System.out.println(isDiv ? "YES" : "NO");
        }
    }
}
