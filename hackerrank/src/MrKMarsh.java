import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MrKMarsh {
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

        int[][][] da = new int[lines][columns][columns];

        int maxPerimeter = 0;

        for (int j = 0; j < columns - 1; j++){
            for (int k = j + 1; k < columns; k++) {
                if (area[0][j] == 1 || area[0][k] == 1 || j + distToRightMarsh[0][j] <= k) {
                    da[0][j][k] = -1;
                }
            }
        }

        for (int i = 1; i < lines; i++) {
            for (int j = 0; j < columns - 1; j++) {
                for (int k = j + 1; k < columns; k++) {
                    if (area[i][j] == 1 || area[i][k] == 1) {
                        da[i][j][k] = -1;
                    } else {
                        if (da[i - 1][j][k] == -1) {
                            da[i][j][k] = (j + distToRightMarsh[i][j]) > k ? 0 : -1;
                        } else {
                            da[i][j][k] = da[i - 1][j][k] + 1;

                            if (j + distToRightMarsh[i][j] > k) {
                                int perimiter = (k - j + da[i][j][k]) * 2;
                                if (maxPerimeter < perimiter) {
                                    maxPerimeter = perimiter;
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println(maxPerimeter == 0 ? "impossible" : maxPerimeter);
    }
}
