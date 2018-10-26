package coprimePaths;

/**
 * Created by Denis_Mironchuk on 10/26/2018.
 */
public class NumberUtils {
    public static int factor(int n, int[] res) {
        int size = 0;

        int n_ = n;
        for (int i = 2; i * i <= n; i++) {
            while (n_ % i == 0) {
                res[size] = i;
                size++;
                n_ /= i;
            }
        }

        if (n_ != 1) {
            res[size] = n_;
            size++;
        }

        return size;
    }

    public static int getUniqueValues(int[] fact, int size, int[] res) {
        int newSize = 0;

        for (int i = 0; i < size; i++) {
            int f = fact[i];
            boolean isUnique = true;

            for (int j = i + 1; isUnique && j < size; j++) {
                isUnique = (f != fact[j]);
            }

            if (isUnique) {
                res[newSize] = f;
                newSize++;
            }
        }

        return newSize;
    }
}
