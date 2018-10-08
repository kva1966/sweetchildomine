package net.namingcrisis.sweetchildomine.jchild;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static net.namingcrisis.sweetchildomine.jchild.Assert.assertFalse;
import static net.namingcrisis.sweetchildomine.jchild.Assert.assertTrue;

/**
 * Stateful circular list that retains a pointer to its last point of iteration.
 *
 * @param <T> element type.
 */
public final class CircularLinkedList<T> {
  private final LinkedList<T> elements;
  private Iterator<T> iterationState;

  public CircularLinkedList(Collection<T> elems) {
    elements = new LinkedList<>(elems);
    iterationState = elements.iterator();
  }

  /**
   * Circulates up to k values, returning the element at that position,
   * removing it from the internal list. The iteration pointer moves to the
   * next item. Throws an error if there are no items left.
   *
   * @param k must be >= 1.
   *
   * @return element at iteration point after circling k times.
   */
  public T circulateAndRemove(int k) {
    assertTrue(k >= 1, () -> "");
    assertFalse(elements.isEmpty(), () -> "No items left in list");

    int curr = 0;

    while (true) {
      for (Iterator<T> i = iterationState.hasNext() ? iterationState : elements.iterator(); i.hasNext();) {
        T elem = i.next();

        if (++curr == k) {
          i.remove();
          iterationState = i;
          return elem;
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
}
