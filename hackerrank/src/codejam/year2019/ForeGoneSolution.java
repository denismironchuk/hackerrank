package codejam.year2019;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ForeGoneSolution {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());
        for (int t = 0; t < T; t++) {
            String n = br.readLine();
            int[] nArr = new int[n.length()];
            int[] aArr = new int[n.length()];
            int[] bArr = new int[n.length()];
            for (int i = 0; i < n.length(); i++) {
                nArr[i] = (int)(n.charAt(i) - '0');
                if (nArr[i] != 4) {
                    aArr[i] = nArr[i];
                } else {
                    aArr[i] = 2;
                    bArr[i] = 2;
                }
            }
            System.out.printf("Case %s: ", t);
            print(aArr);
            System.out.print(" ");
            print(bArr);
            System.out.println();
        }
    }

    private static void print(int[] n) {
        boolean startPrint = false;
        for (int i = 0; i < n.length; i++) {
            if (n[i] != 0 || startPrint) {
                System.out.print(n[i]);
                startPrint = true;
            }
        }
    }
}
