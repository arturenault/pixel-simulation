import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

enum Direction {
  NORTH, EAST, SOUTH, WEST
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
    if (Math.abs(x - b.x) > 1 || Math.abs(y-b.y) > 1)
      throw new IllegalArgumentException("Beings can only move one space per turn"); 

    land[b.x][b.y] = null;
    b.x = x;
    b.y = y;
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
    beings.remove(b);
    land[b.x][b.y] = null;
  }

  public Direction randomDirection() {
    return Direction.values()[gen.nextInt(Direction.values().length)];
  }


  public void reproduce(Being b1, Being b2) {
    int x, y;
    if (gen.nextInt(2) == 0) {
      x = b1.x;
      y = b1.y;
    } else {
      x = b2.x;
      y = b2.y;
    }
    do {
      Direction direction = randomDirection();
      switch (direction) {
        case NORTH:
          y++; break;
        case SOUTH:
          y--; break;
        case EAST:
          x++; break;
        case WEST:
          x--; break;
      }
    } while (land[x][y] != null);

    land[x][y] = new Being(b1, b2, x, y);
    beings.add(land[x][y]);
  }

  public synchronized boolean next() {
    int index = 0;
   
    System.out.println(beings.size());
    if (beings.isEmpty()) return false;
    
    Being b = beings.get(0);
    while (index < beings.size()) {
      if (index > 0) { index = beings.indexOf(b) + 1;}
      b = beings.get(index);

      // handle hostility
      double damage = roll(hostility[b.x][b.y] / 100);
      double resilience = roll(b.strength);
      if (damage > resilience) {
        //kill(b);
      }

      // handle neighbors
      Being[] neighbors = new Being[4];
      neighbors[0] = b.x - 1 > 0 ? land[b.x - 1][b.y] : null;
      neighbors[1] = b.x + 1 < size ? land[b.x + 1][b.y] : null;
      neighbors[2] = b.y - 1 > 0 ? land[b.x][b.y - 1] : null;
      neighbors[3] = b.y + 1 < size ? land[b.x][b.y + 1] : null;

      for(int i = 0; i < neighbors.length; i++) {
        if(neighbors[i] == null) continue;

        if (neighbors[i].valence == b.valence) {
          double fertility;
          if (neighbors[i].fertility > b.fertility) {
            fertility = roll(b.fertility);
          } else {
            fertility = roll(neighbors[i].fertility);
          }

          if (roll(1) / 10 < fertility) { 
            reproduce(neighbors[i], b);
          }
        } else {
          if(roll(b.strength) > roll(neighbors[i].strength)) {
            kill(neighbors[i]);
          } else {
            kill(b);
          }
        }

        Direction d = randomDirection();

        switch(d) {
          case NORTH:
            move(b, b.x, b.y + 1); break;
          case SOUTH:
            move(b, b.x, b.y - 1); break;
          case EAST:
            move(b, b.x + 1, b.y); break;
          case WEST:
            move(b, b.x - 1, b.y); break;
        }
      }
    }
    return true;
  }
}
