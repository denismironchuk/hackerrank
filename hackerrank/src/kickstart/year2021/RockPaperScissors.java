package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class RockPaperScissors {
    private static int ROUNDS = 60;

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            int X = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                double w = Double.parseDouble(tkn.nextToken());
                double e = Double.parseDouble(tkn.nextToken());
                char[] moves = dynProg(w, e);
                StringBuilder movesStr = new StringBuilder();
                for (int i = 1; i <= ROUNDS; i++) {
                    movesStr.append(moves[i]);
                }
                System.out.printf("Case #%s: %s\n", t, movesStr);
            }
        }
    }

    private static char[] dynProg(double w, double e) {
        // index 1 - rock count, index 2 - paper count, index 3 - scissors count
        double[][][] maxExpect = new double[ROUNDS + 1][ROUNDS + 1][ROUNDS + 1];
        maxExpect[1][0][0] = (w + e) / 3;
        maxExpect[0][1][0] = (w + e) / 3;
        maxExpect[0][0][1] = (w + e) / 3;
        char[][][] reachMove = new char[ROUNDS + 1][ROUNDS + 1][ROUNDS + 1];
        reachMove[1][0][0] = 'R';
        reachMove[0][1][0] = 'P';
        reachMove[0][0][1] = 'S';

        double maxExpectScore = -1;
        int finalRockCnt = -1;
        int finalPaperCnt = -1;
        int finalScissorsCnt = -1;

        for (int round = 2; round <= ROUNDS; round++) {

            for (int rockCnt = 0; rockCnt < round; rockCnt++) {
                for (int paperCnt = 0; paperCnt < round - rockCnt; paperCnt++) {
                    int scissorsCnt = round - 1 - rockCnt - paperCnt;

                    double rockProb = (double) scissorsCnt / (double) (round - 1);
                    double paperProb = (double) rockCnt / (double) (round - 1);
                    double scissorsProb = (double) paperCnt / (double) (round - 1);

                    double rockCandidate = maxExpect[rockCnt][paperCnt][scissorsCnt] +  w * scissorsProb + rockProb * e;
                    if (rockCandidate > maxExpect[rockCnt + 1][paperCnt][scissorsCnt]) {
                        maxExpect[rockCnt + 1][paperCnt][scissorsCnt] = rockCandidate;
                        reachMove[rockCnt + 1][paperCnt][scissorsCnt] = 'R';
                    }

                    double paperCandidate = maxExpect[rockCnt][paperCnt][scissorsCnt] +  w * rockProb + paperProb * e;
                    if (paperCandidate > maxExpect[rockCnt][paperCnt + 1][scissorsCnt]) {
                        maxExpect[rockCnt][paperCnt + 1][scissorsCnt] = paperCandidate;
                        reachMove[rockCnt][paperCnt + 1][scissorsCnt] = 'P';
                    }

                    double scissorsCandidate = maxExpect[rockCnt][paperCnt][scissorsCnt] +  w * paperProb + scissorsProb * e;
                    if (scissorsCandidate > maxExpect[rockCnt][paperCnt][scissorsCnt + 1]) {
                        maxExpect[rockCnt][paperCnt][scissorsCnt + 1] = scissorsCandidate;
                        reachMove[rockCnt][paperCnt][scissorsCnt + 1] = 'S';
                    }

                    if (maxExpect[rockCnt + 1][paperCnt][scissorsCnt] > maxExpectScore) {
                        maxExpectScore = maxExpect[rockCnt + 1][paperCnt][scissorsCnt];

                        finalRockCnt = rockCnt + 1;
                        finalPaperCnt = paperCnt;
                        finalScissorsCnt = scissorsCnt;
                    }

                    if (maxExpect[rockCnt][paperCnt + 1][scissorsCnt] > maxExpectScore) {
                        maxExpectScore = maxExpect[rockCnt][paperCnt + 1][scissorsCnt];

                        finalRockCnt = rockCnt;
                        finalPaperCnt = paperCnt + 1;
                        finalScissorsCnt = scissorsCnt;
                    }

                    if (maxExpect[rockCnt][paperCnt][scissorsCnt + 1] > maxExpectScore) {
                        maxExpectScore = maxExpect[rockCnt][paperCnt][scissorsCnt + 1];

                        finalRockCnt = rockCnt;
                        finalPaperCnt = paperCnt;
                        finalScissorsCnt = scissorsCnt + 1;
                    }
                }
            }
        }

        char[] moves = new char[ROUNDS + 1];

        for (int round = ROUNDS; round > 0; round--) {
            moves[round] = reachMove[finalRockCnt][finalPaperCnt][finalScissorsCnt];
            if (moves[round] == 'R') {
                finalRockCnt--;
            }
            if (moves[round] == 'P') {
                finalPaperCnt--;
            }
            if (moves[round] == 'S') {
                finalScissorsCnt--;
            }
        }

        return moves;
    }
}
