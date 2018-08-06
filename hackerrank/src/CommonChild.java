import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CommonChild {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String a = br.readLine();
        String b = br.readLine();

        int aLen = a.length();
        int bLen = b.length();

        int[][] d = new int[bLen + 1][aLen + 1];

        for (int i = 1; i < bLen + 1; i++) {
            char bChar = b.charAt(i - 1);
            for (int j = 1; j < aLen + 1; j++) {
                char aChar = a.charAt(j - 1);
                d[i][j] = Math.max(d[i - 1][j], d[i][j - 1]);

                if (aChar == bChar) {
                    d[i][j] = d[i - 1][j - 1]+1;
                } else {
                    d[i][j] = Math.max(d[i - 1][j], d[i][j - 1]);
                }
            }
        }

        System.out.println(d[bLen][aLen]);
    }
}
