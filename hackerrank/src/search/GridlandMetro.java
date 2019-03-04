package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 2/14/2019.
 */
public class GridlandMetro {
    private static class Train {
        private long r;
        private long c1;
        private long c2;

        public Train(final long r, final long c1, final long c2) {
            this.r = r;
            this.c1 = c1;
            this.c2 = c2;
        }

        public long getR() {
            return r;
        }

        public long getC1() {
            return c1;
        }

        public long getC2() {
            return c2;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer line1 = new StringTokenizer(br.readLine());
        long n = Long.parseLong(line1.nextToken());
        long m = Long.parseLong(line1.nextToken());
        long k = Long.parseLong(line1.nextToken());

        List<Train> trains = new ArrayList<>();

        for (long i = 0; i < k; i++) {
            StringTokenizer row = new StringTokenizer(br.readLine());
            long r = Long.parseLong(row.nextToken());
            long c1 = Long.parseLong(row.nextToken());
            long c2 = Long.parseLong(row.nextToken());
            trains.add(new Train(r, c1, c2));
        }

        trains.sort(Comparator.comparingLong(Train::getR));

        List<Train> singleRowTrains = new ArrayList<>();
        long processingRow = 0;
        long res = 0;
        for (Train t : trains) {
            if (singleRowTrains.isEmpty() || t.getR() == processingRow) {
                processingRow = t.r;
                if (singleRowTrains.isEmpty()) {
                    res += m * (processingRow - 1);
                }
                singleRowTrains.add(t);
            } else {
                res += countFreeCells(singleRowTrains, m);
                singleRowTrains = new ArrayList<>();
                singleRowTrains.add(t);
                res += m * (t.r - processingRow - 1);
                processingRow = t.r;
            }
        }

        res += countFreeCells(singleRowTrains, m) + m * (n - processingRow);

        System.out.println(res);
    }

    private static long countFreeCells(List<Train> singleRowTrains, long cols) {
        if (singleRowTrains.isEmpty()) {
            return 0;
        }
        singleRowTrains.sort(Comparator.comparingLong(Train::getC1));
        long res = 0;
        long lastOccupiedCol = 0;
        for (Train t : singleRowTrains) {
            if (t.c1 > lastOccupiedCol) {
                res += t.c1 - lastOccupiedCol - 1;
            }
            lastOccupiedCol = Math.max(lastOccupiedCol, t.c2);
        }

        res += cols - lastOccupiedCol;

        return res;
    }
}
