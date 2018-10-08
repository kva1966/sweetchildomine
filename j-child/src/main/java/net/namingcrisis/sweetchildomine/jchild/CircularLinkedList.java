package net.namingcrisis.sweetchildomine.jchild;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Stateful circular list that retains a pointer to its last point of iteration.
 *
 * @param <T> element type.
 */
public final class CircularLinkedList<T> {
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
    Assert.assertTrue(k >= 1, () -> "");
    Assert.assertFalse(elements.isEmpty(), () -> "No items left in list");

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
    Assert.assertTrue(elements.size() == 1, () -> "Expecting exactly one element!");
    return elements.getFirst();
  }
}
