package kickstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class Bundling {
    private static final int ALPHABET_SIZE = 26;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());

            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn.nextToken());
                int k = Integer.parseInt(tkn.nextToken());
                List<String> strings = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    strings.add(br.readLine());
                }

                int res = processStrings(strings, 0, k);
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static int processStrings(List<String> strings, int pos, int k) {
        if (strings.size() == 0) {
            return 0;
        }

        int res = 0;

        List<String>[] sorted = new List[ALPHABET_SIZE];

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            sorted[i] = new ArrayList<>();
        }

        for (String string : strings) {
            if (string.length() > pos) {
                sorted[string.charAt(pos) - 'A'].add(string);
            }
        }

        for (int i = 0; i < ALPHABET_SIZE; i++) {
            res += sorted[i].size() / k;
            res += processStrings(sorted[i], pos + 1, k);
        }

        return res;
    }
}
