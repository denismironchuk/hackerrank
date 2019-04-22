package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        int[] toUse = new int[maxDeadline + 1];
        for (int i = 0; i < t; i++) {
            delays[shed[i].deadline] = -shed[i].deadline;
            toUse[shed[i].deadline] = 1;
        }

        for (int i = 0; i < t; i++) {
            Task task = shed[i];
            for (int j = task.deadline; j <= maxDeadline; j++) {
                if (toUse[j] == 1) {
                    delays[j] += task.duration;
                }
            }

            int res = 0;

            for (int j = 0; j <= maxDeadline; j++) {
                if (toUse[j] == 1 && delays[j] > res) {
                    res = delays[j];
                }
            }

            System.out.println(res);
        }
    }
}