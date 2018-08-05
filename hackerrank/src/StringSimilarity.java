import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StringSimilarity {
    public static int[] effective(String s) {
        int n = s.length();
        int[] z = new int[n];
        int l = 0;
        int r = 0;

        for (int i = 1; i < n; i++) {
            if (i >= l && i <= r) {
                z[i] = z[i - l];

                if (i + z[i] - 1 >= r) {
                    z[i] = r - i + 1;
                    for (; i+z[i] < n && s.charAt(z[i]) == s.charAt(i+z[i]); z[i]++);
                }
            } else {
                for (; i+z[i] < n && s.charAt(z[i]) == s.charAt(i+z[i]); z[i]++);
            }

            if (i + z[i] - 1 > r) {
                r = i + z[i] - 1;
                l = i;
            }
        }

        return z;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        StringBuilder result = new StringBuilder();

        for (int t = 0; t < T; t++) {
            String s = br.readLine();
            int[] z = effective(s);

            long sum = s.length();

            for (int i = 1; i < s.length(); i++) {
                sum += z[i];
            }

            result.append(sum).append("\n");
        }

        System.out.println(result.toString());
    }
}
