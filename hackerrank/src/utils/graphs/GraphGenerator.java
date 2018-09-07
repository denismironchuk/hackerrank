package utils.graphs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class GraphGenerator {
    public static List<Node> generateFromInputStream() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());

        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i));
        }

        int m = Integer.parseInt(br.readLine());

        for (int i = 0; i < m; i++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int v1 = Integer.parseInt(tkn.nextToken());
            int v2 = Integer.parseInt(tkn.nextToken());

            nodes.get(v1).addNeigh(nodes.get(v2));
            nodes.get(v2).addNeigh(nodes.get(v1));
        }

        return nodes;
    }

    public static List<Node> generateRandomBipartiteGraph(int n1, int n2, int m) throws IOException {
        int n = n1 + n2;

        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            nodes.add(new Node(i));
        }

        for (int i = 0; i < m; i++) {
            int v1 = (int)(Math.random() * n1);
            int v2 = (int)(Math.random() * n2) + n1;

            nodes.get(v1).addNeigh(nodes.get(v2));
            nodes.get(v2).addNeigh(nodes.get(v1));
        }

        return nodes;
    }
}
