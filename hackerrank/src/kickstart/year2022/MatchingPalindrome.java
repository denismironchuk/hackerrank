package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MatchingPalindrome {

    private static int[] countOddPalindromes(String s) {
        int right = -1;
        int left = -1;
        int[] oddLen = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            if (i >= left && i <= right) {
                oddLen[i] = Math.min(oddLen[left + right - i], right - i + 1);
            }

            while (i + oddLen[i] < s.length() && i - oddLen[i] >= 0
                    && s.charAt(i + oddLen[i]) == s.charAt(i - oddLen[i])) {
                right = i + oddLen[i];
                left = i - oddLen[i];
                oddLen[i]++;
            }
        }
        return oddLen;
    }

    private static int[] countEvenPalindromes(String s) {
        int[] evenLen = new int[s.length()];
        int right = -1;
        int left = -1;
        for (int i = 0; i < s.length(); i++) {
            if (i >= left && i < right) {
                evenLen[i] = Math.min(evenLen[left + right - i - 1], right - i);
            }

            while (i + evenLen[i] < s.length() && i - evenLen[i] - 1 >= 0
                    && s.charAt(i + evenLen[i]) == s.charAt(i - evenLen[i] - 1)) {
                right = i + evenLen[i];
                left = i - evenLen[i] - 1;
                evenLen[i]++;
            }
        }

        return evenLen;
    }
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                String s = br.readLine();
                if (n == 1) {
                    System.out.printf("Case #%s: %s\n", t, s);
                    continue;
                }
                int[] oddLen = countOddPalindromes(s);
                int[] evenLen = countEvenPalindromes(s);
                int minLen = n;
                for (int i = 0; i < s.length(); i++) {
                    if (i - oddLen[i] + 1 == 0) {
                        int remainLen = n - 2 * oddLen[i] + 1;

                        int left = i + oddLen[i];
                        int right = n - 1;

                        if (left > right) {
                            continue;
                        }

                        if (remainLen % 2 == 0 && evenLen[1 + (left + right) / 2] >= remainLen / 2) {
                            minLen = Math.min(minLen, 2 * oddLen[i] - 1);
                        } else if (remainLen % 2 == 1 && 2 * oddLen[(left + right) / 2] - 1 >= remainLen) {
                            minLen = Math.min(minLen, 2 * oddLen[i] - 1);
                        }
                    } else if (i - evenLen[i] == 0) {
                        int remainLen = n - 2 * evenLen[i];

                        int left = i + evenLen[i];
                        int right = n - 1;

                        if (left > right) {
                            continue;
                        }

                        if (remainLen % 2 == 0 && evenLen[1 + (left + right) / 2] >= remainLen / 2) {
                            minLen = Math.min(minLen, 2 * evenLen[i]);
                        } else if (remainLen % 2 == 1 && 2 * oddLen[(left + right) / 2] - 1 >= remainLen) {
                            minLen = Math.min(minLen, 2 * evenLen[i]);
                        }
                    }
                }
                StringBuilder res = new StringBuilder();
                for (int i = minLen - 1; i >= 0; i--) {
                    res.append(s.charAt(i));
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
