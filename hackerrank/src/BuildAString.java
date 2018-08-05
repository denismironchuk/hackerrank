import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Влада on 08.03.2018.
 */
public class BuildAString {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            String line1 = br.readLine();
            StringTokenizer tkn = new StringTokenizer(line1, " ");
            int n = Integer.parseInt(tkn.nextToken());
            int a = Integer.parseInt(tkn.nextToken());
            int b = Integer.parseInt(tkn.nextToken());
            String s = br.readLine();
            List<String> strings = new ArrayList<>();
            strings.add(new String("ZZZ"));
            int sum = 0;
            int currentPos = 0;
            while (currentPos < n) {
                System.out.println(s.substring(0, currentPos));

                String suffix = s.substring(currentPos, n);
                int insertIndex = findPos(suffix, strings, 0, strings.size() - 1);
                strings.add(insertIndex, suffix);
                int commonLen = 0;
                for (int i = insertIndex-1; i > -1;i--) {
                    String neighbour = strings.get(i);
                    int len = 0;
                    for (int j = 0; j < suffix.length() && neighbour.charAt(j) == suffix.charAt(j); j++) {
                        len++;
                    }

                    if (len > neighbour.length() - suffix.length()) {
                        commonLen = neighbour.length() - suffix.length();
                    } else {
                        commonLen = Math.max(commonLen, len);
                        break;
                    }
                }

                for (int i = insertIndex + 1; i < strings.size() - 1; i++) {
                    String neighbour = strings.get(i);
                    int len = 0;
                    for (int j = 0; j < suffix.length() && neighbour.charAt(j) == suffix.charAt(j); j++) {
                        len++;
                    }

                    if (len > neighbour.length() - suffix.length()) {
                        commonLen = neighbour.length() - suffix.length();
                    } else {
                        commonLen = Math.max(commonLen, len);
                        break;
                    }
                }

                if (commonLen == 0) {
                    currentPos++;
                    sum += a;
                } else {
                    sum += Math.min(b, commonLen * a);
                    for (int i = 1; i < commonLen; i++) {
                        String suff = s.substring(currentPos + i, n);
                        int index = findPos(suff, strings, 0, strings.size() - 1);
                        strings.add(index, suff);
                    }
                    currentPos += commonLen;
                }
            }

            System.out.println(sum);
        }
    }

    private static int findPos(String s, List<String> strings, int start, int end) {
        if (end <= start) {
            return start;
        }

        int middle = (end + start) / 2;

        if (s.compareTo(strings.get(middle)) > 0) {
            return findPos(s, strings, middle + 1, end);
        } else {
            return findPos(s, strings, start, middle);
        }
    }
}
