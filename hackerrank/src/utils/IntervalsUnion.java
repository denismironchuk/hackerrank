package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 2/28/2018.
 */
public class IntervalsUnion {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        List<Interval> intervals = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String intervalString = br.readLine();
            StringTokenizer tkn = new StringTokenizer(intervalString, " ");
            int start = Integer.parseInt(tkn.nextToken());
            int end = Integer.parseInt(tkn.nextToken());
            intervals.add(new Interval(start, end));
        }

        intervals.sort(Comparator.comparingInt(Interval::getStart));

        List<Interval> joinedIntervals = new ArrayList<>();
        Interval current = intervals.get(0);
        joinedIntervals.add(current);

        for (int i = 1; i < intervals.size(); i++) {
            Interval process = intervals.get(i);

            if (process.getStart() > current.getEnd()) {
                joinedIntervals.add(process);
                current = process;
            } else {
                current.setEnd(Math.max(process.getEnd(), current.getEnd()));
            }
        }

        for (Interval interval : joinedIntervals) {
            System.out.println(interval);
        }
    }
}
