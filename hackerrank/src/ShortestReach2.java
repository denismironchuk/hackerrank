import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * Created by Denis_Mironchuk on 9/24/2018.
 */
public class ShortestReach2 {
    static class Node {
        private int num;
        private Map<Integer, Integer> dists = new HashMap<>();

        public Node(final int num) {
            this.num = num;
        }

        public int getNum() {
            return num;
        }

        public void addDist(Node neigh, int dist) {
            Integer oldDist = dists.get(neigh.getNum());

            if (null == oldDist) {
                dists.put(neigh.getNum(), dist);
            } else {
                dists.put(neigh.getNum(), Math.min(dist, oldDist));
            }
        }

        public Map<Integer, Integer> getDists() {
            return dists;
        }
    }

    static class TreeNode implements Comparable<TreeNode>{
        private int num;
        private long dist;

        public TreeNode(final int num, final long dist) {
            this.num = num;
            this.dist = dist;
        }

        @Override
        public int compareTo(final TreeNode o) {
            int distsCompare = Long.compare(dist, o.dist);

            if (distsCompare == 0) {
                return Integer.compare(num, o.num);
            } else {
                return distsCompare;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0; t < T; t++) {
            StringTokenizer tkn1 = new StringTokenizer(br.readLine());
            int n = Integer.parseInt(tkn1.nextToken());
            int m = Integer.parseInt(tkn1.nextToken());

            Node[] nodes = new Node[n];

            for (int i = 0; i < n; i++) {
                nodes[i] = new Node(i);
            }

            for (int i = 0; i < m; i++) {
                StringTokenizer edgeTkn = new StringTokenizer(br.readLine());

                int n1 = Integer.parseInt(edgeTkn.nextToken()) - 1;
                int n2 = Integer.parseInt(edgeTkn.nextToken()) - 1;
                int dist = Integer.parseInt(edgeTkn.nextToken());

                nodes[n1].addDist(nodes[n2], dist);
                nodes[n2].addDist(nodes[n1], dist);
            }

            int start = Integer.parseInt(br.readLine()) - 1;

            long[] dists = new long[n];
            Arrays.fill(dists, -1);
            dists[start] = 0;

            int[] processed = new int[n];

            TreeSet<TreeNode> tree = new TreeSet<>();
            tree.add(new TreeNode(start, 0));

            while (!tree.isEmpty()) {
                TreeNode first = tree.pollFirst();
                processed[first.num] = 1;

                Node firstNd = nodes[first.num];

                for (Map.Entry<Integer, Integer> distsEntry : firstNd.getDists().entrySet()) {
                    Node nd = nodes[distsEntry.getKey()];
                    Integer dist = distsEntry.getValue();

                    if (processed[nd.getNum()] == 0) {
                        long oldDist = dists[nd.getNum()];
                        long newDist = first.dist + dist;

                        if (oldDist == -1 || newDist < oldDist) {
                            tree.remove(new TreeNode(nd.getNum(), oldDist));
                            tree.add(new TreeNode(nd.getNum(), newDist));
                            dists[nd.getNum()] = newDist;
                        }
                    }
                }
            }

            StringBuilder res = new StringBuilder();

            for (int i = 0; i < n; i++) {
                if (i != start) {
                    res.append(dists[i]).append(" ");
                }
            }

            System.out.println(res.toString());
        }
    }
}
