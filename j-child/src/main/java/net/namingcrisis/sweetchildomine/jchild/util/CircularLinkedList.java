package net.namingcrisis.sweetchildomine.jchild.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

import static net.namingcrisis.sweetchildomine.jchild.util.Assert.assertTrue;

/**
 * Stateful circular list that retains a pointer to its last point of iteration.
 *
 * @param <T> element type.
 */
public final class CircularLinkedList<T> {
  private final LinkedList<T> elements;
  private Iterator<T> iterationState;

  /**
   * More constructors/factories could be useful, keeping this to our common use
   * case for now.
   *
   * @param elems to initialise this list from.
   */
  public CircularLinkedList(Collection<T> elems) {
    elements = new LinkedList<>(elems);
    iterationState = elements.iterator();
  }

  /**
   * <p>Circulates up to k values, then passing in the iterator at the k'th value
   * to the visitor function, nothing in between. Returns if empty list, visitor
   * never called.</p>
   *
   * @param k must be >= 1, count of element to reach, circling around
   *          if k > list size.
   * @param visitorFn visitor function.
   *
   * @return empty optional if list is empty, otherwise optional contains element
   *   at iteration point after circling k times.
   */
  public Optional<T> circulate(int k, Op<T> visitorFn) {
    assertTrue(k >= 1, () -> "At least 1 element to circulate to expected");

    if (elements.isEmpty()) {
      return Optional.empty();
    }

    int curr = 0;

    while (true) {
      for (Iterator<T> i = iterationState.hasNext() ? iterationState : elements.iterator(); i.hasNext();) {
        T elem = i.next();

        if (++curr == k) {
          visitorFn.apply(i, elem);
          iterationState = i;
          return Optional.ofNullable(elem); // list contract does not prohibit null elems.
        }
      }
    }
  }

  public boolean isEmpty() {
    return elements.isEmpty();
  }

  public int size() {
    return elements.size();
  }

  public T oneAndOnly() {
    assertTrue(elements.size() == 1, () -> "Expecting exactly one element!");
    return elements.getFirst();
  }

  @FunctionalInterface
  public interface Op<T> {
    /**
     * Visitor function.
     *
     * @param it at current point. Clients expected to mainly use this to remove
     *           the current element. Design note: possibly a 'safer' interface that
     *           does not take an iterator, but requires the visitor function to
     *           return a signal operation may be preferable in some cases.
     * @param elem at current iteration {@link Iterator#next()}.
     */
    void apply(Iterator<T> it, T elem);
  }
}
