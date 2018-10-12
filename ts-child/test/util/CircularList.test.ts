import { CircularList } from "@app/util/CircularList";
import { PostVisitOperation } from "../../src/util/CircularList";

function listOf(elems?: number[]) {
  return new CircularList<number>(elems);
}

describe("CircularList", () => {
  test("size", () => {
    expect(listOf().size).toBe(0);
    expect(listOf([]).size).toBe(0);
    expect(listOf([1]).size).toBe(1);
    expect(listOf([1, 6, 8]).size).toBe(3);
  });

  test("isEmpty", () => {
    expect(listOf().isEmpty).toBe(true);
    expect(listOf([]).isEmpty).toBe(true);
    expect(listOf([1]).isEmpty).toBe(false);
    expect(listOf([1, 6, 8]).isEmpty).toBe(false);
  });

  test("oneAndOnly", () => {
    expect(listOf([11]).oneAndOnly()).toBe(11);

    const msg = "Expecting exactly one element!";
    expect(() => listOf().oneAndOnly()).toThrow(msg);
    expect(() => listOf([]).oneAndOnly()).toThrow(msg);
    expect(() => listOf([1, 2]).oneAndOnly()).toThrow(msg);
  });

  test("circulate - on empty list", () => {
    const op = (el: number) =>  { throw new Error("Should never be called!"); };
    listOf().circulate(1000, op);
  });

  test("circulate - with bad circulate counts", () => {
    const neverCalledOp = (el: number) =>  { throw new Error("Should never be called!"); };

    expect(() => listOf([1]).circulate(0, neverCalledOp)).toThrow("At least 1 element to circulate to expected");
    expect(() => listOf([1]).circulate(-1, neverCalledOp)).toThrow("At least 1 element to circulate to expected");
  });

  test("circulate - only calls visitor once at correct count", () => {
    listOf([11, 12, 13])
      .circulate(4, (el: number) => {
        if (el !== 11) {
          throw new Error("Visitor possibly called more than once per circulate in k");
        }
        return null;
      });
  });

  test("circulate - read value", () => {
    const assertRead = (list: CircularList<number>, k: number, expectedVal: number) => {
      const read = list.circulate(k, (el) => expect(el).toBe(expectedVal));
      expect(read).toBe(expectedVal);
    };

    assertRead(listOf([11]), 1, 11);
    assertRead(listOf([11]), 2, 11);
    assertRead(listOf([11]), 3, 11);
    assertRead(listOf([11, 12]), 1, 11);
    assertRead(listOf([11, 12]), 2, 12);
    assertRead(listOf([11, 12]), 3, 11);
    assertRead(listOf([11, 12]), 4, 12);
    assertRead(listOf([11, 12, 13]), 1, 11);
    assertRead(listOf([11, 12, 13]), 3, 13);
    assertRead(listOf([11, 12, 13]), 4, 11);
    assertRead(listOf([11, 12, 13]), 6, 13);
    assertRead(listOf([11, 12, 13]), 9, 13);
  });

  test("circulate - remove element", () => {
    let current;

    // single elem
    const l1: CircularList<number> = listOf([11]);
    current = l1.circulate(1, (el: number) => PostVisitOperation.REQUEST_DELETE);
    expect(current).toBe(11);
    expect(l1.isEmpty).toBe(true);

    // 2 elem, remove last
    const l2: CircularList<number> = listOf([11, 12]);
    current = l2.circulate(2, (el) => PostVisitOperation.REQUEST_DELETE);
    expect(current).toBe(12);
    expect(l2.size).toBe(1);

    // 2 elem, circle over and remove first
    const l3: CircularList<number>= listOf([11, 12]);
    current = l3.circulate(3, (el) => PostVisitOperation.REQUEST_DELETE);
    expect(current).toBe(11);
    expect(l3.size).toBe(1);

    // 3 elem, keep every 2 till 1 left.
    const l4: CircularList<number>= listOf([11, 12, 13]);
    current = l4.circulate(2, (el) => PostVisitOperation.REQUEST_DELETE);
    expect(current).toBe(12);
    current = l4.circulate(2, (el) => PostVisitOperation.REQUEST_DELETE);
    expect(current).toBe(11);
    expect(l4.size).toBe(1);

    // orderly remove one by one leaves list empty
    const elems = [0, 1, 2, 3, 4, 5];
    const l5: CircularList<number> = listOf(elems);
    expect(l5.size).toBe(6); // sanity check
    elems
      .forEach(() =>
        l5.circulate(1, (el) => PostVisitOperation.REQUEST_DELETE)
      );
    expect(l5.isEmpty).toBe(true);
  });
});
