package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class SherlockAndMovingTiles {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        double l = Double.parseDouble(tkn1.nextToken());
        double s1 = Double.parseDouble(tkn1.nextToken());
        double s2 = Double.parseDouble(tkn1.nextToken());

        int Q = Integer.parseInt(br.readLine());

        for (int i = 0; i < Q; i++) {
            double q = Double.parseDouble(br.readLine());
            double res = Math.sqrt(2) * (l - Math.sqrt(q)) / (Math.abs(s1 - s2));
            System.out.printf("%.4f\n", res);
        }
    }
}
