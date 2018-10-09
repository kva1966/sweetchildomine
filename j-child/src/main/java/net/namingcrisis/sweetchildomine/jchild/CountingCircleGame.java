package net.namingcrisis.sweetchildomine.jchild;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import static net.namingcrisis.sweetchildomine.jchild.Assert.assertFalse;
import static net.namingcrisis.sweetchildomine.jchild.Assert.assertTrue;

/**
 * <p>Circle of children playing the counting game.</p>
 *
 * <p><strong>Rules:</strong></p>
 *
 * <ul>
 *  <li>n children stand around a circle.</li>
 *  <li>Starting with a given child and working clockwise, each child gets a
 *    sequential number, which we will refer to as it’s id.</li>
 *  <li>Then starting with the first child, they count out from 1 until k. The
 *    k’th child is now out and leaves the circle. The count starts again
 *    with the child immediately next to the eliminated one.</li>
 *  <li>Children are so removed from the circle one by one. The winner is the
 *    child left standing last.</li>
 * </ul>
 *
 * <p>Non-threadsafe, non-resettable state: init, run, discard.</p>
 *
 * <p><strong>Assumptions:</strong></p>
 *
 * <ul>
 *   <li>Circle must have at least 2 children, or error thrown. A circle of
 *    one child may either hint at narcissistic tendencies, or conversely,
 *    a deep, strange oneness with the universe.</li>
 *
 *   <li>k must be at least 1, but can otherwise be any positive number,
 *    exceeding the number of children, so counts can go round the
 *    circle more than once before a kid is counted out. If 1,
 *    then we essentially have children immediately leaving in
 *    sequence from first to last.</li>
 *
 *   <li>Children IDs start from 0, thus 0 to n-1.</li>
 * </ul>
 */
public final class CountingCircleGame {
  /**
   * Children are simply represented by int ids for this simple scenario.
   */
  private final CircularLinkedList<Integer> children;
  private final int k;

  public static CountingCircleGame of(int nChildren, int kCountPerIteration) {
    return new CountingCircleGame(nChildren, kCountPerIteration);
  }

  private CountingCircleGame(int nChildren, int kCountPerIteration) {
    assertTrue(nChildren >= 2, () -> "at least 2 children expected");
    assertTrue(kCountPerIteration > 0, () -> "at least 1 count (k) expected");

    k = kCountPerIteration;
    children = new CircularLinkedList<>(
      range(0, nChildren)
        .boxed()
        .collect(toList())
    );
  }

  public Result play() {
    assertFalse(children.isEmpty(), () -> "Circle has been broken, no children here");

    /*
     * There's possibly an analytical solution/formula for this, especially useful for
     * large values of n and k, but I haven't attempted to derive it; just iterating
     * through the children -- numerical approach taken.
     */
    List<Integer> exited = new LinkedList<>();

    Supplier<Boolean> oneChildLeft = () -> children.size() == 1;

    while (!oneChildLeft.get()) {
      Integer exitingChild = children
        .circulate(k, (itr, elem) -> itr.remove())
        .orElseThrow(() -> new IllegalStateException("Logic error, always expecting a value!"));

      exited.add(exitingChild);
    }

    return new Result(children.oneAndOnly(), exited);
  }

  /**
   * Data class. Yes, Kotlin would be nicer.
   */
  public static final class Result {
    public final Integer lastChildStanding;
    public final List<Integer> exitedChildrenInSequence;

    private Result(Integer lastChildStanding, List<Integer> exitedChildrenInSequence) {
      this.exitedChildrenInSequence = exitedChildrenInSequence;
      this.lastChildStanding = lastChildStanding;
    }
  }
}
