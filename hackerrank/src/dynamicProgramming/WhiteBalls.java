package dynamicProgramming;

import java.util.*;

public class WhiteBalls {
    public static long binToDec(int[] bin) {
        long mul = 1;
        long dec = 0;

        for (int i = bin.length - 1; i >= 0; i--) {
            dec += mul * bin[i];
            mul *= 2;
        }

        return dec;
    }

    /*private static long[] MULS = new long[] {
            (long)Math.pow(2,31),(long)Math.pow(2,30),(long)Math.pow(2,29),(long)Math.pow(2,28),(long)Math.pow(2,27),
            (long)Math.pow(2,26), (long)Math.pow(2,25),(long)Math.pow(2,24),(long)Math.pow(2,23),(long)Math.pow(2,22),
            (long)Math.pow(2,21), (long)Math.pow(2,20),(long)Math.pow(2,19),(long)Math.pow(2,18),(long)Math.pow(2,17),
            (long)Math.pow(2,16), (long)Math.pow(2,15),(long)Math.pow(2,14),8192,4096,2048,1024,512,256,128,64,32,16,8,4,2,1};*/

    private static long[] MULS = new long[] {
            (long)Math.pow(2,0), (long)Math.pow(2,1), (long)Math.pow(2,2), (long)Math.pow(2,3), (long)Math.pow(2,4),
            (long)Math.pow(2,5), (long)Math.pow(2,6), (long)Math.pow(2,7), (long)Math.pow(2,8), (long)Math.pow(2,9),
            (long)Math.pow(2,10), (long)Math.pow(2,11), (long)Math.pow(2,12), (long)Math.pow(2,13), (long)Math.pow(2,14),
            (long)Math.pow(2,15), (long)Math.pow(2,16), (long)Math.pow(2,17), (long)Math.pow(2,18), (long)Math.pow(2,19),
            (long)Math.pow(2,20), (long)Math.pow(2,21), (long)Math.pow(2,22), (long)Math.pow(2,23), (long)Math.pow(2,24),
            (long)Math.pow(2,25), (long)Math.pow(2,26), (long)Math.pow(2,27), (long)Math.pow(2,28), (long)Math.pow(2,29),
            (long)Math.pow(2,30), (long)Math.pow(2,31)};

    public static void buildNextStates(long state, int len, Set<Long> nexStates) {
        /*int mul = 1;
        for (int pos = 0; pos < len; pos++) {
            long nextState = (state / (mul * 2)) * (mul) + (state % mul);
            //System.out.println(Long.toBinaryString(nextState));
            nexStates.add(nextState);
            mul <<= 1;
        }*/
        for (int pos = 0; pos < len; pos++) {
            long nextState = (state / MULS[pos + 1]) * (MULS[pos]) + (state % MULS[pos]);
            nexStates.add(nextState);
        }
    }

    public static void main(String[] args) {
        Date start = new Date();
        //int[] initialCombArr = new int[] {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0};
        int[] initialCombArr = new int[] {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,0,0,0,0,0,0};
        int initialLen = initialCombArr.length;
        long initialComb = binToDec(initialCombArr);
        Map<Integer, Set<Long>> states = new HashMap<>();
        Set<Long> initialState = new HashSet<>();
        initialState.add(initialComb);
        states.put(initialLen, initialState);

        long totLen = 0;

        for (int len = initialLen; len > 1; len--) {
            //System.out.println(len);
            Set<Long> combs = states.get(len);
            Set<Long> newCombs = new HashSet<>();
            for (Long comb : combs) {
                buildNextStates(comb, len, newCombs);
            }
            states.put(len - 1, newCombs);
            //System.out.println("new size = " + newCombs.size());
            totLen+=newCombs.size();
        }

        Date end = new Date();
        System.out.println((end.getTime() - start.getTime()) + "ms");
        System.out.println(totLen);
    }

    /*public static void main(String[] args) {
        Date start = new Date();
        String initialComb = "101010101010101010101010101010";
        int initialLen = initialComb.length();
        Map<Integer, Set<String>> states = new HashMap<>();
        Set<String> initialState = new HashSet<>();
        initialState.add(initialComb);
        states.put(initialLen, initialState);

        long totLen = 0;

        for (int len = initialLen; len > 1; len--) {
            //System.out.println(len);
            Set<String> combs = states.get(len);
            Set<String> newCombs = new HashSet<>();
            for (String comb : combs) {
                for (int pos = 0; pos < comb.length(); pos++) {
                    newCombs.add(comb.substring(0, pos) + comb.substring(pos + 1));
                }
            }
            states.put(len - 1, newCombs);
            //System.out.println("new size = " + newCombs.size());
            totLen+=newCombs.size();
        }

        Date end = new Date();

        System.out.println(end.getTime() - start.getTime() + "ms");

        System.out.println(totLen);
    }*/
}
