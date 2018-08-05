import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class XorAndSum {
    private static int CNT = 314159;
    public static long MOD = 1000000007;

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
        int perenos = 0;

        for (int i = b.length() - 1; i >= 0; i--) {
            if (a.charAt(i) == '0') {
                res.add(0, (ones[i] + perenos) % 2);
                perenos = (ones[i] + perenos) / 2;
            } else {
                res.add(0,  (zeros[i] + perenos) % 2);
                perenos = (zeros[i] + perenos) / 2;
            }
        }

        int[] onesToRight = new int[CNT];

        if (b.charAt(0) == '1') {
            onesToRight[0] = 1;
        }

        for (int i = 1; i < CNT; i++) {
            onesToRight[i] = onesToRight[i - 1];
            if (i < b.length() && b.charAt(i) == '1') {
                onesToRight[i]++;
            }
        }

        for (int i = CNT - 1; i >= 0; i--) {
            res.add(0, (onesToRight[i] + perenos) % 2);
            perenos = (onesToRight[i] + perenos) / 2;
        }

        while (perenos != 0) {
            res.add(0, perenos % 2);
            perenos = perenos / 2;
        }

        Iterator<Integer> itr = res.iterator();
        long ans = 0;
        while (itr.hasNext()){
            int bit = itr.next();
            ans = (ans * 2) % MOD;
            ans = (ans + bit) % MOD;
        }

        System.out.println(ans);
    }
}