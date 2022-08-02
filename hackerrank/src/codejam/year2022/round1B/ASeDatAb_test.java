package codejam.year2022.round1B;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class ASeDatAb_test {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            Random rnd = new Random();
            for (int t = 1; t <= T; t++) {
                String hiddenValue = "00000000";
                for (int i = 0; i < rnd.nextInt(9); i++) {
                    int r = rnd.nextInt(9);
                    hiddenValue = xor(hiddenValue, rotate("00000001", r));
                }
                int bitCnt = bitsCount(hiddenValue);
                int cnt = 1;
                while (bitCnt != 0) {
                    String toPut;
                    if (bitCnt > 4) {
                        toPut = "11111111";
                        bitCnt = 8 - bitCnt;
                    } else {
                        toPut = "00000000";
                    };
                    for (int i = 0; i < bitCnt; i++) {
                        int r = rnd.nextInt(9);
                        toPut = xor(toPut, rotate("00000001", r));
                    }

                    int r = rnd.nextInt(9);
                    hiddenValue = xor(hiddenValue, rotate(toPut, r));

                    bitCnt = bitsCount(hiddenValue);
                    cnt++;
                }
                System.out.println(cnt);
            }
        }
    }

    private static int bitsCount(String s) {
        int res = 0;
        for (char c : s.toCharArray()) {
            if (c == '1') {
                res += 1;
            }
        }
        return res;
    }

    private static String rotate(String s, int r) {
        char[] sArr = s.toCharArray();
        char[] newArr = new char[sArr.length];
        for (int i = 0; i < sArr.length; i++) {
            newArr[i] = sArr[(i + r) % 8];
        }
        return new String(newArr);
    }

    private static String xor(String s1, String s2) {
        char[] s1Arr = s1.toCharArray();
        char[] s2Arr = s2.toCharArray();
        char[] newArr = new char[s1Arr.length];
        for (int i = 0; i < s1Arr.length; i++) {
            int val1 = s1Arr[i] == '1' ? 1 : 0;
            int val2 = s2Arr[i] == '1' ? 1 : 0;
            newArr[i] = (val1 ^ val2) == 1 ? '1' : '0';
        }
        return new String(newArr);
    }
}
