import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class XorAndSum2 {
    private static long CNT = 314159;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String a = br.readLine();
        String b = br.readLine();

        if (a.length() > b.length()) {
            StringBuilder zerosBuilder = new StringBuilder();
            for (int i = 0; i < a.length() - b.length(); i++) {
                zerosBuilder.append('0');
            }
            zerosBuilder.append(b);
            b = zerosBuilder.toString();
        } else if (a.length() < b.length()) {
            StringBuilder zerosBuilder = new StringBuilder();
            for (int i = 0; i < b.length() - a.length(); i++) {
                zerosBuilder.append('0');
            }
            zerosBuilder.append(a);
            a = zerosBuilder.toString();
        }

        int[] ones = new int[b.length()];
        int[] zeros = new int[b.length()];

        if (b.charAt(b.length() - 1) == '0') {
            zeros[b.length() - 1] = 1;
        } else {
            ones[b.length() - 1] = 1;
        }

        for (int i = b.length() - 2; i >= 0; i--) {
            zeros[i] = zeros[i + 1];
            ones[i] = ones[i + 1];

            if (b.charAt(i) == '0') {
                zeros[i]++;
            } else {
                ones[i]++;
            }
        }

        long cnt = CNT;
        for (int i = b.length() - 1; i >= 0; i--) {
            zeros[i] += cnt;
            cnt--;
        }

        List<Integer> res = new LinkedList<>();
        int move = 0;

        for (int i = b.length() - 1; i >= 0; i--) {
            if (a.charAt(i) == 0) {
                res.add(0, (ones[i] + move) % 2);
                move = (ones[i] + move) / 2;
            } else {
                res.add(0,  (zeros[i] + move) % 2);
                move = (zeros[i] + move) / 2;
            }
        }

        for (long i = CNT - b.length(); i > 0; i--) {

        }

        System.out.println();
    }
}
