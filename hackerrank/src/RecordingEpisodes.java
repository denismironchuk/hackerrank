import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.StringTokenizer;

public class RecordingEpisodes {
    private static class Node {
        private int num;
        private Set<Node> outgoings = new HashSet<>();
        private Set<Node> ingoings = new HashSet<>();
        private int outTime;
        private int conComp;

        public Node(final int num) {
            this.num = num;
        }

        public void addOutgoing(Node neigh) {
            outgoings.add(neigh);
            neigh.addIngoing(this);
        }

        public void addIngoing(Node nd) {
            ingoings.add(nd);
        }

        public Set<Node> getOutgoings() {
            return outgoings;
        }

        public Set<Node> getIngoings() {
            return ingoings;
        }

        public int getOutTime() {
            return outTime;
        }

        public void setOutTime(final int outTime) {
            this.outTime = outTime;
        }

        public int getConComp() {
            return conComp;
        }

        public void setConComp(final int conComp) {
            this.conComp = conComp;
        }

        public int getNum() {
            return num;
        }

        @Override
        public String toString() {
            return String.valueOf(num);
        }
    }

    private static class Episode {
        private int num;
        private int[] airTime;
        private int[] repeatTime;

        private Node airNode;
        private Node repeatNode;

        private Node negAirNode;
        private Node negRepeatNode;

        public Episode(final int num, final int[] airTime, final int[] repeatTime) {
            this.num = num;
            this.airTime = airTime;
            this.repeatTime = repeatTime;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int Q = Integer.parseInt(br.readLine());

        for (int q = 0; q < Q; q++) {
            int n = Integer.parseInt(br.readLine());
            Episode[] episodes = new Episode[n];

            for (int i = 0; i < n; i++) {
                StringTokenizer episodeTkn = new StringTokenizer(br.readLine());
                int airStart = Integer.parseInt(episodeTkn.nextToken());
                int airEnd = Integer.parseInt(episodeTkn.nextToken());

                int repeatStart = Integer.parseInt(episodeTkn.nextToken());
                int repeatEnd = Integer.parseInt(episodeTkn.nextToken());

                episodes[i] = new Episode(i, new int[] {airStart, airEnd}, new int[] {repeatStart, repeatEnd});

                episodes[i].airNode = new Node(i * 4);
                episodes[i].repeatNode = new Node(i * 4 + 1);
                episodes[i].negAirNode = new Node(i * 4 + 2);
                episodes[i].negRepeatNode = new Node(i * 4 + 3);

                episodes[i].airNode.addOutgoing(episodes[i].negRepeatNode);
                episodes[i].repeatNode.addOutgoing(episodes[i].negAirNode);
                episodes[i].negAirNode.addOutgoing(episodes[i].repeatNode);
                episodes[i].negRepeatNode.addOutgoing(episodes[i].airNode);
            }

            int point1 = 0;
            int point2 = 0;

            int resStart = 1;
            int resEnd = 1;

            List<Node> nodes = new LinkedList<>();
            addEpisode(nodes, point1, point2, episodes);

            while (point2 < n) {
                if (point2 < n && isValid(nodes, point1, point2, episodes)) {
                    if (point2 - point1 > resEnd - resStart) {
                        resEnd = point2 + 1;
                        resStart = point1 + 1;
                    }

                    point2++;

                    addEpisode(nodes, point1, point2, episodes);
                } else {
                    if (point2 == n - 1) {
                        break;
                    }

                    removeEpisode(nodes, episodes[point1]);

                    point1++;
                }
            }

            System.out.println(resStart + " " + resEnd);
        }
    }

