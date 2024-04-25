package p0;

import static org.tudalgo.algoutils.student.Student.crash;

/**
 * A recursive implementation of the {@link FibonacciCalculator} interface.
 */
public class RecursiveFibonacciCalculator implements FibonacciCalculator {

    public RecursiveFibonacciCalculator() {
        super();
    }

    @Override
    public int get(int n) {
        if (n<0)
            throw new IllegalArgumentException("Index must be non-negative");

        if (n==0)
            return 0;
        if (n==1)
            return 1;

        return get(n-1) + get(n-2);
    }
}
