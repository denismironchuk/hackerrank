import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class SynchronousShoppingSimple {
    static class Node {
        private int num;
        private int fishTypes = 0;
        private Map<Node, Integer> connectedNodes = new HashMap<>();

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
        long[][] q = new long[fishStates][n];

        for (int i = 0; i < fishStates; i++) {
            for (int j = 0; j < n; j++) {
                q[i][j] = Long.MAX_VALUE;
            }
        }

        q[shops[0].fishTypes][0] = 0;

        long[][] processed = new long[fishStates][n];
        List<Pair> pairs = new ArrayList<>();

        while (true) {
            int minNode = -1;
            int minFishState = -1;

            for (int i = 0; i < fishStates; i++) {
                for (int j = 0; j < n; j++) {
                    if (processed[i][j] == 0 && (minNode == -1 || q[i][j] < q[minFishState][minNode])) {
                        minFishState = i;
                        minNode = j;
                    }
                }
            }

            if (minFishState == -1 && minNode == -1 || (q[minFishState][minNode] == Long.MAX_VALUE)) {
                break;
            }

            processed[minFishState][minNode] = 1;

            if (minNode == n - 1) {
                pairs.add(new Pair(minFishState, q[minFishState][minNode]));
            }

            for (Map.Entry<Node, Integer> ndEntry : shops[minNode].getConnectedNodes().entrySet()) {
                Node nd = ndEntry.getKey();
                int time = ndEntry.getValue();

                int newFishState = minFishState | nd.fishTypes;

                if (processed[newFishState][nd.num] == 1) {
                    continue;
                }

                long newTime = q[minFishState][minNode] + time;

                if (newTime < q[newFishState][nd.num]) {
                    q[newFishState][nd.num] = newTime;
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
