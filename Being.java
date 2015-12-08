import java.util.Random;

public class Being {
  public int reproduced = -100;
  protected int x;
  protected int y;
  protected boolean dead = false;
  protected int valence;
  protected double strength;
  protected double fertility;
  protected double intelligence;
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
    intelligence = gen.nextDouble();
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

    strength = (father.strength + mother.strength) / 2 + gen.nextGaussian() *
            (mother.strength - father.strength) / 2;
    if (strength > 1) strength = 1;
    if (strength < 0) strength = 0;

    fertility = (father.fertility + mother.fertility) / 2 + gen.nextGaussian
            () *
            (mother.fertility - father.fertility) / 2;
    if (fertility > 1) fertility = 1;
    if (fertility < 0) fertility = 0;

    intelligence = (father.intelligence + mother.intelligence) / 2 + gen.nextGaussian()
            *
            (mother.intelligence - father.intelligence) / 2;
    if (intelligence > 1) intelligence = 1;
    if (intelligence < 0) intelligence = 0;
  }

  public String toString() {
    return String.format("S:%.3f F:%.3f C:%.3f", strength, fertility, intelligence);
  }
}
