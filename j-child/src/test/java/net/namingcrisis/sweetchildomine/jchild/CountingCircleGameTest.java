package net.namingcrisis.sweetchildomine.jchild;

import java.util.Arrays;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class CountingCircleGameTest {
  @Test
  public void playMinimumChildren() {
    // Given: Minimum n
    int n = 2;

    // Then:
    // k iterating once around circle
    assertResult(play(n, 1), 1, 0);
    assertResult(play(n, 2), 0, 1);

    // And:
    // k iterating twice around circle - no overflow errors
    assertResult(play(n, 3), 1, 0);
    assertResult(play(n, 4), 0, 1);
  }

  @Test
  public void playGreaterThanMinimumChildren() {
    // Given: Minimum n
    int n = 3;

    // Then:
    // k iterating once around circle
    assertResult(play(n, 1), 2, 0, 1);
    assertResult(play(n, 2), 2, 1, 0);
//    assertResult(play(n, 2), child(0), child(1));

    // And:
    // k iterating twice around circle - no overflow errors
//    assertResult(play(n, 3), child(1), child(0));
//    assertResult(play(n, 4), child(0), child(1));
  }

  private void assertResult(CountingCircleGame.Result result, Integer lastKidStanding, Integer... exitSequence) {
    System.out.println("> Result.lastChild -> " + result.lastChildStanding);
    System.out.println("> Result.exitSequence -> " + result.exitedChildrenInSequence);

    assertEquals(lastKidStanding, result.lastChildStanding);
    assertEquals(Arrays.asList(exitSequence), result.exitedChildrenInSequence);
  }

  private CountingCircleGame.Result play(int n, int k) {
    return CountingCircleGame.of(n, k).play();
  }
}