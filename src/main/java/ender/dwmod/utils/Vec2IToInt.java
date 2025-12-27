package ender.dwmod.utils;

public final class Vec2IToInt {
    
    // --------- N -> Z^2 ---------
    public static long[] nToZ2(long n) {
        if (n < 0) throw new IllegalArgumentException("n must be >= 0");
        if (n == 0) return new long[]{0, 0};

        long k = ringIndex(n);                 // "anneau" (distance Chebyshev)
        long t = 2L * k + 1L;
        long nMax = t * t - 1L;                // valeur au coin (k, -k) de l’anneau k
        long offset = nMax - n;                // distance en arrière depuis (k, -k)

        long x, z;
        if (offset <= 2L * k) {                // bas: de (k,-k) vers (-k,-k)
            x = k - offset;
            z = -k;
        } else if (offset <= 4L * k) {         // gauche: de (-k,-k+1) vers (-k,k)
            x = -k;
            z = -k + (offset - 2L * k);
        } else if (offset <= 6L * k) {         // haut: de (-k+1,k) vers (k,k)
            x = -k + (offset - 4L * k);
            z = k;
        } else {                                // droite: de (k,k-1) vers (k,-k+1)
            x = k;
            z = k - (offset - 6L * k);
        }
        return new long[]{x, z};
    }

    // --------- Z^2 -> N ---------
    public static long z2ToN(long x, long z) {
        long k = Math.max(Math.abs(x), Math.abs(z));
        if (k == 0) return 0;

        long t = 2L * k + 1L;
        long nMax = t * t - 1L;                // au point (k, -k)

        long offset;
        if (z == -k) {                         // bas
            offset = k - x;
        } else if (x == -k) {                  // gauche
            offset = 2L * k + (z + k);
        } else if (z == k) {                   // haut
            offset = 4L * k + (x + k);
        } else {                               // droite (x == k)
            offset = 6L * k + (k - z);
        }

        return nMax - offset;
    }


    private static long ringIndex(long n) {
        long r = isqrt(n + 1);                 // floor(sqrt(n+1))
        long k = (r - 1) / 2;                  // candidat
        long t = 2L * k + 1L;
        long nMax = t * t - 1L;
        if (n > nMax) k++;                     // ajuste si on est au-delà
        return k;
    }


    private static long isqrt(long x) {
        if (x < 0) throw new IllegalArgumentException("x must be >= 0");
        long r = (long) Math.sqrt((double) x);
        while ((r + 1) > 0 && (r + 1) * (r + 1) <= x) r++;
        while (r * r > x) r--;
        return r;
    }
}
