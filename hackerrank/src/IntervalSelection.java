import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 6/1/2018.
 */
public class IntervalSelection {
    private class Interval {
        long start;
        long end;

        public Interval(final long start, final long end) {
            this.start = start;
            this.end = end;
        }
    }

    private void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int S = Integer.parseInt(br.readLine());

        for (int s = 0; s < S; s++) {
            int n = Integer.parseInt(br.readLine());
            List<Interval> intervals = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine(), " ");
                intervals.add(new Interval(Long.parseLong(tkn.nextToken()), Long.parseLong(tkn.nextToken())));
            }

            intervals.sort((o1, o2) -> {
                int cmpRes = Long.compare(o1.start, o2.start);
                return cmpRes == 0 ? Long.compare(o1.end, o2.end) : cmpRes;
            });

            List<Interval> result = new LinkedList<>();

            Iterator<Interval> itr = intervals.iterator();

            while (itr.hasNext()) {
                Interval intr = itr.next();
                int resLen = result.size();

                if (resLen < 2) {
                    result.add(intr);
                    continue;
                }

                int prevInd = -1;
                int beforePrevInd = -1;

                for (int i = resLen - 1; beforePrevInd == -1 && i >= 0; i--) {
                    Interval prevIntr = result.get(i);
                    if (intr.start <= prevIntr.end) {
                        if (prevInd == -1) {
                            prevInd = i;
                        } else {
                            beforePrevInd = i;
                        }
                    }
                }

                if (beforePrevInd != -1) {
                    Interval last = result.get(prevInd);
                    Interval beforeLast = result.get(beforePrevInd);

                    if (intr.end < last.end || intr.end < beforeLast.end) {
                        if (last.end >= beforeLast.end) {
                            result.remove(prevInd);
                        } else {
                            result.remove(beforePrevInd);
                        }

                        result.add(intr);
                    }
                } else {
                    result.add(intr);
                }
            }

            System.out.println(result.size());
        }
    }

    public static void main(String[] args) throws IOException {
        new IntervalSelection().run();
    }
}
