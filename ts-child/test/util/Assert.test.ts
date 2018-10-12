import { Assert } from "@app/util/Assert";

describe("Assert", () => {
  test("assertTrue", () => {
    Assert.assertTrue(true, () => { throw new Error("Must never be called!"); });
    expect(
      () => Assert.assertTrue(false, () => "Failed condition")
    ).toThrow('Failed condition');
  });

  test("assertFalse", () => {
    Assert.assertFalse(false, () => { throw new Error("Must never be called!"); });
    expect(
      () => Assert.assertFalse(true, () => "Failed condition")
    ).toThrow('Failed condition');
  });
});
