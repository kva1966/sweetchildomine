import { CountingCircleGame } from "@app/CountingCircleGame";
import { Result } from "@app/CountingCircleGame";

function play(n: number, k: number): Result {
  return CountingCircleGame.of(n, k).play();
}

function assertResult(result: Result, lastChildStanding: number, ...exitSequence: number[]) {
  expect(result.lastChildStanding).toBe(lastChildStanding);
  expect(result.exitedChildrenInSequence).toEqual(exitSequence);
}

describe("CountingCircleGame", () => {
  test("play - minimum no. of children playing", () => {
    // Given: Minimum n children
    const n: number = 2;

    // Then:
    // for k values 1 to > n (to check overflow conditions)
    assertResult(play(n, 1), 1, 0);
    assertResult(play(n, 2), 0, 1);
    assertResult(play(n, 3), 1, 0);
    assertResult(play(n, 4), 0, 1);
  });

  test("play - greater than minimum no. of children playing", () => {
    // Given: > Minimum n
    const n: number = 3;

    // Then:
    // for k values 1 to > n (to check overflow conditions)
    assertResult(play(n, 1), 2, 0, 1);
    assertResult(play(n, 2), 2, 1, 0);
    assertResult(play(n, 3), 1, 2, 0);
    assertResult(play(n, 4), 1, 0, 2);
  });

  test("init - failure conditions", () => {
    const nValueMsg = 'at least 2 children expected';
    expect(() => play(-1, 1)).toThrow(nValueMsg);
    expect(() => play(0, 1)).toThrow(nValueMsg);
    expect(() => play(1, 1)).toThrow(nValueMsg);

    const kValueMsg = 'at least 1 count (k) expected';
    expect(() => play(2, -1)).toThrow(kValueMsg);
    expect(() => play(2, 0)).toThrow(kValueMsg);
  });

  test("game play - cannot reuse game instance", () => {
    const game: CountingCircleGame = CountingCircleGame.of(5, 1);
    game.play();

    expect(() => game.play())
      .toThrow('Circle has been broken, one child left, game instance cannot be reused')
  });
});
