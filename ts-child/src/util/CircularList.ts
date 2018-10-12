import { Assert } from '@app/util/Assert';

/**
 * Visitor function operation type.
 */
export enum PostVisitOperation {
  /**
   * Visitor requesting that the list remove the visited element.
   */
  REQUEST_DELETE
}

type Op<T> = (elem: T) => PostVisitOperation;

/**
 * <p>Stateful circular list that retains a pointer to its last point of
 * iteration.</p>
 *
 * <p>Implementation/Design Note: uses a native JS array internally, thus
 * representation (and thus performance) is dependent on the JS engine in use.
 * An alternative is to explicitly write a Linked List implementation for this
 * use case. Without having further knowledge of constraints or potential use
 * cases beyond this interview question -- opting for reuse and simplicity.
 * No code written === no code to test and maintain!</p>
 *
 * @param <T> element type.
 */
export class CircularList<T> {
  private readonly elems: T[];
  private iterationIdx: number;

  /**
   * More constructors/factories could be useful, keeping this to our common use
   * case for now.
   *
   * @param elems to initialise this list from, note that a shallow copy is made.
   */
  constructor(elems: T[]) {
    this.iterationIdx = 0;

    if (!elems) {
      this.elems = [];
      return;
    }

    this.elems = elems.slice();
  }

  /**
   * Circulates up to k values, then passing in the element at the k'th value
   * to the visitor function, nothing in between. Returns if empty list, visitor
   * never called.
   *
   * @param k must be >= 1, count of element to reach, circling around
   *          if k > list size.
   * @param visitorFn must return the relevant operation type, or undefined/null.
   *  Returning undefined or some falsey value means no further post-visit
   *  processing is done.
   *
   * @return found element, or undefined if not found.
   */
  circulate(k: number, visitorFn: Op<T>): T {
    Assert.assertTrue(k >= 1, () => 'At least 1 element to circulate to expected');

    if (this.isEmpty) {
      return undefined;
    }

    let curr = 0;

    while (true) {
      let i = this.determineStartIdx(this.iterationIdx);

      while (true) {
        curr++;

        if (curr === k) {
          const elem = this.elems[i];
          this.postVisit(visitorFn(elem), i);
          this.iterationIdx = i;

          return elem;
        }

        i = this.determineStartIdx(++i);
      }
    }
  }

  /**
   * @return true if list empty, false otherwise.
   */
  get isEmpty(): boolean {
    return this.size === 0;
  }

  /**
   * @return list size.
   */
  get size(): number {
    return this.elems.length;
  }

  /**
   * @return the element if exactly one element in list, error thrown otherwise.
   */
  oneAndOnly(): T {
    Assert.assertTrue(this.size === 1, () => 'Expecting exactly one element!');
    return this.elems[0];
  }

  private determineStartIdx(curr: number): number {
    return curr >= this.size ? 0 : curr;
  }

  private postVisit(op: PostVisitOperation, idx: number) {
    // enum can be zero value, can't use arbitrary falsey check.
    if (op === null || op === undefined) {
      return;
    }

    switch (op) {
      case PostVisitOperation.REQUEST_DELETE:
        this.elems.splice(idx, 1);
        return;

      default:
        return Assert.assertNever(op);
    }
  }
}
