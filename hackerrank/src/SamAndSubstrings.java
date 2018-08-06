import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SamAndSubstrings {
    public static final long MODULO = 1000000007;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String n = br.readLine();
        int len = n.length();

        long sum = n.charAt(0) - '0';
        long suffixSum = sum;

        for (int i = 1; i < len; i++) {
            int digit = n.charAt(i) - '0';
            suffixSum = (((suffixSum * 10) % MODULO) + ((i + 1) * digit) % MODULO) % MODULO;
            sum = (sum + suffixSum) % MODULO;
        }

        System.out.println(sum);
    }
}
