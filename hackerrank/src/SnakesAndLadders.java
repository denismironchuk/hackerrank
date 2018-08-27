import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

public class SnakesAndLadders {

    public static final int CELLS = 100;
    public static final int DIE_NUMS = 6;

    static class Node {
        private int num;
        private List<Node> transitions = new ArrayList<>();
        private int labberEnd = -1;
        private int snakeEnd = -1;

        public Node(final int num) {
            this.num = num;
        }

        public void addTransition(Node nd) {
            transitions.add(nd);
        }

        public List<Node> getTransitions() {
            return transitions;
        }

        public int getLabberEnd() {
            return labberEnd;
        }

        public void setLabberEnd(final int labberEnd) {
            this.labberEnd = labberEnd;
        }

        public int getSnakeEnd() {
            return snakeEnd;
        }

        public void setSnakeEnd(final int snakeEnd) {
            this.snakeEnd = snakeEnd;
        }

        public int getNum() {
            return num;
        }
    }
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine());

        for (int t = 0 ; t < T; t++) {
            Node[] nodes = new Node[CELLS];
            for (int i = 0; i < CELLS; i++) {
                nodes[i] = new Node(i);
            }

            int ladders = Integer.parseInt(br.readLine());

            for (int i = 0; i < ladders; i++) {
                StringTokenizer ladderTkn = new StringTokenizer(br.readLine());
                int ladderStart = Integer.parseInt(ladderTkn.nextToken());
                int ladderEnd = Integer.parseInt(ladderTkn.nextToken());

                nodes[ladderStart - 1].setLabberEnd(ladderEnd - 1);
            }

            int snakes = Integer.parseInt(br.readLine());

            for (int i = 0; i < snakes; i++) {
                StringTokenizer snakesTkn = new StringTokenizer(br.readLine());
                int snakeStart = Integer.parseInt(snakesTkn.nextToken());
                int snakeEnd = Integer.parseInt(snakesTkn.nextToken());

                nodes[snakeStart - 1].setLabberEnd(snakeEnd - 1);
            }

            for (int i = 0; i < CELLS; i++) {
                Node nd = nodes[i];
                if (nd.getSnakeEnd() == -1 && nd.getLabberEnd() == -1) {
                    for (int j = 1; j <= DIE_NUMS && i + j < CELLS; j++) {
                        Node dest = nodes[i + j];
                        if (dest.getLabberEnd() != -1) {
                            nd.addTransition(nodes[dest.getLabberEnd()]);
                        } else if (dest.getSnakeEnd() != -1) {
                            nd.addTransition(nodes[dest.getSnakeEnd()]);
                        } else {
                            nd.addTransition(dest);
                        }
                    }
                }
            }

            Queue<Node> q = new LinkedList<>();
            int[] processed = new int[CELLS];
            int[] moves = new int[CELLS];

            Arrays.fill(moves, -1);;

            q.add(nodes[0]);
            processed[nodes[0].getNum()] = 1;
            moves[nodes[0].getNum()] = 0;

            while (!q.isEmpty()) {
                Node nd = q.poll();


                for (Node trans : nd.getTransitions()) {
                    if (processed[trans.getNum()] == 0) {
                        processed[trans.getNum()] = 1;
                        moves[trans.getNum()] = moves[nd.getNum()] + 1;
                        q.add(trans);
                    }
                }
            }

            System.out.println(moves[CELLS - 1]);
        }
    }
}
