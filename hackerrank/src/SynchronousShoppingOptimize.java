import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SynchronousShoppingOptimize {
    static class Node {
        private int num;
        private int fishTypes = 0;
        private Map<Node, Integer> connectedNodes = new HashMap<>();
        //fishType -> time
        private Map<Integer, Long> knownTime = new HashMap<>();

        public Node(final int num, final int fishTypes) {
            this.num = num;
            this.fishTypes = fishTypes;
        }

        public void addConnection(Node nd, Integer time) {
            connectedNodes.put(nd, time);
        }

        public Map<Node, Integer> getConnectedNodes() {
            return connectedNodes;
        }
    }

    private static int fastPow(int n, int p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int m = Integer.parseInt(tkn1.nextToken());
        int k = Integer.parseInt(tkn1.nextToken());

        Node[] shops = new Node[n];

        for (int i = 0; i < n; i++) {
            StringTokenizer tkn2 = new StringTokenizer(br.readLine());
            int supportedTypes = 0;
            int ti = Integer.parseInt(tkn2.nextToken());

            for (int j = 0; j < ti; j++) {
                int a = Integer.parseInt(tkn2.nextToken());
                supportedTypes = supportedTypes | fastPow(2, a - 1);
            }

            shops[i] = new Node(i, supportedTypes);
        }

        for (int i = 0; i < m; i++) {
            StringTokenizer tkn3 = new StringTokenizer(br.readLine());

            int nd1 = Integer.parseInt(tkn3.nextToken());
            int nd2 = Integer.parseInt(tkn3.nextToken());
            int time = Integer.parseInt(tkn3.nextToken());

            shops[nd1 - 1].addConnection(shops[nd2 - 1], time);
            shops[nd2 - 1].addConnection(shops[nd1 - 1], time);
        }

        int fishStates = fastPow(2, k);
        //time -> set of shop numbers
        TreeMap<Long, Set<Integer>>[] q = new TreeMap[fishStates];

        for (int i = 0; i < fishStates; i++) {
            q[i] = new TreeMap<>();
        }

        Set<Integer> initShopsSet = new HashSet<>();
        initShopsSet.add(0);
        q[shops[0].fishTypes].put(0L, initShopsSet);
        shops[0].knownTime.put(shops[0].fishTypes, 0L);

        long[][] processed = new long[fishStates][n];
        List<Pair> pairs = new ArrayList<>();

        while (true) {
            int minShopNum = -1;
            int minFishState = -1;
            long minTime = Long.MAX_VALUE;
            Set<Integer> minTimeShops = null;

            for (int i = 0; i < fishStates; i++) {
                Map.Entry<Long, Set<Integer>> entry = q[i].firstEntry();
                if (null == entry) {
                    continue;
                }

                Long time = entry.getKey();
                Set<Integer> availShops = entry.getValue();
                Integer shop = availShops.iterator().next();

                if ((minShopNum == -1 || time < minTime)) {
                    minTime = time;
                    minShopNum = shop;
                    minFishState = i;
                    minTimeShops = availShops;
                }
            }

            if (minFishState == -1 && minShopNum == -1 || (minTime == Long.MAX_VALUE)) {
                break;
            }

            if (minTimeShops.size() == 1) {
                q[minFishState].pollFirstEntry();
            } else {
                minTimeShops.remove(minShopNum);
            }

            processed[minFishState][minShopNum] = 1;

            if (minShopNum == n - 1) {
                pairs.add(new Pair(minFishState, minTime));
            }

            for (Map.Entry<Node, Integer> ndEntry : shops[minShopNum].getConnectedNodes().entrySet()) {
                Node nd = ndEntry.getKey();
                int time = ndEntry.getValue();

                int newFishState = minFishState | nd.fishTypes;

                if (processed[newFishState][nd.num] == 1) {
                    continue;
                }

                Long knownTime = nd.knownTime.get(newFishState);
                long newTime = minTime + time;

                if (null == knownTime || newTime <  knownTime) {
                    nd.knownTime.put(newFishState, newTime);

                    if (knownTime != null) {
                        Set<Integer> knownShops = q[newFishState].get(knownTime);
                        knownShops.remove(nd.num);
                        if (knownShops.size() == 0) {
                            q[newFishState].remove(knownTime);
                        }
                    }

                    Set<Integer> newShops = q[newFishState].get(newTime);
                    if (null == newShops) {
                        newShops = new HashSet<>();
                        q[newFishState].put(newTime, newShops);
                    }
                    newShops.add(nd.num);
                }
            }
        }


        long result = Long.MAX_VALUE;

        for (int i = 0; i < pairs.size(); i++) {
            Pair p1 = pairs.get(i);
            for (int j = i; j < pairs.size(); j++) {
                Pair p2 = pairs.get(j);
                if ((p1.fishTypes | p2.fishTypes) == fishStates - 1 && Math.max(p1.time, p2.time) < result) {
                    result = Math.max(p1.time, p2.time);
                }
            }
        }

        System.out.println(result);
    }

    static class Pair {
        int fishTypes;
        long time;

        public Pair(final int fishTypes, final long time) {
            this.fishTypes = fishTypes;
            this.time = time;
        }
    }
}
