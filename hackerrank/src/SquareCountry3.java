
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class SquareCountry3 {
    public static void main(String[] args) {
        Date start = new Date();

        int rows = 20;
        int cols = 20;

        Set<Long> row1 = new TreeSet<>();
        int oddCnt = 0;
        long i = 1;
        for (; i < rows - 1; i++) {
            row1.add(i * i);
            if (i % 2 != 0) {
                oddCnt++;
            }
        }

        if (oddCnt % 2 == 0) {
            if (i % 2 == 0) {
                row1.add((i + 1) * (i + 1));
            } else {
                row1.add(i * i);
            }
        } else {
            if (i % 2 == 0) {
                row1.add(i * i);
            } else {
                row1.add((i + 1) * (i + 1));
            }
        }

        long sum = row1.stream().reduce((a, b) -> a+ b).get();

        long lastElmnt = Long.MAX_VALUE;
        for (int j = 1;  j * j <= sum; j++) {
            if (sum % j == 0) {
                long candidat = ((sum / j) - j) / 2;
                if (!row1.contains(candidat * candidat) && candidat < lastElmnt) {
                    lastElmnt = candidat;
                }
            }
        }

        row1.add(lastElmnt*lastElmnt);

        Set<Long> table = new TreeSet<>();
        table.addAll(row1);
/**********************************************/
        Set<Long> col1 = new TreeSet<>();
        col1.add(1l);

        i = 2;
        oddCnt = 1;

        while(col1.size() != cols - 2) {

            if (!isPresent(i, table, row1)) {
                col1.add(i * i);
                Iterator<Long> row1Itr = row1.iterator();
                while(row1Itr.hasNext()) {
                    table.add(row1Itr.next() * i * i);
                    table.add(i * i);
                }

                if (i % 2 != 0) {
                    oddCnt++;
                }
            }

            i++;
        }

        /*****************************/
        if (((oddCnt % 2) ^ (i % 2)) == 0) {
            i++;
        }

        long colSum = col1.stream().reduce((a, b) -> a + b).get();

        while (col1.size() != cols) {
            if (!isPresent(i, table, row1)) {
                Set<Long> tmp = new TreeSet<>();
                Iterator<Long> row1Itr = row1.iterator();
                while(row1Itr.hasNext()) {
                    tmp.add(row1Itr.next() * i * i);
                    tmp.add(i * i);
                }

                long newSum = colSum + (i * i);

                long lastEl = Long.MAX_VALUE;
                for (int j = 1; j * j <= newSum; j++) {
                    if (newSum % j == 0) {
                        long candidat = ((newSum / j) - j) / 2;
                        if (!table.contains(candidat * candidat) && !tmp.contains(candidat * candidat) && candidat < lastEl) {
                            lastEl = candidat;
                        }
                    }
                }

                if (lastEl != Long.MAX_VALUE) {
                    col1.add(i * i);
                    col1.add(lastEl * lastEl);
                }
            }

            i += 2;
        }

        Set<Long> result = new HashSet<>();

        Iterator<Long> rowItr = row1.iterator();
        while(rowItr.hasNext()) {
            Iterator<Long> colItr = col1.iterator();
            long rowEl = rowItr.next();
            long summma = 0;
            while(colItr.hasNext()) {
                long colEl = colItr.next();
                summma += rowEl * colEl;
                System.out.print(rowEl * colEl + " ");
                //result.add(rowEl * colItr.next());
            }

            System.out.println(Math.sqrt(summma));
        }

        Date end = new Date();
        System.out.println(end.getTime() - start.getTime());
    }

    private static boolean isPresent(long i, Set<Long> table, Set<Long> row1) {
        Iterator<Long> row1Itr = row1.iterator();
        boolean isPresent = false;
        while(row1Itr.hasNext() && !isPresent) {
            isPresent = table.contains(row1Itr.next() * i * i);
        }
        return isPresent;
    }
}
