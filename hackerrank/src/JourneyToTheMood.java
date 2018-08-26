import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class JourneyToTheMood {
    static class Node {
        private int num;
        private List<Node> neighbours = new ArrayList<>();

        public Node(int num) {
            this.num = num;
        }

        public void addNeighbour(Node nd) {
            neighbours.add(nd);
        }

        public int getNum() {
            return num;
        }

        public List<Node> getNeighbours() {
            return neighbours;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tkn1 = new StringTokenizer(br.readLine());
        int n = Integer.parseInt(tkn1.nextToken());
        int p = Integer.parseInt(tkn1.nextToken());

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < p; i++) {
            StringTokenizer tkn2 = new StringTokenizer(br.readLine());
            int nd1Num = Integer.parseInt(tkn2.nextToken()) ;
            int nd2Num = Integer.parseInt(tkn2.nextToken());

            nodes[nd1Num].addNeighbour(nodes[nd2Num]);
            nodes[nd2Num].addNeighbour(nodes[nd1Num]);
        }

        int[] countries = new int[n];

        for (int i = 0; i < n; i++) {
            countries[i] = -1;
        }

        int country = -1;
        for (int i = 0; i < n; i++) {
            if (countries[i] == -1) {
                country++;
                deepSearch(nodes[i], countries, country);
            }
        }

        int[] countryAustr = new int[country + 1];

        for (int i = 0; i < n; i++) {
            countryAustr[countries[i]]++;
        }

        long result = 0;

        for (int i = 0; i < country + 1; i++) {
            result += countryAustr[i] * (n - countryAustr[i]);
        }

        result /= 2;

        System.out.println(result);
    }

    private static void deepSearch(Node nd, int[] countries, int country) {
        countries[nd.getNum()] = country;

        for (Node neighbour : nd.getNeighbours()) {
            if (countries[neighbour.getNum()] == -1) {
                deepSearch(neighbour, countries, country);
            }
        }
    }
}
