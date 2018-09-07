package utils.graphs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * Created by Denis_Mironchuk on 9/7/2018.
 */
public class GraphCollapse {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Node> nodes = GraphGenerator.generateFromInputStream();

        StringTokenizer collapseTkn = new StringTokenizer(br.readLine());
        Set<Integer> nodesToCollapse = new HashSet<>();

        while (collapseTkn.hasMoreTokens()) {
            nodesToCollapse.add(Integer.parseInt(collapseTkn.nextToken()));
        }

        Node fake1 = Node.collapseNodes(nodes, nodesToCollapse);

        collapseTkn = new StringTokenizer(br.readLine());
        nodesToCollapse = new HashSet<>();

        while (collapseTkn.hasMoreTokens()) {
            nodesToCollapse.add(Integer.parseInt(collapseTkn.nextToken()));
        }

        Node fake2 = Node.collapseNodes(nodes, nodesToCollapse);

        fake2.unwrap();
        fake1.unwrap();

        System.out.println();
    }
}
