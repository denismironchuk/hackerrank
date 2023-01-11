package kickstart.year2022;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TreeSet;

public class StoryOfSeasons {

    private static class Vegetable {

        private int index;
        private long quantity;
        private long timeToMature;
        private long valueToEarn;

        public Vegetable(int index, long quantity, long timeToMature, long valueToEarn) {
            this.index = index;
            this.quantity = quantity;
            this.timeToMature = timeToMature;
            this.valueToEarn = valueToEarn;
        }

        public int getIndex() {
            return index;
        }

        public long getQuantity() {
            return quantity;
        }

        public long getTimeToMature() {
            return timeToMature;
        }

        public long getValueToEarn() {
            return valueToEarn;
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T ; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                long d = Long.parseLong(tkn1.nextToken());
                int n = Integer.parseInt(tkn1.nextToken());
                long x = Long.parseLong(tkn1.nextToken());
                Comparator<Vegetable> timeToMatureComparator = Comparator
                        .comparingLong(Vegetable::getTimeToMature)
                        .thenComparingInt(Vegetable::getIndex);
                TreeSet<Vegetable> matureSorted = new TreeSet<>(timeToMatureComparator);
                for (int i = 0; i < n; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    long q = Long.parseLong(tkn2.nextToken());
                    long l = Long.parseLong(tkn2.nextToken());
                    long v = Long.parseLong(tkn2.nextToken());
                    matureSorted.add(new Vegetable(i, q, l, v));
                }
                Comparator<Vegetable> valueDescCompare = Comparator
                        .comparingLong(Vegetable::getValueToEarn).reversed()
                        .thenComparingInt(Vegetable::getIndex);
                TreeSet<Vegetable> vegsToPickUp = new TreeSet<>(valueDescCompare);
                Vegetable lastVeg = takeLatestVegetables(matureSorted, vegsToPickUp);
                long currDay = d - lastVeg.timeToMature;
                long res = 0;

                while (!matureSorted.isEmpty()) {
                    long prevDay = d - matureSorted.first().timeToMature;
                    res += pickUpVegetable(x, currDay, prevDay, vegsToPickUp);
                    takeLatestVegetables(matureSorted, vegsToPickUp);
                    currDay = prevDay;
                }

                res += pickUpVegetable(x, currDay, 0, vegsToPickUp);

                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static long pickUpVegetable(long x, long currDay, long prevDay,
                                        TreeSet<Vegetable> vegsToPickUp) {
        long res = 0;
        long seedsToPlant = x * (currDay - prevDay);
        while (seedsToPlant > 0 && !vegsToPickUp.isEmpty()) {
            Vegetable veg = vegsToPickUp.pollFirst();
            long vegSeedsToPlant = Math.min(veg.quantity, seedsToPlant);
            seedsToPlant -= vegSeedsToPlant;
            res += vegSeedsToPlant * veg.valueToEarn;
            veg.quantity -= vegSeedsToPlant;
            if (veg.quantity != 0) {
                vegsToPickUp.add(veg);
            }
        }
        return res;
    }

    private static Vegetable takeLatestVegetables(TreeSet<Vegetable> matureSorted,
                                             TreeSet<Vegetable> vegsToPickUp) {
        Vegetable lastVeg = matureSorted.pollFirst();
        vegsToPickUp.add(lastVeg);
        while (!matureSorted.isEmpty() && lastVeg.timeToMature == matureSorted.first().timeToMature) {
            lastVeg = matureSorted.pollFirst();
            vegsToPickUp.add(lastVeg);
        }
        return lastVeg;
    }
}
