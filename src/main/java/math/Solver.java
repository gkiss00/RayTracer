package math;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    private final static double EPSILON = 0.00001;

    //quadratic equation
    public static List<Double> solve(double a, double b, double c) {
        List<Double> solutions = new ArrayList<>();

        if(a < EPSILON && a > -EPSILON) {
            solutions.add(-c / b);
            return solutions;
        }
        double delta = b * b - 4 * a * c;
        if (delta < 0) {
            return solutions;
        } else if (delta == 0) {
            solutions.add(-b / (2 * a));
        } else {
            solutions.add((-b + Math.sqrt(delta)) / (2 * a));
            solutions.add((-b - Math.sqrt(delta)) / (2 * a));
        }
        return solutions;
    }

    public static List<Double> solve(double a, double b, double c, double d) {
        List<Double> solutions = new ArrayList<>();
        double sub;
        double A, B, C;
        double sq_A, p, q;
        double cb_p, D;

        /*if(a < EPSILON && a > -EPSILON)
            return solve(b, c, d);
        if(d < EPSILON && d > -EPSILON) {
            solutions.add(0.0);
            solutions.addAll(solve(a, b, c));
            return solutions;
        }*/
        /* normal form: x^3 + Ax^2 + Bx + C = 0 */
        A = b / a;
        B = c / a;
        C = d / a;

        /*  substitute x = y - A/3 to eliminate quadric term:x^3 +px + q = 0 */
        sq_A = A * A;
        p = 1.0 / 3 * (-1.0 / 3 * sq_A + B);
        q = 1.0 / 2 * (2.0 / 27 * A * sq_A - 1.0 / 3 * A * B + C);

        /* use Cardano's formula */
        cb_p = p * p * p;
        D = q * q + cb_p;

        if (D < EPSILON && D > -EPSILON) {
            if (q < EPSILON && q > -EPSILON) /* one triple solution */ {
                solutions.add(0.0);
            } else /* one single and one double solution */ {
                double u = Math.cbrt(-q);
                solutions.add(2 * u);
                solutions.add(-u);
            }
        } else if (D < 0) /* Casus irreducibilis: three real solutions */ {
            double phi = 1.0 / 3 * Math.acos(-q / Math.sqrt(-cb_p));
            double t = 2 * Math.sqrt(-p);
            solutions.add(t * Math.cos(phi));
            solutions.add(-t * Math.cos(phi + Math.PI / 3));
            solutions.add(-t * Math.cos(phi - Math.PI / 3));
        } else /* one real solution */ {
            double sqrt_D = Math.sqrt(D);
            double u = Math.cbrt(sqrt_D - q);
            double v = -Math.cbrt(sqrt_D + q);
            solutions.add(u + v);
        }

        /* resubstitute */
        sub = 1.0 / 3 * A;
        for (int i = 0; i < solutions.size(); ++i)
            solutions.set(i, solutions.get(i) - sub);
        return solutions;
    }

    //quartic equation
    //ferraris solution
    public static List<Double> solve(double a, double b, double c, double d, double e) {
        List<Double> solutions = new ArrayList<>();
        double z, u, v, sub;
        double A, B, C, D;
        double sq_A, p, q, r;
        int i;

        /*if(a < EPSILON && a > -EPSILON) {
            return solve(b, c, d, e);
        }
        if(e < EPSILON && e > -EPSILON) {
            solutions.add(0.0);
            solutions.addAll(solve(a, b, c, d));
            return solutions;
        }*/

        /* normal form: x^4 + Ax^3 + Bx^2 + Cx + D = 0 */
        A = b / a;
        B = c / a;
        C = d / a;
        D = e / a;

        /*  substitute x = y - A/4 to eliminate cubic term: x^4 + px^2 + qx + r = 0 */
        sq_A = A * A;
        p = -3.0 / 8 * sq_A + B;
        q = 1.0 / 8 * sq_A * A - 1.0 / 2 * A * B + C;
        r = -3.0 / 256 * sq_A * sq_A + 1.0 / 16 * sq_A * B - 1.0 / 4 * A * C + D;

        if (r < EPSILON && r > -EPSILON) {
            /* no absolute term: y(y^3 + py + q) = 0 */
            solutions.add(0.0);
            solutions.addAll(solve(1, 0, p, q));
        } else {
            /* solve the resolvent cubic ... */
            List<Double> tmp = solve(1, -1.0 / 2 * p, -r, 1.0 / 2 * r * p - 1.0 / 8 * q * q);

            /* ... and take the one real solution ... */
            z = tmp.get(0);

            /* ... to build two quadric equations */
            u = z * z - r;
            v = 2 * z - p;

            if (u < EPSILON && u > -EPSILON)
                u = 0;
            else if (u > 0)
                u = Math.sqrt(u);
            else {
                return solutions;
            }

            if (v < EPSILON && v > -EPSILON)
                v = 0;
            else if (v > 0)
                v = Math.sqrt(v);
            else
                return solutions;
            solutions.addAll(solve(1, q < 0 ? -v : v, z - u));
            solutions.addAll(solve(1, q < 0 ? v : -v, z + u));
        }

        /* resubstitute */
        sub = 1.0 / 4 * A;
        for (i = 0; i < solutions.size(); ++i) {
            solutions.set(i, solutions.get(i) - sub);
        }

        return solutions;
    }
}