    private static boolean addEpisode(List<Node> nodes, int point1, int point2, Episode[] episodes) {
        if (point2 == episodes.length) {
            return false;
        }

        Episode ep2 = episodes[point2];

        nodes.add(ep2.airNode);
        nodes.add(ep2.negAirNode);
        nodes.add(ep2.repeatNode);
        nodes.add(ep2.negRepeatNode);

        for (int i = point1; i < point2; i++) {
            Episode ep1 = episodes[i];

            if (overlaps(ep1.airTime, ep2.airTime)) {
                ep1.airNode.addOutgoing(ep2.negAirNode);
                ep2.airNode.addOutgoing(ep1.negAirNode);
            }

            if (overlaps(ep1.airTime, ep2.repeatTime)) {
                ep1.airNode.addOutgoing(ep2.negRepeatNode);
                ep2.repeatNode.addOutgoing(ep1.negAirNode);
            }

            if (overlaps(ep1.repeatTime, ep2.airTime)) {
                ep1.repeatNode.addOutgoing(ep2.negAirNode);
                ep2.airNode.addOutgoing(ep1.negRepeatNode);
            }

            if (overlaps(ep1.repeatTime, ep2.repeatTime)) {
                ep1.repeatNode.addOutgoing(ep2.negRepeatNode);
                ep2.repeatNode.addOutgoing(ep1.negRepeatNode);
            }
        }

        return true;
    }

    private static boolean isValid(List<Node> nodes, int point1, int point2, Episode[] episodes){
        int[] processed = new int[episodes.length * 4];

        int lastOut = 0;

        List<Node> forSort = new ArrayList<>();

        for (Node nd : nodes) {
            if (processed[nd.getNum()] == 0) {
                lastOut = topoSort(nd, processed, lastOut);
            }
            forSort.add(nd);
        }

        forSort.sort(Comparator.comparingInt(Node::getOutTime).reversed());

        processed = new int[episodes.length * 4];
        Queue<Node> q = new LinkedList<>();
        int compNum = 0;

        for (Node nd : forSort) {
            if (processed[nd.getNum()] == 0) {
                compNum++;
                q.add(nd);

                while (!q.isEmpty()) {
                    Node currNode = q.poll();
                    processed[currNode.getNum()] = 1;
                    currNode.setConComp(compNum);

                    for (Node ingNode : currNode.getIngoings()) {
                        if (processed[ingNode.getNum()] == 0) {
                            q.add(ingNode);
                        }
                    }
                }
            }
        }

        boolean isValid = true;

        for (int i = point1; isValid && i <= point2; i++) {
            Episode ep = episodes[i];
            isValid = !(ep.airNode.getConComp() == ep.negAirNode.getConComp() || ep.repeatNode.getConComp() == ep.negRepeatNode.getConComp());
        }

        return isValid;
    }

    private static int topoSort(Node nd, int[] processed, int lastOut) {
        processed[nd.getNum()] = 1;

        for (Node next : nd.getOutgoings()) {
            if (processed[next.getNum()] == 0) {
                lastOut = topoSort(next, processed, lastOut);
            }
        }

        nd.setOutTime(lastOut + 1);
        return nd.getOutTime();
    }

    private static void removeEpisode(List<Node> nodes, Episode ep) {
        for (Node ing : ep.airNode.ingoings) {
            ing.outgoings.remove(ep.airNode);
        }

        for (Node out : ep.airNode.outgoings) {
            out.ingoings.remove(ep.airNode);
        }

        for (Node ing : ep.repeatNode.ingoings) {
            ing.outgoings.remove(ep.repeatNode);
        }

        for (Node out : ep.repeatNode.outgoings) {
            out.ingoings.remove(ep.repeatNode);
        }

        for (Node ing : ep.negAirNode.ingoings) {
            ing.outgoings.remove(ep.negAirNode);
        }

        for (Node out : ep.negAirNode.outgoings) {
            out.ingoings.remove(ep.negAirNode);
        }

        for (Node ing : ep.negRepeatNode.ingoings) {
            ing.outgoings.remove(ep.negRepeatNode);
        }

        for (Node ing : ep.negRepeatNode.ingoings) {
            ing.outgoings.remove(ep.negRepeatNode);
        }

        nodes.remove(0);
        nodes.remove(0);
        nodes.remove(0);
        nodes.remove(0);
    }

    private static boolean overlaps(int[] intr1, int[] intr2) {
        return intr1[1] >= intr2[0] && intr2[1] >= intr1[0];
    }
}
