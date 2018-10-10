import Foo from '@app/Foo';

export class Bar {
  constructor(public foo: Foo) {
    this.foo = new Foo();
  }
}
