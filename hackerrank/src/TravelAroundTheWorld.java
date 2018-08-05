import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.StringTokenizer;

public class TravelAroundTheWorld {
    int n;
    long c;
    long[] a;
    long[] b;

    public static void main(String[] args) throws IOException {
        new TravelAroundTheWorld().run();
    }

    public void run() throws IOException {
        while (true) {
            //generateFromStream();
            generateRandom();

            int[] canReach0 = new int[n];
            long[] remainFuel = new long[n];

            int[] canReach = new int[n];
            long[] valueToReach = new long[n];

            Date d1 = new Date();
            countOptimal(canReach0, remainFuel);
            countOptimal2(canReach, valueToReach);

            int[] resOpt = new int[n];
            for (int i = 0; i < n; i++) {
                if (canReach0[i] == 1 && canReach[i] == 1 && remainFuel[i] >= valueToReach[i]) {
                    resOpt[i] = 1;
                }
            }

            Date d2 = new Date();

            System.out.println(d2.getTime() - d1.getTime() + "ms");

            int[] canReach0_ = new int[n];
            long[] remainFuel_ = new long[n];

            Date d3 = new Date();
            countTrivial(canReach0_, remainFuel_);
            int[] resTriv = countTrivialAll();
            Date d4 = new Date();

            System.out.println(d4.getTime() - d3.getTime() + "ms");

            int res = 0;
            for (int i = 0; i < n; i++) {
                if (canReach0_[i] != canReach0[i]) {
                    throw new RuntimeException("canReach0 excp");
                }

                if (canReach0_[i] == 1) {
                    if (remainFuel_[i] != remainFuel[i]) {
                        throw new RuntimeException("remainFuel excp");
                    }
                }

                if (resTriv[i] != resOpt[i]) {
                    throw new RuntimeException("resOpt excp");
                }

                res += resOpt[i];
            }

            System.out.println(res);
            System.out.println("------------------");
        }
    }

    private void generateRandom() {
        //n = 1 + (int)(20000 * Math.random());
        n = 100000;
        c = (long)(100 * Math.random());

        //zapravka
        a = new long[n];
        //rashod
        b = new long[n];

        for (int i = 0; i < n; i++) {
            a[i] = (long)(50 * Math.random());
            b[i] = (long)(15 * Math.random());
        }
    }

    private void generateFromStream() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine(), " ");
        n = Integer.parseInt(tkn1.nextToken());
        c = Long.parseLong(tkn1.nextToken());

        //zapravka
        StringTokenizer aTkn = new StringTokenizer(br.readLine(), " ");
        //rashod
        StringTokenizer bTkn = new StringTokenizer(br.readLine(), " ");

        //zapravka
        a = new long[n];
        //rashod
        b = new long[n];

        for (int i = 0; i < n; i++) {
            a[i] = Long.parseLong(aTkn.nextToken());
            b[i] = Long.parseLong(bTkn.nextToken());
        }
    }

    private void countTrivial(int[] canReach0, long[] remainFuel) {
        for (int i = n - 1; i >= 0; i--) {
            long prevFuel = 0;
            boolean wasNegativeFuel = false;
            for (int j = i; j < n; j++) {
                prevFuel = Math.min(Math.min(a[j], c) + prevFuel, c) - b[j];
                if (!wasNegativeFuel && prevFuel < 0) {
                    wasNegativeFuel = true;
                }
            }
            remainFuel[i] = prevFuel;
            canReach0[i] = wasNegativeFuel ? 0 : 1;
        }
    }

    private int[] countTrivialAll() {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            long prevFuel = 0;
            boolean wasNegativeFuel = false;
            for (int k = 0; k < n && !wasNegativeFuel; k++) {
                prevFuel = Math.min(Math.min(a[(i+k) % n], c) + prevFuel, c) - b[(i+k) % n];
                if (prevFuel < 0) {
                    wasNegativeFuel = true;
                }
            }
            res[i] = wasNegativeFuel ? 0 : 1;
        }

        return res;
    }

    private void countOptimal2(int[] canReach, long[] valueToReach) {
        canReach[0] = 1;
        long minPlaceInBak = c - Math.min(a[0], c);
        valueToReach[0] = 0;
        long remainFuel = 0;
        for (int i = 1; i<n; i++) {
            remainFuel = Math.min(c, Math.min(a[i - 1], c) + remainFuel) - b[i - 1];
            //long canFill = Math.min(a[i - 1], c) - b[i - 1];

            if (remainFuel >= 0) {
                valueToReach[i] = valueToReach[i - 1];
                canReach[i] = 1;
                minPlaceInBak = Math.min(minPlaceInBak, c - Math.min(Math.min(a[i], c) + remainFuel, c));
            } else {
                valueToReach[i] = valueToReach[i - 1] - remainFuel;
                //valueToReach[i] = -remainFuel;
                //if (valueToReach[i] <= minPlaceInBak) {
                if (-remainFuel <= minPlaceInBak) {
                    canReach[i] = 1;
                    minPlaceInBak = Math.min(minPlaceInBak + remainFuel, c - Math.min(a[i], c));
                    remainFuel = 0;
                } else {
                    break;
                }
            }
        }
    }

    private void countOptimal(int[] canReach0, long[] remainFuel) {
        long minPlaceInBak = c;
        long minimalFuelLevel = 0;

        long minPlaceInBakGlobal = c;

        for (int i = n - 1; i >= 0; i--) {
            long canFill = Math.min(a[i], c) - b[i];

            if (minimalFuelLevel >= 0) {
                if (canFill >= 0) {
                    canReach0[i] = 1;
                } else {
                    canReach0[i] = 0;
                    minPlaceInBak = c - Math.min(a[i], c);
                    minimalFuelLevel = canFill;
                }
            } else {
                if (canFill < 0) {
                    canReach0[i] = 0;
                    minimalFuelLevel += canFill;
                    minPlaceInBak = Math.min(minPlaceInBak - canFill, c - Math.min(a[i], c));
                } else {
                    long incr = Math.min(canFill, minPlaceInBak);
                    minimalFuelLevel += incr;
                    if (minimalFuelLevel >= 0) {
                        canReach0[i] = 1;
                    } else {
                        canReach0[i] = 0;
                        minPlaceInBak = Math.min(minPlaceInBak - incr, c - Math.min(a[i], c));
                    }
                }
            }

            if (i == n - 1) {
                minPlaceInBakGlobal = c - Math.min(a[i], c);
                remainFuel[i] = canFill;
            } else {
                if (minPlaceInBakGlobal == 0) {
                    remainFuel[i] = remainFuel[i + 1];
                } else {
                    long incr = Math.min(canFill, minPlaceInBakGlobal);
                    remainFuel[i] = remainFuel[i + 1] + incr;
                    minPlaceInBakGlobal = Math.min(minPlaceInBakGlobal - incr, c - Math.min(a[i], c));
                }
            }
        }
    }
}
