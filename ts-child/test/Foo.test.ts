import Foo from "@app/Foo";

describe("Foo", () => {
  it("should return 5", () => {
    expect(new Foo().bar()).toBe(5);
  });
});
