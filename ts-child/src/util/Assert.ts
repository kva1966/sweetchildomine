export type MsgSupplierFn = () => string;

export class Assert {
  static assertTrue(cond: boolean, msgFn: MsgSupplierFn) {
    if (!cond) {
      throw new Error(msgFn());
    }
  }

  static assertFalse(cond: boolean, msgFn: MsgSupplierFn) {
    Assert.assertTrue(!cond, msgFn);
  }

  /**
   * This is very much a TypeScript type system check utility, can't easily test
   * this, since the TS compiler verifies its use and signature at compile time.
   *
   * At runtime, when working with a mix of TS and JS code, the actual logic here
   * might trigger with a bad parameter.
   *
   * See: https://www.typescriptlang.org/docs/handbook/advanced-types.html
   *
   * Search for Exhaustiveness Checking.
   */
  static assertNever(x: never) {
    throw new Error('Unexpected object (did you add a new enum value?)' + x);
  }
}
