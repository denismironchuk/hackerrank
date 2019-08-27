package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class MostDistant {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        List<Long> verticalPoints = new ArrayList<>();
        List<Long> horizontalPoints = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            long x = Long.parseLong(tkn.nextToken());
            long y = Long.parseLong(tkn.nextToken());

            if (x == 0) {
                verticalPoints.add(y);
            }

            if (y == 0) {
                horizontalPoints.add(x);
            }
        }

        verticalPoints.sort(Long::compare);
        horizontalPoints.sort(Long::compare);

        long left = horizontalPoints.get(0);
        long down = verticalPoints.get(0);
        long right = horizontalPoints.get(horizontalPoints.size() - 1);
        long up = verticalPoints.get(verticalPoints.size() - 1);

        double[] dists = new double[6];

        dists[0] = Math.sqrt(left * left + down * down);
        dists[1] = Math.sqrt(left * left + up * up);

        dists[2] = Math.sqrt(right * right + down * down);
        dists[3] = Math.sqrt(right * right + up * up);

        dists[4] = right - left;
        dists[5] = up - down;

        Arrays.sort(dists);

        System.out.printf("%.7f", dists[5]);
    }
}
