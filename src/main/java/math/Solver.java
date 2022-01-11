package math;

import java.util.ArrayList;
import java.util.List;

public class Solver {
    //quadratic equation
    public static List<Double> solve(double a, double b, double c) {
        List<Double> solutions = new ArrayList<>();
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

    //cubic equation
    public static List<Double> solve(double a, double b, double c, double d) {
        if (a == 0)
            return solve(b, c, d);

        List<Double> solutions = new ArrayList<>();
        if(d == 0) {
            solutions.add(0.0);
            List<Double> tmp = solve(a, b, c);
            solutions.addAll(tmp);
        } else {
            b /= a;
            c /= a;
            d /= a;

            double disc, q, r, dum1, s, t, term1, r13;
            q = (3.0 * c - (b * b)) / 9.0;
            r = -(27.0 * d) + b * (9.0 * c - 2.0 * (b * b));
            r /= 54.0;
            disc = q * q * q + r * r;

            term1 = (b / 3.0);

            if (disc > 0) { // one root real, two are complex
                s = r + Math.sqrt(disc);
                s = ((s < 0) ? Math.pow(-s, (1.0 / 3.0)) : Math.pow(s, (1.0 / 3.0)));
                t = r - Math.sqrt(disc);
                t = ((t < 0) ? - Math.pow(-t, (1.0 / 3.0)) : Math.pow(t, (1.0 / 3.0)));
                solutions.add(-term1 + s + t);
            } else if (disc == 0) { // All roots real, at least two are equal.
                r13 = ((r < 0) ? - Math.pow(-r, (1.0 / 3.0)) : Math.pow(r, (1.0/  3.0)));
                solutions.add(-term1 + 2.0 * r13);
                solutions.add(-(r13 + term1));
            } else { // all roots are real and unequal (to get here, q < 0)
                q = -q;
                dum1 = q * q * q;
                dum1 = Math.acos(r / Math.sqrt(dum1));
                r13 = 2.0 * Math.sqrt(q);
                solutions.add(-term1 + r13 * Math.cos(dum1 / 3.0));
                solutions.add(-term1 + r13 * Math.cos((dum1 + 2.0 * Math.PI) / 3.0));
                solutions.add(-term1 + r13 * Math.cos((dum1 + 4.0 * Math.PI) / 3.0));
            }
        }

        return solutions;
    }

    //quartic equation
    //ferraris solution
    public static List<Double> solve(double t4, double t3, double t2, double t1, double t0) {
        List<Double> solutions = new ArrayList<>();
        double[]  c = new double[5];
        c[0] = t0;
        c[1] = t1;
        c[2] = t2;
        c[3] = t3;
        c[4] = t4;
        double[]  coeffs = new double[4];
        double  z, u, v, sub;
        double  A, B, C, D;
        double  sq_A, p, q, r;
        int     i, num;

        /* normal form: x^4 + Ax^3 + Bx^2 + Cx + D = 0 */

        A = c[ 3 ] / c[ 4 ];
        B = c[ 2 ] / c[ 4 ];
        C = c[ 1 ] / c[ 4 ];
        D = c[ 0 ] / c[ 4 ];

    /*  substitute x = y - A/4 to eliminate cubic term:
	x^4 + px^2 + qx + r = 0 */

        sq_A = A * A;
        p = - 3.0/8 * sq_A + B;
        q = 1.0/8 * sq_A * A - 1.0/2 * A * B + C;
        r = - 3.0/256*sq_A*sq_A + 1.0/16*sq_A*B - 1.0/4*A*C + D;

        if (r < 0.00001 && r > -0.00001)
        {
            /* no absolute term: y(y^3 + py + q) = 0 */

//            coeffs[ 0 ] = q;
//            coeffs[ 1 ] = p;
//            coeffs[ 2 ] = 0;
//            coeffs[ 3 ] = 1;

//            List<Double> solutions = new ArrayList<>();
            solutions.add(0.0);
            solutions.addAll(solve(1, 0, p, q));
//            return solutions;
        }
        else
        {
            /* solve the resolvent cubic ... */



//            coeffs[ 0 ] = 1.0/2 * r * p - 1.0/8 * q * q;
//            coeffs[ 1 ] = - r;
//            coeffs[ 2 ] = - 1.0/2 * p;
//            coeffs[ 3 ] = 1;

//            List<Double> solutions = new ArrayList<>();
            solutions.addAll(solve(1, - 1.0/2 * p, - r, 1.0/2 * r * p - 1.0/8 * q * q));

            //(void) SolveCubic(coeffs, s);

            /* ... and take the one real solution ... */

//            z = s[ 0 ];
            z = solutions.get(0);

            /* ... to build two quadric equations */

            u = z * z - r;
            v = 2 * z - p;

            if (u < 0.00001 && u > -0.00001)
                u = 0;
            else if (u > 0)
                u = Math.sqrt(u);
            else
                return new ArrayList<Double>();

            if (v < 0.00001 && v > -0.00001)
                v = 0;
            else if (v > 0)
                v = Math.sqrt(v);
            else
                return new ArrayList<Double>();

            coeffs[ 0 ] = z - u;
            coeffs[ 1 ] = q < 0 ? -v : v;
            coeffs[ 2 ] = 1;

//            List<Double> solutions = new ArrayList<>();
            solutions.addAll(solve(1, q < 0 ? -v : v, z - u));
//            num = SolveQuadric(coeffs, s);

//            coeffs[ 0 ]= z + u;
//            coeffs[ 1 ] = q < 0 ? v : -v;
//            coeffs[ 2 ] = 1;
//
//            num += SolveQuadric(coeffs, s + num);
            solutions.addAll(solve(1, q < 0 ? v : -v, z + u));
        }

        /* resubstitute */

        sub = 1.0/4 * A;

        for (i = 0; i < solutions.size(); ++i)
            solutions.set(i, solutions.get(i) - sub);
//            s[ i ] -= sub;

        return solutions;
//        List<Double> solutions = new ArrayList<>();
//
//        // First step: convert to depressed quartic equation
//        double alpha = -(3 * B * B) / (8 * A * A) + C / A;
//        double beta = B * B * B / (8 * A * A * A) - (B * C) / (2 * A * A) + D / A;
//        double gama = -(3 * B * B * B * B) / (256 * A * A * A * A) + (C * B * B) / (16 * A * A * A) - (D * B) / (4 * A * A) + (E / A);
//
//        if (beta == 0) {
//            double delta1 = alpha * alpha - 4 * gama;
//            if (delta1 == 0) {
//                double delta2 = -alpha / 2;
//                if(delta2 == 0) {
//                    double s1 = (-B / (4 * A));
//                    solutions.add(s1);
//                } else if (delta2 > 0) {
//                    double s1 = (-B / (4 * A)) + Math.sqrt(delta2);
//                    double s2 = (-B / (4 * A)) - Math.sqrt(delta2);
//                    solutions.add(s1);
//                    solutions.add(s2);
//                }
//            }
//            else if (delta1 > 0) {
//                double delta2 = (-alpha + Math.sqrt(delta1)) / 2;
//                double delta3 = (-alpha - Math.sqrt(delta1)) / 2;
//                if(delta2 == 0) {
//                    double s1 = (-B / (4 * A));
//                    solutions.add(s1);
//                } else if (delta2 > 0) {
//                    double s1 = (-B / (4 * A)) + Math.sqrt(delta2);
//                    double s2 = (-B / (4 * A)) - Math.sqrt(delta2);
//                    solutions.add(s1);
//                    solutions.add(s2);
//                }
//                if(delta3 == 0) {
//                    double s1 = (-B / (4 * A));
//                    solutions.add(s1);
//                } else if (delta2 > 0) {
//                    double s1 = (-B / (4 * A)) + Math.sqrt(delta3);
//                    double s2 = (-B / (4 * A)) - Math.sqrt(delta3);
//                    solutions.add(s1);
//                    solutions.add(s2);
//                }
//            }
//        } else {
//            double P = -(alpha * alpha) / 12 - gama;
//            double Q = -(alpha * alpha * alpha) / 108 + (alpha * gama) / 3 -(beta * beta) / 8;
//            double tmp = (Q * Q) / 4 + (P * P * P) / 27;
//            if(tmp >= 0){
//                double R1 = -Q / 2 + Math.sqrt(tmp);
//                double R2 = -Q / 2 - Math.sqrt(tmp);
//                double U = Math.cbrt(R1);
//                double y;
//                if (U == 0) {
//                    y = -(5/6) * alpha - Math.cbrt(Q);
//                } else {
//                    y = -(5/6) * alpha + U - P / (3 * U);
//                }
//                double W = Math.sqrt(alpha + 2 * y);
//                double delta1 = -(3 * alpha + 2 * y + (2 * beta) / W);
//                double delta2 = -(3 * alpha + 2 * y - (2 * beta) / W);
//                if(delta1 == 0) {
//                    double s1 = -(B / (4 * A)) + W / 2;
//                } else if (delta1 > 0) {
//                    double s1 = -(B / (4 * A)) + (W + Math.sqrt(delta1)) / 2;
//                    double s2 = -(B / (4 * A)) - (W + Math.sqrt(delta1)) / 2;
//                    solutions.add(s1);
//                    solutions.add(s2);
//                }
//                if(delta2 == 0) {
//                    double s1 = -(B / (4 * A)) + W / 2;
//                } else if (delta2 > 0) {
//                    double s1 = -(B / (4 * A)) + (W + Math.sqrt(delta2)) / 2;
//                    double s2 = -(B / (4 * A)) - (W + Math.sqrt(delta2)) / 2;
//                    solutions.add(s1);
//                    solutions.add(s2);
//                }
//            }
//
//        }
//        return solutions;

//        double a = A, b = B, c = C, d = D, e = E;
//        double s1 = 2 * c * c * c - 9 * b * c * d + 27 * (a * d * d + b * b * e) - 72 * a * c * e,
//                q1 = c * c - 3 * b * d + 12 * a * e;
//        double discrim1 = -4 * q1 * q1 * q1 + s1 * s1;
//        if(discrim1 >0) {
//            double s2 = s1 + Math.sqrt(discrim1);
//            double q2 = Math.cbrt(s2 / 2),
//                    s3 = q1 / (3 * a * q2) + q2 / (3 * a),
//                    discrim2 = (b * b) / (4 * a * a) - (2 * c) / (3 * a) + s3;
//            if(discrim2>0) {
//                double s4 = Math.sqrt(discrim2);
//                double s5 = (b * b) / (2 * a * a) - (4 * c) / (3 * a) - s3;
//                double s6 = (-(b * b * b) / (a * a * a) + (4 * b * c) / (a * a) - (8 * d) / a) / (4 * s4);
//                double discrim3 = (s5 - s6),
//                        discrim4 = (s5 + s6);
//                // actual root values, may not be set
//                double r1 = 0, r2 = 0, r3 = 0, r4 = 0;
//
//                if(discrim3 > 0) {
//                    double sqrt1 = Math.sqrt(s5-s6);
//                    r1 = -b / (4 * a) - s4/2 + sqrt1 / 2;
//                    r2 = -b / (4 * a) - s4/2 - sqrt1 / 2;
//                } else if(discrim3 == 0) {
//                    // repeated root case
//                    r1 = -b / (4 * a) - s4/2;
//                }
//                if(discrim4 > 0) {
//                    double sqrt2 = Math.sqrt(s5+s6);
//                    r3 = -b / (4 * a) + s4/2 + sqrt2 / 2;
//                    r4 = -b / (4 * a) + s4/2 - sqrt2 / 2;
//                } else if(discrim4 ==0) {
//                    r3 = -b / (4 * a) + s4/2;
//                }
//                if(discrim3 > 0 && discrim4 > 0) {
//                    ArrayList<Double> doubles = new ArrayList<>();
//                    doubles.add(r1);
//                    doubles.add(r2);
//                    doubles.add(r3);
//                    doubles.add(r4);
//                    return doubles;
//                } else if( discrim3 > 0 && discrim4 == 0 ) {
//                    ArrayList<Double> doubles = new ArrayList<>();
//                    doubles.add(r1);
//                    doubles.add(r2);
//                    doubles.add(r3);
//                    return doubles;
//                }
//                else if( discrim3 > 0 && discrim4 < 0 ) {
//                    ArrayList<Double> doubles = new ArrayList<>();
//                    doubles.add(r1);
//                    doubles.add(r2);
//                    return doubles;
//                }
//                else if( discrim3 == 0 && discrim4 > 0 ) {
//                    ArrayList<Double> doubles = new ArrayList<>();
//                    doubles.add(r1);
//                    doubles.add(r3);
//                    doubles.add(r4);
//                    return doubles;
//                }
//                else if( discrim3 == 0 && discrim4 == 0 ) {
//                    ArrayList<Double> doubles = new ArrayList<>();
//                    doubles.add(r1);
//                    doubles.add(r3);
//                    return doubles;
//                }
//                else if( discrim3 == 0 && discrim4 < 0 ) {
//                    ArrayList<Double> doubles = new ArrayList<>();
//                    doubles.add(r1);
//                    return doubles;
//                }
//                else if( discrim3 < 0 && discrim4 > 0 ) {
//                    ArrayList<Double> doubles = new ArrayList<>();
//                    doubles.add(r3);
//                    doubles.add(r4);
//                    return doubles;
//                }
//                else if( discrim3 < 0 && discrim4 == 0 ) {
//                    ArrayList<Double> doubles = new ArrayList<>();
//                    doubles.add(r3);
//                    return doubles;
//                }
//                else if( discrim3 < 0 && discrim4 < 0 ) {
//                    ArrayList<Double> doubles = new ArrayList<>();
//                    return doubles;
//                }
//            }
//        }
//        ArrayList<Double> doubles = new ArrayList<Double>();
//        return doubles;
    }
}
