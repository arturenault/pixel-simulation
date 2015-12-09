import libs.PerlinNoiseGenerator;

import java.util.*;

enum Direction {
  NORTH, EAST, SOUTH, WEST, NONE;

  private static final List<Direction> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
  private static final int SIZE = VALUES.size();
  private static final Random RANDOM = new Random();

  public static Direction random() {
    return VALUES.get(RANDOM.nextInt(SIZE));
  }
}

public class World {
  private final int octaves = 4;
  protected int size;
  protected int population;
  protected Being[][] land;
  protected ArrayList<Being> beings;
  protected float[][] hostility;
  private int turn;
  private double favorability;
  private double fertility;
  private double intelligenceFactor;
  private Random gen;
  private int valences;
  private int gestation;

  public World() {
    beings = new ArrayList<Being>();
    size = 100;
  }

  public World(int size, int population, double fertility, double favorability,
               double intelligenceFactor, int
          valences) {
    turn = 0;
    gen = new Random();
    this.valences = valences;
    this.size = size;
    this.favorability = favorability;
    this.intelligenceFactor = intelligenceFactor;
    this.fertility = fertility;
    this.population = population;

    beings = new ArrayList<Being>();
    land = new Being[size][size];

    hostility = PerlinNoiseGenerator.generateSmoothNoise
            (PerlinNoiseGenerator
            .generateWhiteNoise(size, size), octaves);

    for (int i = 0; i < population; i++) {
      int x, y; 

      do {
        x = gen.nextInt(size);
        y = gen.nextInt(size);
      } while(land[x][y] != null);

      land[x][y] = new Being(i % valences, x, y);
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

    if (land[b.x + x][b.y + y] != null) return;

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




  public synchronized Being reproduce(Being b1, Being b2) {

    if (turn - b1.reproduced < gestation || turn - b2.reproduced < gestation) {
      return null;
    } else {
      b1.reproduced = turn;
      b2.reproduced = turn;
    }
    int x, y, counter = 0;
    if (gen.nextInt(2) == 0) {
      x = b1.x;
      y = b1.y;
    } else {
      x = b2.x;
      y = b2.y;
    }
    do {
      if (counter++ > 1) return null;
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

  public Direction chooseDirection(Being b) {
    if (b.dead) return Direction.NONE;

    float minHostility = hostility[b.x][b.y];
    Direction bestDirection = Direction.NONE;

    if (b.x > 0 && hostility[b.x - 1][b.y] < minHostility) {
      minHostility = hostility[b.x - 1][b.y];
      bestDirection = Direction.WEST;
    }

    if (b.x < size - 1 && hostility[b.x + 1][b.y] < minHostility) {
      minHostility = hostility[b.x + 1][b.y];
      bestDirection = Direction.EAST;
    }

    if (b.y > 0 && hostility[b.x][b.y - 1] < minHostility) {
      minHostility = hostility[b.x][b.y - 1];
      bestDirection = Direction.SOUTH;
    }

    if (b.y < size - 1 && hostility[b.x][b.y + 1] < minHostility) {
      bestDirection = Direction.NORTH;
    }

    return bestDirection;
  }

  public synchronized boolean next() {
    int index = 0;
    if (beings.isEmpty()) return false;

    ListIterator<Being> iter = beings.listIterator();
    while (iter.hasNext()) {
      Being b = iter.next();

      if (b.dead) {
        iter.remove();
        continue;
      }

      // handle hostility
      double damage = roll(hostility[b.x][b.y] * favorability);
      double resilience = roll(b.strength) + roll(b.intelligence) * intelligenceFactor;
      if (damage > resilience) {
        kill(b);
      }

      // handle neighbors
      Being[] neighbors = new Being[4];
      neighbors[0] = b.x - 1 > 0 ? land[b.x - 1][b.y] : null;
      neighbors[1] = b.x + 1 < size ? land[b.x + 1][b.y] : null;
      neighbors[2] = b.y - 1 > 0 ? land[b.x][b.y - 1] : null;
      neighbors[3] = b.y + 1 < size ? land[b.x][b.y + 1] : null;

      for (Being neighbor : neighbors) {
        if (neighbor == null) continue;

        if (neighbor.valence == b.valence) {
          double pregnancy;
          if (neighbor.fertility > b.fertility) {
            pregnancy = roll(b.fertility);
          } else {
            pregnancy = roll(neighbor.fertility);
          }

          double intelligence;
          if (neighbor.intelligence > b.intelligence) {
            intelligence = roll(neighbor.intelligence);
          } else {
            intelligence = roll(b.intelligence);
          }

          if (roll(1) / fertility < pregnancy + intelligence * intelligenceFactor) {
            Being newBeing = reproduce(neighbor, b);
            if (newBeing != null) {
              iter.add(newBeing);
            }
          }

          if (neighbor.intelligence > b.intelligence) {
            b.intelligence += (neighbor.intelligence - b.intelligence) / 10;
          }
        } else {
          if (roll(b.strength) + roll(b.intelligence) * intelligenceFactor >
                  roll
                          (neighbor
                                  .strength) + roll(neighbor.intelligence) * intelligenceFactor) {
            kill(neighbor);
          } else {
            kill(b);
          }
        }
      }
      Direction d = chooseDirection(b);

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
        default:
          break;
      }
    }
    turn++;
    return true;
  }
}
