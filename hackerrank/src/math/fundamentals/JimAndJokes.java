package math.fundamentals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class JimAndJokes {

    private static class Event {

        private int base;
        private int val;

        public Event(int base, int val) {
            this.base = base;
            this.val = val;
        }

        private boolean isValid() {
            if (base < 2) {
                return false;
            }

            boolean isValid = true;

            int val2 = val;

            while (val2 != 0 && isValid) {
                isValid = val2 % 10 < base;
                val2 /= 10;
            }

            return isValid;
        }

        private int getDecimalValue() {
            int res = 0;
            int base2 = 1;
            int val2 = val;

            while (val2 != 0) {
                res += base2 * (val2 % 10);
                val2 /= 10;
                base2 *= base;
            }

            return res;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());

        List<Event> events = new ArrayList<>();

        for (int n = 0; n < N; n++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int base = Integer.parseInt(tkn.nextToken());
            int val = Integer.parseInt(tkn.nextToken());

            Event event = new Event(base, val);
            if (event.isValid()) {
                events.add(event);
            }
        }

        long res = events.stream().collect(Collectors.groupingBy(Event::getDecimalValue)).values().stream().map(List::size)
                .map(Long::valueOf).map(s -> (s * (s - 1)) / 2)
                .reduce((a, b) -> a + b).orElse(0l);

        System.out.println(res);
    }
}
