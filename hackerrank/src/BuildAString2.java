import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BuildAString2 {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        StringBuilder res = new StringBuilder();

        for (int t = 0; t < T; t++) {
            String line1 = br.readLine();
            StringTokenizer tkn = new StringTokenizer(line1, " ");
            int n = Integer.parseInt(tkn.nextToken());
            int a = Integer.parseInt(tkn.nextToken());
            int b = Integer.parseInt(tkn.nextToken());
            String s = br.readLine();
            s+="ZZZ";
            List<Integer> strings = new ArrayList<>();
            strings.add(0);
            strings.add(n);

            int[] costs = new int[n];
            for (int i = 0; i < n; i++) {
                costs[i] = Integer.MAX_VALUE;
            }
            costs[0] = a;
            for (int k = 1; k < n; k++) {
                costs[k] = Math.min(costs[k], costs[k - 1] + a);

                int suffLen = n - k;
                int insertIndex = findPos(k, s, strings, 0, strings.size() - 1, n);
                strings.add(insertIndex, k);
                int commonLen = 0;
                for (int i = insertIndex-1; i > -1;i--) {
                    int neighbourStart = strings.get(i);
                    int neighbourLen =  n - neighbourStart;
                    int len = 0;
                    for (int j = 0; j < suffLen && s.charAt(neighbourStart + j) == s.charAt(k + j); j++) {
                        len++;
                    }

                    if (len > neighbourLen - suffLen) {
                        commonLen =  Math.max(commonLen, neighbourLen - suffLen);
                    } else {
                        commonLen = Math.max(commonLen, len);
                        break;
                    }
                }

                for (int i = insertIndex + 1; i < strings.size() - 1; i++) {
                    int neighbourStart = strings.get(i);
                    int neighbourLen =  n - neighbourStart;
                    int len = 0;
                    for (int j = 0; j < suffLen && s.charAt(neighbourStart + j) == s.charAt(k + j); j++) {
                        len++;
                    }

                    if (len > neighbourLen - suffLen) {
                        commonLen =  Math.max(commonLen, neighbourLen - suffLen);
                    } else {
                        commonLen = Math.max(commonLen, len);
                        break;
                    }
                }

                if (commonLen != 0) {
                    costs[k] = Math.min(costs[k], costs[k - 1] + b);
                    for (int i = 1; k + i < n && i < commonLen; i++) {
                        costs[k + i] = Math.min(costs[k + i], costs[k - 1] + b);
                    }
                }
            }

            res.append(costs[n-1]).append("\n");
        }
        System.out.println(res.toString());
    }

    private static int compare(String source, int startPos1, int startPos2, int n) {
        int res = 0;
        int i = 0;
        int maxPos = Math.max(startPos1, startPos2);
        while (res == 0 && maxPos + i < n) {
            res = Character.compare(source.charAt(startPos1 + i), source.charAt(startPos2 + i));
            i++;
        }

        return res;
    }

    private static int findPos(int searchPos, String source, List<Integer> strings, int start, int end, int n) {
        if (end <= start) {
            return start;
        }

        int middle = (end + start) / 2;

        if (compare(source, searchPos, strings.get(middle), n) > 0) {
            return findPos(searchPos, source, strings, middle + 1, end, n);
        } else {
            return findPos(searchPos, source, strings, start, middle, n);
        }
    }
}
