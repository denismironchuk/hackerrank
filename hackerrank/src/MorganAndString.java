import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MorganAndString {
    public static void main(String[] agrs) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        for (int t = 0; t < T; t++) {
            String a = br.readLine();
            String b = br.readLine();

            int ai = 0;
            int bi = 0;

            StringBuilder res = new StringBuilder();

            while (ai < a.length() && bi < b.length()) {
                int i = 0;
                for (; i + ai < a.length() && i + bi < b.length() && a.charAt(i + ai) == b.charAt(i + bi); i++);

                if (i + ai == a.length() || a.charAt(i + ai) < b.charAt(i + bi)) {
                    res.append(a.charAt(ai));
                    ai++;
                } else {
                    res.append(b.charAt(bi));
                    bi++;
                }
            }

            for (int i = ai; i < a.length(); i++) {
                res.append(a.charAt(i));
            }

            for (int i = bi; i < b.length(); i++) {
                res.append(b.charAt(i));
            }

            System.out.println(res.toString());
        }
    }
}
