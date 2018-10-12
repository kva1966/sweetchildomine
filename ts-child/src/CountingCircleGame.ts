import { CircularList, PostVisitOperation } from '@app/util/CircularList';
import { Assert } from '@app/util/Assert';

export class Result {
  constructor(
    public readonly lastChildStanding: number,
    public readonly exitedChildrenInSequence: number[]
  ) {}
}

/**
 * <p>Circle of children playing the counting game.</p>
 *
 * <p>Rules:</p>
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
 * <p>Non-resettable state: init, run, discard.</p>
 *
 * <p>Assumptions:</p>
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
export class CountingCircleGame {
  private children: CircularList<number>;

  static of(nChildren: number, kCountPerIteration: number): CountingCircleGame {
    return new CountingCircleGame(nChildren, kCountPerIteration);
  }

  private constructor(
    nChildren: number,
    private readonly k: number) {
    Assert.assertTrue(nChildren >= 2, () => 'at least 2 children expected');
    Assert.assertTrue(k > 0, () => 'at least 1 count (k) expected');

    const elems = [...Array(nChildren).keys()];
    this.children = new CircularList<number>(elems);
  }

  play(): Result {
    Assert.assertFalse(this.children.size === 1,
      () => 'Circle has been broken, one child left, game instance cannot be reused');

    const exited: number[] = [];
    const oneChildLeft = () => this.children.size === 1;

    while (!oneChildLeft()) {
      const exitingChild: number = this.children
        .circulate(this.k, (e) => PostVisitOperation.REQUEST_DELETE);
      Assert.assertTrue(exitingChild >= 0, () => 'Logic error, always expecting a value!');

      exited.push(exitingChild);
    }

    return new Result(this.children.oneAndOnly(), exited);
  }
}
