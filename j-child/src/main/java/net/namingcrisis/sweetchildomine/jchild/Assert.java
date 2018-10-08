package net.namingcrisis.sweetchildomine.jchild;

import java.util.function.Supplier;

/**
 * Poor-man's assertions. Don't judge me. However, note the use of Suppliers
 * for lazy eval of messages. Lambdas for the win.
 */
public final class Assert {
  public static void assertTrue(boolean condition, Supplier<String> msgFn) {
    if (!condition) {
      throw new AssertionError(msgFn.get());
    }
  }

  public static void assertFalse(boolean condition, Supplier<String> msgFn) {
    assertTrue(!condition, msgFn);
  }
}
