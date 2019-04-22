package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TaskSheduling {
    private static class Task {
        private int duration;
        private int deadline;

        public Task(int duration, int deadline) {
            this.duration = duration;
            this.deadline = deadline;
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(br.readLine());
        Task[] shed = new Task[t];
        int maxDeadline = -1;
        for (int i = 0; i < t; i++) {
            StringTokenizer tkn = new StringTokenizer(br.readLine());

            int deadline = Integer.parseInt(tkn.nextToken());
            int duration = Integer.parseInt(tkn.nextToken());

            Task task = new Task(duration, deadline);
            shed[i] = task;

            if (deadline > maxDeadline) {
                maxDeadline = deadline;
            }
        }

        int[] delays = new int[maxDeadline + 1];
        Arrays.fill(delays, Integer.MIN_VALUE);

        for (int i = 0; i < t; i++) {
            delays[shed[i].deadline] = -shed[i].deadline;
        }

        int[] tree = new int[4 * (maxDeadline + 1)];
        buildSegTree(delays, tree, 1, 0, maxDeadline);
        printTree(tree, 1, 0, maxDeadline);

        for (int i = 0; i < t; i++) {
            updateInterval(tree, 1, 0, maxDeadline, shed[i].deadline, maxDeadline, shed[i].duration);
            System.out.println(tree[1]);
            printTree(tree, 1, 0, maxDeadline);
        }
    }

    private static void printTree(int[] tree, int v, int left, int right) {
        if (left == right) {
            System.out.println("[" + left + " - " + right + "] = " + tree[v]);
        } else {
            int mid = (left + right) / 2;
            printTree(tree, 2 * v, left, mid);
            printTree(tree, 2 * v + 1, mid + 1, right);
            System.out.println("[" + left + " - " + right + "] = " + tree[v]);
        }
    }

    private static void buildSegTree(int[] delays, int[] tree, int v, int left, int right) {
        if (left == right) {
            tree[v] = delays[left];
            //System.out.println("[" + left + " - " + right + "] = " + tree[v]);
        } else {
            int mid = (left + right) / 2;
            buildSegTree(delays, tree, 2 * v, left, mid);
            buildSegTree(delays, tree, 2 * v + 1, mid + 1, right);
            tree[v] = Math.max(tree[2 * v], tree[2 * v + 1]);
            //System.out.println("[" + left + " - " + right + "] = " + tree[v]);
        }
    }

    private static void updateInterval(int[] tree, int v, int l, int r, int intL, int intR, int incr) {
        if (intL > intR) {
            return;
        }

        if (l == intL && r == intR) {
            tree[v] += incr;
        } else {
            int mid = (l + r) / 2;
            updateInterval(tree, v * 2,        l,       mid, intL,                    Math.min(mid, intR), incr);
            updateInterval(tree, v * 2 + 1, mid + 1, r,   Math.max(mid + 1, intL), intR,                incr);
            tree[v] = Math.max(tree[2 * v], tree[2 * v + 1]);
        }
    }

    private static int getMaxValue(int[] tree, int v, int l, int r, int intL, int intR) {
        if (intL > intR) {
            return Integer.MIN_VALUE;
        }

        if (l == intL && r == intR) {
            return tree[v];
        } else {
            int mid = (l + r) / 2;
            return Math.max(getMaxValue(tree, 2 * v, l, mid, intL, Math.min(mid, intR)),
                    getMaxValue(tree, 2 * v + 1, mid + 1, r, Math.max(mid + 1, intL), intR));
        }
    }
}