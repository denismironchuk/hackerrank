package kickstart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class BusRoutes {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                long dest = Long.parseLong(tkn1.nextToken());

                long[] routes = new long[n];

                StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                for (int i = 0; i < n; i++) {
                    routes[i] = Long.parseLong(tkn2.nextToken());
                }

                long res = search(1, dest, dest, routes);
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static long search(long start, long end, long dest, long[] routes) {
        if (start == end) {
            return start;
        }

        long middle = ((start + end) / 2) + 1;

        if (isPossible(middle, dest, routes)) {
            return search(middle, end, dest, routes);
        } else {
            return search(start, middle - 1, dest, routes);
        }
    }

    private static boolean isPossible(long start, long dest, long[] routes) {
        long prevDay = start;
        for (long route : routes) {
            long day = prevDay / route;
            if (prevDay % route != 0) {
                day++;
            }
            prevDay = day * route;
        }
        return prevDay <= dest;
    }
}
