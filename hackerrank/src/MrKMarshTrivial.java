import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MrKMarshTrivial {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line1Str = br.readLine();
        StringTokenizer line1Tkn = new StringTokenizer(line1Str, " ");
        int lines = Integer.parseInt(line1Tkn.nextToken());
        int columns = Integer.parseInt(line1Tkn.nextToken());

        int[][] area = new int[lines][columns];
        for (int i = 0; i < lines; i++) {
            String line = br.readLine();
            for (int j = 0; j < line.length(); j++) {
                area[i][j] = line.charAt(j) == 46 ? 0 : 1;
            }
        }

        int[][] distToRightMarsh = new int[lines][columns];

        for (int i = 0; i < lines; i++) {
            for (int j = columns - 1; j > -1; j--) {
                if (area[i][j] == 1) {
                    distToRightMarsh[i][j] = 0;
                } else if (j == columns - 1) {
                    distToRightMarsh[i][j] = 1;
                } else {
                    distToRightMarsh[i][j] = distToRightMarsh[i][j + 1] + 1;
                }
            }
        }

        int[][] distToDown = new int[lines][columns];

        for (int i = lines - 1; i  > -1; i--) {
            for (int j = 0; j < columns; j++) {
                if (area[i][j] == 1) {
                    distToDown[i][j] = 0;
                } else if (i == lines - 1) {
                    distToDown[i][j] = 1;
                } else {
                    distToDown[i][j] = distToDown[i+1][j] + 1;
                }
            }
        }

        int maxPerimeter = 0;

        for (int i = 0; i < lines - 1; i++) {
            for (int j = 0; j < columns - 1 && area[i][j] == 0; j++) {

                for (int i1 = i + 1; i1 < lines && area[i1][j] == 0; i1++) {
                    for (int j1 = j + 1; j1 < columns && area[i1][j1] == 0 && area[i][j1] == 0; j1++) {
                        if (j + distToRightMarsh[i][j] > j1 && i + distToDown[i][j] > i1 && i + distToDown[i][j1] > i1 && j + distToRightMarsh[i1][j] > j1) {
                            int perim = (i1 - i + j1 - j) * 2;
                            if (perim > maxPerimeter) {
                                if (perim == 126) {
                                    System.out.println();
                                }
                                maxPerimeter = perim;
                            }
                        }
                    }
                }
            }
        }

        System.out.println(maxPerimeter);
    }
}
