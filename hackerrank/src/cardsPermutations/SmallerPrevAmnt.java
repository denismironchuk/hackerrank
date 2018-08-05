package cardsPermutations;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Влада on 09.07.2018.
 */
public class SmallerPrevAmnt {
    public static void main(String[] args) {
        while (true) {
            int n = 100000;
            int[] arr = new int[n];
            Set<Integer> usedVals = new HashSet<>();
            for (int i = 0; i < n; i++) {
                int val;

                do {
                    val = (int) (Math.random() * n * 1000);
                } while (usedVals.contains(val));

                arr[i] = val;
                usedVals.add(val);
                //System.out.printf("%d ", val);
            }

            int[] prevSmallerPos = new int[n];
            prevSmallerPos[n - 1] = -1;
            int itr = 0;
            for (int i = n - 2; i >= 0; i--) {
                int smalPos = i + 1;
                while (smalPos != -1 && arr[i] < arr[smalPos]) {
                    itr++;
                    smalPos = prevSmallerPos[smalPos];
                }
                prevSmallerPos[i] = smalPos;
            }

            int[] prevSmallerPosTrivial = new int[n];
            prevSmallerPosTrivial[n - 1] = -1;
            for (int i = n - 2; i >= 0; i--) {
                int smalPos = i + 1;
                while (smalPos < n && arr[i] < arr[smalPos]) {
                    smalPos++;
                }
                if (smalPos == n) {
                    prevSmallerPosTrivial[i] = -1;
                } else {
                    prevSmallerPosTrivial[i] = smalPos;
                }
            }

            for (int i = 0; i < n; i++) {
                if (prevSmallerPos[i] != prevSmallerPosTrivial[i]) {
                    throw new RuntimeException();
                }
            }

            System.out.println(itr);
        }
    }
}
