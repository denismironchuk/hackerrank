package codejam.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class FinalExam {

    private static class Interval implements Comparable<Interval> {
        private long start;
        private long end;

        public Interval(long start, long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Interval o) {
            if (this.end < o.start) {
                return -1;
            } else if (this.start > o.end) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int n = Integer.parseInt(tkn1.nextToken());
                int m = Integer.parseInt(tkn1.nextToken());

                TreeSet<Interval> tree = new TreeSet<>();
                for (int i = 0; i < n; i++) {
                    StringTokenizer intervalTkn = new StringTokenizer(br.readLine());
                    long start = Long.parseLong(intervalTkn.nextToken());
                    long end = Long.parseLong(intervalTkn.nextToken());
                    tree.add(new Interval(start, end));
                }

                List<Long> result = new ArrayList<>();
                StringTokenizer students = new StringTokenizer(br.readLine());
                for (int i = 0; i < m; i++) {
                    long student = Long.parseLong(students.nextToken());
                    Interval studentInterval = new Interval(student, student);
                    Interval upperCandidate = tree.ceiling(studentInterval);
                    Interval lowerCandidate = tree.floor(studentInterval);
                    if (upperCandidate != null && lowerCandidate != null) {
                        if (upperCandidate.equals(lowerCandidate)) {
                            result.add(student);
                            tree.remove(studentInterval);

                            if (upperCandidate.start <= student - 1) {
                                tree.add(new Interval(upperCandidate.start, student - 1));
                            }
                            if (student + 1 <= upperCandidate.end) {
                                tree.add(new Interval(student + 1, upperCandidate.end));
                            }
                        } else {
                            if (student - lowerCandidate.end <= upperCandidate.start - student) {
                                result.add(lowerCandidate.end);
                                tree.remove(lowerCandidate);
                                if (lowerCandidate.start <= lowerCandidate.end - 1) {
                                    tree.add(new Interval(lowerCandidate.start, lowerCandidate.end - 1));
                                }
                            } else {
                                result.add(upperCandidate.start);
                                tree.remove(upperCandidate);
                                if (upperCandidate.start + 1 <= upperCandidate.end) {
                                    tree.add(new Interval(upperCandidate.start + 1, upperCandidate.end));
                                }
                            }
                        }
                    } else if (upperCandidate != null) {
                        if (upperCandidate.compareTo(studentInterval) == 0) {
                            result.add(student);
                            tree.remove(upperCandidate);

                            if (upperCandidate.start <= student - 1) {
                                tree.add(new Interval(upperCandidate.start, student - 1));
                            }
                            if (student + 1 <= upperCandidate.end) {
                                tree.add(new Interval(student + 1, upperCandidate.end));
                            }
                        } else {
                            result.add(upperCandidate.start);
                            tree.remove(upperCandidate);
                            if (upperCandidate.start + 1 <= upperCandidate.end) {
                                tree.add(new Interval(upperCandidate.start + 1, upperCandidate.end));
                            }
                        }
                    } else if (lowerCandidate != null) {
                        if (lowerCandidate.compareTo(studentInterval) == 0) {
                            result.add(student);
                            tree.remove(lowerCandidate);

                            if (lowerCandidate.start <= student - 1) {
                                tree.add(new Interval(lowerCandidate.start, student - 1));
                            }
                            if (student + 1 <= lowerCandidate.end) {
                                tree.add(new Interval(student + 1, lowerCandidate.end));
                            }
                        } else {
                            result.add(lowerCandidate.end);
                            tree.remove(lowerCandidate);
                            if (lowerCandidate.start <= lowerCandidate.end - 1) {
                                tree.add(new Interval(lowerCandidate.start, lowerCandidate.end - 1));
                            }
                        }
                    }
                }

                System.out.printf("Case #%s: %s\n", t, result.stream().map(String::valueOf).collect(Collectors.joining(" ")));
            }
        }
    }
}
