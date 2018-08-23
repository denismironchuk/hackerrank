import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Denis_Mironchuk on 8/23/2018.
 */
public class SynchronousShopping {
    static class Node {
        private int num;
        private int fishTypes = 0;
        private Map<Node, Integer> connectedNodes = new HashMap<>();
        //fish types -> time
        private Map<Integer, Long> processedDists = new HashMap<>();

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

        public int getFishTypes() {
            return fishTypes;
        }

        public void setFishTypes(final int fishTypes) {
            this.fishTypes = fishTypes;
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

    static class State {
        private Node dest;
        private int fishTypes;

        public State(final Node dest, final int fishTypes) {
            this.dest = dest;
            this.fishTypes = fishTypes;
        }

        public Node getDest() {
            return dest;
        }

        public void setDest(final Node dest) {
            this.dest = dest;
        }

        public int getFishTypes() {
            return fishTypes;
        }

        public void setFishTypes(final int fishTypes) {
            this.fishTypes = fishTypes;
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

            for (int j = 0; i < ti; j++) {
                int a = Integer.parseInt(tkn2.nextToken());
                supportedTypes = supportedTypes | fastPow(2, a);
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

        TreeMap<Long, Set<State>> q = new TreeMap<>(Long::compare);
        Set<State> st = new HashSet<>();
        st.add(new State(shops[0], shops[0].getFishTypes()));
        q.put(0L, st);

        shops[0].processedDists.put(shops[0].getFishTypes(), 0L);

        //node -> fish types -> time
        Map<Integer, Map<Integer, Long>> result = new HashMap<>();

        while (!q.isEmpty()) {
            Map.Entry<Long, Set<State>> first = q.firstEntry();
            Set<State> states = first.getValue();
            State state = states.iterator().next();
            states.remove(state);

            if (states.size() == 0) {
                q.pollFirstEntry();
            }

            Map<Integer, Long> res = result.get(state.getDest().num);
            if (res == null) {
                res = new HashMap<>();
                result.put(state.getDest().num, res);
            }
            res.put(state.fishTypes, first.getKey());

            for (Map.Entry<Node, Integer> entry : state.getDest().getConnectedNodes().entrySet()) {
                Node node = entry.getKey();
                Integer time = entry.getValue();

                for (Map.Entry<Integer, Long> fishTypesDistsEntry : node.processedDists.entrySet()) {
                    int fishType = fishTypesDistsEntry.getKey();
                    long timeToReach = fishTypesDistsEntry.getValue();

                    if (!result.containsKey(node.num) || !result.get(node.num).containsKey(fishType)) {

                    }
                }
            }
        }

    }
}
