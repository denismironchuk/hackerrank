package codejam.year2021;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DigitBlocks {

    public static void main(String[] args) {
        int n = 20;
        int b = 15;

        long sum = 0;

        for (int t = 0; t < 50; t++) {
            List<Integer>[] piles = new ArrayList[n];

            for (int i = 0; i < n; i++) {
                piles[i] = new ArrayList<>();
            }

            int countOfNines = 2;
            int pileToFill = 0;
            int secondRowLimit = 5;
            LinkedList<Integer> filledPiles = new LinkedList<>();

            LinkedList<Integer> toPut8 = new LinkedList<>();
            LinkedList<Integer> toPut9 = new LinkedList<>();

            for (int i = 0; i < n * b; i++) {
                int digit = (int) (Math.random() * 10);

                if (pileToFill == n) {
                    if (digit == 8 && !toPut8.isEmpty()) {
                        int pile = toPut8.pollFirst();
                        piles[pile].add(digit);
                        toPut9.add(pile);
                    } else if (digit == 9 && !toPut9.isEmpty()) {
                        int pile = toPut9.pollFirst();
                        piles[pile].add(digit);
                    } else if (digit == 9 && !toPut8.isEmpty()) {
                        int pile = toPut8.pollFirst();
                        piles[pile].add(digit);
                        toPut9.add(pile);
                    } else if (!toPut8.isEmpty()) {
                        int pile = toPut8.pollFirst();
                        piles[pile].add(digit);
                        toPut9.add(pile);
                    } else {
                        int pile = toPut9.pollFirst();
                        piles[pile].add(digit);
                    }
                } else {
                    if (digit == 8 && !toPut8.isEmpty()) {
                        int pile = toPut8.pollFirst();
                        piles[pile].add(digit);
                        toPut9.add(pile);
                    } else if (digit == 9 && !toPut9.isEmpty()) {
                        int pile = toPut9.pollFirst();
                        piles[pile].add(digit);
                    } else if (digit == 9 && !toPut8.isEmpty()) {
                        int pile = toPut8.pollFirst();
                        piles[pile].add(digit);
                        toPut9.add(pile);
                    } else {
                        piles[pileToFill].add(digit);
                        if (piles[pileToFill].size() == b - 2) {
                            toPut8.add(pileToFill);
                            pileToFill++;
                        }
                    }
                }

                /*if (pileToFill == n || (digit == 9 && !filledPiles.isEmpty())) {
                    int pile = filledPiles.pollFirst();
                    piles[pile].add(digit);
                    if (piles[pile].size() < b) {
                        filledPiles.add(0,pile);
                    } else if (countOfNines == 2) {
                        if (pile == secondRowLimit) {
                            countOfNines = 1;
                            filledPiles.clear();
                            pileToFill = pile + 1;
                        }
                    }
                } else {
                    piles[pileToFill].add(digit);
                    if (piles[pileToFill].size() == b - countOfNines) {
                        filledPiles.add(pileToFill);
                        pileToFill++;
                    }
                }*/

                /*int pile = (int) (Math.random() * n);
                while (piles[pile].size() >= b) {
                    pile = (int) (Math.random() * n);
                }
                piles[pile].add(digit);*/
                /*List<List<Integer>> pilesLessThenDigit = new ArrayList<>();
                List<List<Integer>> pilesMoreThenDigit = new ArrayList<>();
                for (int j = 0; j < n; j++) {
                    if (piles[j].size() <= digit) {
                        pilesLessThenDigit.add(piles[j]);
                    } else {
                        pilesMoreThenDigit.add(piles[j]);
                    }
                }

                if (pilesLessThenDigit.size() > 0) {
                    pilesLessThenDigit.sort((p1, p2) -> Integer.compare(p2.size(), p1.size()));
                    pilesLessThenDigit.get(0).add(digit);
                } else {
                    pilesMoreThenDigit.sort((p1, p2) -> Integer.compare(p1.size(), p2.size()));
                    pilesMoreThenDigit.get(0).add(digit);
                }*/

            }

            long res = 0;
            for (int i = 0; i < n; i++) {
                System.out.println(piles[i]);
                //System.out.println(convertToNumber(piles[i]));
                res += convertToNumber(piles[i]);
            }
            //System.out.println(res);
            sum += res;
            System.out.println("==================");
        }
        System.out.println(sum);
    }

    private static long convertToNumber(List<Integer> digits) {
        long res = 0;
        for (int i = digits.size() - 1; i >= 0; i--) {
            res *= 10;
            res += digits.get(i);
        }
        return res;
    }
}
