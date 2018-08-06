import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SeparateNumbers {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            String s = br.readLine();

            boolean valid = false;
            int len = 1;

            for (; len <= s.length() / 2 && !valid; len++) {
                String aSTR = s.substring(0, len);
                long a = Long.parseLong(aSTR);

                StringBuilder testStrBuilder = new StringBuilder();
                testStrBuilder.append(a);
                while (testStrBuilder.length() < s.length()) {
                    a++;
                    testStrBuilder.append(a);
                }

                valid = s.equals(testStrBuilder.toString());

                if (valid) {
                    System.out.printf("YES %s\n", s.substring(0, len));
                }
            }

            if (!valid) {
                System.out.println("NO");
            }
        }

    }
}
