package net.namingcrisis.sweetchildomine.jchild.util;

import org.junit.Assert;

import static org.junit.Assert.assertEquals;

public final class TestUtil {
  private TestUtil() {
    // static util only.
  }

  public static void mustFail(Op op, String errMsg) {
    try {
      op.apply();
      Assert.fail("<test> Failure expected but passed");
    } catch (AssertionError e) {
      assertEquals(errMsg, e.getMessage());
    }
  }

  @FunctionalInterface
  public interface Op {
    void apply();
  }
}
