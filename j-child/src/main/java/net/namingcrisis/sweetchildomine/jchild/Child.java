package net.namingcrisis.sweetchildomine.jchild;

/**
 * For this toy assignment, strictly speaking, the child abstraction is possibly
 * overkill. Should we add more attributes, however, then dealing directly with
 * low-level things like ids, strings, etc., etc. is icky.
 */
public final class Child {
  private final int id;

  public static Child newChild(int id) {
    return new Child(id);
  }

  private Child(int id) {
    this.id = id;
  }

  public int id() {
    return this.id;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Child)) {
      return false;
    }

    return this.id() == ((Child) obj).id();
  }

  @Override
  public String toString() {
    return String.format("Child[%s]", id);
  }
}
