package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class DigitBlocksFinal {

    public static void main(String[] args) throws IOException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());
            int T = Integer.parseInt(tkn.nextToken());
            int n = Integer.parseInt(tkn.nextToken());
            int b = Integer.parseInt(tkn.nextToken());

            for (int t = 0; t < T; t++) {
                List<Integer>[] piles = new ArrayList[n];

                for (int i = 0; i < n; i++) {
                    piles[i] = new ArrayList<>();
                }

                int pileToFill = 0;

                LinkedList<Integer> toPut8 = new LinkedList<>();
                LinkedList<Integer> toPut9 = new LinkedList<>();

                for (int i = 0; i < n * b; i++) {
                    //int digit = (int) (Math.random() * 10);
                    int digit = Integer.parseInt(br.readLine());
                    int pile = 0;
                    if (pileToFill == n) {
                        if (digit == 8 && !toPut8.isEmpty()) {
                            pile = toPut8.pollFirst();
                            piles[pile].add(digit);
                            toPut9.add(pile);
                        } else if (digit == 9 && !toPut9.isEmpty()) {
                            pile = toPut9.pollFirst();
                            piles[pile].add(digit);
                        } else if (digit == 9 && !toPut8.isEmpty()) {
                            pile = toPut8.pollFirst();
                            piles[pile].add(digit);
                            toPut9.add(pile);
                        } else if (!toPut8.isEmpty()) {
                            pile = toPut8.pollFirst();
                            piles[pile].add(digit);
                            toPut9.add(pile);
                        } else {
                            pile = toPut9.pollFirst();
                            piles[pile].add(digit);
                        }
                    } else {
                        if (digit == 8 && !toPut8.isEmpty()) {
                            pile = toPut8.pollFirst();
                            piles[pile].add(digit);
                            toPut9.add(pile);
                        } else if (digit == 9 && !toPut9.isEmpty()) {
                            pile = toPut9.pollFirst();
                            piles[pile].add(digit);
                        } else if (digit == 9 && !toPut8.isEmpty()) {
                            pile = toPut8.pollFirst();
                            piles[pile].add(digit);
                            toPut9.add(pile);
                        } else {
                            pile = pileToFill;
                            piles[pileToFill].add(digit);
                            if (piles[pileToFill].size() == b - 2) {
                                toPut8.add(pileToFill);
                                pileToFill++;
                            }
                        }
                    }

                    System.out.println(pile + 1);
                }
            }
            int result = Integer.parseInt(br.readLine());
            if (result == -1) {
                throw new RuntimeException();
            }
        }
    }
}
