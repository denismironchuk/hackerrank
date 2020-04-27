package codejam.year2020.round1A;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class SquareDance {
    static class Point {
        private int row;
        private int col;

        public Point(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }
    }

    static class Cell {
        private Point coordinates;
        private long skill;

        private Cell left;
        private Cell right;
        private Cell up;
        private Cell down;

        boolean isEliminated = false;

        public Cell(Point coordinates, long skill) {
            this.coordinates = coordinates;
            this.skill = skill;
        }

        public long removeIfNeeded(Set<Point> eliminatedPoints) {
            double neighboursAvg = 0;
            double neighboursCnt = 0;

            if (left != null) {
                neighboursAvg += left.skill;
                neighboursCnt++;
            }

            if (right != null) {
                neighboursAvg += right.skill;
                neighboursCnt++;
            }

            if (up != null) {
                neighboursAvg += up.skill;
                neighboursCnt++;
            }

            if (down != null) {
                neighboursAvg += down.skill;
                neighboursCnt++;
            }

            if (neighboursCnt == 0 || skill >= neighboursAvg / neighboursCnt) {
                return 0;
            }

            eliminatedPoints.add(this.coordinates);
            isEliminated = true;

            return this.skill;
        }

        public void modifyNeighbours(Set<Point> affectedPoints) {
            if (left != null) {
                left.right = this.right;
                if (!left.isEliminated) {
                    affectedPoints.add(left.coordinates);
                }
            }

            if (right != null) {
                right.left = this.left;
                if (!right.isEliminated) {
                    affectedPoints.add(right.coordinates);
                }
            }

            if (up != null) {
                up.down = this.down;
                if (!up.isEliminated) {
                    affectedPoints.add(up.coordinates);
                }
            }

            if (down != null) {
                down.up = this.up;
                if (!down.isEliminated) {
                    affectedPoints.add(down.coordinates);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn1 = new StringTokenizer(br.readLine());
                int r = Integer.parseInt(tkn1.nextToken());
                int c = Integer.parseInt(tkn1.nextToken());

                Cell[][] skills = new Cell[r][c];
                long skillsSum = 0;
                Set<Point> affectedPoints = new HashSet<>();
                for (int row = 0; row < r; row++) {
                    StringTokenizer rowTkn = new StringTokenizer(br.readLine());
                    for (int col = 0; col < c; col++) {
                        long skillLevel = Long.parseLong(rowTkn.nextToken());
                        skills[row][col] = new Cell(new Point(row, col), skillLevel);
                        if (row > 0) {
                            skills[row - 1][col].down = skills[row][col];
                            skills[row][col].up = skills[row - 1][col];
                        }
                        if (col > 0) {
                            skills[row][col - 1].right = skills[row][col];
                            skills[row][col].left = skills[row][col - 1];
                        }
                        skillsSum += skills[row][col].skill;
                        affectedPoints.add(skills[row][col].coordinates);
                    }
                }

                long resultSkillsSum = skillsSum;
                while (!affectedPoints.isEmpty()) {
                    Set<Point> eliminatedPoints = new HashSet<>();
                    long eliminatedSkills = 0;

                    for (Point affectedPoint : affectedPoints) {
                        eliminatedSkills += skills[affectedPoint.row][affectedPoint.col].removeIfNeeded(eliminatedPoints);
                    }

                    if (eliminatedSkills == 0) {
                        break;
                    }

                    skillsSum -= eliminatedSkills;
                    resultSkillsSum += skillsSum;

                    Set<Point> affectedPointsNew = new HashSet<>();

                    for (Point eliminatedPoint : eliminatedPoints) {
                        skills[eliminatedPoint.row][eliminatedPoint.col].modifyNeighbours(affectedPointsNew);
                    }
                    affectedPoints = affectedPointsNew;
                }

                System.out.printf("Case #%s: %s\n", t, resultSkillsSum);
            }
        }
    }
}
