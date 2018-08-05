package cardsPermutations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CardsPermutation {
    private static int[] randomPermsGenerator(int n) {
        int[] perms = new int[n];
        Set<Integer> usedValues = new HashSet<>();

        for (int i = 0; i < n; i++) {
            if (Math.random() > 0.60) {
                int val = 1 + (int)(Math.random() * n);
                int j = 0;
                for (; usedValues.contains(val) && j < 3; j++) {
                    val = 1 + (int)(Math.random() * n);
                }
                if (!usedValues.contains(val)) {
                    perms[i] = val;
                    usedValues.add(val);
                }
            }
            System.out.print(perms[i] + " ");
        }
        System.out.println();
        return perms;
    }

    public static void main(String[] args) throws IOException {
        while(true) {
            /*BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int n = Integer.parseInt(br.readLine());
            int[] perm = new int[n];
        StringTokenizer permTkn = new StringTokenizer(br.readLine());

        for (int i = 0; i < n; i++) {
            perm[i] = Integer.parseInt(permTkn.nextToken());
        }*/

            int n = 20;
            int[] perm = randomPermsGenerator(n);

            int[] undefinedAmnt = new int[n];
            undefinedAmnt[n - 1] = 0;

            for (int i = n - 2; i >= 0; i--) {
                undefinedAmnt[i] = undefinedAmnt[i + 1] + (perm[i + 1] == 0 ? 1 : 0);
            }

            int totalUndef = undefinedAmnt[0] + (perm[0] == 0 ? 1 : 0);

            long[] bin = new long[n];
            bin[n - 1] = 1;
            long chisl = totalUndef;
            long znam = 1;

            for (int i = n - 2; i >= 0; i--) {
                if (undefinedAmnt[i] == undefinedAmnt[i + 1]) {
                    bin[i] = bin[i + 1];
                } else {
                    bin[i] = bin[i + 1] * chisl / znam;
                    chisl--;
                    znam++;
                }
            }

            long[] incr = new long[n];
            incr[n - 1] = 0;

            long currentIncr = perm[n - 1] == 0 ? 1 : 0;

            chisl = totalUndef - 1;
            znam = 1;

            for (int i = n - 2; i >= 0; i--) {
                if (perm[i] != 0) {
                    incr[i] = perm[i + 1] != 0 ? incr[i + 1] : currentIncr;
                } else {
                    if (0 == currentIncr) {
                        currentIncr = 1;
                    } else {
                        currentIncr *= chisl;
                        currentIncr /= znam;
                        chisl--;
                        znam++;
                    }
                }
            }

            long[] colSum = new long[n];
            int cell = n - 1;
            while (cell >= 0 && perm[cell] != 0) {
                cell--;
            }

            if (cell >= 0) {
                colSum[cell] = 1;
            }

            chisl = totalUndef - 1;
            znam = 1;

            for (int i = cell - 1; i >= 0; i--) {
                if (perm[i] == 0) {
                    colSum[i] = colSum[i + 1] * chisl / znam;
                    chisl--;
                    znam++;
                } else {
                    colSum[i] = colSum[i + 1];
                }
            }

            long[] rowSum = new long[n];
            cell = n - 1;
            while (cell >= 0 && perm[cell] != 0) {
                cell--;
            }
            if (cell >= 0) {
                rowSum[cell] = totalUndef;
            }

            chisl = totalUndef - 1;
            znam = 2;

            for (int i = cell - 1; i >= 0; i--) {
                if (perm[i] == 0) {
                    rowSum[i] = rowSum[i + 1] * chisl / znam;
                    chisl--;
                    znam++;
                } else {
                    rowSum[i] = rowSum[i + 1];
                }
            }

        /*
         * These calculations should be optimized
         */
            /*int[] lessAmntLeft = new int[n + 1];
            List<Integer> processedLeft = new ArrayList<>();//increasing order

            for (int i = n - 1; i >= 0; i--) {
                if (perm[i] != 0) {
                    if (processedLeft.size() == 0) {
                        processedLeft.add(perm[i]);
                    }  else {
                        int posToInsert = findFirstGreater(processedLeft, perm[i], 0, processedLeft.size() - 1);
                        lessAmntLeft[perm[i]] = posToInsert;
                        processedLeft.add(posToInsert, perm[i]);
                    }
                }
            }*/

            /*int[] lessAmntRight = new int[n + 1];
            List<Integer> processedRight = new ArrayList<>();//increasing order

            for (int i = 0; i < n; i++) {
                if (perm[i] != 0) {
                    if (processedRight.size() == 0) {
                        processedRight.add(perm[i]);
                    }  else {
                        int posToInsert = findFirstGreater(processedRight, perm[i], 0, processedRight.size() - 1);
                        lessAmntRight[perm[i]] = posToInsert;
                        processedRight.add(posToInsert, perm[i]);
                    }
                }
            }*/

            /******************************/

            int[] defVals = new int[n - totalUndef];
            int defValsSize = 0;

            for (int i = 0; i < n; i++) {
                if (perm[i] != 0) {
                    defVals[defValsSize] = perm[i];
                    defValsSize++;
                }
            }

            int[] lessAmntLeft = new int[n + 1];
            int[] prevSmallerPos = new int[n - totalUndef];
            prevSmallerPos[n - totalUndef - 1] = -1;
            lessAmntLeft[defVals[n - totalUndef - 1]] = 0;
            for (int i = n - totalUndef - 2; i >= 0; i--) {
                int smalPos = i + 1;
                while (smalPos != -1 && defVals[i] < defVals[smalPos]) {
                    smalPos = prevSmallerPos[smalPos];
                }
                prevSmallerPos[i] = smalPos;
                if (prevSmallerPos[i] == -1) {
                    lessAmntLeft[defVals[i]] = 0;
                } else {
                    lessAmntLeft[defVals[i]] = lessAmntLeft[defVals[prevSmallerPos[i]]] + 1;
                }
            }

            Arrays.sort(defVals);

            long[] greaterUndef = new long[n + 1];
            long[] smallerDefined = new long[n + 1];
            long totalSum = 0;

            for (int i = 0; i < defValsSize; i++) {
                int definedValue = defVals[i];
                int greaterCnt = n - definedValue - (defValsSize - i - 1);
                greaterUndef[definedValue] = greaterCnt;
                totalSum += greaterCnt;

                smallerDefined[definedValue] = i;
            }
            /******************************/
            long[] resultInpt = new long[n];

            for (int i = n - 1; i >= 0; i--) {
                if (perm[i] != 0) {
                    resultInpt[i] = (((incr[i] * (perm[i] - 1 - smallerDefined[perm[i]]))) +
                            (lessAmntLeft[perm[i]] * bin[i]));

                    //resultInpt[i] = incr[i] * (perm[i] - 1 - lessAmntLeft[perm[i]] - lessAmntRight[perm[i]]) + lessAmntLeft[perm[i]] * bin[i];

                    long originalVal = CardsPermutationTrivial.getValue(perm, n, i, perm[i]);

                    if (resultInpt[i] != originalVal) {
                        throw new RuntimeException();
                    }
                }
            }


            int undef = totalUndef;
            for (int i = 0; i < n; i++) {
                if (perm[i] == 0) {
                    resultInpt[i] = rowSum[i] * undef * (undef - 1) / 2;
                    resultInpt[i] += colSum[i] * totalSum;

                    long originalVal = CardsPermutationTrivial.getValue(perm, n, i);
                    if (resultInpt[i] != originalVal) {
                        throw new RuntimeException();
                    }
                    undef--;
                } else {
                    totalSum -= greaterUndef[perm[i]];
                }
            }

            int undefRight = undefinedAmnt[0];
            int undefLeft = 0;

            long rightFact = fact(undefRight);
            long leftFact = 1;
            resultInpt[0] *= rightFact;

            for (int i = 1; i < n; i++) {
                if (perm[i] == 0) {
                    rightFact /= undefRight;
                    undefRight--;
                }

                if (perm[i - 1] == 0) {
                    undefLeft++;
                    leftFact *= undefLeft;
                }

                resultInpt[i] *= (rightFact * leftFact);
            }

            long fact = 1;
            int cnt = 1;

            for (int i = n - 2; i >= 0; i--) {
                resultInpt[i] *= fact;
                cnt++;
                fact *= cnt;
            }

            long result = fact(totalUndef);
            for (int i = 0; i < n; i++) {
                result += resultInpt[i];
            }
            System.out.println(result);
        }
    }

    private static long fact(int n) {
        if (n == 0) {
            return 1;
        }

        return n * fact(n - 1);
    }

    //list is sorted in increasing order
    public static int findFirstGreater(List<Integer> list, int val, int start, int end) {
        if (val > list.get(list.size() - 1)) {
            return list.size();
        }

        if (val < list.get(0)) {
            return 0;
        }

        if (start == end) {
            return start;
        }

        int middle = (start + end) / 2;
        int middleElmnt = list.get(middle);
        if (val < middleElmnt) {
            return findFirstGreater(list, val, start, middle);
        } else {
            return findFirstGreater(list, val, middle + 1, end);
        }

    }
}
