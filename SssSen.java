import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SssSen {
    public static final int PRIME = 257;

    public static int modPow(int base, int exp, int mod) {
        long res = 1;
        long b = base;
        while (exp > 0) {
            if ((exp & 1) != 0) res = (res * b) % mod;
            b = (b * b) % mod;
            exp >>= 1;
        }
        return (int) res;
    }

    public static List<int[]> shamirSplit(int secret, int n, int k) {
        int[] coeffs = new int[k];
        coeffs[0] = secret;
        Random rnd = new Random();
        for (int i = 1; i < k; i++) {
            coeffs[i] = rnd.nextInt(PRIME - 1) + 1;
        }

        List<int[]> shares = new ArrayList<>();
        for (int x = 1; x <= n; x++) {
            long y = 0, powX = 1;
            for (int i = 0; i < k; i++) {
                y = (y + coeffs[i] * powX) % PRIME;
                powX = (powX * x) % PRIME;
            }
            shares.add(new int[]{x, (int) y});
        }
        return shares;
    }

    public static int shamirCombine(List<int[]> shares) {
        long secret = 0;
        for (int i = 0; i < shares.size(); i++) {
            long xi = shares.get(i)[0];
            long yi = shares.get(i)[1];

            long num = 1, den = 1;
            for (int j = 0; j < shares.size(); j++) {
                if (i == j) continue;
                long xj = shares.get(j)[0];
                num = (num * (-xj + PRIME)) % PRIME;
                den = (den * (xi - xj + PRIME)) % PRIME;
            }
            long inv = modPow((int) den, PRIME - 2, PRIME);
            long term = yi * num % PRIME * inv % PRIME;
            secret = (secret + term) % PRIME;
        }
        return (int) secret;
    }
}

