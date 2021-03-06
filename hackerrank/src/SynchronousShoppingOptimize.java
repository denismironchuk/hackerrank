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
        //time -> fishTypes -> set of shop numbers
        TreeMap<Long, Map<Integer, Set<Integer>>> q = new TreeMap<>();

        Set<Integer> initShopsSet = new HashSet<>();
        initShopsSet.add(0);

        TreeMap<Integer, Set<Integer>> initFishState = new TreeMap<>();
        initFishState.put(shops[0].fishTypes, initShopsSet);

        q.put(0L, initFishState);
        shops[0].knownTime.put(shops[0].fishTypes, 0L);

        long[][] processed = new long[fishStates][n];
        List<Pair> pairs = new ArrayList<>();

        while (true) {
            Map.Entry<Long, Map<Integer, Set<Integer>>> entry = q.firstEntry();
            if (null == entry) {
                break;
            }

            long minTime = entry.getKey();
            Map<Integer, Set<Integer>> fishTypesMap = entry.getValue();
            Map.Entry<Integer, Set<Integer>> fishTypesEntry = fishTypesMap.entrySet().iterator().next();
            int minFishState = fishTypesEntry.getKey();
            Set<Integer> minTimeShops = fishTypesEntry.getValue();
            int minShopNum = minTimeShops.iterator().next();

            if (minTimeShops.size() == 1) {
                fishTypesMap.remove(minFishState);
                if (fishTypesMap.size() == 0) {
                    q.pollFirstEntry();
                }
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
                        Map<Integer, Set<Integer>> fishStatesMap = q.get(knownTime);
                        Set<Integer> knownShops = fishStatesMap.get(newFishState);
                        knownShops.remove(nd.num);
                        if (knownShops.size() == 0) {
                            fishStatesMap.remove(newFishState);
                            if (fishStatesMap.isEmpty()){
                                q.remove(knownTime);
                            }
                        }
                    }

                    Map<Integer, Set<Integer>> fishStatesMap = q.get(newTime);
                    if (fishStatesMap == null) {
                        fishStatesMap = new HashMap<>();
                        q.put(newTime, fishStatesMap);
                    }

                    Set<Integer> newShops = fishStatesMap.get(newFishState);
                    if (null == newShops) {
                        newShops = new HashSet<>();
                        fishStatesMap.put(newFishState, newShops);
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
