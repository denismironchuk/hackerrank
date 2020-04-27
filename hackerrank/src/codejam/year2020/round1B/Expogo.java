package codejam.year2020.round1B;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Expogo {
    public static void main(String[] args) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            int T = Integer.parseInt(br.readLine());
            for (int t = 1; t <= T; t++) {
                StringTokenizer tkn = new StringTokenizer(br.readLine());
                int x = Integer.parseInt(tkn.nextToken());
                int y = Integer.parseInt(tkn.nextToken());
                if ((x + y) % 2 == 0) {
                    System.out.printf("Case #%s: IMPOSSIBLE\n", t);
                    continue;
                }

                StringBuilder moves = new StringBuilder();
                while (Math.abs(x) > 1 || Math.abs(y) > 1) {
                    int xCur = Math.abs(x) % 2;
                    int yCur = Math.abs(y) % 2;

                    int xNext = (Math.abs(x) >> 1) % 2;
                    int yNext = (Math.abs(y) >> 1) % 2;

                    if (xCur == 1 && yCur == 0) {
                        if (xNext != yNext) {
                            if (x > 0) {
                                moves.append("E");
                                x -= 1;
                            } else {
                                moves.append("W");
                                x += 1;
                            }
                        } else {
                            if (x > 0) {
                                moves.append("W");
                                x += 1;
                            } else {
                                moves.append("E");
                                x -= 1;
                            }
                        }
                    } else {
                        if (xNext != yNext) {
                            if (y > 0) {
                                moves.append("N");
                                y -= 1;
                            } else {
                                moves.append("S");
                                y += 1;
                            }
                        } else {
                            if (y > 0) {
                                moves.append("S");
                                y += 1;
                            } else {
                                moves.append("N");
                                y -= 1;
                            }
                        }
                    }

                    x >>= 1;
                    y >>= 1;
                }

                if (Math.abs(x) != Math.abs(y)) {
                    if (Math.abs(x) == 1 && Math.abs(y) == 0) {
                        if (x > 0) {
                            moves.append("E");
                            x -= 1;
                        } else {
                            moves.append("W");
                            x += 1;
                        }
                    } else {
                        if (y > 0) {
                            moves.append("N");
                            y -= 1;
                        } else {
                            moves.append("S");
                            y += 1;
                        }
                    }
                }

                System.out.printf("Case #%s: %s\n", t, moves);
            }
        }
    }
}
