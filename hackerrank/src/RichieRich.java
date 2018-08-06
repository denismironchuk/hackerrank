import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 2/21/2018.
 */
public class RichieRich {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line1 = br.readLine();
        StringTokenizer tkn = new StringTokenizer(line1, " ");
        int n = Integer.parseInt(tkn.nextToken());
        int k = Integer.parseInt(tkn.nextToken());

        String s = br.readLine();

        Set<Integer> diffPos = new HashSet<>();

        int len = s.length();
        for (int i = 0; i < len / 2; i++) {
            char first = s.charAt(i);
            char last = s.charAt(len - i - 1);
            if (first != last) {
                if (first > last) {
                    diffPos.add(i);
                } else {
                    diffPos.add(len - i - 1);
                }
            }
        }

        if (diffPos.size() > k) {
            System.out.println(-1);
            return;
        }

        char[] sArr = s.toCharArray();
        int used = 0;
        for (int pos : diffPos) {
            sArr[len - pos - 1] = sArr[pos];
            used++;
        }

        k = k - used;

        for (int i = 0; i < len / 2; i++) {
            if (sArr[i] != '9') {
                int cost = diffPos.contains(i) || diffPos.contains(len - i - 1) ? 1 : 2;
                if (k >= cost) {
                    sArr[i] = '9';
                    sArr[len - i - 1] = '9';

                    k -= cost;
                }
            }
        }

        if (k >= 1 && len % 2 != 0) {
            sArr[len / 2] = '9';
        }

        for (char c : sArr) {
            System.out.print(c);
        }
    }
}
