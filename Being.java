import java.util.Random;

public class Being {
  protected int x;
  protected int y;
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

  public Being(Being father, Being mother) {
    if (father.valence == mother.valence) {
      valence = father.valence;
    } else {
      throw new IllegalArgumentException("Parents must have the same valence.");
    }

    strength = father.strength + mother.strength + gen.nextGaussian() * (mother.strength - father.strength) / 2;

    fertility = father.fertility + mother.fertility + gen.nextGaussian() * (mother.fertility - father.fertility) / 2;


    creativity = father.creativity + mother.creativity + gen.nextGaussian() * (mother.creativity - father.creativity) / 2;
  }

  public String toString() {
    return String.format("S:%.3f F:%.3f C:%.3f", strength, fertility, creativity);
  }
}
