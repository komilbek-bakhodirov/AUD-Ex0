package p0;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * An iterative implementation of the {@link FibonacciCalculator} interface.
 */
public class IterativeFibonacciCalculator implements FibonacciCalculator {

    public IterativeFibonacciCalculator() {
        super();
    }

    @Override
    public int get(int n) {
        if (n<0)
            throw new IllegalArgumentException("Index must be non-negative");

        int a = 0;
        int b = 1;

        if (n==0)
            return a;
        if (n==1)
            return b;

        for (int i = 2; i <=n ; i++) {
            int next = a+b;
            a=b;
            b=next;
        }
        return b;
    }
}
