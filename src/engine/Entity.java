package engine;

public abstract class Entity {
  private final EntityType type;
  private final char displayValue;

  Entity(EntityType type, char displayValue) {
    this.type = type;
    this.displayValue = displayValue;
  }

  public EntityType getType() {
    return type;
  }

  public char toChar() {
    return displayValue;
  }
}
