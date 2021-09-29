package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TrashBins {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                String s = br.readLine();
                int[] bins = new int[n];
                for (int i = 0; i < n; i++) {
                    bins[i] = s.charAt(i) - '0';
                }
                int[] distsToLeft = new int[n];
                int[] distsToRight = new int[n];

                distsToLeft[0] = bins[0] == 1 ? 0 : Integer.MAX_VALUE / 2;
                distsToRight[n - 1] = bins[n - 1] == 1 ? 0 : Integer.MAX_VALUE / 2;

                for (int i = 1; i < n; i++) {
                    if (bins[i] == 1) {
                        distsToLeft[i] = 0;
                    } else {
                        distsToLeft[i] = distsToLeft[i - 1] + 1;
                    }

                    if (bins[n - i - 1] == 1) {
                        distsToRight[n - i - 1] = 0;
                    } else {
                        distsToRight[n - i - 1] = distsToRight[n - i] + 1;
                    }
                }

                long res = 0;

                for (int i = 0; i < n; i++) {
                    res += Math.min(distsToLeft[i], distsToRight[i]);
                }

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }
}
