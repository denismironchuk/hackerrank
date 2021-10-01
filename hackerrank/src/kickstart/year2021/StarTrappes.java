package kickstart.year2021;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class StarTrappes {

    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                int n = Integer.parseInt(br.readLine());
                long[] xCords = new long[n];
                long[] yCords = new long[n];
                for (int i = 0; i < n; i++) {
                    StringTokenizer tkn = new StringTokenizer(br.readLine());
                    xCords[i] = Long.parseLong(tkn.nextToken());
                    yCords[i] = Long.parseLong(tkn.nextToken());
                }
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                long blueX = Long.parseLong(tkn.nextToken());
                long blueY = Long.parseLong(tkn.nextToken());

                double perimeter = Double.MAX_VALUE;
                boolean found = false;

                for (int i = 0; i < n - 2; i++) {
                    for (int j = i + 1; j < n - 1; j++) {
                        for (int k = j + 1; k < n; k++) {
                            if (isPointInsideTriangle(blueX, blueY, xCords[i], yCords[i],
                                    xCords[j], yCords[j], xCords[k], yCords[k])) {
                                double perimeterCandidate = getDist(xCords[i], yCords[i],  xCords[j], yCords[j]) +
                                        getDist(xCords[j], yCords[j],  xCords[k], yCords[k]) +
                                        getDist(xCords[k], yCords[k],  xCords[i], yCords[i]);

                                if (!found || perimeterCandidate < perimeter) {
                                    perimeter = perimeterCandidate;
                                    found = true;
                                }
                            }
                        }
                    }
                }

                for (int i = 0; i < n - 1; i++) {
                    for (int j = i + 1; j < n; j++) {
                        if (getDoubledOrientedArea(xCords[i], yCords[i], xCords[j], yCords[j], blueX, blueY) == 0) {
                            long v1_x = xCords[i] - blueX;
                            long v1_y = yCords[i] - blueY;

                            long v2_x = xCords[j] - blueX;
                            long v2_y = yCords[j] - blueY;

                            if (v1_x * v2_x < 0 || v1_y * v2_y < 0) {
                                double minLeftSideLen = Double.MAX_VALUE;
                                boolean leftSideFound = false;

                                double minRightSideLen = Double.MAX_VALUE;
                                boolean rightSideFound = false;

                                for (int k = 0; k < n; k++) {
                                    if (k == i || k == j) {
                                        continue;
                                    }

                                    long signedArea = getDoubledOrientedArea(xCords[i], yCords[i], xCords[j], yCords[j], xCords[k], yCords[k]);
                                    if (signedArea != 0) {
                                        double candidate = getDist(xCords[j], yCords[j], xCords[k], yCords[k]) + getDist(xCords[k], yCords[k], xCords[i], yCords[i]);
                                        if (signedArea > 0) {
                                            if (!leftSideFound || candidate < minLeftSideLen) {
                                                minLeftSideLen = candidate;
                                                leftSideFound = true;
                                            }
                                        } else  {
                                            if (!rightSideFound || candidate < minRightSideLen) {
                                                minRightSideLen = candidate;
                                                rightSideFound = true;
                                            }
                                        }
                                    }
                                }

                                if (leftSideFound && rightSideFound) {
                                    double candidate = minLeftSideLen + minRightSideLen;
                                    if (!found || candidate < perimeter) {
                                        perimeter = candidate;
                                        found = true;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!found) {
                    System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                } else {
                    System.out.printf("Case #%s: %.6f\n", t, perimeter);
                }
            }
        }
    }

    private static double getDist(long x1, long y1, long x2, long y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    private static long getDoubledOrientedArea(long x1, long y1, long x2, long y2, long x3, long y3) {
        return (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1);
    }

    private static boolean isPointInsideTriangle(long x, long y,
                                                 long x1, long y1, long x2, long y2, long x3, long y3) {

        long area1 = getDoubledOrientedArea(x1, y1, x2, y2, x, y);
        long area2 = getDoubledOrientedArea(x2, y2, x3, y3, x, y);
        long area3 = getDoubledOrientedArea(x3, y3, x1, y1, x, y);
        if (area1 == 0 || area2 == 0 || area3 == 0) {
            return false;
        }
        int sign1 = area1 > 0 ? 1 : -1;
        int sign2 = area2 > 0 ? 1 : -1;
        int sign3 = area3 > 0 ? 1 : -1;

        return sign1 == sign2 && sign2 == sign3;
    }
}
