import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

public class TheValueOfFriendship2 {
    static class Node {
        private int num;
        private List<Node> friends = new ArrayList<>();

        public Node(final int num) {
            this.num = num;
        }

        public void addFriend(Node friend) {
            friends.add(friend);
        }

        public int getNum() {
            return num;
        }

        public List<Node> getFriends() {
            return friends;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0 ; q < Q; q++) {
            StringTokenizer line1Tkn = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(line1Tkn.nextToken());
            int m = Integer.parseInt(line1Tkn.nextToken());

            Node[] nodes = new Node[n];

            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < m; i++) {
                StringTokenizer friendTkn = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(friendTkn.nextToken()) - 1;
                int y = Integer.parseInt(friendTkn.nextToken()) - 1;

                nodes[x].addFriend(nodes[y]);
                nodes[y].addFriend(nodes[x]);
            }

            int[] processed = new int[n];
            int islnd = 0;
            long[] islndVertsCnt = new long[n];
            Arrays.fill(islndVertsCnt, Long.MAX_VALUE);

            long[] islndEdgeCnt = new long[n];

            for (int i = 0; i < n; i++) {
                if (processed[i] == 0) {
                    islndVertsCnt[islnd] = 0;
                    buildConComponents(nodes[i], processed, islnd, islndVertsCnt, islndEdgeCnt);
                    islndEdgeCnt[islnd] /= 2;
                    islnd++;
                }
            }

            long edgesToDelete = 1;
            long pairs = 0;
            long res = 0;

            for (int i = 0; i < islnd; i++) {
                edgesToDelete += islndEdgeCnt[i] - islndVertsCnt[i] + 1;
                pairs += islndVertsCnt[i] * (islndVertsCnt[i] - 1);
            }

            res += edgesToDelete * pairs;

            Arrays.sort(islndVertsCnt);
            for (int i = 0; i < islnd; i++) {
                pairs -= islndVertsCnt[i] * (islndVertsCnt[i] - 1);
                for (int j = 1; j < islndVertsCnt[i]; j++) {
                    res += pairs;
                    res += (islndVertsCnt[i] - j) * (islndVertsCnt[i] - j - 1);
                }
            }

            System.out.println(res);
        }
    }

    public static void buildConComponents(Node nd, int[] processed, int islnd, long[] islndVertsCnt, long[] islndEdgeCnt) {
        processed[nd.getNum()] = 1;
        islndVertsCnt[islnd]++;

        for (Node next : nd.getFriends()) {
            islndEdgeCnt[islnd]++;
            if (processed[next.getNum()] == 0) {
                buildConComponents(next, processed, islnd, islndVertsCnt, islndEdgeCnt);
            }
        }
    }
}
