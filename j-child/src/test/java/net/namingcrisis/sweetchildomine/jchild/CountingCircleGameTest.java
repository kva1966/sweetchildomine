package net.namingcrisis.sweetchildomine.jchild;

import java.util.Arrays;

import org.junit.Test;

import static net.namingcrisis.sweetchildomine.jchild.util.TestUtil.mustFail;
import static org.junit.Assert.assertEquals;

public final class CountingCircleGameTest {
  @Test
  public void minimumNoOfChildrenPlaying() {
    // Given: Minimum n children
    int n = 2;

    // Then:
    // for k values 1 to > n (to check overflow conditions)
    assertResult(play(n, 1), 1, 0);
    assertResult(play(n, 2), 0, 1);
    assertResult(play(n, 3), 1, 0);
    assertResult(play(n, 4), 0, 1);
  }

  @Test
  public void greaterThanMinimumNoOfChildrenPlaying() {
    // Given: > Minimum n
    int n = 3;

    // Then:
    // for k values 1 to > n (to check overflow conditions)
    assertResult(play(n, 1), 2, 0, 1);
    assertResult(play(n, 2), 2, 1, 0);
    assertResult(play(n, 3), 1, 2, 0);
    assertResult(play(n, 4), 1, 0, 2);
  }

  @Test
  public void initFailure() {
    String nValueMsg = "at least 2 children expected";
    mustFail(() -> play(-1, 1), nValueMsg);
    mustFail(() -> play(0, 1), nValueMsg);
    mustFail(() -> play(1, 1), nValueMsg);

    String kValueMsg = "at least 1 count (k) expected";
    mustFail(() -> play(2, -1), kValueMsg);
    mustFail(() -> play(2, 0), kValueMsg);
  }

  @Test
  public void cannotReuseGameInstance() {
    CountingCircleGame game = CountingCircleGame.of(5, 1);
    game.play();

    mustFail(
      game::play,
      "Circle has been broken, one child left, game instance cannot be reused"
    );
  }

  private void assertResult(CountingCircleGame.Result result, Integer lastKidStanding, Integer... exitSequence) {
    assertEquals(lastKidStanding, result.lastChildStanding);
    assertEquals(Arrays.asList(exitSequence), result.exitedChildrenInSequence);
  }

  private CountingCircleGame.Result play(int n, int k) {
    return CountingCircleGame.of(n, k).play();
  }
}