import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class KingdomDivision {
    private static long MODULO = 1000000007;

    private class Node {
        int num;
        List<Node> neigbours = new ArrayList<>();

        public Node(final int num) {
            this.num = num;
        }
    }

    private class NodeData{
        long k = 1;
        long s = 1;

        long nk = 1;
        long ns = 1;
    }

    private NodeData getNodeData(Node v, int[] processed) {

        NodeData result = new NodeData();
        boolean isLeaf = true;

        for (Node neighbour : v.neigbours) {
            if (processed[neighbour.num] == 0) {
                isLeaf = false;
                processed[neighbour.num] = 1;
                NodeData nodeData = getNodeData(neighbour, processed);

                result.ns = (result.ns * nodeData.k) % MODULO;
                result.nk = (result.nk * nodeData.s) % MODULO;

                result.k = (result.k * ((nodeData.k + nodeData.s + nodeData.nk) % MODULO)) % MODULO;
                result.s = (result.s * ((nodeData.k + nodeData.s + nodeData.ns) % MODULO)) % MODULO;
            }
        }

        if (isLeaf) {
            result.k = 0;
            result.s = 0;
        } else {
            result.k = (result.k - result.nk + MODULO) % MODULO;
            result.s = (result.s - result.ns + MODULO) % MODULO;
        }

        return result;
    }

    private void run() throws IOException {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader br = new BufferedReader(new FileReader("D:/kingdomData.txt"));
        int n = Integer.parseInt(br.readLine());
        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < n - 1; i++) {
            String edgeStr = br.readLine();
            StringTokenizer tkn = new StringTokenizer(edgeStr, " ");
            int v1Num = Integer.parseInt(tkn.nextToken()) - 1;
            int v2Num = Integer.parseInt(tkn.nextToken()) - 1;

            Node v1 = nodes[v1Num];
            Node v2 = nodes[v2Num];

            v1.neigbours.add(v2);
            v2.neigbours.add(v1);
        }

        int[] processed = new int[n];
        processed[0] = 1;
        NodeData res = getNodeData(nodes[0], processed);
        System.out.println((res.k + res.s) % MODULO);
    }

    public static void main(String[] args) throws IOException {
        new KingdomDivision().run();
    }
}
