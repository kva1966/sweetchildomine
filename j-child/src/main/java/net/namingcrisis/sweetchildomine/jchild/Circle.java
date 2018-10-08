package net.namingcrisis.sweetchildomine.jchild;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;
import static net.namingcrisis.sweetchildomine.jchild.Assert.assertFalse;
import static net.namingcrisis.sweetchildomine.jchild.Assert.assertTrue;

/**
 * <p>Circle of children playing.</p>
 *
 * <p>Non-threadsafe, non-resettable state: init, run, discard.</p>
 *
 * <strong>Assumptions:</strong>
 *
 * <ul>
 *   <li>Circle must have at least 2 children, or error thrown. A circle of
 *   one child may either hint at narcissistic tendencies, or conversely,
 *   a deep, strange one-ness with the universe.</li>
 *
 *   <li>k must be at least 1, but can otherwise be any positive number,
 *        exceeding the number of children, so counts can go round the
 *        circle more than once before a kid is counted out. If 1,
 *        then we essentially have children immediately leaving in
 *        sequence from first to last.</li>
 *
 *   <li>Children IDs start from 0, thus 0 to n-1.</li>
 * </ul>
 */
public final class Circle {
  /**
   * Linked structures, all things being equal, are generally more space efficient,
   * Time and indexed access not necessary for this task.
   *
   * Am aware that better to use interfaces, but this is an internal design
   * choice. An interesting situation is size(), here I'm cheating and relying
   * on LinkedList having O(1) for size().
   *
   * Additionally, could write a custom circular list with nodes implementation,*
   */
  private final CircularLinkedList<Child> children;
  private final int k;

  public static Circle of(int nChildren, int kCountPerIteration) {
    return new Circle(nChildren, kCountPerIteration);
  }

  private Circle(int nChildren, int kCountPerIteration) {
    assertTrue(nChildren >= 2, () -> "at least 2 children expected");
    assertTrue(kCountPerIteration > 0, () -> "at least 1 count (k) expected");

    k = kCountPerIteration;
    children = new CircularLinkedList<>(
      range(0, nChildren)
        .mapToObj(Child::newChild)
        .collect(Collectors.toList())
    );
  }

  public Result play() {
    assertFalse(children.isEmpty(), () -> "Circle has been broken, no children here");

    /*
     * There's possibly an analytical solution/formula for this, especially useful for
     * large values of n and k, but I haven't attempted to derive it; just iterating
     * through the children -- numerical approach taken.
     */
    List<Child> exited = new LinkedList<>();

    Supplier<Boolean> oneChildLeft = () -> children.size() == 1;

    while (!oneChildLeft.get()) {
      exited.add(children.circulateAndRemove(k));
    }

    return new Result(children.oneAndOnly(), exited);
  }

  /**
   * Data class. Yes, Kotlin would be nicer.
   */
  public static final class Result {
    public final Child lastChildStanding;
    public final List<Child> exitedChildrenInSequence;

    private Result(Child lastChildStanding, List<Child> exitedChildrenInSequence) {
      this.exitedChildrenInSequence = exitedChildrenInSequence;
      this.lastChildStanding = lastChildStanding;
    }
  }
}
