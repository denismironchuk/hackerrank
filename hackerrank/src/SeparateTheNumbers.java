import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SeparateTheNumbers {
    private static int numLen(int n) {
        int len = 0;

        while (n != 0) {
            len++;
            n /= 10;
        }

        return len;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            String s = br.readLine();

            boolean valid = false;
            for (int len = 1; len <= s.length() / 2; len++) {
                String aSTR = s.substring(0, len);
                int a = Integer.parseInt(aSTR);
                int pos = len;
                valid = !aSTR.startsWith("0");
                while(valid && pos < s.length()) {
                    a++;
                    int numLen = numLen(a);
                    String nextStr = s.substring(pos, pos+numLen);
                    valid = !nextStr.startsWith("0") && a == Integer.parseInt(nextStr);
                    pos+=numLen;
                }
                if (valid) {
                    System.out.printf("YES %s\n", s.substring(0, len));
                    break;
                }
            }
            if (!valid) {
                System.out.println("NO");
            }
        }

    }
}
