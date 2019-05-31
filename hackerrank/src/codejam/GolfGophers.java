package codejam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class GolfGophers {
    private static final int[] DIVS = {17, 13, 11, 9, 7, 5, 4};
    private static final int nighs = 7;
    private static  final int mills = 18;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());

        int T = Integer.parseInt(tkn1.nextToken());
        int n = Integer.parseInt(tkn1.nextToken());
        int m = Integer.parseInt(tkn1.nextToken());

        for (int t = 0; t < T; t++) {
            Set<Integer> candidates = new HashSet<>();
            for (int i = 1; i <= m; i++) {
                candidates.add(i);
            }

            for (int i = 0; i < 7; i++) {
                StringBuilder request = new StringBuilder();
                for (int j = 0; j < mills; j++) {
                    request.append(DIVS[i]).append(" ");
                }
                System.out.println(request);
                StringTokenizer tkn = new StringTokenizer(br.readLine());

                int sum = 0;
                for (int j = 0; j < mills; j++) {
                    sum += Integer.parseInt(tkn.nextToken());
                }

                sum = sum % DIVS[i];

                Set<Integer> newCandidates = new HashSet<>();

                while (sum <= m) {
                    if (candidates.contains(sum)) {
                        newCandidates.add(sum);
                    }

                    sum += DIVS[i];
                }

                candidates = newCandidates;
            }

            System.out.println(candidates.iterator().next());

            int state = Integer.parseInt(br.readLine());

            if (state == -1) {
                return;
            }
        }
    }
}
