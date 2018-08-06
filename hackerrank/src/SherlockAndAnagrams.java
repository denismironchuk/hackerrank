import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SherlockAndAnagrams {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        StringBuilder answer = new StringBuilder();

        for (int t = 0; t < T; t++) {
            String s = br.readLine();

            List<String> substrings = new ArrayList<>();
            int len = s.length();

            for (int i = 0; i < len; i++) {
                for (int j = i; j < len; j++) {
                    char[] sorted = s.substring(i, j + 1).toCharArray();
                    Arrays.sort(sorted);
                    substrings.add(new String(sorted));
                }
            }
            substrings.sort(String::compareTo);

            int res = 0;
            int amnt = 1;

            for (int i = 1; i < substrings.size(); i++) {
                if (substrings.get(i).equals(substrings.get(i - 1))) {
                    amnt++;
                } else {
                    res += amnt * (amnt - 1) / 2;
                    amnt = 1;
                }
            }

            res += amnt * (amnt - 1) / 2;

            answer.append(res).append("\n");
        }

        System.out.println(answer.toString());
    }
}
