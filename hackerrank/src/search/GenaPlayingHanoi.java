package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class GenaPlayingHanoi {
    private static final int RODS = 4;
    private static final int MAX_DISKS = 10;
    private static int[] BASE = new int[] {1, 4, 16, 64, 256, 1024, 4096, 16384, 65536, 262144};
    private static int POSITIONS_AMNT = 1048576;

    private static class Position {
        private int[][] rods = new int[RODS][MAX_DISKS];
        private int[] rodDisksCnt = new int[RODS];
        private int disks;
        private int move = 0;

        public Position(int[] disksPlaces, int move) {
            this.move = move;
            disks = disksPlaces.length;

            for (int i = disks - 1; i > -1; i--) {
                int rodNum = disksPlaces[i];
                rods[rodNum][rodDisksCnt[rodNum]] = i;
                rodDisksCnt[rodNum]++;
            }
        }

        public int countCode() {
            int res = 0;
            for (int rod = 0; rod < RODS; rod++) {
                for (int diskPos = 0; diskPos < rodDisksCnt[rod]; diskPos++) {
                    res += BASE[rods[rod][diskPos]] * rod;
                }
            }
            return res;
        }

        public int[] generateDiskPlaces() {
            int[] res = new int[disks];

            for (int rod = 0; rod < RODS; rod++) {
                for (int diskPos = 0; diskPos < rodDisksCnt[rod]; diskPos++) {
                    res[rods[rod][diskPos]] = rod;
                }
            }

            return res;
        }

        public List<Position> generateNextPositions(int[] processed) {
            List<Position> nextPositions = new ArrayList<>();

            for (int rod = 0; rod < RODS; rod++) {
                if (rodDisksCnt[rod] > 0) {
                    rodDisksCnt[rod]--;
                    int top = rods[rod][rodDisksCnt[rod]];

                    for (int rod2 = 0; rod2 < RODS; rod2++) {
                        if (rod != rod2 && (rodDisksCnt[rod2] == 0 || rods[rod2][rodDisksCnt[rod2] - 1] > top)) {
                            rods[rod2][rodDisksCnt[rod2]] = top;
                            rodDisksCnt[rod2]++;
                            int positionCode = countCode();
                            if (processed[positionCode] == 0) {
                                nextPositions.add(new Position(generateDiskPlaces(), move + 1));
                                processed[positionCode] = 1;
                            }
                            rodDisksCnt[rod2]--;
                        }
                    }
                    rodDisksCnt[rod]++;
                }
            }

            return nextPositions;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine());
        int[] disks = new int[n];
        StringTokenizer disksTkn = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            disks[i] = Integer.parseInt(disksTkn.nextToken()) - 1;
        }

        int[] processedPostions = new int[POSITIONS_AMNT + 1];
        int targetPositionCode = new Position(disks, -1).countCode();

        int[] initialDisks = new int[n];
        for (int i = 0; i < n; i++) {
            initialDisks[i] = 0;
        }

        Position initialPos = new Position(initialDisks, 0);
        processedPostions[initialPos.countCode()] = 1;
        Queue<Position> q = new LinkedList<>();
        q.add(initialPos);

        while (!q.isEmpty()) {
            Position current = q.poll();
            List<Position> nexts = current.generateNextPositions(processedPostions);
            for (Position nextPos : nexts) {
                if (nextPos.countCode() == targetPositionCode) {
                    System.out.println(nextPos.move);
                    return;
                } else {
                    q.add(nextPos);
                }
            }
        }

        System.out.println();
    }
}
