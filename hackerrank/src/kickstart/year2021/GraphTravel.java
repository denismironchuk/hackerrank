package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class GraphTravel {

    private static long[] combs;
    private static long[] combScores;
    private static List<Node>[] combRooms;
    private static Node[] rooms;

    private static class Node {

        private int num;
        private long l;
        private long r;
        private long a;

        private Set<Node> neighbours = new HashSet<>();

        public Node(int num, long l, long r, long a) {
            this.num = num;
            this.l = l;
            this.r = r;
            this.a = a;
        }

        public void addNeighbour(Node neighbour) {
            neighbours.add(neighbour);
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int m = Integer.parseInt(tkn1.nextToken());
                long k = Long.parseLong(tkn1.nextToken());

                rooms = new Node[n];
                for (int i = 0; i < n; i++) {
                    StringTokenizer tkn2 = new StringTokenizer(br.readLine());
                    long l = Long.parseLong(tkn2.nextToken());
                    long r = Long.parseLong(tkn2.nextToken());
                    long a = Long.parseLong(tkn2.nextToken());
                    rooms[i] = new Node(i, l, r, a);
                }

                for (int i = 0; i < m; i++) {
                    StringTokenizer tkn3 = new StringTokenizer(br.readLine());
                    int room1 = Integer.parseInt(tkn3.nextToken());
                    int room2 = Integer.parseInt(tkn3.nextToken());
                    rooms[room1].addNeighbour(rooms[room2]);
                    rooms[room2].addNeighbour(rooms[room1]);
                }

                int lim = fastPow(2, n);
                combs = new long[lim + 1];
                combScores = new long[lim + 1];
                combRooms = new List[lim + 1];

                for (int comb = 1; comb < lim; comb++) {
                    combRooms[comb] = new ArrayList<>();
                    int index = 0;
                    int comb_ = comb;
                    while (comb_ != 0) {
                        if (comb_ % 2 == 1) {
                            combRooms[comb].add(rooms[index]);
                            combScores[comb] += rooms[index].a;
                        }
                        index++;
                        comb_ /= 2;
                    }
                }

                long res = 0;
                Arrays.fill(combs, -1);
                for (int comb = 1; comb <= lim; comb++) {
                    if (combScores[comb] == k) {
                        res += getCombWays(comb);
                    }
                }
                System.out.printf("Case #%s: %s\n", t, res);
            }
        }
    }

    private static long getCombWays(int comb) {
        if (combs[comb] != -1) {
            return combs[comb];
        }

        long scoreInComb = combScores[comb];
        List<Node> roomsInComb = combRooms[comb];

        if (roomsInComb.size() == 1) {
            combs[comb] = 1;
        } else {
            combs[comb] = 0;
            for (Node room : roomsInComb) {
                boolean hasPath = false;
                long otherSum = scoreInComb - room.a;
                if (otherSum >= room.l && otherSum <= room.r) {
                    for (Node otherRoom : roomsInComb) {
                        if (otherRoom == room) {
                            continue;
                        }
                        if (otherRoom.neighbours.contains(room)) {
                            hasPath = true;
                            break;
                        }
                    }
                }

                if (hasPath) {
                    combs[comb] += getCombWays(comb ^ fastPow(2, room.num));
                }
            }
        }
        return combs[comb];
    }

    public static int fastPow(int n, int p) {
        if (p == 0) {
            return 1;
        }

        if (p % 2 == 0) {
            return fastPow(n * n, p / 2);
        } else {
            return n * fastPow(n, p - 1);
        }
    }
}
