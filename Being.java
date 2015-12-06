import java.util.Random;

public class Being {
  protected int x;
  protected int y;
  protected boolean dead = false;
  protected int valence;
  protected double strength;
  protected double fertility;
  protected double creativity;
  private static Random gen;

  public Being(int v, int xCoord, int yCoord) {
    if (gen == null) {
      gen = new Random();
    }
    
    valence = v;
    x = xCoord;
    y = yCoord;

    strength    = gen.nextDouble();
    fertility   = gen.nextDouble();
    creativity  = gen.nextDouble();
  }

  public void kill() {
    dead = true;
  }

  public Being(Being father, Being mother, int x, int y) {
    if (father.valence == mother.valence) {
      valence = father.valence;
    } else {
      throw new IllegalArgumentException("Parents must have the same valence.");
    }

    this.x = x;
    this.y = y;

    strength = father.strength + mother.strength + gen.nextGaussian() *
            (mother.strength - father.strength) / 20;
    if (strength > 1) strength = 1;
    if (strength < 0) strength = 0;

    fertility = father.fertility + mother.fertility + gen.nextGaussian() *
            (mother.fertility - father.fertility) / 20;
    if (fertility > 1) fertility = 1;
    if (fertility < 0) fertility = 0;

    creativity = father.creativity + mother.creativity + gen.nextGaussian() *
            (mother.creativity - father.creativity) / 20;
    if (creativity > 1) creativity = 1;
    if (creativity < 0) creativity = 0;
  }

  public String toString() {
    return String.format("S:%.3f F:%.3f C:%.3f", strength, fertility, creativity);
  }
}
