package p0;

import org.tudalgo.algoutils.tutor.general.assertions.Context;
import org.tudalgo.algoutils.tutor.general.match.Matcher;
import org.tudalgo.algoutils.tutor.general.reflections.BasicMethodLink;
import org.tudalgo.algoutils.tutor.general.reflections.MethodLink;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.code.CtLoop;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.reference.CtExecutableReference;

import java.util.*;
import java.util.stream.Collectors;

import static org.tudalgo.algoutils.tutor.general.assertions.Assertions2.assertTrue;

public class TutorUtils {

    /**
     * Tests whether the given method is iterative.
     *
     * @param method     the method to test
     * @param methodName the name of the method
     * @param context    the context
     * @param skips      defines the methods to skip
     */
    @SafeVarargs
    public static void assertIterative(
        MethodLink method,
        String methodName,
        Context.Builder<?> context,
        Matcher<MethodLink>... skips
    ) {
        var calls = getRecursiveCalls(method, skips);
        var callsName = calls.stream().map(MethodLink::name).collect(Collectors.toSet());
        assertTrue(calls.isEmpty(), context.add("Method calls (iterative)", callsName).build(),
            result -> "The %s should be iterative, but found a recursion in %s".formatted(methodName, callsName)
        );
    }

    /**
     * Returns the method calls in the given method.
     *
     * @param method  the method to get the method calls for
     * @param visited the visited methods so far (to prevent infinite recursion)
     * @param skips   defines the methods to skip
     * @return the method calls in the given method
     */
    private static Set<MethodLink> getMethodCalls(
        MethodLink method,
        Set<MethodLink> visited,
        Matcher<MethodLink> skips
    ) {
        CtMethod<?> ctMethod;
        try {
            ctMethod = ((BasicMethodLink) method).getCtElement();
        } catch (NullPointerException | ArrayIndexOutOfBoundsException e) {
            // java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0
            // java.lang.NullPointerException: Cannot invoke "String.toCharArray()" because "this.content" is null
            // Occurs if we read src code from stdlib - skip them
            return Set.of();
        }
        return ctMethod.filterChildren(it -> it instanceof CtInvocation<?>)
            .list()
            .stream()
            .filter(element -> element instanceof CtInvocation<?> invocation)
            .map(element -> (CtInvocation<?>) element)
            .map(CtInvocation::getExecutable)
            .map(CtExecutableReference::getActualMethod)
            .filter(Objects::nonNull)
            .map(BasicMethodLink::of)
            .filter(methodLink -> skips.match(methodLink).negative())
            .collect(Collectors.toSet());
    }

    /**
     * Returns the recursive calls in the given method (including all method-calls in the method).
     *
     * @param method the method to get the recursive calls for
     * @param skips  defines the methods to skip
     * @return the recursive calls in the given method
     */
    @SafeVarargs
    public static Set<MethodLink> getRecursiveCalls(MethodLink method, Matcher<MethodLink>... skips) {
        Set<MethodLink> recursion = new HashSet<>();
        Set<MethodLink> visited = new HashSet<>();
        visited.add(method);
        computeRecursiveCalls(method, recursion, visited, Arrays.stream(skips)
            .reduce(Matcher::or)
            .orElse(Matcher.never()));
        return recursion;
    }

    /**
     * Computes the recursive calls in the given method (including all method-calls in the method).
     *
     * @param method  the method to get the recursive calls for
     * @param found   the so far found recursive calls
     * @param visited the visited methods so far (to prevent infinite recursion)
     * @param skips   defines the methods to skip
     */
    private static void computeRecursiveCalls(
        MethodLink method, Set<MethodLink> found,
        Set<MethodLink> visited,
        Matcher<MethodLink> skips
    ) {
        var methodCalls = getMethodCalls(method, visited, skips);
        found.addAll(methodCalls.stream().filter(visited::contains).collect(Collectors.toSet()));
        for (MethodLink methodLink : methodCalls) {
            if (!visited.contains(methodLink)) {
                visited.add(methodLink);
                computeRecursiveCalls(methodLink, found, visited, skips);
            }
        }
    }

}
