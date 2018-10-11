package net.namingcrisis.sweetchildomine.jchild.util;

import org.junit.Test;

import static net.namingcrisis.sweetchildomine.jchild.util.TestUtil.mustFail;

public final class AssertTest {

  @Test
  public void assertTrue() {
    Assert.assertTrue(true, () -> {
      throw new AssertionError("Never expected to be called!");
    });

    mustFail(
      () -> Assert.assertTrue(false, () -> "Failed condition"),
      "Failed condition"
    );
  }

  @Test
  public void assertFalse() {
    Assert.assertFalse(false, () -> {
      throw new AssertionError("Never expected to be called!");
    });

    mustFail(
      () -> Assert.assertFalse(true, () -> "Failed condition"),
      "Failed condition"
    );
  }
}
