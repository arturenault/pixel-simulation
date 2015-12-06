import java.util.*;

enum Direction {
  NORTH, EAST, SOUTH, WEST;

  private static final List<Direction> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
  private static final int SIZE = VALUES.size();
  private static final Random RANDOM = new Random();

  public static Direction random() {
    return VALUES.get(RANDOM.nextInt(SIZE));
  }
}

public class World {
  protected int size;
  protected int population;
  protected Being[][] land;
  protected ArrayList<Being> beings;
  private double[][] hostility;
  private Random gen;

  public World(int s, int initialPopulation) {
    gen = new Random();
    size = s;
    population = initialPopulation;

    beings = new ArrayList<Being>();
    land = new Being[size][size];
    hostility = new double[size][size];

    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        hostility[i][j] = gen.nextDouble(); // TODO find good way of creating appropriately distributed hostilities
      }
    }

    for (int i = 0; i < initialPopulation; i++) {
      int x, y; 

      do {
        x = gen.nextInt(size);
        y = gen.nextInt(size);
      } while(land[x][y] != null);

      land[x][y] = new Being(i % 2, x, y); //TODO determine correct number of valences
      beings.add(land[x][y]);
    }
  }

  public void move(Being b, int x, int y) {
    if(Math.abs(x) + Math.abs(y) > 1) {
      throw new IllegalArgumentException("Beings can only move a space at a " +
              "time");
    }

    if (x < 0 && b.x == 0) x = 0;
    if (y < 0 && b.y == 0) y = 0;
    if (x > 0 && b.x == size - 1) x = 0;
    if (y > 0 && b.y == size - 1) y = 0;

    land[b.x][b.y] = null;
    b.x += x;
    b.y += y;
    land[b.x][b.y] = b;
  }

  public String toString() {
    StringBuilder output = new StringBuilder();
    for (int i = 0; i < land.length; i++) {
      for (int j = 0; j < land[i].length; j++) {
        if (land[i][j] == null) {
          output.append("\t-\t");
        } else { 
          output.append(land[i][j]);
        }
        output.append("\t");
      }
      output.append("\n");
    }
    return output.toString();
  }

  public double roll(double max) {
    return gen.nextDouble() * max;
  }

  public void kill(Being b) {
    b.kill();
    land[b.x][b.y] = null;
  }




  public Being reproduce(Being b1, Being b2) {
    int x, y;
    if (gen.nextInt(2) == 0) {
      x = b1.x;
      y = b1.y;
    } else {
      x = b2.x;
      y = b2.y;
    }
    do {
      Direction direction = Direction.random();
      switch (direction) {
        case NORTH:
          if (y < size - 1) y++; break;
        case SOUTH:
          if (y > 0) y--; break;
        case EAST:
          if (x < size - 1) x++; break;
        case WEST:
          if (x > 0) x--; break;
      }
    } while (land[x][y] != null);

    land[x][y] = new Being(b1, b2, x, y);
    return land[x][y];
  }

  public synchronized boolean next() {
    int index = 0;
    if (beings.isEmpty()) return false;

    System.out.println(beings.size());
    ListIterator<Being> iter = beings.listIterator();
    while (iter.hasNext()) {
      Being b = iter.next();

      if (b.dead) {
        iter.remove();
        continue;
      }

      // handle hostility
      double damage = roll(hostility[b.x][b.y] / 100);
      double resilience = roll(b.strength);
      if (damage > resilience) {
        kill(b);
      }

      // handle neighbors
      Being[] neighbors = new Being[4];
      neighbors[0] = b.x - 1 > 0 ? land[b.x - 1][b.y] : null;
      neighbors[1] = b.x + 1 < size ? land[b.x + 1][b.y] : null;
      neighbors[2] = b.y - 1 > 0 ? land[b.x][b.y - 1] : null;
      neighbors[3] = b.y + 1 < size ? land[b.x][b.y + 1] : null;

      for (int i = 0; i < neighbors.length; i++) {
        if (neighbors[i] == null) continue;

        if (neighbors[i].valence == b.valence) {
          double fertility;
          if (neighbors[i].fertility > b.fertility) {
            fertility = roll(b.fertility);
          } else {
            fertility = roll(neighbors[i].fertility);
          }

          if (roll(1) / 10 < fertility) {
            iter.add(reproduce(neighbors[i], b));
          }
        } else {
          if (roll(b.strength) > roll(neighbors[i].strength)) {
            kill(neighbors[i]);
          } else {
            kill(b);
          }
        }
      }
      Direction d = Direction.random();

      switch (d) {
        case NORTH:
          move(b, 0, 1);
          break;
        case SOUTH:
          move(b, 0, -1);
          break;
        case EAST:
          move(b, 1, 0);
          break;
        case WEST:
          move(b, -1, 0);
          break;
      }
    }
    return true;
  }
}
