package p0;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;
import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtMethod;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.*;
import static org.tudalgo.algoutils.tutor.general.assertions.Assertions4.assertIsNotRecursively;

@TestForSubmission
public class IterativeFibonacciCalculatorTests {

    private static final FibonacciCalculator FIBONACCI_CALCULATOR = new IterativeFibonacciCalculator();

    @ParameterizedTest
    @CsvFileSource(resources = "/fibonacci.csv")
    public void testFibonacci(int n, int expected) {
        Context context = contextBuilder()
            .subject("IterativeFibonacciCalculator#get(int)")
            .add("n", n)
            .build();

        assertEquals(expected, FIBONACCI_CALCULATOR.get(n), context,
            TR -> "Returned number did not match the expected Fibonacci number");
    }

    @Test
    public void testIterative() throws NoSuchMethodException {
        MethodLink methodLink = BasicMethodLink.of(IterativeFibonacciCalculator.class.getMethod("get", int.class));

        TutorUtils.assertIterative(methodLink, "get(int)", contextBuilder(), Matcher.never());
    }
}
