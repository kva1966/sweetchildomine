package net.namingcrisis.sweetchildomine.jchild;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.Test;

import static net.namingcrisis.sweetchildomine.jchild.TestUtil.mustFail;
import static org.junit.Assert.*;

public final class CircularLinkedListTest {
  @Test
  public void size() {
    assertEquals(0, listOf().size());
    assertEquals(1, listOf(1).size());
    assertEquals(3, listOf(1, 6, 8).size());
  }

  @Test
  public void isEmpty() {
    assertTrue(listOf().isEmpty());
    assertFalse(listOf(1).isEmpty());
    assertFalse(listOf(1, 6, 8).isEmpty());
  }

  @Test
  public void oneAndOnly() {
    assertEquals(1, listOf(1).oneAndOnly().intValue());

    String msg = "Expecting exactly one element!";
    mustFail(() -> listOf().oneAndOnly(), msg);
    mustFail(() -> listOf(1, 2).oneAndOnly(), msg);
  }

  @Test
  public void circulateOnEmptyList() {
    CircularLinkedList.Op<Integer> op = (itr, elem) -> {
      throw new AssertionError("Should never be called!");
    };
    listOf().circulate(1000, op);
  }

  @Test
  public void circulateWithBadCounts() {
    CircularLinkedList.Op<Integer> neverCalledOp = (itr, v) -> {
      throw new AssertionError("should never be called");
    };
    String errMsg = "At least 1 element to circulate to expected";

    mustFail(() -> listOf(1).circulate(0, neverCalledOp), errMsg);
    mustFail(() -> listOf(1).circulate(-1, neverCalledOp), errMsg);
  }

  @Test
  public void circulateOnlyCallsVisitorOnceAtRightCountOnly() {
    listOf(11, 12, 13)
      .circulate(4, (itr, el) -> {
        if (el != 11) {
          throw new AssertionError("Visitor possibly called more than once per circulate in k");
        }
      });
  }

  @Test
  public void circulateAndRead() {
    Function<Integer, CircularLinkedList.Op<Integer>> opFactory =
      expectedVal -> (it, val) -> assertEquals(expectedVal, val);

    listOf(11).circulate(1, opFactory.apply(11));
    listOf(11).circulate(2, opFactory.apply(11));
    listOf(11).circulate(3, opFactory.apply(11));
    listOf(11, 12).circulate(1, opFactory.apply(11));
    listOf(11, 12).circulate(2, opFactory.apply(12));
    listOf(11, 12).circulate(3, opFactory.apply(11));
    listOf(11, 12).circulate(4, opFactory.apply(12));
    listOf(11, 12, 13).circulate(1, opFactory.apply(11));
    listOf(11, 12, 13).circulate(3, opFactory.apply(13));
    listOf(11, 12, 13).circulate(4, opFactory.apply(11));
    listOf(11, 12, 13).circulate(6, opFactory.apply(13));
    listOf(11, 12, 13).circulate(9, opFactory.apply(13));
  }

  /**
   * Focus here is on removal in some scenarios: we have verified more extensive
   * circular visitation behaviour in {@link #circulateAndRead()}.
   */
  @Test
  public void circulateAndRemove() {
    // single elem
    CircularLinkedList<Integer> l1 = listOf(11);
    l1.circulate(1, (itr, el) -> itr.remove());
    assertTrue(l1.isEmpty());

    // 2 elem, remove last
    CircularLinkedList<Integer> l2 = listOf(11, 12);
    l2.circulate(2, (itr, el) -> {
      if (el == 12) {
        itr.remove();
      }
    });
    assertEquals(1, l2.size());

    // 2 elem, circle over and remove first
    CircularLinkedList<Integer> l3 = listOf(11, 12);
    l3.circulate(3, (itr, el) -> {
      if (el == 11) {
        itr.remove();
      } else {
        throw new AssertionError("Should never be called more than once!");
      }
    });
    assertEquals(1, l3.size());

    // orderly remove one by one leaves list empty
    CircularLinkedList<Integer> l4 = listOf(0, 1, 2, 3, 4, 5);
    assertEquals(6, l4.size()); // sanity check
    IntStream.range(0, 6)
      .forEach(val ->
        l4.circulate(1, (itr, e) -> itr.remove())
      );
    assertTrue(l4.isEmpty());
  }

  private CircularLinkedList<Integer> listOf(Integer... elems) {
    return new CircularLinkedList<>(Arrays.asList(elems));
  }
}